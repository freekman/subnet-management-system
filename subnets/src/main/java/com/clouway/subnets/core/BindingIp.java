package com.clouway.subnets.core;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public class BindingIp {


  public final  String value;

  public BindingIp(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BindingIp ip = (BindingIp) o;

    if (value != null ? !value.equals(ip.value) : ip.value != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return value != null ? value.hashCode() : 0;
  }
}
