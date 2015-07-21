package com.clouway.subnets.http;

import org.junit.Test;

import static org.junit.Assert.*;

public class NewSubnetDTOEqualityTest {

  @Test
  public void equal() throws Exception {
    NewSubnetDTO dto1=new NewSubnetDTO( "nodeID", "IP", 30, "description");
    NewSubnetDTO dto2=new NewSubnetDTO( "nodeID", "IP", 30, "description");
    assertEquals(dto1,dto2);
  }

  @Test
  public void notEqual() throws Exception {
    NewSubnetDTO dto1=new NewSubnetDTO( "nodeID", "IP", 25, "description");
    NewSubnetDTO dto2=new NewSubnetDTO( "nodeID", "IP", 30, "description");
    assertNotEquals(dto1,dto2);
  }

}