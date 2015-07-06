package com.clouway.subnets.http;

import com.clouway.subnets.core.NetworkNode;
import com.clouway.subnets.core.NetworkNodeFinder;
import com.clouway.subnets.core.NetworkNodeStore;
import com.clouway.subnets.core.NewNetworkNode;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Request.RequestRead;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.clouway.subnets.http.IsEqualToReply.isEqualToReply;
import static com.google.common.collect.Lists.newArrayList;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class NetworkNodeServiceTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private NetworkNodeStore store = context.mock(NetworkNodeStore.class);
  private NetworkNodeFinder finder = context.mock(NetworkNodeFinder.class);

  private NetworkNodeService service = new NetworkNodeService(store, finder);

  private Request request = context.mock(Request.class);

  private RequestRead requestRead = context.mock(RequestRead.class);

  @Test
  public void registerNewNetworkNode() throws Exception {
    final NewNetworkNodeDTO node = new NewNetworkNodeDTO("Internet", "id1");

    context.checking(new Expectations() {{
      oneOf(store).register(new NewNetworkNode("Internet", "id1"));
      will(returnValue("id1"));
    }});

    Reply<?> reply = service.register(fakeRequest(node));
    assertThat(reply, isEqualToReply(replyWith("id1", SC_OK)));
  }

  @Test
  public void getAllNetworkNodesByParent() throws Exception {
    final List<NetworkNode> nodes = newArrayList(new NetworkNode("id1", "id2", "test", true));

    context.checking(new Expectations() {{
      oneOf(finder).findAllWithParent("id2");
      will(returnValue(nodes));
    }});

    Reply<?> reply = service.getAll("id2");

    List<NetworkNodeDTO> expected = newArrayList(new NetworkNodeDTO("id1", "id2", "test", true));

    assertThat(reply, isEqualToReply(Reply.with(expected).status(SC_OK)));
  }

  @Test
  public void deleteExistingNode() throws Exception {
    final String dummyId = "123abc";

    context.checking(new Expectations() {{
      oneOf(store).delete(dummyId);
    }});

    Reply<?> reply = service.delete(dummyId);

    assertThat(reply, isEqualToReply(replyWith("Successfully removed node", SC_OK)));
  }

  private Request fakeRequest(final NewNetworkNodeDTO node) {
    context.checking(new Expectations() {{

      oneOf(request).read(NewNetworkNodeDTO.class);
      will(returnValue(requestRead));

      oneOf(requestRead).as(Json.class);
      will(returnValue(node));
    }});

    return request;
  }

  private Reply<?> replyWith(String msg, int status) {
    return Reply.with(msg).status(status);
  }
}