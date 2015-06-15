package com.clouway.subnets.http;

import com.clouway.subnets.core.Category;
import com.clouway.subnets.core.CategoryException;
import com.clouway.subnets.core.CategoryRegistry;
import com.clouway.subnets.core.CategoryRepository;
import com.clouway.subnets.core.MessageResponse;
import com.clouway.subnets.core.NewCategory;
import com.google.inject.Inject;

import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
class CategoryRegistryImpl implements CategoryRegistry {

  private final CategoryRepository repository;

  @Inject
  public CategoryRegistryImpl(CategoryRepository repository) {
    this.repository = repository;
  }

  public MessageResponse register(NewCategory category) {
    try {
      repository.register(category);
    } catch (CategoryException e) {

      return new MessageResponse("Category already exists", SC_BAD_REQUEST);
    }

    return new MessageResponse("Registration successful", SC_OK);
  }

  @Override
  public List<Category> findAll() {
    return repository.findAll();
  }

  @Override
  public MessageResponse delete(String id) {
    repository.delete(id);

    return new MessageResponse("Successfully removed category", SC_OK);
  }

}
