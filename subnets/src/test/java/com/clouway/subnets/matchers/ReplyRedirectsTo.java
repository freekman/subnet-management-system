package com.clouway.subnets.matchers;

import com.google.sitebricks.headless.Reply;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

/**
 * Created byivan.genchev1989@gmail.com.
 */
public class ReplyRedirectsTo extends TypeSafeMatcher<Reply> {
  private String expectedPath;

  public ReplyRedirectsTo(String expectedPath) {
    this.expectedPath = expectedPath;
  }

  @Override
  protected boolean matchesSafely(Reply reply) {
    try {
      Field redirectUri = reply.getClass().getDeclaredField("redirectUri");
      redirectUri.setAccessible(true);
      String redirectPath = (String) redirectUri.get(reply);
      return expectedPath.equals(redirectPath);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return true;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("The redirect  does not match - expectedPath.");
  }

  @Factory
  public static <T> ReplyRedirectsTo redirectsTo(String path) {
    return new ReplyRedirectsTo(path);
  }
}
