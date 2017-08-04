/*
 * Copyright (c) 2017 Amazon.com, Inc. All rights reserved.
 *
 * LICENCE: This work is proprietary information of Amazon.com, Inc. and may not
 * be redistributed outside of Amazon without explicit written permission. If
 * you are not an Amazon employee, please delete this file without reading
 * further.
 */

package science.canonn.edrp

import java.util.concurrent.TimeUnit

import com.github.benmanes.caffeine.cache.{Cache, CacheLoader, Caffeine, LoadingCache}
import com.typesafe.config.{Config, ConfigFactory}
import cz.alenkacz.db.postgresscala.Connection
import cz.alenkacz.db.postgresscala.PostgresConnection
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global

object Store {

  val connectionString = s"jdbc:postgresql://${Constants.db_host}:${Constants.db_port}/${Constants.db_name}?user=${Constants.db_user}&password=${Constants.db_pass}"

  val config: Config = ConfigFactory.parseString(
        s"""
           |database {
           |  connectionString = "$connectionString"
           |}
         """.stripMargin).getConfig("database")

  def withConnection[T](f: Connection => T): T = {
    val connection = Await.result(PostgresConnection.fromConfig(config), 5.seconds)
    val r = f(connection)
    connection.close()
    Thread.sleep(30)
    r
  }

  def execute(c: Connection, s: String): Unit = {
    Await.result(c.execute(s), 40.seconds)
  }

  def querySystems(c: Connection, s: String): Seq[System] = {
    Await.result(c.query[System](s,
      r => {
        System(
          r("id").long,
          r("name").string,
          r("coords").string.split(',')
            .map(_.trim)
            .map(_.stripPrefix("("))
            .map(_.stripSuffix(")"))
            .map(_.toDouble))
      }), 20.seconds)
  }

  def queryBodies(c: Connection, s: String): Seq[Body] = {
    Await.result(c.query[Body](s, r => Body(r("system_id").long, r("name").string, r("updated_at").long, r("type_id").intOpt, r("type_name").string, r("distance_to_arrival").intOpt, r("terraforming_state_id").intOpt, r("terraforming_state_name").string, r("value").int)), 20.seconds)
  }

  implicit val formats = DefaultFormats

  def loadSystems: ()=>Unit = () => {
    for (lines <- Source.fromFile(Constants.file_systems).getLines().drop(1).grouped(200)) {
      val values = lines.map(line => {
        val fields = line.split('|').map(_.trim)
        val id = fields(0).toLong
        val name = fields(2).stripPrefix("\"").stripSuffix("\"")
        val x = fields(3).toDouble
        val y = fields(4).toDouble
        val z = fields(5).toDouble
        val is_populated = fields(6)
        s"($id, '${name.replace("'","''")}', cube(ARRAY[$x,$y,$z]), '$is_populated')"
      }).mkString(", ")
      val insertsql = s"INSERT INTO system (id, name, coords, is_populated) VALUES $values ON CONFLICT (id) DO UPDATE SET name = excluded.name, coords = excluded.coords, is_populated = excluded.is_populated"
      println(insertsql)
      withConnection(execute(_,insertsql))
    }
  }

  def loadBodies: ()=>Unit = () => {
    for (lines <- Source.fromFile(Constants.file_bodies).getLines().grouped(200)) {
      val values = lines.map(line => {
        val n = parse(line).extract[Body]
        s"(${n.system_id},'${n.name.replace("'","''")}',to_timestamp(${n.updated_at}),${n.type_id.getOrElse(0)},'${(if(n.type_name == null) "" else n.type_name).replace("'","''")}',${n.distance_to_arrival.getOrElse(0)},${n.terraforming_state_id.getOrElse(0)},'${(if(n.terraforming_state_name == null) "" else n.terraforming_state_name).replace("'","''")}',${n.value})"
      }).mkString(", ")
      val insertsql = s"INSERT INTO body (system_id, name, updated_at, type_id, type_name, distance_to_arrival, terraforming_state_id, terraforming_state_name, value) VALUES $values ON CONFLICT (system_id, name) DO UPDATE SET updated_at = excluded.updated_at, type_id = excluded.type_id, type_name = excluded.type_name, distance_to_arrival = excluded.distance_to_arrival, terraforming_state_id = excluded.terraforming_state_id, terraforming_state_name = excluded.terraforming_state_name, value = excluded.value"
      withConnection(execute(_,insertsql))
    }
  }

  def systemTypeAhead(input: String): Seq[String] =
     withConnection[Seq[System]](querySystems(_,
       s"SELECT * FROM system WHERE ngrams_vector(name) @@ '$input'::tsquery order by name limit 10;")).map(_.name)

  def getSystems(maxDistanceFromSol: Int = 1000,
                 maxDistanceToArrival: Int = 20000,
                 minValue: Int = 2000000): Seq[System] = {
    val query = s"""
       |  select system.*
       |  from system join body on id = system_id
       |  where system.is_populated = 'false'
       |    and system.coords <-> cube(array[0.0,0.0,0.0]) < $maxDistanceFromSol
       |    and body.distance_to_arrival < $maxDistanceToArrival
       |  group by system.id
       |  having sum(value) > $minValue
     """.stripMargin

    withConnection[Seq[System]](querySystems(_, query))
  }

  lazy val systemCache: LoadingCache[(Int,Int,Int), Seq[System]] = Caffeine.newBuilder()
    .maximumSize(10000)
    .refreshAfterWrite(1, TimeUnit.DAYS)
    .build(
      new CacheLoader[(Int, Int, Int), Seq[System]] {
        @throws[Exception]
        def load(key: (Int, Int, Int)): Seq[System] = {
          println("cache miss")
          Store.getSystems(key._1,key._2,key._3)
        }
      }
    )

  lazy val typeaheadCache: LoadingCache[String, Seq[String]] = Caffeine.newBuilder()
    .maximumSize(10000)
    .refreshAfterWrite(1, TimeUnit.DAYS)
    .build(
      new CacheLoader[String, Seq[String]] {
        @throws[Exception]
        def load(key: String): Seq[String] = {
          println("ta cache miss")
          Store.systemTypeAhead(key)
        }
      }
    )
}
