package com.clouway.subnets.core;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public class Binding {
  private String id;
  public final String ip;
  public final String description;

  public Binding(String id,String description,String ip) {
    this.id = id;
    this.ip = ip;
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Binding binding = (Binding) o;

    if (description != null ? !description.equals(binding.description) : binding.description != null) return false;
    if (id != null ? !id.equals(binding.id) : binding.id != null) return false;
    if (ip != null ? !ip.equals(binding.ip) : binding.ip != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (ip != null ? ip.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }
}
