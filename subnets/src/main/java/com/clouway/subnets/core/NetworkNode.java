package com.clouway.subnets.core;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class NetworkNode {

  public final String name;
  public final String id;
  public final String parentId;
  public final boolean state;

  public NetworkNode(String name, String id, String parentId, boolean state) {
    this.name = name;
    this.id = id;
    this.parentId = parentId;
    this.state = state;
  }
}
