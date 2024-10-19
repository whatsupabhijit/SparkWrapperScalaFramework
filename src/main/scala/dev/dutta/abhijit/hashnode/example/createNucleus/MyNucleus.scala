package dev.dutta.abhijit.hashnode.example.createNucleus

import dev.dutta.abhijit.hashnode.nucleus.Nucleus
import dev.dutta.abhijit.hashnode.schema.NucleusInput

object MyNucleus {

  implicit val nucleus: Nucleus[NucleusInput] = Nucleus()

}
