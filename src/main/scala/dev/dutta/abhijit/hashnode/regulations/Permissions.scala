package dev.dutta.abhijit.hashnode.regulations

import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.constants.StringConstants._

case class Permissions(
                      isPermission1: Boolean = false,
                      isPermission2: Boolean = false,
                      permission3: String = STRING_EMPTY,
                      permission4: Int = INT_ZERO)

object Permissions {

  def fromRules(rule: AtomicRule): Permissions =
    Permissions(
      isPermission1 = rule.getPermission[Boolean](AtomicRule.PERMISSION_1),
      isPermission2 = rule.getPermission[Boolean](AtomicRule.PERMISSION_2),
      permission3 = rule.getPermission[String](AtomicRule.PERMISSION_3),
      permission4 = rule.getPermission[Int](AtomicRule.PERMISSION_4)
    )

}

