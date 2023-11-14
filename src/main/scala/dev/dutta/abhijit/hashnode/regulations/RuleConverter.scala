package dev.dutta.abhijit.hashnode.regulations

sealed trait RuleConverter[T] {
  def convert(value: String): T
}

sealed trait RuleListConverter[T] {
  def convert(values: List[String]): List[T]
}

object RuleConverter {
  implicit val string2String: RuleConverter[String] =
    new RuleConverter[String] {
      def convert(value: String): String = value
    }

  implicit val string2Int: RuleConverter[Int] =
    new RuleConverter[Int] {
      def convert(value: String): Int = value.toInt
    }

  implicit val string2Double: RuleConverter[Double] =
    new RuleConverter[Double] {
      def convert(value: String): Double = value.toDouble
    }

  implicit val string2Float: RuleConverter[Float] =
    new RuleConverter[Float] {
      def convert(value: String): Float = value.toFloat
    }

  implicit val string2Boolean: RuleConverter[Boolean] =
    new RuleConverter[Boolean] {
      def convert(value: String): Boolean = value.toBoolean
    }

  implicit val stringList2String: RuleListConverter[String] =
    new RuleListConverter[String] {
      def convert(values: List[String]): List[String] = values
    }

}
