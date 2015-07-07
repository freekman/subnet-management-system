package com.clouway.subnets.persistence;

import com.clouway.subnets.core.IllegalRequestException;
import com.clouway.subnets.core.Message;
import com.clouway.subnets.core.NewSubnet;
import com.clouway.subnets.core.OverlappingSubnetException;
import com.clouway.subnets.core.Subnet;
import com.github.fakemongo.Fongo;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoDatabase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class PersistentSubnetRepositoryTest {
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
    List<Subnet> expected = Lists.newArrayList(new Subnet(id, "TV", "0.0.0.0", 31, "note"));
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
  @Test
  public void findById() throws Exception {
    NewSubnet newSubnet = new NewSubnet("nodeId", "10.1.2.0", 23, "");
    String id = repository.register(newSubnet);
    Subnet subnet = repository.findById(id).get();
    Subnet actual = new Subnet(id, newSubnet.nodeId, newSubnet.subnetIP, newSubnet.slash, "note");

    assertThat(subnet, is(actual));
  }

  @Test
  public void removeSubnet() throws Exception {
    NewSubnet newSubnet = new NewSubnet("nodeId", "10.1.2.0", 23, "");
    String id = repository.register(newSubnet);
    repository.remove(id);
    Optional<Subnet> subnet = repository.findById(id);

    assertTrue(!subnet.isPresent());
  }

  @Test(expected = IllegalRequestException.class)
  public void removeNotExistingSubnet() throws Exception {
    repository.remove("a3424324bc");
  }

  @Test
  public void updateDescription() throws Exception {
    NewSubnet newSubnet = new NewSubnet("nodeId", "10.1.2.0", 23, "");
    String id = repository.register(newSubnet);

    repository.updateDescription(id,new Message("TV"));

    Subnet subnet = repository.findById(id).get();
    Subnet actual = new Subnet(id, newSubnet.nodeId, newSubnet.subnetIP, newSubnet.slash, "TV");

    assertThat(subnet, is(actual));
  }

}