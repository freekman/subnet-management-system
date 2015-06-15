package com.clouway.subnets.core;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class NewCategory {

  public final String type;

  public NewCategory(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NewCategory)) return false;

    NewCategory category = (NewCategory) o;

    return !(type != null ? !type.equals(category.type) : category.type != null);
  }

  @Override
  public int hashCode() {
    return type != null ? type.hashCode() : 0;
  }
}
