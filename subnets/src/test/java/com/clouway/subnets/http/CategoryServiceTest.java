package com.clouway.subnets.http;

import com.clouway.subnets.core.Category;
import com.clouway.subnets.core.CategoryRegistry;
import com.clouway.subnets.core.MessageResponse;
import com.clouway.subnets.core.NewCategory;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Request.RequestRead;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.clouway.subnets.http.IsEqualToReply.isEqualToReply;
import static com.google.common.collect.Lists.newArrayList;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class CategoryServiceTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private CategoryRegistry registry = context.mock(CategoryRegistry.class);

  private CategoryService service =new CategoryService(registry);

  private Request request = context.mock(Request.class);

  private RequestRead requestRead = context.mock(RequestRead.class);

  @Test
  public void registerNewCategory() throws Exception {
    final NewCategoryDTO category = new NewCategoryDTO("Internet");

    final MessageResponse response = new MessageResponse("Registration successful", SC_OK);

    context.checking(new Expectations() {{
      oneOf(registry).register(new NewCategory("Internet"));
      will(returnValue(response));
    }});


    Reply<?> reply = service.register(fakeRequest(category));

    assertThat(reply, isEqualToReply(replyWith(response.message, response.httpStatusCode)));
  }

  @Test
  public void registerExistingCategory() throws Exception {
    final NewCategoryDTO category = new NewCategoryDTO("Television");

    final MessageResponse response = new MessageResponse("Category already exists", SC_BAD_REQUEST);

    context.checking(new Expectations() {{
      oneOf(registry).register(with(any(NewCategory.class)));
      will(returnValue(response));
    }});

    Reply<?> reply = service.register(fakeRequest(category));

    assertThat(reply, isEqualToReply(replyWith(response.message, response.httpStatusCode)));
  }

  @Test
  public void getAllCategories() throws Exception {
    final List<Category> categories = newArrayList(new Category("1", "some value"));

    context.checking(new Expectations() {{
      oneOf(registry).findAll();
      will(returnValue(categories));
    }});

    Reply<?> reply = service.getAll();

    List<CategoryDTO> expected = newArrayList(new CategoryDTO("1", "some value"));

    assertThat(reply, isEqualToReply(Reply.with(expected).status(SC_OK)));
  }

  @Test
  public void deleteExistingCategory() throws Exception {
    final MessageResponse response = new MessageResponse("random msg", SC_OK);

    final String dummyId = "123abc";

    context.checking(new Expectations() {{
      oneOf(registry).delete(dummyId);
      will(returnValue(response));
    }});

    Reply<?> reply = service.delete(dummyId);

    assertThat(reply, isEqualToReply(replyWith(response.message, response.httpStatusCode)));
  }

  private Request fakeRequest(final NewCategoryDTO category) {
    context.checking(new Expectations() {{

      oneOf(request).read(NewCategoryDTO.class);
      will(returnValue(requestRead));

      oneOf(requestRead).as(Json.class);
      will(returnValue(category));
    }});

    return request;
  }

  private Reply<?> replyWith(String msg, int status) {
    return Reply.with(msg).status(status);
  }
}