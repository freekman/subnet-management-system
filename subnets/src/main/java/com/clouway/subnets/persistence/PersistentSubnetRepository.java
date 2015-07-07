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
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.or;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
class PersistentSubnetRepository implements SubnetRegister, SubnetFinder {

  private MongoDatabase mongoDatabase;

  @Inject
  public PersistentSubnetRepository(MongoDatabase mongoDatabase) {
    this.mongoDatabase = mongoDatabase;
  }

  @Override
  public String register(NewSubnet newSubnet) {
    List<Subnet> overlapingSubnets = getOverlappingSubnets(newSubnet, 1);

    if (overlapingSubnets.size() != 0) {
      throw new OverlappingSubnetException();
    }

    Document document = new Document();
    nets().insertOne(document
            .append("nodeId", newSubnet.nodeId)
            .append("ip", newSubnet.subnetIP)
            .append("slash", newSubnet.slash)
            .append("description", "note")
            .append("minIP", newSubnet.getMinIP())
            .append("maxIP", newSubnet.getMaxIP()));
    String id = document.getObjectId("_id").toString();
    return id;
  }

  @Override
  public void remove(String id) {
    try {
      nets().deleteOne(new Document("_id", new ObjectId(id)));
    } catch (IllegalArgumentException e) {
      throw new IllegalRequestException();
    }
  }

  @Override
  public void updateDescription(String id, Message message) {
    nets().updateOne(new Document("_id", new ObjectId(id)), new Document("$set", new Document("description", message.text)));
  }

  @Override
  public void resize(String id, Slash newSlash) {
    Optional<Subnet> subnet = getSubnetByID(id);
    if (subnet.isPresent()) {
      NewSubnet newSubnet = adapt(newSlash, subnet);
      List<Subnet> overlappingSubnets = getOverlappingSubnets(adapt(newSlash, subnet), 2);

      if (overlappingSubnets.size() == 1 && overlappingSubnets.contains(subnet.get())) {
        updateSubnetSize(newSubnet, newSlash, id);
      } else {
        throw new OverlappingSubnetException();
      }

    }
  }


  @Override
  public List<Subnet> findAll() {
    List<Subnet> subnetList = new ArrayList<>();
    FindIterable<Document> document = nets().find();
    for (Document doc : document) {
      String id = doc.getObjectId("_id").toString();
      String ip = doc.getString("ip");
      String category = doc.getString("nodeId");
      int slash = doc.getInteger("slash");
      String description = doc.getString("description");
      subnetList.add(new Subnet(id, category, ip, slash, description));
    }
    return subnetList;
  }

  @Override
  public Optional<Subnet> findById(String id) {
    return getSubnetByID(id);
  }

  @Override
  public Optional<NewSubnet> findSubnetById(String id) {
    return getNewSubnetByID(id);
  }


  @Override
  public List<Subnet> findAllByParent(String id) {
    return null;
  }

  /**
   * Returns an ArraryList of all subnets witch range overlaps with a given subnet,or an empty array list.
   */
  private List<Subnet> getOverlappingSubnets(NewSubnet subnet, int limit) {
    List<Subnet> subnetList = new ArrayList<>();
    FindIterable<Document> document = nets().find(or(
            getNewSubnetInRangeOfOldSubnet(subnet),
            getOldSubnetInRangeOfNewSubnet(subnet))).limit(limit);
    addSubnetToList(subnetList, document);
    return subnetList;
  }

  /**
   * Adds Subnets from a query document  to a given List.
   */
  private void addSubnetToList(List<Subnet> subnetList, FindIterable<Document> document) {
    for (Document doc : document) {
      String subnetId = doc.getObjectId("_id").toString();
      String ip = doc.getString("ip");
      String nodeId = doc.getString("nodeId");
      int slash = doc.getInteger("slash");
      String description = doc.getString("description");
      subnetList.add(new Subnet(subnetId, nodeId, ip, slash, description));
    }
  }

  /**
   * If the new subnet subnet.minIp >= currentSubnet.minIP  and subnet.minIp <= currentSubnet.maxIP then it is in the range of that subnet ,or
   * subnet.maxIP >= currentSubnet.minIP  and subnet.maxIP <= currentSubnet.maxIP then it is in the range of that subnet .
   */
  private Bson getOldSubnetInRangeOfNewSubnet(NewSubnet newSubnet) {
    return or(
            and(gte("minIP", newSubnet.getMinIP()), gte("maxIP", newSubnet.getMinIP()),
                    and(lte("minIP", newSubnet.getMaxIP()), lte("maxIP", newSubnet.getMaxIP()))));
  }

  /**
   * *If the new subnet subnet.minIp <= currentSubnet.minIP  and subnet.minIp <= currentSubnet.maxIP then it is in the range of that subnet ,and
   * subnet.maxIP >= currentSubnet.minIP  and subnet.maxIP >= currentSubnet.maxIP then the current subnet is in the range of the new subnet .
   */
  private Bson getNewSubnetInRangeOfOldSubnet(NewSubnet newSubnet) {
    return and(
            and(lte("minIP", newSubnet.getMinIP()), gte("maxIP", newSubnet.getMinIP()),
                    and(lte("minIP", newSubnet.getMaxIP()), gte("maxIP", newSubnet.getMaxIP()))));
  }

  private Optional<Subnet> getSubnetByID(String id) {
    FindIterable<Document> document = nets().find(new Document("_id", new ObjectId(id)));
    for (Document doc : document) {
      String subnetId = doc.getObjectId("_id").toString();
      String ip = doc.getString("ip");
      String nodeId = doc.getString("nodeId");
      int slash = doc.getInteger("slash");
      String description = doc.getString("description");
      return Optional.of(new Subnet(subnetId, nodeId, ip, slash, description));
    }
    return Optional.absent();
  }

  private Optional<NewSubnet> getNewSubnetByID(String id) {
    FindIterable<Document> document = nets().find(new Document("_id", new ObjectId(id)));
    for (Document doc : document) {
      String subnetId = doc.getObjectId("_id").toString();
      String ip = doc.getString("ip");
      String nodeId = doc.getString("nodeId");
      int slash = doc.getInteger("slash");
      String description = doc.getString("description");
      return Optional.of(new NewSubnet(nodeId, ip, slash, description));
    }
    return Optional.absent();
  }

  /**
   * Updates the subnet by changing the slash,minIP,maxIP and description
   */
  private void updateSubnetSize(NewSubnet subnet, Slash newSlash, String id) {
    nets().updateOne(new Document("_id", new ObjectId(id)),
            new Document("$set",
                    new Document("slash", newSlash.value)
                            .append("minIP", subnet.getMinIP())
                            .append("maxIP", subnet.getMaxIP())
                            .append("description", "note")));
  }

  private NewSubnet adapt(Slash newSlash, Optional<Subnet> subnet) {
    return new NewSubnet(subnet.get().nodeId, subnet.get().subnetIP, newSlash.value, subnet.get().description);
  }

  private MongoCollection<Document> nets() {
    return mongoDatabase.getCollection("nets");
  }

}
