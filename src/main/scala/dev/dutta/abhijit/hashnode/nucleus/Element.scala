package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset, Row}

import java.io.Serializable
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe.TypeTag

class Element[I <: ElementOverriders: TypeTag]
(
  name: String
) (
  implicit compound: Compound[I]
) extends Calculable[I] with Serializable {

  val elementName: String = name

  // Methods for child class i.e. Atom
  def add(atom: Atom[I, _]): Unit = listOfAtoms += atom

  // Class Variables and Methods
  private val listOfAtoms: ListBuffer[Atom[I, _]] = ListBuffer()
  val allAtoms: List[Atom[I, _]] = listOfAtoms.toList // TODO: TODO_ID_1

  // Logic for handling Vector - Online
  override def calc(i: Vector[I]): AtomTable = allAtoms.flatMap(_.calc(i))


  // Logic for handling Spark Dataset - Batch
  lazy val schema: StructType = StructType(allAtoms.map(_.structField))

  private lazy val getAllApplicableAtomValues: I => Row = (i: I) =>
    allAtoms
    .map(_.logicForAnAtom)
    .map(logicForAnAtom => logicForAnAtom(i))

  private def withCalculatedAtoms(i: I): Row = Row.fromSeq(getAllApplicableAtomValues(i))

  override def calcDataset(i: Dataset[I]): DataFrame = {
    implicit val encoder: ExpressionEncoder[Row] = RowEncoder(schema = schema)
    i.map(row => withCalculatedAtoms(row))
  }

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

  // TODO: Fix the batch version as below propery map through the dataset
//  implicit class CalcSpark[I <: ElementOverriders](dataset: Dataset[I]) {
//    def calcSpark(element: Element[I])(implicit ss: SparkSession): DataFrame =
//      dataset.map(row => element.getAllApplicableAtomValues(row))(RowEncoder(element.schema))
//  }

}