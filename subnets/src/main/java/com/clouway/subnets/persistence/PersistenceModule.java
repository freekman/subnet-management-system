package com.clouway.subnets.persistence;

import com.clouway.subnets.core.SubnetFinder;
import com.clouway.subnets.core.SubnetRegister;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class PersistenceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SubnetRegister.class).to(PersistentSubnetRepository.class);
    bind(SubnetFinder.class).to(PersistentSubnetRepository.class);
  }
  @Provides
  @Singleton
  MongoClient provideMongoClient() {
    MongoClient client = new MongoClient();
    return  client;
  }
  @Provides
  MongoDatabase provideMongoDatabase(MongoClient client) {
    return client.getDatabase("subnets");
  }
}
