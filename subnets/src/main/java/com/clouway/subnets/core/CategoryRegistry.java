package com.clouway.subnets.core;

import java.util.List;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public interface CategoryRegistry {

  /**
   * Attempt to register new nodeId and return a MessageResponse containing information about the execution of the operation.
   *
   * @param category new nodeId to be registered if not existing.
   * @return MessageResponse containing information about successful registration or e already existing nodeId.
   */
  MessageResponse register(NewCategory category);

  /**
   * Find all registered categories.
   *
   * @return list containing all categories.
   */
  List<Category> findAll();

  /**
   * Deletes a nodeId from the registry.
   *
   * @param id of nodeId to be deleted.
   * @return A MessageResponse containing information about the removal of a nodeId.
   */
  MessageResponse delete(String id);
}
