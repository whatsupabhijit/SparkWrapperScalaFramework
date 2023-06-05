package dev.dutta.abhijit.hashnode

import java.io.Serializable

case class AtomOutput[O](
                        name: String,
                        value: Vector[O],
                        elementName: String)
                        (implicit converter: Converter[O]) extends Serializable

object AtomOutput {
  /**
   * Type alias to complex list of Atoms with ouput type associated
   * */
  private type atomTable = List[AtomOutput[_]]

  /**
   * type alias methods
   * @param atomList list of Atoms
   */
  implicit class AtomOutputDerivations(atomList: atomTable) {

    /**
     * Converts the AtomTable to a simple Map
     * @return a map where name of the Atom is the key
     */
    def toMap: Map[String, AtomOutput[_]] = atomList.toMap

  }

}
