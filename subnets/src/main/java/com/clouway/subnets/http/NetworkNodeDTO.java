package com.clouway.subnets.http;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
final class NetworkNodeDTO {

  private final class NodeDataDTO {
    final String id;
    final String parentId;

    public NodeDataDTO(String id, String parentId) {
      this.id = id;
      this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof NodeDataDTO)) return false;

      NodeDataDTO nodeData = (NodeDataDTO) o;

      if (id != null ? !id.equals(nodeData.id) : nodeData.id != null) return false;
      return !(parentId != null ? !parentId.equals(nodeData.parentId) : nodeData.parentId != null);

    }

    @Override
    public int hashCode() {
      int result = id != null ? id.hashCode() : 0;
      result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
      return result;
    }
  }

  public final String label;
  public final boolean noLeaf;
  public final NodeDataDTO data;
  public final List<NetworkNodeDTO> children = newArrayList();

  public NetworkNodeDTO(String id, String parentId, String label, boolean isParent) {
    this.label = label;
    this.noLeaf = isParent;
    data = new NodeDataDTO(id, parentId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NetworkNodeDTO)) return false;

    NetworkNodeDTO that = (NetworkNodeDTO) o;

    if (noLeaf != that.noLeaf) return false;
    if (label != null ? !label.equals(that.label) : that.label != null) return false;
    if (data != null ? !data.equals(that.data) : that.data != null) return false;
    return !(children != null ? !children.equals(that.children) : that.children != null);

  }

  @Override
  public int hashCode() {
    int result = label != null ? label.hashCode() : 0;
    result = 31 * result + (noLeaf ? 1 : 0);
    result = 31 * result + (data != null ? data.hashCode() : 0);
    result = 31 * result + (children != null ? children.hashCode() : 0);
    return result;
  }
}
