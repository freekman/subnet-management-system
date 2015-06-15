package com.clouway.subnets.http;


import com.clouway.subnets.core.HibernateValidationException;
import com.clouway.subnets.core.ObjectCastException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.sitebricks.client.Transport;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Set;

/**
 * Created byivan.genchev1989@gmail.com.
 */

public class Json implements Transport {
  private HttpServletResponse response;

  @Inject
  public Json(HttpServletResponse response) {
    this.response = response;
  }

  @Override
  public <T> T in(InputStream in, Class<T> type) throws IOException {
    String json = "";
    json = getJson(in, json);
    Set<ConstraintViolation<T>> errors = null;
    T object = getObjectFromJson(type, json, null);
    if (null != object) {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      errors = validator.validate(object);
    }
    if (errors.size() == 0) {
      return object;
    } else {
      throw new HibernateValidationException();
    }
  }

  @Override
  public <T> T in(InputStream in, TypeLiteral<T> type) throws IOException {
    return null;
  }

  @Override
  public <T> void out(OutputStream out, Class<T> type, T data) throws IOException {
  }


  @Override
  public String contentType() {
    return null;
  }

  private <T> T getObjectFromJson(Class<T> type, String json, T object) {
    Gson gson = new Gson();
    try {
      object = gson.fromJson(json, type);
    } catch (JsonSyntaxException e) {
      throw new ObjectCastException();
    }
    return object;
  }

  private String getJson(InputStream in, String object) throws IOException {
    InputStreamReader is = new InputStreamReader(in);
    StringBuilder sb = new StringBuilder();
    BufferedReader br = new BufferedReader(is);
    String read = br.readLine();
    while (read != null) {
      object = read;
      sb.append(read);
      read = br.readLine();
    }
    return object;
  }

}
