package com.clouway.subnets.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class BindingEqualityTest {

  @Test
  public void equal() throws Exception {
    Binding binding1=new Binding("123","aaa","0.0.0.0");
    Binding binding2=new Binding("123","aaa","0.0.0.0");
    assertEquals(binding1,binding2);
  }

  @Test
  public void notEqual() throws Exception {
    Binding binding1=new Binding("123","bbb","0.0.0.0");
    Binding binding2=new Binding("123","aaa","0.0.0.0");
    assertNotEquals(binding1,binding2);
  }


}