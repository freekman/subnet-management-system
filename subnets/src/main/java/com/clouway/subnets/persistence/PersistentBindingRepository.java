package com.clouway.subnets.persistence;

import com.clouway.subnets.core.*;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;

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
    bindings().insertMany(allBindings(subnet, null, id));
  }

  @Override
  public void removePerSubnet(String subnetId) {
    bindings().deleteMany(new Document("subnetId", subnetId));
  }

  @Override
  public void resizePerSubnet(Subnet subnet, Slash newSlash) {
    NewSubnet oldSubnet = adapt(subnet, subnet.slash);
    NewSubnet newSubnet = adapt(subnet, newSlash.value);
    if (Long.compare(oldSubnet.getMaxIP(), newSubnet.getMaxIP()) > 0) {
      resizeToSmallerSubnet(subnet, newSubnet);
    } else if (Long.compare(oldSubnet.getMaxIP(), newSubnet.getMaxIP()) < 0) {
      resizeToBiggerSubnet(subnet.id, oldSubnet, newSubnet.slash);
    }
  }

  @Override
  public void updateDescription(String id, Message message) {
    bindings().updateOne(new Document("_id", new ObjectId(id)), new Document("$set", new Document("description", message.text)));

  }

  @Override
  public List<Binding> findAllBySubnetID(String id) {
    Document query = new Document()
            .append("subnetId", id);
    FindIterable<Document> documents = bindings().find(query);
    return getAllBindings(documents);
  }

  @Override
  public Optional<BindingWithId> findByIP(String subnetId, String ip) {

    FindIterable<Document> document = bindings().find(and(eq("subnetId", subnetId), eq("ip", ip))).limit(1);
    for (Document doc : document) {
      String id = doc.getObjectId("_id").toString();
      String subNetId = doc.getString("subnetId");
      String bindingIp = doc.getString("ip");
      String description = doc.getString("description");
      return Optional.of(new BindingWithId(id,bindingIp,subNetId, description));
    }

    return Optional.absent();
  }

  /**
   * Calculates how many bindings to insert without copying existing ones so a new Resized subnet will be complete-this is done to save the existing bindings
   *
   * @param id-the        mongo id for the subnet
   * @param oldSubnet-the subnet before the refactoring
   * @param newSlash      - integer for the new slash
   */
  private void resizeToBiggerSubnet(String id, NewSubnet oldSubnet, int newSlash) {
    NewSubnet resized = new NewSubnet(oldSubnet.nodeId, IP.getHost(oldSubnet.getMaxIP()), newSlash, "");
    List<Document> documents = allBindings(resized, oldSubnet, id);
    bindings().insertMany(documents);
  }

  /**
   * Removes the overlapping bindings
   */
  private void resizeToSmallerSubnet(Subnet subnet, NewSubnet newSubnet) {
    bindings().deleteMany(and(eq("subnetId", subnet.id), gte("position", newSubnet.getMaxIP())));
  }

  private List<Binding> getAllBindings(FindIterable<Document> documents) {
    List<Binding> bindings = new ArrayList<>();
    for (Document document : documents) {
      String subnetId = document.getString("subnetId");
      String ip = (String) document.get("ip");
      String description = (String) document.get("description");
      bindings.add(new Binding(subnetId, description, ip));
    }
    return bindings;
  }

  /**
   * Make a List of Documents with ip and an empty description for all the available IPs.
   *
   * @return
   */
  private List<Document> allBindings(NewSubnet newSubnet, NewSubnet oldSubnet, String id) {
    List<Document> documentList = new ArrayList<>();
    Long min;
    if (null != oldSubnet) {
      min = oldSubnet.getMaxIP();
    } else {
      min = newSubnet.getMinIP();
    }
    Long max = newSubnet.getMaxIP();
    while (min < max) {
      documentList.add(new Document()
              .append("ip", IP.getHost(min))
              .append("description", "note")
              .append("subnetId", id)
              .append("position", min));
      min++;
    }
    return documentList;
  }

  private NewSubnet adapt(Subnet subnet, int newSlash) {
    return new NewSubnet(subnet.nodeId, subnet.subnetIP, newSlash, subnet.description);
  }

  private MongoCollection<Document> bindings() {
    return database.getCollection("bindings");
  }


}
