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
) (
  implicit compound: Compound[I]
) extends Calculable[I] with Serializable {

  val elementName: String = name

  // Class Variables and Methods
  private val listOfAtoms: ListBuffer[Atom[I, _]] = ListBuffer()
  def add(atom: Atom[I, _]): Unit = listOfAtoms += atom
  private val molecule: List[Atom[I, _]] = listOfAtoms.toList

  // Logic for handling Vector - Online
  override def calc(i: Vector[I]): AtomTable = molecule.flatMap(_.calc(i))


  // Logic for handling Spark Dataset - Batch
  lazy val schema: StructType = StructType(molecule.map(_.structField))
  private def getAtomValuesForEachRow(i: I): List[_] = molecule
    .map(_.calcLogic)
    .map(anAtomCalcFunction => anAtomCalcFunction(i))
  private def functionToCalcAtomsForEachSparkRow(rowInput: I): Row =
    Row.fromSeq(getAtomValuesForEachRow(rowInput))
  override def calcDataset(i: Dataset[I]): DataFrame = {
    implicit val encoder: ExpressionEncoder[Row] = RowEncoder(schema = schema)
    i.map(row => functionToCalcAtomsForEachSparkRow(row))
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

    // TODO: whenever you create an Element you should add the Element to the Compound
//    compound.add(element)

    element
  }


  implicit class CalcVector[I <: ElementOverriders](vector: Vector[I]) {
    def calcVector(element: Element[I]): AtomTable = element.calc(vector)
  }

  implicit class CalcSpark[I <: ElementOverriders](dataset: Dataset[I]) {
    def calcSpark(element: Element[I])(implicit ss: SparkSession): DataFrame =
      dataset.map(row => element.getAtomValuesForEachRow(row))(RowEncoder(element.schema))
  }

}