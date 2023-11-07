package dev.dutta.abhijit.hashnode.generics

import dev.dutta.abhijit.hashnode.constants.StringConstants

object VectorFunctions {

  implicit class NumericDerivations[T](number: T)(implicit numeric: Numeric[T]) {
    def isZero: Boolean = number == numeric.zero
    def isNonZero: Boolean = number != numeric.zero
  }

  implicit class BooleanDerivations(condition: Boolean) {
    def toYN: String =
      if (condition) StringConstants._Y
      else StringConstants._N
  }

}
