package com.clouway.subnets;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public abstract class EqualityTest<T> {

  private Class<T> target;

  public EqualityTest() {
    target = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  @Test
  public void equalityTest() throws Exception {
    EqualsVerifier.forClass(target).allFieldsShouldBeUsed().verify();
  }
}
