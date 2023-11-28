package dev.dutta.abhijit.hashnode.testData

import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.constants.StringConstants._
import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomMap
import dev.dutta.abhijit.hashnode.nucleus.Nucleus.nucleus
import dev.dutta.abhijit.hashnode.schema.NucleusInput

object NucleusTestData {

  val rec01: NucleusInput = NucleusInput(
    identifier = _IDENTIFIER_1,
    firstElementData = Vector(_ONE, _TWO, _THREE))

  val rec02: NucleusInput = NucleusInput(
    identifier = _IDENTIFIER_2,
    firstElementData = Vector(_THREE, _THREE, _THREE))

  val onlineRecords: Vector[NucleusInput] = Vector(rec01, rec02)

  val onlineResultMap: AtomMap = nucleus.calc(onlineRecords)

}
