package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.schema.FlattenedInput

import java.io.Serializable
import scala.reflect.runtime.universe.TypeTag

class Compound[I <: ElementOverriders: TypeTag](elementTransformer: FlattenedInput => I)
                                               (implicit val core: Core) extends Serializable