package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.converter.Converter
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.types.{DataType, StructField}
import org.apache.spark.sql.{DataFrame, Dataset}

import java.io.Serializable
import scala.reflect.runtime.universe.TypeTag

class Atom[I <: ElementOverriders: TypeTag, O: TypeTag]
(
  name: String,
  description: String,
  howToCalcDefault: I => O,
  isNoAtomNotApplicable: Boolean = false,
  howToCalcNoAtom: I => O,
    // isCalcAtomRequired: Boolean // TODO: TODO_ID:1 Atom need not be generated always
  howToCalcAtom: I => O
)(
  implicit c: Converter[O], element: Element[I, _]
) extends Serializable with Calculable[I] {

  val atomName: String = name
  private val calcDefault: I => O = howToCalcDefault
  private val calcNoAtom: I => O = howToCalcNoAtom
  private val calcAtom: I => O = howToCalcAtom
  val elementName: String = element.elementName

  /**
   * Calculates the output of a vector of specific source type I
   *
   * @return List of Atom's calculated values (String for now, will be changed to some custom class)
   * */
   lazy val logicForAnAtom: I => O = (i: I) => {
     try {
       if (isNoAtomNotApplicable && i.isNoAtomFound) calcNoAtom(i)
       else if (i.isToBeDefaulted) calcDefault(i)
       else calcAtom(i)
     } catch {
       case exception: Exception =>
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
   }

  private def toOutput(v: Vector[O]): List[AtomOutput[O]] =
    List(
      AtomOutput(
        name = atomName,
        value = v,
        elementName = elementName
      )
    )

  // Logic for handling Vector - Online
  override def calc(records: Vector[I]): List[AtomOutput[_]] = toOutput(records.map(logicForAnAtom))

  // Logic for handling Spark Dataset - Batch
  implicit val encoder: ExpressionEncoder[O] = ExpressionEncoder[O]
  override def calc(records: Dataset[I]): DataFrame = records.map(logicForAnAtom).toDF()

  // Methods for parent class i.e. Element
  private def sparkDataType: DataType = c.dataType
  def structField: StructField = StructField(atomName, sparkDataType, nullable = false)

}

object Atom extends Serializable {

  def apply[I <: ElementOverriders: TypeTag, O: TypeTag](
    name: String,
    description: String,
    defaultValue: I => O,
    isNoAtomNotApplicable: Boolean,
    noAtomValue: I => O
  )(
    calc: I => O
  )(
    implicit c: Converter[O], element: Element[I, _]
  ): Atom[I, O] = {

    val atom = new Atom[I, O](
      name = name,
      description = description,
      howToCalcDefault = defaultValue,
      isNoAtomNotApplicable = isNoAtomNotApplicable,
      howToCalcNoAtom = noAtomValue,
      howToCalcAtom = calc
    )

    element.add(atom)
    atom
  }

}
