package com.clouway.subnets.core;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public interface SubnetStore {

  /**
   * Register a new subnet if that does not throw an exception all the bindings for the subnet
   * will be saved.
   *
   * return OverlappingSubnetException if the new subnet range is overlapping with an existing one,
   * SubnetAlreadyExistException if the new subnet is equal to an existing subnet.
   */
  String register(NewSubnet newSubnet);


  /**
   * Updates the NewSubnet by using the slash value
   * Trows OverlappingSubnetException if the new range for the subnet is overlapping.
   *
   * @param slash-int for the new slash.
   * returns  a new NewSlash containing the new slash value
   */
  NewSlash resize(String id, int slash);

}
