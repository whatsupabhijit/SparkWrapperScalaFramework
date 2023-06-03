package dev.dutta.abhijit.hashnode

import java.io.Serializable

/**
 * This trait should be provided as input to all relevant atoms.
 * An element is Neutron when corresponding atom can't be calculated.
 * When an atom can't be calculated depends on own business logic.
 * */

trait ElementOverriders extends Serializable {
  def isNoAtomFound: Boolean
  def elementId: String
  def elementMetaData: Vector[String]
}
