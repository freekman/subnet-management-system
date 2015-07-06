package com.clouway.subnets.http;

import javax.validation.constraints.NotNull;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
class NewNetworkNodeDTO {

  @NotNull
  public final String name;
  @NotNull
  public final String parentId;

  public NewNetworkNodeDTO(String name, String parentId) {
    this.name = name;
    this.parentId = parentId;
  }

}
