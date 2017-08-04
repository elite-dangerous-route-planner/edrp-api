import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import ScalateKeys._

val ScalatraVersion = "2.5.1"

ScalatraPlugin.scalatraSettings

scalateSettings

organization := "science.canonn"

name := "edrp"

version := "0.1.0"

scalaVersion := "2.12.2"

resolvers += Classpaths.typesafeReleases
resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.2.15.v20160210" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "cz.alenkacz.db" % "postgres-scala_2.12" % "0.5.0",
  "org.json4s" %% "json4s-native" % "3.5.3",
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.scalatest" % "scalatest_2.12" % "3.0.1" % "test",
  "com.github.ben-manes.caffeine" % "caffeine" % "2.5.3",
  "com.google.code.findbugs" % "jsr305" % "3.0.2" % "compile"

)

scalateTemplateConfig in Compile := {
  val base = (sourceDirectory in Compile).value
  Seq(
    TemplateConfig(
      base / "webapp" / "WEB-INF" / "templates",
      Seq.empty,  /* default imports should be added here */
      Seq(
        Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
      ),  /* add extra bindings here */
      Some("templates")
    )
  )
}

enablePlugins(JettyPlugin)
