package dev.dutta.abhijit.hashnode.schema

import dev.dutta.abhijit.hashnode.regulations.Permissions
import java.time.LocalDate

case class Source1Derivation(source1: Source1Layout,
                             appDate: LocalDate,
                             permissions: Permissions)
