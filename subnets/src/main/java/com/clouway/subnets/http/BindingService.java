package com.clouway.subnets.http;


import com.clouway.subnets.core.BindingFinder;
import com.clouway.subnets.core.BindingIp;
import com.clouway.subnets.core.BindingRegister;
import com.clouway.subnets.core.BindingWithId;
import com.clouway.subnets.core.Message;
import com.clouway.subnets.core.SecureTransport;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;
import com.google.sitebricks.http.Put;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
@At("/r/bindings")
@Service
@SecureTransport
public class BindingService {

  private BindingFinder bindingFinder;
  private BindingRegister bindingRegister;

  @Inject
  public BindingService(BindingFinder bindingFinder, BindingRegister bindingRegister) {
    this.bindingFinder = bindingFinder;
    this.bindingRegister = bindingRegister;
  }

  @Post
  @At("/:id")
  public Reply<?> getBinding(@Named("id") String id, Request request) {
    BindingIpDTO dto = request.read(BindingIpDTO.class).as(Json.class);
    BindingIp bindingIp = adapt(dto);
    Optional<BindingWithId> binding = bindingFinder.findByIP(id, bindingIp.value);
    if (binding.isPresent()) {
      return Reply.with(binding.get()).as(Json.class).ok();
    }
    return Reply.with("Binding not found!").status(HttpServletResponse.SC_NOT_FOUND);

  }

  @Put
  @At("/:id/description")
  public Reply<?> updateDescription(@Named("id") String id, Request request) {
    MessageDTO messageDTO = request.read(MessageDTO.class).as(Json.class);
    Message message = adapt(messageDTO);
    bindingRegister.updateDescription(id,message);
    return Reply.saying().ok();

  }

  private Message adapt(MessageDTO messageDTO) {
    return new Message(messageDTO.text);
  }


  private BindingIp adapt(BindingIpDTO dto) {
    return new BindingIp(dto.value);
  }
}
