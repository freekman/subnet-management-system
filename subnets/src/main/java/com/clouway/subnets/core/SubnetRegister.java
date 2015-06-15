package com.clouway.subnets.core;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public interface SubnetRegister {

  /**
   * Register a new subnet
   * return OverlappingSubnetException if the new subnet range is overlapping with an existing one,
   * SubnetAlreadyExistException if the new subnet is equal to an existing subnet.
   */
  String register(NewSubnet newSubnet);

}
