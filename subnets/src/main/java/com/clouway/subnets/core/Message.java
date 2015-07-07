package com.clouway.subnets.core;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public class Message {
  public final String text;

  public Message(String text) {
    this.text = text;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Message message = (Message) o;

    if (text != null ? !text.equals(message.text) : message.text != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return text != null ? text.hashCode() : 0;
  }
}
