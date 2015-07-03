package com.clouway.subnets.core;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public class Subnet {
  private String id;
  private final String nodeId;
  private final String subnetIP;
  private final int slash;
  private final String description;


  public Subnet(String id,String nodeId, String subnetIP, int slash, String description) {
    this.id = id;
    this.nodeId = nodeId;
    this.subnetIP = subnetIP;
    this.slash = slash;
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Subnet subnet = (Subnet) o;

    if (slash != subnet.slash) return false;
    if (nodeId != null ? !nodeId.equals(subnet.nodeId) : subnet.nodeId != null) return false;
    if (description != null ? !description.equals(subnet.description) : subnet.description != null) return false;
    if (id != null ? !id.equals(subnet.id) : subnet.id != null) return false;
    if (subnetIP != null ? !subnetIP.equals(subnet.subnetIP) : subnet.subnetIP != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (nodeId != null ? nodeId.hashCode() : 0);
    result = 31 * result + (subnetIP != null ? subnetIP.hashCode() : 0);
    result = 31 * result + slash;
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }
}
