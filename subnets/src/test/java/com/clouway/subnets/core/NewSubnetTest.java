package com.clouway.subnets.core;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NewSubnetTest {
  private NewSubnetBuilder builder;

  @Before
  public void setUp() throws Exception {
    builder = new NewSubnetBuilder();
  }

  @Test
  public void minIP() throws Exception {
    NewSubnet subnet = builder.subnetIP("10.1.2.0").slash(23).build();
    Long result = subnet.getMinIP();

    assertThat(result, is(167838208l));
  }

  @Test
  public void maxIP() throws Exception {
    NewSubnet subnet = builder.subnetIP("10.1.2.0").slash(23).build();
    Long result = subnet.getMaxIP();

    assertThat(result, is(167838720l));

  }

  @Test
  public void mask() throws Exception {
    NewSubnet subnet = builder.subnetIP("10.1.2.0").slash(23).build();
    Long result = subnet.getSubnetMask();

    assertThat(result, is(4294966784l));
  }

  @Test
  public void maxMask() throws Exception {
    NewSubnet subnet = builder.subnetIP("10.1.2.0").slash(32).build();
    Long result = subnet.getSubnetMask();

    assertThat(result, is(4294967295l));
  }

  @Test
  public void getHost() throws Exception {
    NewSubnet subnet = builder.build();
    String host = subnet.getHost(4294967295l);

    assertThat(host, is("255.255.255.255"));
  }

  @Test
  public void getAnotherHost() throws Exception {
    NewSubnet subnet = builder.build();
    String host = subnet.getHost(167838208l);

    assertThat(host, is("10.1.2.0"));
  }

}

