package dev.dutta.abhijit.hashnode.schema

import dev.dutta.abhijit.hashnode.ElementOverriders

case class FlattenedInput(
                         identifier: String,
                         dataSource1: Vector[Source1Derivation] = Vector()
                         ) extends ElementOverriders
