package dev.dutta.abhijit.hashnode.constants

object ExceptionConstants {

  // Error Codes goes here
  lazy val ATOM_CALC_001: String = "ATOM_CALC_001"

  // Error Descriptions goes here
  lazy val ATOM_CALC_EXCEPTION: String = "Exception occurred when trying to calculate" +
    "either default value or no atom value or actual value"

  // Error Code vs Error Description Map
  lazy val ExceptionCodesWithDesc: Map[String, String] = Map(
    (ATOM_CALC_001, ATOM_CALC_EXCEPTION),
  )

}
