package dev.dutta.abhijit.hashnode

import java.io.Serializable
import scala.reflect.runtime.universe.TypeTag

class Element[I <: ElementOverriders: TypeTag](elementName: String)
                                              (implicit compound: Compound[I]) extends Serializable