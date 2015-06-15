package com.clouway.subnets.core;

import java.util.List;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public interface SubnetFinder {

  /**
   * Finds all subnets if there are no subnets in the db an empty list is returned.
   */
  List<Subnet> findAll();

}
