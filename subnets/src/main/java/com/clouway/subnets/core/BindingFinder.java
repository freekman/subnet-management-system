package com.clouway.subnets.core;

import com.google.common.base.Optional;

import java.util.List;

/**
 * Created by ivan.genchev1989@gmail.com.
 */

public interface BindingFinder {

  /**
   * @return a List of bindings for the subnet or an empty List if no bindings exist.
   */
  List<Binding> findAllBySubnetID(String id);

  Optional<Binding> findByIP(String subnetId,String ip);
}
