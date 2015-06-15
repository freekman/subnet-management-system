package com.clouway.subnets.http;

import com.clouway.subnets.core.Category;
import com.clouway.subnets.core.CategoryRegistry;
import com.clouway.subnets.core.MessageResponse;
import com.clouway.subnets.core.NewCategory;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Delete;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.*;
import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
@At("/r/category")
@Service
class CategoryService {

  private final CategoryRegistry registry;

  @Inject
  public CategoryService(CategoryRegistry registry) {
    this.registry = registry;
  }

  @Get
  public Reply<?> getAll() {
    List<CategoryDTO> categories = adapt(registry.findAll());

    return Reply.with(categories).status(SC_OK).as(Json.class);
  }

  @Post
  public Reply<?> register(Request request) {
    NewCategoryDTO dto = request.read(NewCategoryDTO.class).as(Json.class);

    NewCategory category = adapt(dto);

    MessageResponse response = registry.register(category);

    return Reply.with(response.message).status(response.httpStatusCode).as(Json.class);
  }

  @At("/:id")
  @Delete
  public Reply<?> delete(@Named("id") String id) {
    MessageResponse response = registry.delete(id);

    return Reply.with(response.message).status(response.httpStatusCode).as(Json.class);
  }

  private NewCategory adapt(NewCategoryDTO dto) {
    return new NewCategory(dto.type);
  }

  private List<CategoryDTO> adapt(List<Category> categories) {
    ArrayList<CategoryDTO> dtos = newArrayList();

    for (Category each : categories) {
      dtos.add(new CategoryDTO(each.id, each.type));
    }

    return dtos;
  }

}
