package com.clouway.subnets.persistence;

import com.clouway.subnets.core.IllegalRequestException;
import com.clouway.subnets.core.NewSubnet;
import com.clouway.subnets.core.OverlappingSubnetException;
import com.clouway.subnets.core.Subnet;
import com.clouway.subnets.core.SubnetFinder;
import com.clouway.subnets.core.SubnetStore;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
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
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.or;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
class PersistentSubnetRepository implements SubnetStore, SubnetFinder {

  private MongoDatabase mongoDatabase;

  @Inject
  public PersistentSubnetRepository(MongoDatabase mongoDatabase) {
    this.mongoDatabase = mongoDatabase;
  }

  @Override
  public String register(NewSubnet newSubnet) {
    isOverlapping(newSubnet);

    Document subnet = new Document()
            .append("nodeId", newSubnet.nodeId)
            .append("ip", newSubnet.subnetIP)
            .append("slash", newSubnet.slash)
            .append("description", "note")
            .append("minIP", newSubnet.getMinIP())
            .append("maxIP", newSubnet.getMaxIP());

    nets().insertOne(subnet);
    String id = subnet.getObjectId("_id").toString();

    bindings().insertMany(bindings(id, newSubnet));

    return id;
  }

  @Override
  public void remove(String id) {
    if (!getSubnetById(id).isPresent()) {
      throw new IllegalRequestException();
    }
    nets().deleteOne(new Document("_id", new ObjectId(id)));
    bindings().deleteMany(new Document("subnetId", id));
  }

  @Override
  public List<Subnet> findAll() {
    List<Subnet> subnetList = new ArrayList<>();
    FindIterable<Document> document = nets().find();
    for (Document doc : document) {

      String id = doc.getObjectId("_id").toString();
      String ip = doc.getString("ip");
      String nodeId = doc.getString("nodeId");
      int slash = doc.getInteger("slash");
      String description = doc.getString("description");
      subnetList.add(new Subnet(id, nodeId, ip, slash, description));

    }
    return subnetList;
  }

  @Override
  public List<Subnet> findAllByParent(String id) {
    return null;
  }

  @Override
  public Optional<Subnet> findById(String id) {
    return getSubnetById(id);
  }

  /**
   * @param id-subnet Id
   * @return Returns Optional<Subnet>
   */
  private Optional<Subnet> getSubnetById(String id) {
    Document document = nets().find(eq("_id", new ObjectId(id))).first();
    if (document != null) {

      return adapt(id, document);
    }

    return Optional.absent();
  }

  /**
   * * If the new subnet overlaps any other subnet range the method will throw OverlappingSubnetsException .
   */
  private void isOverlapping(NewSubnet newSubnet) {

    FindIterable<Document> document = nets().find(or(
            getNewSubnetInRangeOfOldSubnet(newSubnet),
            getOldSubnetInRangeOfNewSubnet(newSubnet))).limit(1);
    if (null != document.first()) {
      throw new OverlappingSubnetException();
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

  /**
   * Make a List of Documents with ip and an empty description for all the available IPs.
   *
   * @return
   */
  private List<Document> bindings(String id, NewSubnet newSubnet) {
    List<Document> documents = Lists.newArrayList();
    Long min = newSubnet.getMinIP();
    Long max = newSubnet.getMaxIP();
    while (min < max) {
      documents.add(new Document("ip", newSubnet.getHost(min)).append("description", "note").append("subnetId", id));
      min++;
    }
    return documents;
  }

  private Optional<Subnet> adapt(String id, Document doc) {
    String nodeId = doc.getString("nodeId");
    String ip = doc.getString("ip");
    int slash = doc.getInteger("slash");
    String description = doc.getString("description");

    return Optional.of(new Subnet(id, nodeId, ip, slash, description));
  }

  private MongoCollection<Document> bindings() {
    return mongoDatabase.getCollection("bindings");
  }

  private MongoCollection<Document> nets() {
    return mongoDatabase.getCollection("nets");
  }

}
