package dev.dutta.abhijit.hashnode

import scala.reflect.runtime.universe.TypeTag

import java.io.Serializable

//
//class Atom[I <: ElementOverriders: TypeTag, O: TypeTag](name: String,
//              description: String,
//              defaultVal: O,
//              calcNoIndication: I => O,
//              calcAtom: I => O) extends Serializable with Calculable[I]

object Atom