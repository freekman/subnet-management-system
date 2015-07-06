package com.clouway.subnets.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class SubnetEqualityTest {

  @Test
  public void equal() throws Exception {
    Subnet subnet1 = new Subnet("123","TV","0.0.0.0",23,"");
    Subnet subnet2 = new Subnet("123","TV","0.0.0.0",23,"");
    assertEquals(subnet1,subnet2);
  }

  @Test
  public void notEqual() throws Exception {
    Subnet subnet1 = new Subnet("321","TV","0.0.0.0",23,"");
    Subnet subnet2 = new Subnet("123","TV","0.0.0.0",23,"");
    assertNotEquals(subnet1, subnet2);
  }

}