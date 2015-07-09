package com.clouway.subnets.persistence;

import com.clouway.subnets.core.*;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.or;

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

    if (oldSubnet.slash < newSubnet.slash) {
      removeOverlappingBindings(subnet.id, newSubnet);
    } else if (oldSubnet.slash > newSubnet.slash) {
      addNewBindings(subnet.id, oldSubnet, newSubnet);
    }
  }

  @Override
  public void updateDescription(String id, Message message) {
    bindings().updateOne(new Document("_id", new ObjectId(id)), new Document("$set", new Document("description", message.text)));

  }

  @Override
  public Optional<BindingWithId> findByIP(String subnetId, String ip) {

    FindIterable<Document> document = bindings().find(and(eq("subnetId", subnetId), eq("ip", ip))).limit(1);
    for (Document doc : document) {
      String id = doc.getObjectId("_id").toString();
      String subNetId = doc.getString("subnetId");
      String bindingIp = doc.getString("ip");
      String description = doc.getString("description");
      return Optional.of(new BindingWithId(id, bindingIp, subNetId, description));
    }

    return Optional.absent();
  }

  /**
   * Calculates how many bindings to insert without copying existing ones so a new Resized subnet will be complete-this is done to save the existing bindings
   * @param id-the        mongo id for the subnet
   * @param oldSubnet-the subnet before the refactoring
   */
  private void addNewBindings(String id, NewSubnet oldSubnet, NewSubnet newSubnet) {
    List<Document> newBindings = allBindings(newSubnet, oldSubnet, id);
    bindings().insertMany(newBindings);
  }

  /**
   * Removes the overlapping bindings
   */
  private void removeOverlappingBindings(String id, NewSubnet newSubnet) {
    Bson bson = or(and(eq("subnetId", id), gt("position", newSubnet.getMaxIP())), and(eq("subnetId", id), lt("position", newSubnet.getMinIP())));

    bindings().deleteMany(bson);
  }
  /**
   * Make a List of Documents with bindings if no oldSubnet is provided the function will make a list only for the newSubnet,
   * if a oldSubnet is provided then bindings will be provided to the oldSubnet to become like the newSubnet with the deference
   * That this way the old bindings are preserved.
   */
  private List<Document> allBindings(NewSubnet newSubnet, NewSubnet oldSubnet, String id) {
    List<Document> documentList = new ArrayList<>();
    Long min;
    Long max;
    if (null != oldSubnet && Long.compare(newSubnet.getMinIP(), oldSubnet.getMinIP()) < 0) {
      min = newSubnet.getMinIP();
      max = oldSubnet.getMinIP();
      boolean clause = Long.compare(newSubnet.getMinIP(), oldSubnet.getMinIP()) == 0;
      addToBindingsDocument(id, documentList, min, max, clause);
    }
    if (null != oldSubnet && Long.compare(newSubnet.getMaxIP(), oldSubnet.getMaxIP()) > 0) {
      min = oldSubnet.getMaxIP();
      max = newSubnet.getMaxIP();
      boolean clause = Long.compare(newSubnet.getMaxIP(), oldSubnet.getMaxIP()) == 0;
      addToBindingsDocument(id, documentList, min, max, clause);
    }
    if (null == oldSubnet) {
      min = newSubnet.getMinIP();
      max = newSubnet.getMaxIP();
      addToBindingsDocument(id, documentList, min, max, false);
    }
    return documentList;
  }

  private void addToBindingsDocument(String id, List<Document> documentList, Long min, Long max, boolean breakingClause) {
    while (min < max) {
      if (breakingClause) break;
      documentList.add(new Document()
              .append("ip", IP.getHost(min))
              .append("description", "note")
              .append("subnetId", id)
              .append("position", min));
      min++;
    }
  }

  private NewSubnet adapt(Subnet subnet, int newSlash) {
    return new NewSubnet(subnet.nodeId, subnet.subnetIP, newSlash, subnet.description);
  }

  private MongoCollection<Document> bindings() {
    return database.getCollection("bindings");
  }


}
