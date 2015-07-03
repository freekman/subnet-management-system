package com.clouway.subnets.http;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
class NewSubnetDTO {
  @NotEmpty
  final String nodeId;
  @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$",
          message = "IP validation failed.")
  final String ip;
  @Min(20)
  @Max(32)
  final int slash;
  final String description;

  NewSubnetDTO(String nodeId, String ip, int slash, String description) {
    this.nodeId = nodeId;
    this.ip = ip;
    this.slash = slash;
    this.description = description;
  }

}
