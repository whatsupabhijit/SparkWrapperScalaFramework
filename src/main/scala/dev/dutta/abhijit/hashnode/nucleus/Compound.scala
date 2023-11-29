package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import dev.dutta.abhijit.hashnode.schema.NucleusInput
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.types.StructType

import java.io.Serializable
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe.TypeTag

class Compound[I <: ElementOverriders: TypeTag]
(mutator: NucleusInput => I)
(implicit val nucleus: Nucleus)
  extends Calculable[I] with Serializable {

  // Methods for child class i.e. Element
  private val elementsBuffer: ListBuffer[Element[I]] = new ListBuffer()
  def add(element: Element[I]): Unit = elementsBuffer.append(element)

  // Class Variables and Methods
  lazy val elements: List[Element[I]] = elementsBuffer.toList
  lazy val atoms: List[Atom[I, _]] = elements.flatMap(_.atomsBuffer.toList)  // TODO: TODO_ID_1
  lazy val atomLogics: List[I => _] = atoms.map(_.logicForAnAtom)

  // Logic for handling Vector - Online
  override def calc(records: Vector[I]): AtomTable = elementsBuffer.toList
    .flatMap(_.atomsBuffer.toList).flatMap(_.calc(records))

  def mutateAndCalc(records: Vector[NucleusInput]): AtomTable = {
    val mutatedInput: Vector[I] = records.map(mutator)
    calc(mutatedInput)
  }

  // Logic for handling Spark Dataset - Batch
  lazy val schema: StructType = StructType(atoms.map(_.structField))
  implicit val encoder: ExpressionEncoder[Row] = RowEncoder(schema = schema)
  def withAtoms(aRecord: I): Row = Row.fromSeq(atomLogics.map(_(aRecord)))
  def withAtoms(aRecord: NucleusInput): Row = Row.fromSeq(atomLogics.map(_(mutator(aRecord))))
  override def calc(records: Dataset[I]): DataFrame = records.map(withAtoms)
}

object Compound extends Serializable {

  def apply[I <: ElementOverriders : TypeTag]
  (mutator: NucleusInput => I)(implicit nucleus: Nucleus): Compound[I] = {
    val compound = new Compound[I](mutator)
    nucleus.add(compound)
    compound
  }

  implicit class CalcOnline[I <: ElementOverriders](records: Vector[I]) {
    def calc(compound: Compound[I]): AtomTable = compound.calc(records)
  }

  implicit class CalcBatch[I <: ElementOverriders](records: Dataset[I]) {
    def calc(compound: Compound[I])(implicit ss: SparkSession): DataFrame =
      records.map(compound.withAtoms)(compound.encoder)
  }

}
