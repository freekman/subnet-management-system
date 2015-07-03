package com.clouway.subnets.core;


import java.util.List;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public interface CategoryRepository {

  /**
   * Register new nodeId.
   *
   * @param category to be registered.
   * @return the id of the registered nodeId.
   */
  String register(NewCategory category);

  /**
   * Deletes a nodeId.
   *
   * @param id of nodeId to be deleted.
   */
  void delete(String id);

  /**
   * Finds all categories that have been registered.
   *
   * @return List of all categories.
   */
  List<Category> findAll();

}
