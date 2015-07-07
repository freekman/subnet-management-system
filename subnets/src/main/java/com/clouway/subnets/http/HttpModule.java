package com.clouway.subnets.http;

import com.clouway.subnets.core.IP;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.sitebricks.SitebricksModule;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class HttpModule extends SitebricksModule {
  @Override
  protected void configureSitebricks() {
    at("/r/subnets").serve(SubnetService.class);
    at("/r/bindings").serve(BindingService.class);
  }
  
  
  @Provides
  @Singleton
  static IP provideMongoClient() {
    return new IP();
  }

}
