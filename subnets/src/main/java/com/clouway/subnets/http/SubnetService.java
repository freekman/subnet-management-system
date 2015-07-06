package com.clouway.subnets.http;

import com.clouway.subnets.core.BindingRegister;
import com.clouway.subnets.core.SecureTransport;
import com.clouway.subnets.core.NewSubnet;

import com.clouway.subnets.core.SubnetRegister;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
@At("/r/subnets")
@Service
@SecureTransport
public class SubnetService {

  private SubnetRegister subnetRegister;
  private BindingRegister bindingRegister;

  @Inject
  public SubnetService(SubnetRegister subnetRegister,BindingRegister bindingRegister) {
    this.subnetRegister = subnetRegister;
    this.bindingRegister = bindingRegister;
  }

  @Post
  public Reply<?> register(Request request) {
    NewSubnetDTO dto = request.read(NewSubnetDTO.class).as(Json.class);
    NewSubnet newSubnet = adapt(dto);
    String id=subnetRegister.register(newSubnet);
    bindingRegister.registerPerSubnet(newSubnet,id);
    return Reply.with("Registration successful !").status(200);
  }

  private NewSubnet adapt(NewSubnetDTO dto) {
    return new NewSubnet(dto.nodeId, dto.ip, dto.slash,dto.description);
  }
}
