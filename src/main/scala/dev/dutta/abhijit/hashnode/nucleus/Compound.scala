package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import dev.dutta.abhijit.hashnode.schema.NucleusInput
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.types.{StructField, StructType}

import java.io.Serializable
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe.TypeTag

class Compound[I <: ElementOverriders: TypeTag]
(mutator: NucleusInput => I)
(implicit val nucleus: Nucleus)
  extends Calculable[I] with Serializable {

  // Methods for child class i.e. Element
  val elementsBuffer: ListBuffer[Element[I]] = new ListBuffer()
  def add(element: Element[I]): Unit = elementsBuffer.append(element)

  // Class Variables and Methods
  implicit class ElementListBufferDerivations(elementBuffer: ListBuffer[Element[I]]) {
    // TODO: TODO_ID_1
    def getAtoms: List[Atom[I, _]] = elementBuffer.toList.flatMap(_.atomsBuffer.toList)
  }

  lazy val atoms: List[Atom[I, _]] = elementsBuffer.getAtoms
  lazy val atomLogics: List[I => _] = elementsBuffer.getAtoms.map(_.logicForAnAtom)
  lazy val schema2: Seq[StructField] = elementsBuffer.getAtoms.map(_.structField) // DEBUG

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
  def withAtoms(aRecord: NucleusInput): Row = Row.fromSeq(elementsBuffer.getAtoms.map(_.logicForAnAtom).map(_(mutator(aRecord))))
  override def calc(records: Dataset[I]): DataFrame = records.map(withAtoms)
}

object Compound extends Serializable {

  def apply[I <: ElementOverriders : TypeTag]
  (mutator: NucleusInput => I)(implicit nucleus: Nucleus): Compound[I] = {
    val compound = new Compound[I](mutator)
    nucleus.add(compound)
    compound
  }

}
