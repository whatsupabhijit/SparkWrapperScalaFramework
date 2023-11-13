package dev.dutta.abhijit.hashnode.guide

import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.guide.schema.FirstElementInput
import dev.dutta.abhijit.hashnode.nucleus.{Atom, Compound, Element}
import dev.dutta.abhijit.hashnode.schema.NucleusInput

object SampleFirstElement {

  // Mutate the NucleusInput to the desired Input Layout applicable to that Element.
  def mutateFirstElementInput(nucleusInput: NucleusInput): FirstElementInput =
    FirstElementInput(nucleusInput.firstElementData, nucleusInput.isNoAtomFound)

  implicit val firstCompound: Compound[FirstElementInput] =
    Compound[FirstElementInput](mutator = mutateFirstElementInput)

  implicit val firstElement: Element[FirstElementInput] =
    Element[FirstElementInput](elementName = "First Element")

  val atom1: Atom[FirstElementInput, Int] =
    Atom[FirstElementInput, Int](
      name = "Atom 1",
      description = "Atom 1 lorem ipsum lorem ipsum",
      defaultValue = (_: FirstElementInput) => _ZERO
    )(
      isNoAtomNotApplicable = true,
      noAtomValue = (_: FirstElementInput) => _MINUS1
    )(
      calc = (i: FirstElementInput) => {
        i.firstData.filter(_.isValidInt).sum
    })

}
