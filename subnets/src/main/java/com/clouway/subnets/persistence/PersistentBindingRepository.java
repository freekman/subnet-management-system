package com.clouway.subnets.persistence;

import com.clouway.subnets.core.Binding;
import com.clouway.subnets.core.BindingFinder;
import com.clouway.subnets.core.BindingRegister;
import com.clouway.subnets.core.IP;
import com.clouway.subnets.core.NewSubnet;
import com.google.inject.Inject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
class PersistentBindingRepository implements BindingRegister, BindingFinder {
  private MongoDatabase database;

  @Inject
  public PersistentBindingRepository(MongoDatabase database) {
    this.database = database;
  }

  @Override
  public void registerPerSubnet(NewSubnet subnet, String id) {
    bindings().insertMany(allBindings(subnet, id));
  }

  @Override
  public void removePerSubnet(String subnetId) {
    bindings().deleteMany(new Document("subnetId", subnetId));
  }

  @Override
  public List<Binding> findAllBySubnetID(String id) {
    Document query = new Document()
            .append("subnetId", id);
    FindIterable<Document> documents = bindings().find(query);
    return getAllBindings(documents);
  }

  private List<Binding> getAllBindings(FindIterable<Document> documents) {
    List<Binding> bindings = new ArrayList<>();
    for (Document document : documents) {
      String id = document.getString("subnetId");
      String ip = (String) document.get("ip");
      String description = (String) document.get("description");
      bindings.add(new Binding(id, description, ip));
    }
    return bindings;
  }

  /**
   * Make a List of Documents with ip and an empty description for all the available IPs.
   *
   * @return
   */
  private List<Document> allBindings(NewSubnet newSubnet, String id) {
    List<Document> documentList = new ArrayList<>();
    Long min = newSubnet.getMinIP();
    Long max = newSubnet.getMaxIP();
    while (min < max) {
      documentList.add(new Document().append("ip", IP.getHost(min)).append("description", "note").append("subnetId", id));
      min++;
    }
    return documentList;
  }

  private MongoCollection<Document> bindings() {
    return database.getCollection("bindings");
  }


}
