package com.clouway.subnets.http;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by ivan.genchev1989@gmail.com
 */
final class SlashDTO {
  @Min(20)
  @Max(32)
  final int value;

  public SlashDTO(int value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SlashDTO slashDTO = (SlashDTO) o;

    if (value != slashDTO.value) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return value;
  }
}
