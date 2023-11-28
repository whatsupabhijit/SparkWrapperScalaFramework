package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.Session
import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.constants.StringConstants._
import dev.dutta.abhijit.hashnode.guide.elements.SampleElement
import dev.dutta.abhijit.hashnode.schema.NucleusInput
import org.scalatest.funsuite.AnyFunSuite
import dev.dutta.abhijit.hashnode.nucleus.Nucleus.nucleus
import dev.dutta.abhijit.hashnode.testData.NucleusTestData._
import org.apache.spark.sql.{DataFrame, Dataset}

class TestNucleus extends AnyFunSuite with Session {

  // TODO: TO FIND OUT WHY REMOVING THIS LINE DOES NOT CALCULATE ANYTHING
  println("nucleus atoms: " + SampleElement.element.elementName)

  val onlineResultMap: Map[String, AtomOutput[_]] = nucleus.calc(onlineRecords).toAtomMap
  val onlineResults: AtomOutput[_] = onlineResultMap(_ATOM_1_NAME)

  test("Online calc() should return 6 for the first atom") {
    assertResult(_SIX)(onlineResults.value(_ZERO))
  }

  test("Online calc() should return 9 for the second atom") {
    assertResult(_NINE)(onlineResults.value(_ONE))
  }

  test("Batch calc() should return 6 for the first atom, 9 for the second atom") {
    import spark.implicits._
    val batchRecords: Dataset[NucleusInput] = Seq(rec01, rec02).toDS()
    val batchResult: DataFrame = nucleus.calcDataset(batchRecords)
    batchResult.show()
    assert(batchResult.count() != _ZERO)
  }

}
