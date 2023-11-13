package dev.dutta.abhijit.hashnode.guide.schema

import dev.dutta.abhijit.hashnode.nucleus.ElementOverriders

// You can create your ElementInput schema with the required data sources
// For simplicity we have created a vector of Int but
// for practical implementation it would be your custom data type vector.

// Make sure to add those variables which are defined in ElementOverriders
case class FirstElementInput(firstData: Vector[Int],
                             isNoAtomFound: Boolean) extends ElementOverriders {
  lazy val isToBeDefaulted: Boolean = firstData.isEmpty
}
