package com.clouway.subnets.http;

import com.clouway.subnets.core.Category;
import com.clouway.subnets.core.CategoryException;
import com.clouway.subnets.core.CategoryRegistry;
import com.clouway.subnets.core.CategoryRepository;
import com.clouway.subnets.core.MessageResponse;
import com.clouway.subnets.core.NewCategory;
import com.google.common.collect.Lists;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.*;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class CategoryRegistryImplTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private CategoryRepository repository = context.mock(CategoryRepository.class);

  private CategoryRegistry registry = new CategoryRegistryImpl(repository);

  @Test
  public void registerNewCategory() throws Exception {
    final NewCategory category = new NewCategory("Internet");

    context.checking(new Expectations() {{
      oneOf(repository).register(category);
    }});

    MessageResponse response = registry.register(category);

    assertThat(response.message, is(equalTo("Registration successful")));

    assertThat(response.httpStatusCode, is(equalTo(SC_OK)));
  }

  @Test
  public void registerExistingCategory() throws Exception {
    final NewCategory category = new NewCategory("Television");

    context.checking(new Expectations() {{
      oneOf(repository).register(with(any(NewCategory.class)));
      will(throwException(new CategoryException()));
    }});

    MessageResponse response = registry.register(category);

    assertThat(response.message, is(equalTo("Category already exists")));

    assertThat(response.httpStatusCode, is(equalTo(SC_BAD_REQUEST)));
  }

  @Test
  public void findAllCategories() throws Exception {
    final List<Category> categories = newArrayList(new Category("1", "test"));

    context.checking(new Expectations() {{
      oneOf(repository).findAll();
      will(returnValue(categories));
    }});

    List<Category> all = registry.findAll();

    assertThat(all, is(equalTo(categories)));
  }

  @Test
  public void deleteCategory() throws Exception {
    final String dummyId = "123abc";

    context.checking(new Expectations() {{
      oneOf(repository).delete(dummyId);
    }});

    MessageResponse response = registry.delete(dummyId);

    assertThat(response.message, is(equalTo("Successfully removed nodeId")));

    assertThat(response.httpStatusCode, is(equalTo(SC_OK)));
  }

}