package dev.dutta.abhijit.hashnode.regulations

import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.constants.StringConstants._

case class Permissions(
                      isPermission1: Boolean = false,
                      isPermission2: Boolean = false,
                      permission3: String = STRING_EMPTY,
                      permission4: Int = ZERO)

object Permissions {

  def fromRules(atomicRule: AtomicRule): Permissions =
    Permissions(
      isPermission1 = atomicRule.getPermission[Boolean](AtomicRule.PERMISSION_1),
      isPermission2 = atomicRule.getPermission[Boolean](AtomicRule.PERMISSION_2),
      permission3 = atomicRule.getPermission[String](AtomicRule.PERMISSION_3),
      permission4 = atomicRule.getPermission[Int](AtomicRule.PERMISSION_4)
    )

}

