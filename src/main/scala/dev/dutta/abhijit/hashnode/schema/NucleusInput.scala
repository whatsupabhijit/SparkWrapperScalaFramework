package dev.dutta.abhijit.hashnode.schema

import dev.dutta.abhijit.hashnode.constants.StringConstants._
import dev.dutta.abhijit.hashnode.nucleus.ElementOverriders

case class NucleusInput(identifier: String = _EMPTY,
    // TODO: Find a way to dynamically populate this data source vectors whoever is calling nucleus.
                        firstElementData: Vector[Int]
                       ) extends ElementOverriders {
  lazy val isNoAtomFound: Boolean = false // TODO: Apply the logic here
  lazy val isToBeDefaulted: Boolean = false // TODO: Apply the actual logic
}
