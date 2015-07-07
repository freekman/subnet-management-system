package com.clouway.subnets.http;

import com.clouway.subnets.core.Binding;
import com.clouway.subnets.core.BindingFinder;
import com.google.common.base.Optional;
import com.google.sitebricks.client.Transport;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Request.RequestRead;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

import static com.clouway.subnets.matchers.ReplyContainsObject.contains;
import static com.clouway.subnets.matchers.ReplyStatus.statusIs;
import static org.hamcrest.MatcherAssert.assertThat;

public class BindingServiceTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private BindingService bindingService;
  private BindingFinder bindingFinder;
  private Request request;

  @Before
  public void setUp() throws Exception {
    request = context.mock(Request.class);
    bindingFinder = context.mock(BindingFinder.class);
    bindingService = new BindingService(bindingFinder);
  }

  @Test
  public void provideSubnetByIP() throws Exception {
    final String id = "abc";
    final String ip = "0.0.0.0";
    final Binding binding = new Binding(id, "note", ip);

    context.checking(new Expectations() {{
      oneOf(request).read(BindingIpDTO.class);
      will(returnValue(new RequestRead<BindingIpDTO>() {
        @Override
        public BindingIpDTO as(Class<? extends Transport> transport) {
          return new BindingIpDTO(ip);
        }
      }));
      oneOf(bindingFinder).findByIP(id, ip);
      will(returnValue(Optional.of(binding)));
    }});

    Reply reply = bindingService.getBinding(id, request);
    assertThat(reply,contains(binding));
    assertThat(reply,statusIs(200));
  }
  @Test
  public void subnetNotFound() throws Exception {
    final String id = "abc";
    final String ip = "0.0.0.0";

    context.checking(new Expectations() {{
      oneOf(request).read(BindingIpDTO.class);
      will(returnValue(new RequestRead<BindingIpDTO>() {
        @Override
        public BindingIpDTO as(Class<? extends Transport> transport) {
          return new BindingIpDTO(ip);
        }
      }));
      oneOf(bindingFinder).findByIP(id, ip);
      will(returnValue(Optional.absent()));
    }});

    Reply reply = bindingService.getBinding(id, request);
    assertThat(reply,statusIs(HttpServletResponse.SC_NOT_FOUND));
  }

}