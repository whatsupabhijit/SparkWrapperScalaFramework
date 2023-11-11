package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import dev.dutta.abhijit.hashnode.schema.FlattenedInput
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.types.StructType

import java.io.Serializable
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe.TypeTag

class Compound[I <: ElementOverriders: TypeTag]
(
  elementTransformer: FlattenedInput => I
)(
  implicit val nucleus: Nucleus
) extends Calculable[I] with Serializable {

  // Methods for child class i.e. Element
  def add(element: Element[I]): Unit = elementsBuffer += element

  // Class Variables and Methods
  private val elementsBuffer: ListBuffer[Element[I]] = new ListBuffer()
  private val allElements: List[Element[I]] = elementsBuffer.toList
  private val allAtoms: List[Atom[I, _]] = allElements.flatMap(_.allAtoms)  // TODO: TODO_ID_1
  private val allAtomLogics: List[I => _] = allAtoms.map(_.logicForAnAtom)

  // Logic for handling Vector - Online
  override def calc(i: Vector[I]): AtomTable = allAtoms.flatMap(_.calc(i))

  // Logic for handling Spark Dataset - Batch
  lazy val schema: StructType = StructType(allAtoms.map(_.structField))
  implicit val encoder: ExpressionEncoder[Row] = RowEncoder(schema = schema)
  private def withCalculatedAtoms(i: I): Row = Row.fromSeq(allAtomLogics.map(_(i)))
  override def calcDataset(i: Dataset[I]): DataFrame = i.map(withCalculatedAtoms)

}

object Compound extends Serializable {

  def apply[I <: ElementOverriders : TypeTag](
                                               elementTransformer: FlattenedInput => I
                                             ) (implicit nucleus: Nucleus) : Compound[I] = {
    val compound = new Compound[I](elementTransformer)
//    nucleus.add(compound)
    compound
  }

  implicit class CalcVector[I <: ElementOverriders](vector: Vector[I]) {
    def calcVector(compound: Compound[I]): AtomTable = compound.calc(vector)
  }

  implicit class CalcSpark[I <: ElementOverriders](dataset: Dataset[I]) {
    def calcSpark(compound: Compound[I])(implicit ss: SparkSession): DataFrame =
      dataset.map(row => compound.withCalculatedAtoms(row))(compound.encoder)
  }

}