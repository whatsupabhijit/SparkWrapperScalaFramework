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