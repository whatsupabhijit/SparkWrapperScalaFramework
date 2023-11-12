package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

import java.io.Serializable
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe.TypeTag

class Element[I <: ElementOverriders: TypeTag]
(
  name: String
) extends Calculable[I] with Serializable {

  val elementName: String = name

  // Methods for child class i.e. Atom
  def add(atom: Atom[I, _]): Unit = atomsBuffer += atom

  // Class Variables and Methods
  private val atomsBuffer: ListBuffer[Atom[I, _]] = ListBuffer()
  val allAtoms: List[Atom[I, _]] = atomsBuffer.toList // TODO: TODO_ID_1
  private lazy val allAtomLogics: List[I => _] = allAtoms.map(_.logicForAnAtom)

  // Logic for handling Vector - Online
  override def calc(i: Vector[I]): AtomTable = allAtoms.flatMap(_.calc(i))

  // Logic for handling Spark Dataset - Batch
  lazy val schema: StructType = StructType(allAtoms.map(_.structField))
  implicit val encoder: ExpressionEncoder[Row] = RowEncoder(schema = schema)
  private def withCalculatedAtoms(row: I): Row = Row.fromSeq(allAtomLogics.map(_(row)))
  override def calcDataset(i: Dataset[I]): DataFrame = i.map(withCalculatedAtoms)

}

object Element extends Serializable {

  def apply[I <: ElementOverriders: TypeTag]
  (
    elementName: String
  )(
    implicit compound: Compound[I]
  ): Element[I] = {
    val element: Element[I] = new Element[I](name = elementName)
    compound.add(element)
    element
  }


  implicit class CalcVector[I <: ElementOverriders](vector: Vector[I]) {
    def calcVector(element: Element[I]): AtomTable = element.calc(vector)
  }

  implicit class CalcSpark[I <: ElementOverriders](dataset: Dataset[I]) {
    def calcSpark(element: Element[I])(implicit ss: SparkSession): DataFrame =
      dataset.map(row => element.withCalculatedAtoms(row))(element.encoder)
  }

}