package com.clouway.subnets.http;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
final class CategoryDTO {

  public final String id;

  public final String type;

  public CategoryDTO(String id, String type) {
    this.id = id;
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CategoryDTO)) return false;

    CategoryDTO that = (CategoryDTO) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    return !(type != null ? !type.equals(that.type) : that.type != null);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    return result;
  }
}
