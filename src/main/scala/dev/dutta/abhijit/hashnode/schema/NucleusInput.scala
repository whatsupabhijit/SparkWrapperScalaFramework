package dev.dutta.abhijit.hashnode.schema

import dev.dutta.abhijit.hashnode.constants.StringConstants._
import dev.dutta.abhijit.hashnode.nucleus.ElementOverriders

case class NucleusInput(identifier: String = _EMPTY,
                        dataSource1: Vector[Source1Derivation] = Vector()
                       ) extends ElementOverriders {
  lazy val elementId: String = ""
  lazy val isNoAtomFound: Boolean = false // TODO: Apply the logic here
  lazy val isToBeDefaulted: Boolean = false // TODO: Apply the actual logic
}
