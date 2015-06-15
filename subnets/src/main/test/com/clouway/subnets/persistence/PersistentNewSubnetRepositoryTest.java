package com.clouway.subnets.persistence;

import com.clouway.subnets.core.NewSubnet;
import com.clouway.subnets.core.OverlappingSubnetException;
import com.clouway.subnets.core.Subnet;
import com.github.fakemongo.Fongo;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoDatabase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PersistentNewSubnetRepositoryTest {
  private PersistentSubnetRepository repository;

  @Before
  public void setUp() throws Exception {
    MongoDatabase db = new Fongo("Fake subnet managment system").getDatabase("subnets");
    repository = new PersistentSubnetRepository(db);
  }

  @Test
  public void registerNewSubnet() throws Exception {
    final NewSubnet dummyNewSubnet = new NewSubnet("TV", "0.0.0.0", 31, "");
    String id = repository.register(dummyNewSubnet);
    List<Subnet> expected = Lists.newArrayList(new Subnet(id, "TV", "0.0.0.0", 31, ""));
    List<Subnet> actual=repository.findAll();
    assertEquals(expected,actual);
  }

  @Test(expected = OverlappingSubnetException.class)
  public void overlappingSubnet() throws Exception {
    repository.register(new NewSubnet("TV", "10.1.2.0", 23, ""));
    repository.register(new NewSubnet("TV", "10.1.3.248", 30, ""));
  }

  @Test(expected = OverlappingSubnetException.class)
  public void anotherOverlappingSubnet() throws Exception {
    repository.register(new NewSubnet("TV", "0.0.1.0", 31, ""));
    repository.register(new NewSubnet("TV", "0.0.0.0", 21, ""));
  }

}