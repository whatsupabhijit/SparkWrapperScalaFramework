package dev.dutta.abhijit.hashnode.testData

import dev.dutta.abhijit.hashnode.schema.NucleusInput

object NucleusTestData {

  val rec01: NucleusInput = NucleusInput(
    identifier = "rec#1",
    firstElementData = Vector(1, 2, 3))

  val rec02: NucleusInput = NucleusInput(
    identifier = "rec#2",
    firstElementData = Vector(1, 2, 3))

  val onlineRecords: Vector[NucleusInput] = Vector(rec01, rec02)

}
