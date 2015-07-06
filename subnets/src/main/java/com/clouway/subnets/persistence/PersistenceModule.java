package com.clouway.subnets.persistence;

import com.clouway.subnets.PropertyReader;
import com.clouway.subnets.core.BindingFinder;
import com.clouway.subnets.core.BindingRegister;
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

  private PropertyReader reader;

  public PersistenceModule(PropertyReader reader) {
    this.reader = reader;
  }

  @Override
  protected void configure() {
    bind(BindingRegister.class).to(PersistentBindingRepository.class);
    bind(BindingFinder.class).to(PersistentBindingRepository.class);
    bind(SubnetRegister.class).to(PersistentSubnetRepository.class);
    bind(SubnetFinder.class).to(PersistentSubnetRepository.class);
  }

  @Singleton
  @Provides
  MongoClient provideMongoClient() {
    return new MongoClient(reader.getDbHost(), reader.getDbPort());
  }

  @Provides
  MongoDatabase provideMongoDatabase(MongoClient client) {
    return client.getDatabase("subnets");
  }
}
