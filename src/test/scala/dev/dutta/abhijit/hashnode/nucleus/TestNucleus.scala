package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.Session
import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.guide.elements.SampleElement
import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import dev.dutta.abhijit.hashnode.schema.NucleusInput
import org.scalatest.funsuite.AnyFunSuite
import dev.dutta.abhijit.hashnode.nucleus.Nucleus.nucleus
import dev.dutta.abhijit.hashnode.testData.NucleusTestData._
import org.apache.spark.sql.{DataFrame, Dataset}

class TestNucleus extends AnyFunSuite with Session {

  println("nucleus atoms: " + SampleElement.element.elementName)

  test("Online calc() should return 6 for the first atom, 9 for the second atom") {
    val onlineResult: AtomTable = nucleus.calc(onlineRecords)
    println("onlineResult: " + onlineResult.toAtomMap)
    assertResult(List(_SIX, _SIX))(onlineResult.toAtomMap.get("Atom-1"))
  }

  test("Batch calc() should return 6 for the first atom, 9 for the second atom") {
    import spark.implicits._
    val batchRecords: Dataset[NucleusInput] = Seq(rec01, rec02).toDS()
    val batchResult: DataFrame = nucleus.calcDataset(batchRecords)
    batchResult.show()
    assert(batchResult.count() != _ZERO)

  }
}
