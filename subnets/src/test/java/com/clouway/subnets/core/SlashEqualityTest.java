package com.clouway.subnets.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class SlashEqualityTest {

  @Test
  public void equal() throws Exception {
    Slash slash1 = new Slash(1);
    Slash slash2 = new Slash(1);
    assertEquals(slash1, slash2);
  }

  @Test
  public void notEqual() throws Exception {
    Slash slash1 = new Slash(21);
    Slash slash2 = new Slash(1);
    assertNotEquals(slash1, slash2);
  }

}