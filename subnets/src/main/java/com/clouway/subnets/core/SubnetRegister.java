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

  /**
   * Removes subnet by given ID
   * Throws IllegalRequestException if subnet not found
   *
   * @param id
   */
  void remove(String id);

  /**
   * Update the old description (by default the subnet description is note)
   * @param id-the subnet id
   * @param message-message obj containing the new message
   */
  void updateDescription(String id, Message message);
}
