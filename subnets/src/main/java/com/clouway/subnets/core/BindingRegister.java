package com.clouway.subnets.core;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public interface BindingRegister {

  /**
   * Register all bindings for a given subnet in the bindings collection
   * @param subnet-a new subnet
   * @param id-the   id of the new subnet
   */
  void registerPerSubnet(NewSubnet subnet, String id);

  /**
   * Removes all bindings for the given subnet
   * @param subnetId
   */
  void removePerSubnet(String subnetId);
}
