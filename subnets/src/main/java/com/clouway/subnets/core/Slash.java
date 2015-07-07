package com.clouway.subnets.core;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public class Slash {
  public final int value;

  public Slash(int value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Slash slash = (Slash) o;

    if (value != slash.value) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return value;
  }
}
