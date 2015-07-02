package com.clouway.subnets.matchers;

import com.google.sitebricks.headless.Reply;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

/**
 * Created byivan.genchev1989@gmail.com.
 */
public class ReplyStatus extends TypeSafeMatcher<Reply> {
  private int statusCode;

  public ReplyStatus(int statusCode) {

    this.statusCode = statusCode;
  }

  @Override
  protected boolean matchesSafely(Reply reply) {
    try {
      Field status = reply.getClass().getDeclaredField("status");
      status.setAccessible(true);
      int code = (int) status.get(reply);
      return code == statusCode;
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("Status does not match!");
  }

  @Factory
  public static <T> ReplyStatus statusIs(int statusCode) {
    return new ReplyStatus(statusCode);
  }

  public static void bla(){

  }
}
