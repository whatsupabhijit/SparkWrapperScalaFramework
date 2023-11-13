package dev.dutta.abhijit.hashnode.nucleus

import dev.dutta.abhijit.hashnode.nucleus.AtomOutput.AtomTable
import org.apache.spark.sql.{DataFrame, Dataset}
/**
 * A Calculable object is associated with a list of Atoms that can be called
 * @tparam I Common input type of Atoms associated with the object (it must not be a Neutron)
 * */
trait Calculable[I <: ElementOverriders] {

  /**
   * Calculates the output of a vector of specific source type I
   * @param i Vector of I
   * @return List of Atom's calculated values (String for now, will be changed to some custom class)
   * */
  def calc(i: Vector[I]): AtomTable

  /**
   * Calculates the output of a dataset of specific source type I
   * @param i Dataset of I
   * @return Dataframe of Atom's calculated values
   * */
  def calc(i: Dataset[I]): DataFrame
}
