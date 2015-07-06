package com.clouway.subnets.http;

import com.google.sitebricks.SitebricksModule;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class HttpModule extends SitebricksModule {
  @Override
  protected void configureSitebricks() {
    at("/r/subnets").serve(SubnetService.class);
    at("/r/nodes").serve(NetworkNodeService.class);
  }
  
}
