package dev.dutta.abhijit.hashnode.example.elements

import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.constants.StringConstants._
import dev.dutta.abhijit.hashnode.example.createNucleus.MyNucleus.nucleus
import dev.dutta.abhijit.hashnode.example.schema.ElementInput
import dev.dutta.abhijit.hashnode.nucleus.{Atom, Compound, Element}
import dev.dutta.abhijit.hashnode.schema.NucleusInput

object SampleElement {

  lazy val mutator: NucleusInput => ElementInput = (ni: NucleusInput) =>
    ElementInput(ni.firstElementData, ni.isNoAtomFound)

  implicit val compound: Compound[ElementInput, NucleusInput] = Compound(mutator)

  implicit val element: Element[ElementInput, NucleusInput] = Element(elementName = "First Element")

  Atom[ElementInput, Int](
    name = _ATOM_1_NAME,
    description = _ATOM_1_DESC,
    defaultValue = (_: ElementInput) => _ZERO,
    isNoAtomNotApplicable = true,
    noAtomValue = (_: ElementInput) => _MINUS1
  )((i: ElementInput) => {
    i.firstData.filter(_.isValidInt).sum
  })

  // Keep on adding Atoms with business logics below
}
