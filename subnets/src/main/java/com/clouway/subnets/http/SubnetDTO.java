package com.clouway.subnets.http;

/**
 * Created by ivan.genchev1989@gmail.com
 */
final class SubnetDTO {
  final String nodeId;
  final String ip;
  final int slash;
  final String description;

  SubnetDTO(String nodeId, String ip, int slash, String description) {
    this.nodeId = nodeId;
    this.ip = ip;
    this.slash = slash;
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SubnetDTO subnetDTO = (SubnetDTO) o;

    if (slash != subnetDTO.slash) return false;
    if (description != null ? !description.equals(subnetDTO.description) : subnetDTO.description != null) return false;
    if (ip != null ? !ip.equals(subnetDTO.ip) : subnetDTO.ip != null) return false;
    if (nodeId != null ? !nodeId.equals(subnetDTO.nodeId) : subnetDTO.nodeId != null) return false;

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
