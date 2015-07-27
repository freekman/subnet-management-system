package com.clouway.subnets.core;

/**
 * Created by ivan.genchev1989@gmail.com
 */
public final class NewSlash {
  public final int value;

  public NewSlash(int value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NewSlash newSlash = (NewSlash) o;

    if (value != newSlash.value) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return value;
  }
}
