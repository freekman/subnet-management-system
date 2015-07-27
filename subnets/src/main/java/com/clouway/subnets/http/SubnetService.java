package com.clouway.subnets.http;

import com.clouway.subnets.core.NewSubnet;
import com.clouway.subnets.core.SecureTransport;
import com.clouway.subnets.core.Subnet;
import com.clouway.subnets.core.SubnetFinder;
import com.clouway.subnets.core.SubnetStore;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Delete;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
@At("/r/subnets")
@Service
@SecureTransport
public class SubnetService {

  private SubnetStore subnetStore;
  private SubnetFinder finder;

  @Inject
  public SubnetService(SubnetStore subnetStore, SubnetFinder finder) {
    this.subnetStore = subnetStore;
    this.finder = finder;
  }

  @Post
  public Reply<?> register(Request request) {
    NewSubnetDTO dto = request.read(NewSubnetDTO.class).as(Json.class);
    NewSubnet newSubnet = adapt(dto);
    subnetStore.register(newSubnet);
    return Reply.with("Registration successful !").status(200);
  }

  @Get
  @At("/:id")
  public Reply<?> getSubnet(@Named("id") String id) {
    Optional<Subnet> subnet = finder.findById(id);
    if (!subnet.isPresent()) {
      return Reply.with("Subnet not found!").status(SC_NOT_FOUND);
    }
    SubnetDTO dto=adapt(subnet.get());
    return Reply.with(dto).as(Json.class).ok();
  }

  @Delete
  @At("/:id")
  public Reply<?> removeSubnet(@Named("id") String id) {
    subnetStore.remove(id);
    return Reply.saying().ok();
  }

  private SubnetDTO adapt(Subnet subnet) {
    return new SubnetDTO(subnet.nodeId, subnet.subnetIP, subnet.slash, subnet.description);
  }

  private NewSubnet adapt(NewSubnetDTO dto) {
    return new NewSubnet(dto.nodeId, dto.ip, dto.slash, dto.description);
  }
}
