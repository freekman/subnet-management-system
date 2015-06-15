package com.clouway.subnets.http;

import javax.validation.constraints.NotNull;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
final class NewCategoryDTO {

  @NotNull
  public final String type;

  public NewCategoryDTO(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NewCategoryDTO)) return false;

    NewCategoryDTO that = (NewCategoryDTO) o;

    return !(type != null ? !type.equals(that.type) : that.type != null);

  }

  @Override
  public int hashCode() {
    return type != null ? type.hashCode() : 0;
  }
}
