package com.clouway.subnets.persistence;

import com.clouway.subnets.core.Binding;
import com.clouway.subnets.core.BindingWithId;
import com.clouway.subnets.core.NewSubnet;
import com.clouway.subnets.core.Slash;
import com.clouway.subnets.core.Subnet;
import com.github.fakemongo.Fongo;
import com.google.common.base.Optional;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class PersistentBindingRepositoryTest {
  private MongoDatabase database;
  private PersistentBindingRepository repository;

  @Before
  public void setUp() throws Exception {
    database = new Fongo("Fake subnet management system").getDatabase("subnets");
    repository = new PersistentBindingRepository(database);

  }

  @Test
  public void registerBindingsPerSubnet() throws Exception {
    NewSubnet newSubnet = new NewSubnet("aaa", "0.0.0.0", 31, "");
    final String id = "5597c56acc795e2e35b27102";
    repository.registerPerSubnet(newSubnet, id);

    List<Binding> expected = new ArrayList<Binding>() {{
      add(new Binding(id, "note", "0.0.0.0"));
      add(new Binding(id, "note", "0.0.0.1"));
    }};

    List<Binding> bindings = repository.findAllBySubnetID(id);
    Assert.assertThat(bindings, is(expected));
  }

  @Test
  public void removeAllBindings() throws Exception {
    NewSubnet newSubnet = new NewSubnet("aaa", "0.0.0.0", 31, "");
    final String id = "5597c56acc795e2e35b27102";
    repository.registerPerSubnet(newSubnet, id);
    repository.removePerSubnet(id);
    List<Binding> bindings = repository.findAllBySubnetID(id);

    assertThat(bindings.size(), is(0));
  }

  @Test
  public void resizeForSmallerSubnet() throws Exception {
    NewSubnet newSubnet = new NewSubnet("aaa", "3.3.3.3", 30, "");
    final String id = "5597c56acc795e2e35b27102";
    repository.registerPerSubnet(newSubnet, id);
    repository.resizePerSubnet(new Subnet(id, "aaa", "3.3.3.3", 30, ""), new Slash(31));

    List<Binding> expected = new ArrayList<Binding>() {{
      add(new Binding(id, "note", "3.3.3.2"));
      add(new Binding(id, "note", "3.3.3.3"));
    }};

    List<Binding> bindings = repository.findAllBySubnetID(id);
    Assert.assertThat(bindings, is(expected));
  }


  @Test
  public void resizeForBiggerSubnet() throws Exception {
    NewSubnet newSubnet = new NewSubnet("aaa", "3.3.3.3", 31, "");
    final String id = "5597c56acc795e2e35b27102";
    repository.registerPerSubnet(newSubnet, id);
    repository.resizePerSubnet(new Subnet(id, "aaa", "3.3.3.3", 31, ""), new Slash(30));

    List<Binding> expected = new ArrayList<Binding>() {{
      add(new Binding(id, "note", "3.3.3.2"));
      add(new Binding(id, "note", "3.3.3.3"));
      add(new Binding(id, "note", "3.3.3.0"));
      add(new Binding(id, "note", "3.3.3.1"));
    }};

    List<Binding> bindings = repository.findAllBySubnetID(id);
    Assert.assertThat(bindings, is(expected));
  }

  @Test
  public void resizeFromBothEnds() throws Exception {
    NewSubnet newSubnet = new NewSubnet("aaa", "3.3.3.3", 31, "");
    final String id = "5597c56acc795e2e35b27102";
    repository.registerPerSubnet(newSubnet, id);
    repository.resizePerSubnet(new Subnet(id, "aaa", "3.3.3.3", 31, ""), new Slash(29));

    List<Binding> expected = new ArrayList<Binding>() {{
      add(new Binding(id, "note", "3.3.3.2"));
      add(new Binding(id, "note", "3.3.3.3"));
      add(new Binding(id, "note", "3.3.3.0"));
      add(new Binding(id, "note", "3.3.3.1"));
      add(new Binding(id, "note", "3.3.3.4"));
      add(new Binding(id, "note", "3.3.3.5"));
      add(new Binding(id, "note", "3.3.3.6"));
      add(new Binding(id, "note", "3.3.3.7"));
    }};

    List<Binding> bindings = repository.findAllBySubnetID(id);
    Assert.assertThat(bindings, is(expected));
  }


  @Test
  public void findBindingByIP() throws Exception {
    NewSubnet newSubnet = new NewSubnet("aaa", "0.0.0.0", 31, "");
    final String id = "5597c56acc795e2e35b27102";
    repository.registerPerSubnet(newSubnet, id);

    BindingWithId binding = repository.findByIP(id, "0.0.0.0").get();

    String bindingID=getBindingId(id,"0.0.0.0");
    BindingWithId actual = new BindingWithId(bindingID,"0.0.0.0",id, "note");

    assertThat(binding, is(actual));
  }


  @Test
  public void findBindingByAnotherIP() throws Exception {
    NewSubnet newSubnet = new NewSubnet("aaa", "0.0.0.0", 31, "");
    final String id = "5597c56acc795e2e35b27102";
    repository.registerPerSubnet(newSubnet, id);
    Optional<BindingWithId> optional = repository.findByIP(id, "10.2.5.5");

    assertTrue(!optional.isPresent());
  }

  private String getBindingId(String subnetId, String ip) {
    FindIterable<Document> documents = database.getCollection("bindings").find(and(eq("subnetId", subnetId), eq("ip", ip))).limit(1);
    for (Document doc : documents) {
      return doc.getObjectId("_id").toString();
    }
    return null;
  }

}