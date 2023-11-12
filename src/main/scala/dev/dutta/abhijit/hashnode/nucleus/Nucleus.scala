package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import dev.dutta.abhijit.hashnode.schema.NucleusInput
import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.sql.types.StructType

import java.io.Serializable
import scala.collection.mutable.ListBuffer

class Nucleus extends Calculable[_] with Serializable {
  // Methods for child class i.e. Compound
  def add(compound: Compound[_]): Unit = compoundBuffer += compound

  // Class Variables and Methods
  private val compoundBuffer: ListBuffer[Element[_]] = new ListBuffer()
  private val allCompounds: List[Element[_]] = compoundBuffer.toList
  private val allAtoms: List[Atom[_, _]] = allCompounds.flatMap(_.allAtoms) // TODO: TODO_ID_1
  private val allAtomLogics: List[_ => _] = allAtoms.map(_.logicForAnAtom)

  // Logic for handling Vector - Online
//  override def calc(i: Vector[NucleusInput]): AtomTable = allAtoms.flatMap(_.calc())

  // Logic for handling Spark Dataset - Batch
  lazy val schema: StructType = StructType(allAtoms.map(_.structField))
  implicit val encoder: ExpressionEncoder[Row] = RowEncoder(schema = schema)

//  private def withCalculatedAtoms(row: NucleusInput): Row = Row.fromSeq(allAtomLogics.map(_(row)))

//  def calcDataset(i: Dataset[NucleusInput]): DataFrame = i.map(withCalculatedAtoms)
}

object Nucleus {
  implicit val nucleus: Nucleus = new Nucleus()
}
