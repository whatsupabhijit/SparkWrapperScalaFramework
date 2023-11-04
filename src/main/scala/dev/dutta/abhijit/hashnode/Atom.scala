package dev.dutta.abhijit.hashnode

import scala.reflect.runtime.universe.TypeTag

import java.io.Serializable

class Atom[I <: ElementOverriders: TypeTag, O: TypeTag]
(name: String,
 description: String,
 whenDefault: I => O,
 isNoAtom: Boolean,
 whenNoAtom: I => O,
 calcAtom: I => O)(implicit c: Converter[O], element: Element[I])
  extends Serializable with Calculable[I] {
  /**
   * Calculates the output of a vector of specific source type I
   *
   * @param i Vector of I
   * @return List of Atom's calculated values (String for now, will be changed to some custom class)
   * */
  override def calc(i: I): O = {
    try {
      if (i.isNoAtomFound && )
    }

  }
}

object Atom
