package com.clouway.subnets.core;

import java.util.List;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public interface NetworkNodeFinder {

  /**
   * Return a List containing all NetworkNode's that have a common parent id.
   *
   * @param id of the parent node.
   * @return a list with all NetworkNodes.
   */
  List<NetworkNode> findAllWithParent(String id);

}
