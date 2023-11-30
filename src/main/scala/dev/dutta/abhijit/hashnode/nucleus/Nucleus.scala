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
  lazy val atoms: List[Atom[_, _]] = compoundBuffer.toList.map(_.atoms).flatten // TODO: TODO_ID_1
  lazy val schema: StructType = StructType(atoms.map(_.structField))
  implicit lazy val encoder: ExpressionEncoder[Row] = RowEncoder(schema = schema)

  // Logic for handling Vector - Online
  def calc(records: Vector[NucleusInput]): AtomMap = compoundBuffer.toList
    .flatMap(_.mutateAndCalc(records)).toAtomMap

  // Logic for handling Spark Dataset - Batch
  def withAtoms(ni: NucleusInput): Row = Row.fromSeq(compoundBuffer.map(
    a => {
      println("a.withAtoms(ni): " + a.withAtoms(ni))
      a.withAtoms(ni)
     }
  ))

  def calc(records: Dataset[NucleusInput]): DataFrame = {
    println("schema in calc: " + schema)
    println("atoms:" + atoms.map(a => (a.structField, a.atomName)))
    records.map(rec => withAtoms(rec))
  }

}

object Nucleus {
  implicit val nucleus: Nucleus = new Nucleus()
}
