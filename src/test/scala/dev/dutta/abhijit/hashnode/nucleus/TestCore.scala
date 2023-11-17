package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.Session
import dev.dutta.abhijit.hashnode.guide.elements.SampleFirstElement
import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import dev.dutta.abhijit.hashnode.schema.NucleusInput
import org.scalatest.funsuite.AnyFunSuite
import dev.dutta.abhijit.hashnode.nucleus.Nucleus.nucleus
import org.apache.spark.sql.{DataFrame, Dataset}

class TestCore extends AnyFunSuite with Session {

  println(SampleFirstElement.firstElement.elementName)
  println("compound added?: " + nucleus.allCompounds)
//  println("element added?: " + nucleus.allCompounds.head.allElements)
//  println("atom added?: " + nucleus.allCompounds.head.allElements.head.allAtoms)

  val rec01: NucleusInput = NucleusInput(
    identifier = "rec#1",
    firstElementData = Vector(1, 2, 3)
  )

  val rec02: NucleusInput = NucleusInput(
    identifier = "rec#2",
    firstElementData = Vector(2, 3, 4)
  )

  val onlineRecords: Vector[NucleusInput] = Vector(rec01, rec02)

//
//  import spark.implicits._
//  val batchRecords: Dataset[NucleusInput] = Seq(rec01, rec02).toDS()
//  val batchResult: DataFrame = nucleus.calc(batchRecords)

  test("Online calc() should return 6 for the first atom, 9 for the second atom") {
    val onlineResult: AtomTable = nucleus.calc(onlineRecords)
    println("onlineResult: " + onlineResult)
//    batchResult.show()


//    assertResult(List(6, 9))(onlineResult.map(res => res.name -> res.value).toMap.get("Atom 1"))
  }
}
