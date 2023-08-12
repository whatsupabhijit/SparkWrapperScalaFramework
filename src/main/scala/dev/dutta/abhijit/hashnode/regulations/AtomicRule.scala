package dev.dutta.abhijit.hashnode.regulations

import dev.dutta.abhijit.hashnode.constants.StringConstants

case class AtomicRule(ruleGroup: RuleGroup) {

  val rules: List[Rule] = ruleGroup.rules

  val stringRules: Map[String, String] = rules
    .filter(_.`type` == "String")
    .map(rule => rule.name -> rule.values.head)
    .toMap

  val stringListRules: Map[String, List[String]] = rules
    .filter(_.`type` == "StringList")
    .map(rule => rule.name -> rule.values)
    .toMap
}

object AtomicRule {

  lazy val PERMISSION_1: String = StringConstants.PERMISSION_1
  lazy val PERMISSION_2: String = StringConstants.PERMISSION_2
  lazy val PERMISSION_3: String = StringConstants.PERMISSION_3
  lazy val PERMISSION_4: String = StringConstants.PERMISSION_4

  implicit class RulesDerivation(ruleGroup: RuleGroup) {

    def getPermission[T](permissionName: String)
                        (implicit converter: RuleConverter[T]): T =
      ruleGroup.getValue[T](name = permissionName)

    def getAllPermissions[T](permissionName: String)
                            (implicit converter: RuleListConverter[T]): List[T] =
      ruleGroup.getValues[T](name = permissionName)

    // TODO: we need to create the RuleGroup schema and update this function value.
//    def setPermissions(permissionName: String, values: List[String]): RulesGroup =
//      ruleGroup.rules.map(_.values.copy(values = values))

  }

}

