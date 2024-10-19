package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.Session
import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.constants.StringConstants._
import dev.dutta.abhijit.hashnode.example.createNucleus.MyNucleus.nucleus
import dev.dutta.abhijit.hashnode.schema.NucleusInput
import org.scalatest.funsuite.AnyFunSuite
import dev.dutta.abhijit.hashnode.testData.NucleusTestData._
import org.apache.spark.sql.{DataFrame, Dataset}

class TestAtom1Batch extends AnyFunSuite with Session {

  implicit val atomKeyToTest: String = _ATOM_1_NAME

  test("Batch calc() should return 6 for the first atom, 9 for the second atom") {
    import spark.implicits._
//    val batchRecords: Dataset[NucleusInput] = Seq(rec01, rec02).toDF().as[NucleusInput]
    val batchRecords: Dataset[NucleusInput] = Seq(rec01, rec02).toDS()
    val batchResult: DataFrame = nucleus.calc(batchRecords)
    batchResult.printSchema()
    assert(batchResult.count() != _ZERO)
  }

}
