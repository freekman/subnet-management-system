package com.clouway.subnets.http;

import com.clouway.subnets.core.NetworkNode;
import com.clouway.subnets.core.NetworkNodeFinder;
import com.clouway.subnets.core.NetworkNodeStore;
import com.clouway.subnets.core.NewNetworkNode;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Delete;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
@At("/r/nodes")
@Service
class NetworkNodeService {

  private final NetworkNodeStore store;
  private final NetworkNodeFinder finder;

  @Inject
  public NetworkNodeService(NetworkNodeStore store, NetworkNodeFinder finder) {
    this.store = store;
    this.finder = finder;
  }

  @At("/:id")
  @Get
  public Reply<?> getAll(@Named("id") String parentId) {
    List<NetworkNode> nodes = finder.findAllWithParent(parentId);

    List<NetworkNodeDTO> dtos = adapt(nodes);

    return Reply.with(dtos).status(SC_OK).as(Json.class);
  }

  @Post
  public Reply<?> register(Request request) {
    NewNetworkNodeDTO dto = request.read(NewNetworkNodeDTO.class).as(Json.class);

    NewNetworkNode node = adapt(dto);

    String id = store.register(node);

    return Reply.with(id).status(SC_OK).as(Json.class);
  }

  @At("/:id")
  @Delete
  public Reply<?> delete(@Named("id") String id) {
    store.delete(id);

    return Reply.with("Successfully removed node").status(SC_OK).as(Json.class);
  }

  private NewNetworkNode adapt(NewNetworkNodeDTO dto) {
    return new NewNetworkNode(dto.name, dto.parentId);
  }

  private List<NetworkNodeDTO> adapt(List<NetworkNode> nodes) {
    ArrayList<NetworkNodeDTO> dtos = newArrayList();

    for (NetworkNode each : nodes) {
      dtos.add(new NetworkNodeDTO(each.id, each.parentId,each.name, each.isParent));
    }

    return dtos;
  }

}
