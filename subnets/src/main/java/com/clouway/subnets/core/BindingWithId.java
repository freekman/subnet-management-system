package com.clouway.subnets.core;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public class BindingWithId {
  public final String id;
  public final String ip;
  public final String subnetId;
  public final String description;

  public BindingWithId(String id, String ip, String subnetId, String description) {
    this.id = id;
    this.ip = ip;
    this.subnetId = subnetId;
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BindingWithId that = (BindingWithId) o;

    if (description != null ? !description.equals(that.description) : that.description != null) return false;
    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
    if (subnetId != null ? !subnetId.equals(that.subnetId) : that.subnetId != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (ip != null ? ip.hashCode() : 0);
    result = 31 * result + (subnetId != null ? subnetId.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }
}
