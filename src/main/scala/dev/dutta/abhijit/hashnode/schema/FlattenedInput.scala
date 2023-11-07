package dev.dutta.abhijit.hashnode.schema

import dev.dutta.abhijit.hashnode.constants.StringConstants._
import dev.dutta.abhijit.hashnode.nucleus.ElementOverriders

case class FlattenedInput(
                           identifier: String = _EMPTY,
                           dataSource1: Vector[Source1Derivation] = Vector()
                         ) extends ElementOverriders
