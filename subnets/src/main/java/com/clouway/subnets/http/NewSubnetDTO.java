package com.clouway.subnets.http;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
class NewSubnetDTO {
  @NotEmpty
  public String category;
  @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$",
          message = "IP validation failed.")
  public final String ip;
  @Min(20)
  @Max(32)
  public final int slash;
  public final String description;

  public NewSubnetDTO(String category, String ip, int slash, String description) {
    this.category = category;
    this.ip = ip;
    this.slash = slash;
    this.description = description;
  }

}
