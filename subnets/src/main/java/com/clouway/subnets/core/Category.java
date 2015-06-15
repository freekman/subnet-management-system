package com.clouway.subnets.core;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public final class Category {

  public final String id;
  public final String type;

  public Category(String id, String type) {
    this.id = id;
    this.type = type;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Category)) return false;

    Category category = (Category) o;

    if (id != null ? !id.equals(category.id) : category.id != null) return false;
    return !(type != null ? !type.equals(category.type) : category.type != null);

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    return result;
  }
}
