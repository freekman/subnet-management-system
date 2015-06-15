package com.clouway.subnets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class PropertyReader {

  private final Properties properties;

  public PropertyReader(String propName) {
    properties = new Properties();
    try {
      InputStream stream = new FileInputStream(propName);
      properties.load(stream);
    } catch (IOException e) {
      //e.printStackTrace();
    }
  }

  public int getJettyPort() {
    try {

      return Integer.parseInt(properties.getProperty("jetty.port"));
    } catch (NumberFormatException e){
      return 8080;
    }
  }

  public String getDbHost() {
    return properties.getProperty("db.host");
  }

  public int getDbPort() {
    return Integer.parseInt(properties.getProperty("db.port"));
  }

  public String getDbName() {
    return properties.getProperty("db.name");
  }

}
