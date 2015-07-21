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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NewSubnetDTO dto = (NewSubnetDTO) o;

    if (slash != dto.slash) return false;
    if (description != null ? !description.equals(dto.description) : dto.description != null) return false;
    if (ip != null ? !ip.equals(dto.ip) : dto.ip != null) return false;
    if (nodeId != null ? !nodeId.equals(dto.nodeId) : dto.nodeId != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = nodeId != null ? nodeId.hashCode() : 0;
    result = 31 * result + (ip != null ? ip.hashCode() : 0);
    result = 31 * result + slash;
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }

}
