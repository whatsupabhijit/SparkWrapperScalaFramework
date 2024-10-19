package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.constants.StringConstants._
import dev.dutta.abhijit.hashnode.example.createNucleus.MyNucleus.nucleus
import dev.dutta.abhijit.hashnode.example.elements.SampleElement
import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomMap
import dev.dutta.abhijit.hashnode.testData.NucleusTestData.onlineRecords
import org.scalatest.funsuite.AnyFunSuite

class TestAtom1Online extends AnyFunSuite {

  implicit val atomKeyToTest: String = _ATOM_1_NAME

  // TODO: Figure out a way to initialize all the example elements.
  println(s"SampleElement: ${SampleElement.getClass}")

  val onlineResultMap: AtomMap = nucleus.calc(onlineRecords)

  test("calc() on 2 records should return 6, 9 for the first atom") {
    assertResult(List(_SIX, _NINE))(onlineRecords.indices.map(r => onlineResultMap.valueOfRecord(r)))
  }

}
