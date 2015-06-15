package com.clouway.subnets;

import com.clouway.subnets.core.PropertyReader;
import com.clouway.subnets.http.HttpModule;
import com.clouway.subnets.persistence.PersistenceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class Main {

  public static void main(String[] args) throws Exception {
    PropertyReader reader = getPropertyReader(args);
    Server jetty = getJetty(reader);
    jetty.start();
    jetty.join();
  }

  private static Server getJetty(final PropertyReader reader) {
    Server server = new Server(reader.getJettyPort());

    ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

    servletContextHandler.addEventListener(new GuiceServletContextListener() {
      @Override
      protected Injector getInjector() {
        return Guice.createInjector(new HttpModule(), new PersistenceModule());
      }
    });

    servletContextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

    // You MUST add DefaultServlet or your server will always return 404s
    servletContextHandler.addServlet(DefaultServlet.class, "/");
    servletContextHandler.setResourceBase("src/main/webapp");
    return server;
  }

  private static PropertyReader getPropertyReader(String[] args) {
    String configurationFile = "config.properties";

    if (args != null && args.length != 0) {
      configurationFile = args[0];
    }

    return new PropertyReader(configurationFile);
  }
}
