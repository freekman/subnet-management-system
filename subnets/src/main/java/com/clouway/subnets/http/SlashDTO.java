package com.clouway.subnets.http;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
class SlashDTO {
  @Min(20)
  @Max(32)
  public final int value;

  public SlashDTO(int value) {

    this.value = value;
  }
}
