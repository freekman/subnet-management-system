package com.clouway.subnets.core;

import java.util.List;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public interface NetworkNodeStore {

  /**
   * Register a new child node with a given name and parentId.
   * After registering the new node then its hosting node becomes a parent.
   * <p>To register a root node a id of 'root' needs to used.</p>
   *
   * @param node new node to be registered.
   * @return id of the registered node
   */
  String register(NewNetworkNode node);

  /**
   * Delete a registered node and its children. If the deleted node is a single child node then its parent becomes a child node.
   *
   * @param id of the node.
   */
  void delete(String id);

}
