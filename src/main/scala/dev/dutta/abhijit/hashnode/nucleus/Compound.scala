package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import dev.dutta.abhijit.hashnode.schema.FlattenedInput

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
  def add(element: Element[I]): Unit = listOfElements += element

  // Class Variables and Methods
  private val listOfElements: ListBuffer[Element[I]] = new ListBuffer()
  private val allElements: List[Element[I]] = listOfElements.toList
  private val allAtoms: List[Atom[I, _]] = allElements.flatMap(_.allAtoms)  // TODO: TODO_ID_1

  // Logic for handling Vector - Online
  override def calc(i: Vector[I]): AtomTable = allAtoms.flatMap(_.calc(i))

  // Logic for handling Spark Dataset - Batch
  lazy val calcLogics: List[I => _] = allAtoms.map(_.logicForAnAtom)

}