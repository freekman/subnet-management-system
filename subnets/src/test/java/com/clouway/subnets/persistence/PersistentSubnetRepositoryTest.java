package com.clouway.subnets.persistence;

import com.clouway.subnets.core.Binding;
import com.clouway.subnets.core.IllegalRequestException;
import com.clouway.subnets.core.NewSubnet;
import com.clouway.subnets.core.OverlappingSubnetException;
import com.clouway.subnets.core.Subnet;
import com.github.fakemongo.Fongo;
import com.google.common.base.Optional;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.mongodb.client.model.Filters.eq;
import static org.hamcrest.CoreMatchers.is;
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

    List<Subnet> expectedSubnet = newArrayList(new Subnet(id, "TV", "0.0.0.0", 31, "note"));
    List<Subnet> actualSubnet = repository.findAll();

    List<Binding> expectedBindings = newArrayList(new Binding(id, "note", "0.0.0.0"), new Binding(id, "note", "0.0.0.1"));
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

  @Test
  public void findSubnetById() throws Exception {
    final NewSubnet dummyNewSubnet = new NewSubnet("TV", "0.0.0.0", 31, "");
    String id = repository.register(dummyNewSubnet);

    Subnet expected = new Subnet(id, dummyNewSubnet.nodeId, dummyNewSubnet.subnetIP, dummyNewSubnet.slash, "note");

    Subnet result = repository.findById(id).get();

    assertThat(result, is(expected));
  }

  @Test
  public void findSubnetByUnexistingId() throws Exception {
    Optional<Subnet> result = repository.findById("55ae2920cc795e24b1858c5c");
    assertFalse(result.isPresent());
  }

  @Test
  public void removeSubnet() throws Exception {
    final NewSubnet dummyNewSubnet = new NewSubnet("TV", "0.0.0.0", 31, "");
    String id = repository.register(dummyNewSubnet);

    repository.remove(id);

    Optional<Subnet> result = repository.findById(id);
    List<Binding> bindings=findAllBindingsBySubnet(id);

    assertFalse(result.isPresent());
    assertThat(bindings.size(),is(0));
  }

  @Test(expected = IllegalRequestException.class)
  public void removeMissingSubnet() throws Exception {
    String id = "55ae2920cc795e24b1858c5c";

    repository.remove(id);
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