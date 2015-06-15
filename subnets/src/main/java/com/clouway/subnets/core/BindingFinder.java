package com.clouway.subnets.core;

import java.util.List;

/**
 * Created by ivan.genchev1989@gmail.com.
 */

public interface BindingFinder {
  /**
   * @return a List of bindings for the subnet or an empty List if no bindings exist.
   */
  List<Binding> findAllBySubnetID(String id);

}
