package com.clouway.subnets.core;

import java.util.List;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public interface CategoryRegistry {

  /**
   * Attempt to register new category and return a MessageResponse containing information about the execution of the operation.
   *
   * @param category new category to be registered if not existing.
   * @return MessageResponse containing information about successful registration or e already existing category.
   */
  MessageResponse register(NewCategory category);

  /**
   * Find all registered categories.
   *
   * @return list containing all categories.
   */
  List<Category> findAll();

  /**
   * Deletes a category from the registry.
   *
   * @param id of category to be deleted.
   * @return A MessageResponse containing information about the removal of a category.
   */
  MessageResponse delete(String id);
}
