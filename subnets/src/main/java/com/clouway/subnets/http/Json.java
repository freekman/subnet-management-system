package com.clouway.subnets.http;


import com.clouway.subnets.core.HibernateValidationException;
import com.google.gson.Gson;
import com.google.inject.TypeLiteral;
import com.google.sitebricks.client.Transport;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
class Json implements Transport {

  private Gson gson = new Gson();
  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Override
  public <T> T in(InputStream inputStream, Class<T> aClass) throws IOException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    T dto = gson.fromJson(reader.readLine(), aClass);

    reader.close();

    Set<ConstraintViolation<T>> constraintViolations = validator.validate(dto);

    if (constraintViolations.isEmpty()) {
      return dto;
    }

    throw new HibernateValidationException();
  }

  @Override
  public <T> T in(InputStream inputStream, TypeLiteral<T> typeLiteral) throws IOException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    T dto = gson.fromJson(reader.readLine(), typeLiteral.getType());

    reader.close();

    Set<ConstraintViolation<T>> constraintViolations = validator.validate(dto);
    if (constraintViolations.isEmpty()) {
      return dto;
    }

    throw new HibernateValidationException();
  }

  @Override
  public <T> void out(OutputStream outputStream, Class<T> aClass, T t) throws IOException {
    PrintWriter writer = new PrintWriter(new BufferedOutputStream(outputStream));
    writer.println(gson.toJson(t));
    writer.flush();
    writer.close();
  }

  @Override
  public String contentType() {
    return "application/json";
  }

  private <T> String buildErrorMessage(Set<ConstraintViolation<T>> violations) {
    Iterator<ConstraintViolation<T>> iterator = violations.iterator();
    StringBuilder builder = new StringBuilder();

    while (iterator.hasNext()) {
      ConstraintViolation<T> next = iterator.next();
      builder.append(next.getPropertyPath().iterator().next().getName())
              .append(" - ")
              .append(next.getMessage())
              .append("\n");
    }

    return builder.toString();
  }

}
