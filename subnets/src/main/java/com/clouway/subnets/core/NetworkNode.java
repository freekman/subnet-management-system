package com.clouway.subnets.core;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public final class NetworkNode {

  public final String id;
  public final String parentId;
  public final String name;
  public final boolean isParent;

  public NetworkNode(String id, String parentId, String name,boolean isParent) {
    this.id = id;
    this.parentId = parentId;
    this.name = name;
    this.isParent = isParent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NetworkNode)) return false;

    NetworkNode that = (NetworkNode) o;

    if (isParent != that.isParent) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    return !(parentId != null ? !parentId.equals(that.parentId) : that.parentId != null);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (id != null ? id.hashCode() : 0);
    result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
    result = 31 * result + (isParent ? 1 : 0);
    return result;
  }

}
