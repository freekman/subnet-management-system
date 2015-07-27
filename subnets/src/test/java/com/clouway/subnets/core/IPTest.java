package com.clouway.subnets.core;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class IPTest {
  private IP ip;

  @Before
  public void setUp() throws Exception {
    ip = new IP();
  }

  @Test
  public void happyPath() throws Exception {
    Long result = ip.getNetworkIP("10.1.2.0");
    assertThat(result, is(167838208l));
  }

  @Test
  public void getMaxIP() throws Exception {
    Long result = ip.getNetworkIP("255.255.255.255");
    assertThat(result, is(4294967295l));
  }

  @Test
  public void getHost() throws Exception {
    String host = ip.getHost(4294967295l);
    assertThat(host,is("255.255.255.255"));
  }
  @Test
  public void getAnotherHost() throws Exception {
    String host = ip.getHost(167838208l);
    assertThat(host,is("10.1.2.0"));
  }
}