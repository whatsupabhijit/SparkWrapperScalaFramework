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
    // isCaclAtomRequired: Boolean // TODO: It is possible that Atom need not be generated always
  howToCalcAtom: I => O
)(
  belongsToWhichElement: Element[I]
)(
  implicit c: Converter[O],
) extends Serializable with Calculable[I] {

  private val atomName: String = name
  private val calcDefault: I => O = howToCalcDefault
  private val calcNoAtom: I => O = howToCalcNoAtom
  private val calcAtom: I => O = howToCalcAtom
  private val elementName: String = belongsToWhichElement.elementName

  /**
   * Calculates the output of a vector of specific source type I
   *
   * @param i Vector of I
   * @return List of Atom's calculated values (String for now, will be changed to some custom class)
   * */
   val calcLogic: I => O = (i: I) => {
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

  override def calc(i: Vector[I]): List[AtomOutput[O]] =
    toOutput(i.map(calcLogic))


  override def calcDataset(i: Dataset[I]): DataFrame = {
    // ExpressionEncoder is a class in Apache Spark's DataFrame API that provides a way to convert
    // between Spark's internal binary format (Catalyst expressions) and the corresponding JVM objects.
    // It's used to encode and decode data when working with Datasets in Spark.
    // In simple terms, it helps Spark understand how to serialize and deserialize data
    // when you convert between Datasets and DataFrames.
    implicit val encoder: ExpressionEncoder[O] = ExpressionEncoder[O]
    i.map((row: I) => calcLogic(row)).toDF()
  }

  private def sparkDataType: DataType = c.dataType

  def structField: StructField = StructField(atomName, sparkDataType, nullable = false)

}

object Atom extends Serializable {

  def apply[I <: ElementOverriders: TypeTag, O: TypeTag](
    name: String,
    description: String,
    defaultValue: I => O
  )(
    isNoAtomNotApplicable: Boolean,
    noAtomValue: I => O
  )(
    calc: I => O
  )(
    element: Element[I]
  )(
    implicit c: Converter[O]
  ): Atom[I, O] = {

    val atom = new Atom[I, O](
      name = name,
      description = description,
      howToCalcDefault = defaultValue,
      isNoAtomNotApplicable = isNoAtomNotApplicable,
      howToCalcNoAtom = noAtomValue,
      howToCalcAtom = calc
    )(element)

    element.add(atom)

    atom
  }

  implicit class CalcDataset[I <: ElementOverriders](dataset: Dataset[I]) {
    def calcSpark[O](atom: Atom[I, O]): DataFrame =
      atom.calcDataset(dataset)
  }

}
