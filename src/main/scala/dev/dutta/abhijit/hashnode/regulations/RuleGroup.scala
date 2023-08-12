package dev.dutta.abhijit.hashnode.regulations

case class RuleGroup(rules: List[Rule]) {

  private def getRuleFor(name: String): Option[Rule] =
    if (rules != null) rules.find(rule => rule.name.equalsIgnoreCase(name))
    else None

  def getValue[T](name: String)(implicit converter: RuleConverter[T]): T = {
    getRuleFor(name) match {
      case Some(rule) =>
        try {
          converter.convert(rule.values.head)
        }
        catch {
          case _: Exception =>
            throw new Exception(s"Rule name: ${rule.name} type is wrong.")
        }

      case None =>
        throw new Exception(s"Rule name: $name not present")
    }
  }
  def getValues[T](name: String)(implicit converter: RuleListConverter[T]): List[T] = {
    getRuleFor(name) match {
      case Some(rule) =>
        try {
          converter.convert(rule.values)
        }
        catch {
          case _: Exception =>
            throw new Exception(s"Rule name: ${rule.name} type is wrong.")
        }

      case None =>
        throw new Exception(s"Rule name: $name not present")
    }
  }


}
