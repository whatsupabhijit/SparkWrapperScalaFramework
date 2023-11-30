package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.Session
import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.constants.StringConstants._
import dev.dutta.abhijit.hashnode.guide.elements.SampleElement
import dev.dutta.abhijit.hashnode.guide.elements.SampleElement.element.schema
import dev.dutta.abhijit.hashnode.schema.NucleusInput
import org.scalatest.funsuite.AnyFunSuite
import dev.dutta.abhijit.hashnode.nucleus.Nucleus.nucleus
import dev.dutta.abhijit.hashnode.testData.NucleusTestData._
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.{DataFrame, Dataset}

// TODO: Create a testSuite to run sequentially and add do not discover here
class TestAtom1 extends AnyFunSuite with Session {

  // TODO: TO FIND OUT WHY REMOVING THIS LINE DOES NOT CALCULATE ANYTHING
  println(SampleElement.element.elementName)

  implicit val atomKeyToTest: String = _ATOM_1_NAME

  test("Online calc() should return 6 for the first atom") {
    assertResult(expected = _SIX)(actual = onlineResultMap.valueOfRecord(_ZERO))
  }

  test("Online calc() should return 9 for the second atom") {
    assertResult(expected = _NINE)(actual = onlineResultMap.valueOfRecord(_ONE))
  }

  test("Batch calc() should return 6 for the first atom, 9 for the second atom") {
    import spark.implicits._
//    val batchRecords: Dataset[NucleusInput] = Seq(rec01, rec02).toDF().as[NucleusInput]
    val batchRecords: Dataset[NucleusInput] = Seq(rec01, rec02).toDS()
    val batchResult: DataFrame = nucleus.calc(batchRecords)
    batchResult.printSchema()
    assert(batchResult.count() != _ZERO)
  }

}
