package com.clouway.subnets.core;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public class NewSubnet {
  public final String nodeId;
  public final String subnetIP;
  public final int slash;
  public final String description;
  public static IP ip = new IP();

  public NewSubnet(String nodeId, String subnetIP, int slash, String description) {
    this.nodeId = nodeId;
    this.subnetIP = subnetIP;
    this.slash = slash;
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NewSubnet newSubnet = (NewSubnet) o;

    if (slash != newSubnet.slash) return false;
    if (nodeId != null ? !nodeId.equals(newSubnet.nodeId) : newSubnet.nodeId != null) return false;
    if (subnetIP != null ? !subnetIP.equals(newSubnet.subnetIP) : newSubnet.subnetIP != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = nodeId != null ? nodeId.hashCode() : 0;
    result = 31 * result + (subnetIP != null ? subnetIP.hashCode() : 0);
    result = 31 * result + slash;
    return result;
  }

  public Long getMinIP() {
    return new IP().getNetworkIP(subnetIP) & getSubnetMask();
  }

  public Long getMaxIP() {
    return getMinIP() + (1 << 32 - slash);
  }

  public Long getSubnetMask() {
    long mask = 0xFFFFFFFFL << (32 - slash) & 0xFFFFFFFFL;
    return mask;
  }

}
