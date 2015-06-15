package com.clouway.subnets.core;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public class NewSubnetBuilder {
  private String category = "";
  private String subnetIP = "";
  private int slash = 0;
  private String description = "";

  public NewSubnetBuilder category(String category) {
    this.category = category;
    return this;
  }

  public NewSubnetBuilder subnetIP(String subnetIP) {
    this.subnetIP = subnetIP;
    return this;
  }

  public NewSubnetBuilder slash(int slash) {
    this.slash = slash;
    return this;
  }

  public NewSubnetBuilder description(String description) {
    this.description = description;
    return this;
  }

  public NewSubnet build() {
    return new NewSubnet(category, subnetIP, slash, description);
  }
}
