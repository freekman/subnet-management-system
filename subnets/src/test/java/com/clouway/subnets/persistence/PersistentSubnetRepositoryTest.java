package com.clouway.subnets.persistence;

import com.clouway.subnets.core.Binding;
import com.clouway.subnets.core.NewSubnet;
import com.clouway.subnets.core.OverlappingSubnetException;
import com.clouway.subnets.core.Subnet;
import com.github.fakemongo.Fongo;
import com.google.common.collect.Lists;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

public class PersistentSubnetRepositoryTest {
  private PersistentSubnetRepository repository;
  private MongoDatabase db;

  @Before
  public void setUp() throws Exception {
    db = new Fongo("Fake subnet managment system").getDatabase("subnets");
    repository = new PersistentSubnetRepository(db);


  }

  @Test
  public void registerNewSubnet() throws Exception {
    final NewSubnet dummyNewSubnet = new NewSubnet("TV", "0.0.0.0", 31, "");
    String id = repository.register(dummyNewSubnet);

    List<Subnet> expectedSubnet = Lists.newArrayList(new Subnet(id, "TV", "0.0.0.0", 31, "note"));
    List<Subnet> actualSubnet = repository.findAll();

    List<Binding> expectedBindings = Lists.newArrayList(new Binding(id, "note", "0.0.0.0"),new Binding(id, "note", "0.0.0.1"));
    List<Binding> actualBindings = findAllBindingsBySubnet(id);

    assertEquals(expectedSubnet, actualSubnet);
    assertEquals(expectedBindings, actualBindings);
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

  /**
   * @param id-subnet id
   * @return List of Bindings per subnet.
   */
  private List<Binding> findAllBindingsBySubnet(String id) {
    FindIterable<Document> documents = db.getCollection("bindings").find(eq("subnetId", id));

    return adapt(documents);
  }

  private List<Binding> adapt(FindIterable<Document> documents) {
    List<Binding> bindings = new ArrayList<>();

    for (Document document : documents) {
      String id = document.getString("subnetId");
      String ip = (String) document.get("ip");
      String description = (String) document.get("description");
      bindings.add(new Binding(id, description, ip));
    }

    return bindings;
  }

}