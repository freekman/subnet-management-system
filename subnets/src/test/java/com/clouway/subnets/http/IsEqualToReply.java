package com.clouway.subnets.http;

import com.google.common.base.Optional;
import com.google.sitebricks.headless.Reply;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;


/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class IsEqualToReply extends TypeSafeMatcher<Reply<?>> {

  private Reply<?> expectedValue;

  private Optional<Object> expectedEntity;
  private Optional<Object> actualEntity;

  private Optional<Object> expectedUri;
  private Optional<Object> actualUri;

  private Optional<Object> expectedStatus;
  private Optional<Object> actualStatus;

  public IsEqualToReply(Reply<?> expectedValue) {
    this.expectedValue = expectedValue;
  }

  @Factory
  public static Matcher<Reply<?>> isEqualToReply(Reply<?> operand) {
    return new IsEqualToReply(operand);
  }

  @Override
  protected boolean matchesSafely(Reply<?> actualValue) {

    expectedEntity = getDeclaredFieldValue(expectedValue, "entity");
    actualEntity = getDeclaredFieldValue(actualValue, "entity");

    expectedUri = getDeclaredFieldValue(expectedValue, "redirectUri");
    actualUri = getDeclaredFieldValue(actualValue, "redirectUri");

    expectedStatus = getDeclaredFieldValue(actualValue, "status");
    actualStatus = getDeclaredFieldValue(expectedValue, "status");

    boolean match = areEqual(expectedEntity, actualEntity);

    if (match) {
      match = areEqual(expectedUri, actualUri);
    }

    if (match) {
      match = areEqual(expectedStatus, actualStatus);
    }

    return match;
  }

  @Override
  public void describeTo(Description description) {
    description
            .appendText("entity:'" + actualEntity.orNull() + "'")
            .appendText(" redirectUri:'" + actualUri.orNull() + "'")
            .appendText(" status:'" + actualStatus.orNull() + "'")

            .appendText(" to equal \n\t\t")

            .appendText(" entity:'" + expectedEntity.orNull() + "'")
            .appendText(" redirectUri:'" + expectedUri.orNull() + "'")
            .appendText(" status:'" + expectedStatus.orNull() + "'");
  }

  @Override
  protected void describeMismatchSafely(Reply<?> item, Description mismatchDescription) {
    mismatchDescription
            .appendText("was entity:'" + expectedEntity.orNull() + "'")
            .appendText("redirectUri:'" + expectedUri.orNull() + "'")
            .appendText("status:'" + expectedStatus.orNull() + "'");
  }

  private Optional<Object> getDeclaredFieldValue(Object item, String fieldName) {
    try {
      Field field = item.getClass().getDeclaredField(fieldName);

      field.setAccessible(true);

      Object value = field.get(item);

      return (value == null ? Optional.absent() : Optional.of(value));

    } catch (NoSuchFieldException e) {
      e.printStackTrace();

    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    return Optional.absent();
  }

  private boolean areEqual(Optional optExpected, Optional optActual) {

    if (optExpected.isPresent() && optActual.isPresent()) {

      Object expected = optExpected.get();

      Object actual = optActual.get();

      return actual.equals(expected);
    } else {
      return bothAreNull(optExpected.orNull(), optActual.orNull());
    }
  }

  private boolean bothAreNull(Object first, Object second) {
    return (first == null && second == null);
  }

}
