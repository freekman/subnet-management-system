package com.clouway.subnets.http;

import com.clouway.subnets.core.BindingRegister;
import com.clouway.subnets.core.NewSubnet;
import com.clouway.subnets.core.Subnet;
import com.clouway.subnets.core.SubnetFinder;
import com.clouway.subnets.core.SubnetRegister;
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

public class SubnetServiceTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  private SubnetService subnetService;
  private Request request;
  private SubnetRegister subnetRegister;
  private SubnetFinder subnetFinder;
  private BindingRegister bindingRegister;

  @Before
  public void setUp() throws Exception {
    subnetFinder = context.mock(SubnetFinder.class);
    bindingRegister = context.mock(BindingRegister.class);
    request = context.mock(Request.class);
    subnetRegister = context.mock(SubnetRegister.class);
    subnetService = new SubnetService(subnetRegister, bindingRegister, subnetFinder);
  }

  @Test
  public void registerSubnet() throws Exception {
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
      oneOf(subnetRegister).register(newSubnet);
      will(returnValue(id));
      oneOf(bindingRegister).registerPerSubnet(newSubnet, id);
    }});
    Reply reply = subnetService.register(request);
    assertThat(reply, statusIs(200));
  }

  @Test
  public void provideRequestedSubnet() throws Exception {
    final String id = "123456asd";
    final Subnet subnet = new Subnet(id, "nodeID", "0.0.0.0", 23, "note");

    context.checking(new Expectations() {{
      oneOf(subnetFinder).findById(id);
      will(returnValue(Optional.of(subnet)));
    }});

    Reply reply = subnetService.getSubnetById(id);
    assertThat(reply, contains(subnet));
    assertThat(reply, statusIs(200));
  }

  @Test
  public void requestedSubnetNotFound() throws Exception {
    final String id = "123456asd";

    context.checking(new Expectations() {{
      oneOf(subnetFinder).findById(id);
      will(returnValue(Optional.absent()));
    }});

    Reply reply = subnetService.getSubnetById(id);
    assertThat(reply, statusIs(HttpServletResponse.SC_NOT_FOUND));
  }

  @Test
  public void removeSubnet() throws Exception {
    final String id = "123asdfgh";
    context.checking(new Expectations() {{
      oneOf(subnetRegister).remove(id);
      oneOf(bindingRegister).removePerSubnet(id);
    }});
    Reply reply = subnetService.removeSubnet(id);
    assertThat(reply,statusIs(200));
  }
}