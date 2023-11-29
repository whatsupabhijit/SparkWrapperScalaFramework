package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.converter.Converter

import java.io.Serializable

case class AtomOutput[O](
                        name: String,
                        value: Vector[O],
                        elementName: String)(implicit c: Converter[O]) extends Serializable

object AtomOutput {
  /**
   * Type alias to complex list of Atoms with ouput type associated
   * */
  type AtomTable = List[AtomOutput[_]]
  type AtomMap = Map[String, AtomOutput[_]]

  /**
   * type alias methods
   * @param atomList list of Atoms
   */
  implicit class AtomOutputDerivations(atomList: AtomTable) {

    /**
     * Converts the AtomTable to a simple Map
     * @return a map where name of the Atom is the key
     */
    def toAtomMap: Map[String, AtomOutput[_]] = atomList.map(x => x.name -> x).toMap

  }

  implicit class AtomMapDerivations(atomMap: AtomMap) {
    private def valueOf(key: String): Vector[_] = atomMap(key).value
    def valueOfRecord(index: Int)(implicit key: String): Any = valueOf(key)(index)
  }

}
