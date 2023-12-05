package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomMap
import dev.dutta.abhijit.hashnode.schema.NucleusInput
import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.sql.types.StructType

import java.io.Serializable
import scala.collection.mutable.ListBuffer

class Nucleus extends Serializable {
  // Methods for child class i.e. Compound
  private val compoundBuffer: ListBuffer[Compound[_]] = new ListBuffer()
  def add(compound: Compound[_]): Unit = compoundBuffer.append(compound)

  // Class Variables and Methods
  // Do not change the .map.flatten to flatMap as it will impact the output
  implicit class ElementListBufferDerivations(compoundBuffer: ListBuffer[Compound[_]]) {
    // TODO: TODO_ID_1
    def getAtoms: List[Atom[_, _]] = compoundBuffer.toList.map(_.atoms).flatten
  }
  lazy val atoms: List[Atom[_, _]] = compoundBuffer.getAtoms // TODO: TODO_ID_1
  lazy val schema: StructType = StructType(compoundBuffer.getAtoms.map(_.structField))
  implicit lazy val encoder: ExpressionEncoder[Row] = RowEncoder(schema = schema)

  // Logic for handling Vector - Online
  def calc(records: Vector[NucleusInput]): AtomMap = compoundBuffer.toList
    .flatMap(_.mutateAndCalc(records)).toAtomMap

  // Logic for handling Spark Dataset - Batch
  def withAtoms(ni: NucleusInput): Row = Row.fromSeq(compoundBuffer.toList.map(_.withAtoms(ni)))

  def calc(records: Dataset[NucleusInput]): DataFrame = {
    lazy val schema: StructType = StructType(compoundBuffer.toList.map(_.schema2).flatten) // DEBUG
    implicit lazy val encoder: ExpressionEncoder[Row] = RowEncoder(schema = schema) // DEBUG
    println("compoundBuffer size:" + compoundBuffer.size) // DEBUG
    println("schema in calc: " + schema) // DEBUG
    println("final output: ") // DEBUG
    val dd: DataFrame = records.map(rec => withAtoms(rec)) // DEBUG
    dd.printSchema() // DEBUG
    records.map(rec => withAtoms(rec))
  }

}

object Nucleus {
  implicit val nucleus: Nucleus = new Nucleus()
}
