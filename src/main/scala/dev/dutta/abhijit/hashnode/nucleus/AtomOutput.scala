package dev.dutta.abhijit.hashnode.nucleus

import java.io.Serializable

case class AtomOutput[O](
                        name: String,
                        value: Vector[O],
                        elementName: String) extends Serializable

object AtomOutput {
  /**
   * Type alias to complex list of Atoms with ouput type associated
   * */
  type AtomTable = List[AtomOutput[_]]

  /**
   * type alias methods
   * @param atomList list of Atoms
   */
  implicit class AtomOutputDerivations(atomList: AtomTable) {

    /**
     * Converts the AtomTable to a simple Map
     * @return a map where name of the Atom is the key
     */
    def toMap: Map[String, AtomOutput[_]] = atomList.map(x => x.name -> x).toMap

  }

}
