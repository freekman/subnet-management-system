package com.clouway.subnets.persistence;

import com.clouway.subnets.core.IP;
import com.clouway.subnets.core.NewSlash;
import com.clouway.subnets.core.NewSubnet;
import com.clouway.subnets.core.OverlappingSubnetException;
import com.clouway.subnets.core.Subnet;
import com.clouway.subnets.core.SubnetFinder;
import com.clouway.subnets.core.SubnetStore;
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
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
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
    if (!getOverlappingSubnets(newSubnet, 1).isEmpty()) {
      throw new OverlappingSubnetException();
    }

    Document subnet = new Document()
            .append("nodeId", newSubnet.nodeId)
            .append("ip", newSubnet.subnetIP)
            .append("slash", newSubnet.slash)
            .append("description", "note")
            .append("minIP", newSubnet.getMinIP())
            .append("maxIP", newSubnet.getMaxIP());

    nets().insertOne(subnet);
    String id = subnet.getObjectId("_id").toString();

    bindings().insertMany(allBindings(id, newSubnet, null));

    return id;
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

  @Override
  public NewSlash resize(String id, int slash) {
    Optional<Subnet> subnet = getSubnetById(id);
    if (subnet.isPresent()) {
      NewSubnet newSubnet = adapt(slash, subnet.get());
      List<Subnet> overlappingSubnets = getOverlappingSubnets(newSubnet, 2);

      if (overlappingSubnets.size() == 1 && overlappingSubnets.contains(subnet.get())) {
        resizeSubnet(id, newSubnet, slash);
        resizeBindings(subnet.get(), slash);
      } else {
        throw new OverlappingSubnetException();
      }

    }
    return new NewSlash(slash);
  }

  private void resizeBindings(Subnet subnet, int newSlash) {
    NewSubnet oldSubnet = adapt(subnet.slash, subnet);
    NewSubnet newSubnet = adapt(newSlash, subnet);

    if (oldSubnet.slash < newSubnet.slash) {
      removeOverlappingBindings(subnet.id, newSubnet);
    } else if (oldSubnet.slash > newSubnet.slash) {
      addNewBindings(subnet.id, oldSubnet, newSubnet);
    }
  }

  /**
   * Removes the overlapping bindings
   */
  private void removeOverlappingBindings(String id, NewSubnet newSubnet) {

    Bson lessThenMin = and(eq("subnetId", id), lt("position", newSubnet.getMinIP()));
    Bson greaterThenMax = and(eq("subnetId", id), gte("position", newSubnet.getMaxIP()));

    bindings().deleteMany(greaterThenMax);
    bindings().deleteMany(lessThenMin);

  }

  /**
   * Calculates how many bindings to insert without copying existing ones so a new Resized subnet will be complete-this is done to save the existing bindings
   *
   * @param id-the        mongo id for the subnet
   * @param oldSubnet-the subnet before the refactoring
   */
  private void addNewBindings(String id, NewSubnet oldSubnet, NewSubnet newSubnet) {
    List<Document> newBindings = allBindings(id, newSubnet, oldSubnet);
    bindings().insertMany(newBindings);
  }

  /**
   * Make a List of Documents with bindings if no oldSubnet is provided the function will make a list only for the newSubnet,
   * if a oldSubnet is provided then bindings will be provided to the oldSubnet to become like the newSubnet with the difference
   * that this way the old bindings are preserved.
   */
  private List<Document> allBindings(String id, NewSubnet newSubnet, NewSubnet oldSubnet) {
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

  /**
   * Updates the subnet by changing the slash,minIP,maxIP and description
   */
  private void resizeSubnet(String id, NewSubnet subnet, int newSlash) {
    nets().updateOne(new Document("_id", new ObjectId(id)),
            new Document("$set",
                    new Document("slash", newSlash)
                            .append("minIP", subnet.getMinIP())
                            .append("maxIP", subnet.getMaxIP())
                            .append("description", subnet.description)));
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

  private Optional<Subnet> adapt(String id, Document doc) {
    String nodeId = doc.getString("nodeId");
    String ip = doc.getString("ip");
    int slash = doc.getInteger("slash");
    String description = doc.getString("description");

    return Optional.of(new Subnet(id, nodeId, ip, slash, description));
  }


  private NewSubnet adapt(int slash, Subnet subnet) {
    return new NewSubnet(subnet.nodeId, subnet.subnetIP, slash, subnet.description);
  }

  private MongoCollection<Document> bindings() {
    return mongoDatabase.getCollection("bindings");
  }

  private MongoCollection<Document> nets() {
    return mongoDatabase.getCollection("nets");
  }

}
