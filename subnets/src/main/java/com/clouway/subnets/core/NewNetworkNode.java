package com.clouway.subnets.core;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public final class NewNetworkNode {

  public final String name;
  public final String parentId;

  public NewNetworkNode(String name, String parentId) {
    this.name = name;
    this.parentId = parentId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NewNetworkNode)) return false;

    NewNetworkNode that = (NewNetworkNode) o;

    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    return !(parentId != null ? !parentId.equals(that.parentId) : that.parentId != null);

  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
    return result;
  }



}
