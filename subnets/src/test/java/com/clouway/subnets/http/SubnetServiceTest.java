package com.clouway.subnets.http;

import com.clouway.subnets.core.NewSubnet;
import com.clouway.subnets.core.Subnet;
import com.clouway.subnets.core.SubnetFinder;
import com.clouway.subnets.core.SubnetStore;
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

import static com.clouway.subnets.matchers.ReplyContainsObject.contains;
import static com.clouway.subnets.matchers.ReplyStatus.statusIs;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.hamcrest.MatcherAssert.assertThat;

public class SubnetServiceTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private SubnetService subnetService;
  private final Request request = context.mock(Request.class);
  private final SubnetStore subnetStore = context.mock(SubnetStore.class);
  private final SubnetFinder finder = context.mock(SubnetFinder.class);


  @Before
  public void setUp() throws Exception {
    subnetService = new SubnetService(subnetStore, finder);
  }

  @Test
  public void register() throws Exception {
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

  @Test
  public void getSubnet() throws Exception {
    final String id = "55ae2920cc795e24b1858c5c";
    final Subnet subnet = new Subnet(id, "nodeID", "IP", 30, "description");
    SubnetDTO dto = new SubnetDTO(subnet.nodeId, subnet.subnetIP, subnet.slash, subnet.description);

    context.checking(new Expectations() {{
      oneOf(finder).findById(id);
      will(returnValue(Optional.of(subnet)));
    }});

    Reply reply = subnetService.getSubnet(id);
    assertThat(reply, contains(dto));
    assertThat(reply, statusIs(200));
  }

  @Test
  public void missingSubnet() throws Exception {
    final String id = "55ae2920cc795e24b1858c5c";

    context.checking(new Expectations() {{
      oneOf(finder).findById(id);
      will(returnValue(Optional.absent()));
    }});

    Reply reply = subnetService.getSubnet(id);

    assertThat(reply, statusIs(SC_NOT_FOUND));
  }
}