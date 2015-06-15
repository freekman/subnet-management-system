package com.clouway.subnets.http;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
class JsonParseException extends RuntimeException {

  private final String message;

  public JsonParseException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
