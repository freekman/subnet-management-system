package com.clouway.subnets.http;

import com.clouway.subnets.core.Binding;
import com.clouway.subnets.core.BindingFinder;
import com.clouway.subnets.core.BindingIp;
import com.clouway.subnets.core.SecureTransport;
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
@At("/r/bindings")
@Service
@SecureTransport
public class BindingService {

  private BindingFinder bindingFinder;

  @Inject
  public BindingService(BindingFinder bindingFinder) {
    this.bindingFinder = bindingFinder;
  }

  @Post
  @At("/:id")
  public Reply<?> getBinding(@Named("id")String id, Request request) {
    BindingIpDTO dto = request.read(BindingIpDTO.class).as(Json.class);
    BindingIp bindingIp = adapt(dto);
    Optional<Binding> binding = bindingFinder.findByIP(id, bindingIp.value);
    if (binding.isPresent()) {
      return Reply.with(binding.get()).as(Json.class).ok();
    }
    return Reply.with("Binding not found!").status(HttpServletResponse.SC_NOT_FOUND);

  }

  private BindingIp adapt(BindingIpDTO dto) {
    return new BindingIp(dto.value);
  }
}
