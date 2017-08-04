/*
 * Copyright (c) 2017 Amazon.com, Inc. All rights reserved.
 *
 * LICENCE: This work is proprietary information of Amazon.com, Inc. and may not
 * be redistributed outside of Amazon without explicit written permission. If
 * you are not an Amazon employee, please delete this file without reading
 * further.
 */

package science.canonn.edrp

object Constants {
  val db_host = sys.env("EDRP_DB_HOST")
  val db_port = sys.env("EDRP_DB_PORT")
  val db_user = sys.env("EDRP_DB_USER")
  val db_pass = sys.env("EDRP_DB_PASS")
  val db_name = sys.env("EDRP_DB_NAME")
  val file_systems = "/usr/share/edrp/systemcoords.csv"
  val file_bodies = "/usr/share/edrp/valuablebodies.jsonl"
}
