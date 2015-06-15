package com.clouway.subnets.http;

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
public class RegisterService {

  private SubnetRegister register;
  @Inject
  public RegisterService(SubnetRegister register) {
    this.register = register;
  }
  @At("/new")
  @Post
  public Reply<?> register(Request request) {
    NewSubnetDTO dto = request.read(NewSubnetDTO.class).as(Json.class);
    NewSubnet newSubnet = adapt(dto);
    register.register(newSubnet);
    return Reply.with("Registration successful !").status(200);
  }

  private NewSubnet adapt(NewSubnetDTO dto) {
    return new NewSubnet(dto.category, dto.ip, dto.slash,dto.description);
  }
}
