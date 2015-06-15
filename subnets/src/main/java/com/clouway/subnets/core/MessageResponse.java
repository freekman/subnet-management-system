package com.clouway.subnets.core;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class MessageResponse {

  public final String message;
  public final int httpStatusCode;

  public MessageResponse(String message, int httpStatusCode) {
    this.message = message;
    this.httpStatusCode = httpStatusCode;
  }
}
