package com.clouway.subnets.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class MessageEqualityTest {

  @Test
  public void equal() throws Exception {
    Message message1 = new Message("abc");
    Message message2 = new Message("abc");
    assertEquals(message1, message2);
  }

  @Test
  public void notEqual() throws Exception {
    Message message1 = new Message("ab");
    Message message2 = new Message("abc");
    assertNotEquals(message1, message2);
  }

}