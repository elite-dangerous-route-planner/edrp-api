/*
 * Copyright (c) 2017 Amazon.com, Inc. All rights reserved.
 *
 * LICENCE: This work is proprietary information of Amazon.com, Inc. and may not
 * be redistributed outside of Amazon without explicit written permission. If
 * you are not an Amazon employee, please delete this file without reading
 * further.
 */

package science.canonn.edrp

case class PathResult (system_id: Long,
                       system_name: String,
                       name: String,
                       distance_to_arrival: Option[Int],
                       type_name: String,
                       value: Int)
