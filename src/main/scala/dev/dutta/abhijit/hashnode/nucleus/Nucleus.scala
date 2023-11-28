package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import dev.dutta.abhijit.hashnode.schema.NucleusInput
import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.{Dataset, DataFrame, Row}
import org.apache.spark.sql.types.StructType

import java.io.Serializable
import scala.collection.mutable.ListBuffer

class Nucleus extends Serializable {
  // Methods for child class i.e. Compound
  val compoundBuffer: ListBuffer[Compound[_]] = new ListBuffer()
  def add(compound: Compound[_]): Unit = compoundBuffer.append(compound)

  // Class Variables and Methods
  lazy val compounds: List[Compound[_]] = compoundBuffer.toList
  lazy val atoms: List[Atom[_, _]] = compounds.map(_.atoms).flatten // TODO: TODO_ID_1

  // Logic for handling Vector - Online
  def calc(records: Vector[NucleusInput]): AtomTable = {
    println("total compounds: " + compounds.size)
    compounds.flatMap(_.calcNotMutated(records))
  }

  // Logic for handling Spark Dataset - Batch
  lazy val schema: StructType = StructType(atoms.map(_.structField))
  implicit val encoder: ExpressionEncoder[Row] = RowEncoder(schema = schema)
  def withAtoms(ni: NucleusInput): Row = Row.fromSeq(compounds.map(_.withAtoms(ni)))
  def calcDataset(records: Dataset[NucleusInput]): DataFrame = records.map(withAtoms)
}

object Nucleus {
  implicit val nucleus: Nucleus = new Nucleus()
}
