package dev.dutta.abhijit.hashnode

import java.io.Serializable
import scala.reflect.runtime.universe.TypeTag

class Compound[I <: ElementOverriders: TypeTag](elementTransformer: FlattenedInput => I)
                                               (implicit val core: Core) extends Serializable