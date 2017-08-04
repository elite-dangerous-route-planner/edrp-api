/*
 * Copyright (c) 2017 Amazon.com, Inc. All rights reserved.
 *
 * LICENCE: This work is proprietary information of Amazon.com, Inc. and may not
 * be redistributed outside of Amazon without explicit written permission. If
 * you are not an Amazon employee, please delete this file without reading
 * further.
 */

package science.canonn.edrp


case class System(id: Long, name: String, coords: Seq[Double], is_populated: Boolean)
