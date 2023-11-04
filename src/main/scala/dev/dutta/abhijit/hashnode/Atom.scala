package dev.dutta.abhijit.hashnode

import dev.dutta.abhijit.hashnode.constants.ExceptionConstants.{ATOM_CALC_001, ATOM_CALC_EXCEPTION, ExceptionCodesWithDesc}

import scala.reflect.runtime.universe.TypeTag
import java.io.Serializable
import scala.util.{Failure, Success, Try}

class Atom[I <: ElementOverriders: TypeTag, O: TypeTag]
(name: String,
 description: String,
 whenDefault: I => O,
 isNoAtomNotApplicable: Boolean = false,
 whenNoAtom: I => O,
 whenAtom: I => O)(implicit c: Converter[O], element: Element[I])
  extends Serializable with Calculable[I] {

  private val atomName: String = name
  private val atomDescription: String = description
  private val calcDefault: I => O = whenDefault
  private val calcNoAtom: I => O = whenNoAtom
  private val calcAtom: I => O = whenAtom
  private val elementName: String = element.elementName

  /**
   * Calculates the output of a vector of specific source type I
   *
   * @param i Vector of I
   * @return List of Atom's calculated values (String for now, will be changed to some custom class)
   * */
  def calcLogic(i: I): O = Try {
      if (isNoAtomNotApplicable && i.isNoAtomFound) calcNoAtom(i)
      else if (i.isToBeDefaulted) calcDefault(i)
      else calcAtom(i)
    } match {
      case Success(value) => value
      case Failure(exception) =>
        println(exception.toString)
        throw new Exception(exception)

          // TODO: Create AtomException class
//      case Failure(exception) =>throw new AtomException(
//        errorRecordIdentifier = i.identifier,
//        errorTimeIdentifier = i.processingDateTime,
//        errorCode = ATOM_CALC_001,
//        errorMessage = ExceptionCodesWithDesc(ATOM_CALC_001),
//        errorException = exception)
    }


  private def toOutput(v: Vector[O]): List[AtomOutput[O]] =
    List(
      AtomOutput(
        name = atomName,
        value = v,
        elementName = elementName
      )
    )


  override def calc(i: Vector[I]): List[AtomOutput[O]] = {
    toOutput(i.map(calcLogic))
//      .toOutput // TODO: Create a VectorFunctions implicit class and put the toOutput there.
  }
}

object Atom
