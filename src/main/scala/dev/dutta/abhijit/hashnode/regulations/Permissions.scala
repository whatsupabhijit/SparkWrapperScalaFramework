package dev.dutta.abhijit.hashnode.regulations

import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.constants.StringConstants._

case class Permissions(
                      isPermission1: Boolean = false,
                      isPermission2: Boolean = false,
                      permission3: String = STRING_EMPTY,
                      permission4: Int = INT_ZERO)

object Permissions {

  def fromRules(rules: Rules): Permissions =
    Permissions(
      isPermission1 = rules.getPermission[Boolean](Rules.PERMISSION_1),
      isPermission2 = rules.getPermission[Boolean](Rules.PERMISSION_2),
      permission3 = rules.getPermission[String](Rules.PERMISSION_3),
      permission4 = rules.getPermission[Int](Rules.PERMISSION_4)
    )

}

