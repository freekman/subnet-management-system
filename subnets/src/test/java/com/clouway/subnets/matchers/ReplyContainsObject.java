package com.clouway.subnets.matchers;

import com.google.sitebricks.headless.Reply;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

/**
 * Created byivan.genchev1989@gmail.com.
 */
public class ReplyContainsObject extends TypeSafeMatcher<Reply> {
  private Object obj;
  public ReplyContainsObject(Object obj) {
    this.obj = obj;
  }

  @Override
  protected boolean matchesSafely(Reply reply) {
    try {
      Field declaredField = reply.getClass().getDeclaredField("entity");
      declaredField.setAccessible(true);
      return declaredField.get(reply).equals(obj);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("Does not contain the object "+obj);
  }

  @Factory
  public static <T> ReplyContainsObject contains(Object obj) {
    return new ReplyContainsObject(obj);
  }
}
