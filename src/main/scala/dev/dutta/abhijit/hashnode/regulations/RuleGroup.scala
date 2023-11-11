package dev.dutta.abhijit.hashnode.regulations

import scala.util.{Try, Success, Failure}

case class RuleGroup(rules: List[Rule]) {

  private def getRuleFor(name: String): Option[Rule] =
    if (rules != null) rules.find(_.name.equalsIgnoreCase(name))
    else None

  def getValue[T](name: String)(implicit converter: RuleConverter[T]): T =
    getRuleFor(name) match {
      case Some(rule) =>
        Try (converter.convert(rule.values.head)) match {
          case Success(value) => value
          case Failure(_) => throw new Exception(s"Rule name: ${rule.name} type is wrong.")
        }
      case None => throw new Exception(s"Rule name: $name not present")
    }

  def getValues[T](name: String)(implicit converter: RuleListConverter[T]): List[T] =
    getRuleFor(name) match {
      case Some(rule) =>
        Try (converter.convert(rule.values)) match {
          case Success(value) => value
          case Failure(_) => throw new Exception(s"Rule name: ${rule.name} type is wrong.")
        }
      case None => throw new Exception(s"Rule name: $name not present")
    }

}
