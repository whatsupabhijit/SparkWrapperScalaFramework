package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.Session
import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.guide.elements.SampleElement
import dev.dutta.abhijit.hashnode.guide.schema.ElementInput
import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import dev.dutta.abhijit.hashnode.schema.NucleusInput
import org.scalatest.funsuite.AnyFunSuite
import dev.dutta.abhijit.hashnode.nucleus.Nucleus.nucleus
import org.apache.spark.sql.{DataFrame, Dataset}

class TestNucleus extends AnyFunSuite with Session {

//  println(SampleFirstElement.firstCompound.schema)
//  println(SampleFirstElement.firstElement.elementName)
//  println("compound added?: " + nucleus.allCompounds)
//  println("element added?: " + nucleus.allCompounds.head.allElements)
//  println("atom added?: " + nucleus.allCompounds.head.allElements.head.allAtoms)

  val rec01: NucleusInput = NucleusInput(
    identifier = "rec#1",
    firstElementData = Vector(1, 2, 3))

  val rec02: NucleusInput = NucleusInput(
    identifier = "rec#2",
    firstElementData = Vector(1, 2, 3))

  val onlineRecords: Vector[NucleusInput] = Vector(rec01, rec02)

//
//  import spark.implicits._
//  val batchRecords: Dataset[NucleusInput] = Seq(rec01, rec02).toDS()
//  val batchResult: DataFrame = nucleus.calc(batchRecords)

  test("Online calc() should return 6 for the first atom, 9 for the second atom") {
    println("nucleus atoms: " + SampleElement.element.elementName)

    def mutate(nucleusInput: NucleusInput): ElementInput =
      ElementInput(nucleusInput.firstElementData, nucleusInput.isNoAtomFound)

    implicit val compound: Compound[ElementInput] = Compound[ElementInput](mutate)

    implicit val element: Element[ElementInput] = Element[ElementInput](elementName = "First Element")

    val atom1: Atom[ElementInput, Int] =
      Atom[ElementInput, Int](
        name = "Atom-1",
        description = "Atom 1 lorem ipsum lorem ipsum",
        defaultValue = (_: ElementInput) => _ZERO,
        isNoAtomNotApplicable = true,
        noAtomValue = (_: ElementInput) => _MINUS1
      )((i: ElementInput) => {
        i.firstData.filter(_.isValidInt).sum
      })

    println("atoms:" + SampleElement.element.atoms.map(_.atomName))
    val onlineResult: AtomTable = nucleus.calc(onlineRecords)
    println("onlineResult: " + onlineResult.toAtomMap)
    //    assertResult(List(6, 6))(onlineResult.toAtomMap.get("Atom 1"))
    //    batchResult.show()
  }
}
