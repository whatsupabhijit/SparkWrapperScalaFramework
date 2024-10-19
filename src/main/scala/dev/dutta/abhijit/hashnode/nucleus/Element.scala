package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset, Row}

import java.io.Serializable
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe.TypeTag

class Element[I <: ElementOverriders: TypeTag, T]
(
  name: String
) (
    implicit compound: Compound[I, T]
) extends Calculable[I] with Serializable {

  val elementName: String = name

  // Methods for child class i.e. Atom
  val atomsBuffer: ListBuffer[Atom[I, _]] = ListBuffer()
  def add(atom: Atom[I, _]): Unit = atomsBuffer += atom

  // Class Variables and Methods
  lazy val atoms: List[Atom[I, _]] = atomsBuffer.toList // TODO: TODO_ID_1
  lazy val atomLogics: List[I => _] = atomsBuffer.toList.map(_.logicForAnAtom)

  // Logic for handling Vector - Online
  override def calc(records: Vector[I]): AtomTable = atoms.flatMap(_.calc(records))

  // Logic for handling Spark Dataset - Batch
  lazy val schema: StructType = StructType(atoms.map(_.structField))
  implicit val encoder: ExpressionEncoder[Row] = RowEncoder(schema = schema)
  def withAtoms(aRecord: I): Row = Row.fromSeq(atomLogics.map(_(aRecord)))
  override def calc(records: Dataset[I]): DataFrame = records.map(withAtoms)

}

object Element extends Serializable {

  def apply[I <: ElementOverriders: TypeTag, T](elementName: String)(implicit compound: Compound[I, T])
  : Element[I, T] = {
    val element: Element[I, T] = new Element[I, T](elementName)
    compound.add(element)
    element
  }

}
