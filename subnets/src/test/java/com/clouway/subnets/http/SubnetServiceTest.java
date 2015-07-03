package com.clouway.subnets.http;


import com.clouway.subnets.core.NewSubnet;
import com.clouway.subnets.core.SubnetStore;
import com.google.sitebricks.client.Transport;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Request.RequestRead;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.clouway.subnets.matchers.ReplyStatus.statusIs;
import static org.hamcrest.MatcherAssert.assertThat;

public class SubnetServiceTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  private SubnetService subnetService;
  private Request request;
  private SubnetStore subnetStore;


  @Before
  public void setUp() throws Exception {

    request = context.mock(Request.class);
    subnetStore = context.mock(SubnetStore.class);
    subnetService = new SubnetService(subnetStore);
  }

  @Test
  public void happyPath() throws Exception {
    final String id = "123456asd";
    final NewSubnet newSubnet = new NewSubnet("asd", "asd", 1, "");

    context.checking(new Expectations() {{
      oneOf(request).read(NewSubnetDTO.class);
      will(returnValue(new RequestRead<NewSubnetDTO>() {
        @Override
        public NewSubnetDTO as(Class<? extends Transport> transport) {
          return new NewSubnetDTO("asd", "asd", 1, "");
        }
      }));
      oneOf(subnetStore).register(newSubnet);
    }});
    Reply reply = subnetService.register(request);
    assertThat(reply, statusIs(200));
  }
}