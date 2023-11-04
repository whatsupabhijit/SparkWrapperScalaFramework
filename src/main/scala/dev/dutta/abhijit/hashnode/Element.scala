package dev.dutta.abhijit.hashnode

import java.io.Serializable
import scala.reflect.runtime.universe.TypeTag

class Element[I <: ElementOverriders: TypeTag](name: String)
                                              (implicit compound: Compound[I]) extends Serializable {
  val elementName: String = name
}