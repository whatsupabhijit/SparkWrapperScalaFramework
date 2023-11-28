package dev.dutta.abhijit.hashnode.guide.elements

import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.constants.StringConstants._
import dev.dutta.abhijit.hashnode.guide.schema.ElementInput
import dev.dutta.abhijit.hashnode.nucleus.{Atom, Compound, Element}
import dev.dutta.abhijit.hashnode.schema.NucleusInput
import dev.dutta.abhijit.hashnode.nucleus.Nucleus.nucleus

object SampleElement {

  // Mutate the NucleusInput to the desired Input Layout applicable to that Element.
  private def mutate(nucleusInput: NucleusInput): ElementInput =
    ElementInput(nucleusInput.firstElementData, nucleusInput.isNoAtomFound)

  implicit val compound: Compound[ElementInput] = Compound[ElementInput](mutate)

  implicit val element: Element[ElementInput] = Element[ElementInput](elementName = "First Element")

  val atom1: Atom[ElementInput, Int] =
    Atom[ElementInput, Int](
      name = _ATOM_1_NAME,
      description = _ATOM_1_DESC,
      defaultValue = (_: ElementInput) => _ZERO,
      isNoAtomNotApplicable = true,
      noAtomValue = (_: ElementInput) => _MINUS1
    )((i: ElementInput) => {
        i.firstData.filter(_.isValidInt).sum
    })

}
