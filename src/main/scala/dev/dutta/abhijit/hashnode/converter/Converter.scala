package dev.dutta.abhijit.hashnode.converter

import org.apache.spark.sql.types._

import java.io.Serializable
import java.sql.{Date, Timestamp}

/**
 * This is mainly used to limit the output type of Atom.
 * This also helps in mapping scala Type to corresponding Spark data types
 * @tparam O Output Type of the Atom
 * */
sealed trait Converter[O] extends Serializable {
  def dataType: DataType
}

object Converter {

  implicit val intConverter: Converter[Int] = new Converter[Int] {
    def dataType: DataType = IntegerType
  }

  implicit val longConverter: Converter[Long] = new Converter[Long] {
    def dataType: DataType = LongType
  }

  implicit val floatConverter: Converter[Float] = new Converter[Float] {
    def dataType: DataType = FloatType
  }

  implicit val doubleConverter: Converter[Double] = new Converter[Double] {
    def dataType: DataType = DoubleType
  }

  implicit val booleanConverter: Converter[Boolean] = new Converter[Boolean] {
    def dataType: DataType = BooleanType
  }

  implicit val timestampConverter: Converter[Timestamp] = new Converter[Timestamp] {
    def dataType: DataType = TimestampType
  }

  implicit val dateConverter: Converter[Date] = new Converter[Date] {
    def dataType: DataType = DateType
  }

  implicit val stringConverter: Converter[String] = new Converter[String] {
    def dataType: DataType = StringType
  }

}
