package com.clouway.subnets.http;

import com.clouway.subnets.core.NewSubnet;

import com.clouway.subnets.core.SubnetRegister;
import com.google.sitebricks.client.Transport;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Request.RequestRead;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static matchers.ReplyStatus.statusIs;

import static org.hamcrest.MatcherAssert.assertThat;

public class RegisterServiceTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  private RegisterService registerService;
  private Request request;
  private SubnetRegister register;

  @Before
  public void setUp() throws Exception {
    request = context.mock(Request.class);
    register = context.mock(SubnetRegister.class);
    registerService = new RegisterService(register);
  }

  @Test
  public void happyPath() throws Exception {
    context.checking(new Expectations() {{
      oneOf(request).read(NewSubnetDTO.class);
      will(returnValue(new RequestRead<NewSubnetDTO>() {
        @Override
        public NewSubnetDTO as(Class<? extends Transport> transport) {
          return new NewSubnetDTO("asd", "asd", 1,"");
        }
      }));
      oneOf(register).register(new NewSubnet("asd", "asd", 1,""));
    }});
    Reply reply = registerService.register(request);
    assertThat(reply,statusIs(200));
  }
}