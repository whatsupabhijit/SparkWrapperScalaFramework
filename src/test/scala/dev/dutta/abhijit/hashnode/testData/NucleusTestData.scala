package dev.dutta.abhijit.hashnode.testData

import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.constants.StringConstants._
import dev.dutta.abhijit.hashnode.nucleus.Nucleus
import dev.dutta.abhijit.hashnode.schema.NucleusInput

object NucleusTestData {

  // create the test data
  val rec01: NucleusInput = NucleusInput(
    identifier = _IDENTIFIER_1,
    firstElementData = Vector(_ONE, _TWO, _THREE))

  val rec02: NucleusInput = NucleusInput(
    identifier = _IDENTIFIER_2,
    firstElementData = Vector(_THREE, _THREE, _THREE))

  val onlineRecords: Vector[NucleusInput] = Vector(rec01, rec02)




}
