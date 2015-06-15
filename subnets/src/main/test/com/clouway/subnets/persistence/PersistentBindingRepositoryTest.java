package com.clouway.subnets.persistence;

import com.clouway.subnets.core.Binding;
import com.clouway.subnets.core.NewSubnet;
import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoDatabase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PersistentBindingRepositoryTest {
  private MongoDatabase database;
  private PersistentBindingRepository repository;
  private PersistentSubnetRepository subnetRepository;

  @Before
  public void setUp() throws Exception {
    database = new Fongo("Fake subnet management system").getDatabase("subnets");
    repository = new PersistentBindingRepository(database);
    subnetRepository = new PersistentSubnetRepository(database);
  }

  @Test
  public void happyPath() throws Exception {
    NewSubnet newSubnet = new NewSubnet("aaa", "0.0.0.0", 31, "");
    final String id = subnetRepository.register(newSubnet);

    List<Binding> expected = new ArrayList<Binding>() {{
      add(new Binding(id, "", "0.0.0.0"));
      add(new Binding(id, "", "0.0.0.1"));
    }};

    List<Binding> bindings = repository.findAllBySubnetID(id);
    Assert.assertThat(bindings, is(expected));
  }

}