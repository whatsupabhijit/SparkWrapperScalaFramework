package dev.dutta.abhijit.hashnode.schema

import dev.dutta.abhijit.hashnode.ElementOverriders
import dev.dutta.abhijit.hashnode.constants.StringConstants._

case class FlattenedInput(
                           identifier: String = EMPTY_STRING,
                           dataSource1: Vector[Source1Derivation] = Vector()
                         ) extends ElementOverriders
