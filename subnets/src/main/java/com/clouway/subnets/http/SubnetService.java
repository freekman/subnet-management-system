package com.clouway.subnets.http;

import com.clouway.subnets.core.BindingRegister;
import com.clouway.subnets.core.NewSubnet;
import com.clouway.subnets.core.SecureTransport;
import com.clouway.subnets.core.Subnet;
import com.clouway.subnets.core.SubnetFinder;
import com.clouway.subnets.core.SubnetRegister;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
@At("/r/subnets")
@Service
@SecureTransport
public class SubnetService {

  private SubnetRegister subnetRegister;
  private BindingRegister bindingRegister;
  private SubnetFinder subnetFinder;

  @Inject
  public SubnetService(SubnetRegister subnetRegister, BindingRegister bindingRegister, SubnetFinder subnetFinder) {
    this.subnetRegister = subnetRegister;
    this.bindingRegister = bindingRegister;
    this.subnetFinder = subnetFinder;
  }

  @Post
  public Reply<?> register(Request request) {
    NewSubnetDTO dto = request.read(NewSubnetDTO.class).as(Json.class);
    NewSubnet newSubnet = adapt(dto);
    String id = subnetRegister.register(newSubnet);
    bindingRegister.registerPerSubnet(newSubnet, id);
    return Reply.with("Registration successful !").status(200);
  }

  @Get
  @At("/:id")
  public Reply<?> getSubnetById(@Named("id") String id) {
    Optional<Subnet> subnet = subnetFinder.findById(id);

    if (!subnet.isPresent()) {
      return Reply.saying().status(HttpServletResponse.SC_NOT_FOUND);
    }
    return Reply.with(subnet.get()).as(Json.class).ok();
  }

  private NewSubnet adapt(NewSubnetDTO dto) {
    return new NewSubnet(dto.nodeId, dto.ip, dto.slash, dto.description);
  }
}
