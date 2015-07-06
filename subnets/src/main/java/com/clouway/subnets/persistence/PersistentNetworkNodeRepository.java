package com.clouway.subnets.persistence;

import com.clouway.subnets.core.NetworkNode;
import com.clouway.subnets.core.NetworkNodeException;
import com.clouway.subnets.core.NetworkNodeFinder;
import com.clouway.subnets.core.NetworkNodeStore;
import com.clouway.subnets.core.NewNetworkNode;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
class PersistentNetworkNodeRepository implements NetworkNodeStore, NetworkNodeFinder{

  private final MongoDatabase db;

  @Inject
  public PersistentNetworkNodeRepository(MongoDatabase db) {
    this.db = db;
  }

  @Override
  public String register(NewNetworkNode newNode) {
    String name = newNode.name;
    String parentId = newNode.parentId;

    if (isExisting(newNode)) {
      throw new NetworkNodeException("Node " + name + " on level " + newNode.parentId + " exists.");
    }

    Document node = new Document("name", name)
            .append("parentId", parentId)
            .append("isParent", false);

    nodes().insertOne(node);

    setParent(newNode.parentId, true);

    String id = node.getObjectId("_id").toString();

    return id;
  }

  @Override
  public List<NetworkNode> findAllWithParent(String id) {
    MongoCursor<Document> documents = nodes().find(eq("parentId", id)).iterator();

    List<NetworkNode> nodes = newArrayList();

    while (documents.hasNext()) {
      Document each = documents.next();
      NetworkNode node = adapt(each);
      nodes.add(node);
    }

    return nodes;
  }

  @Override
  public void delete(String id) {
    if (hasChildren(id)) {
      throw new NetworkNodeException("Attempting to remove node with children.");
    }

    Document node = nodes().find(eq("_id", new ObjectId(id))).first();
    String parentId = node.getString("parentId");

    nodes().deleteOne(eq("_id", new ObjectId(id)));

    if (!hasChildren(parentId)) {
      setParent(parentId, false);
    }
  }

  private boolean isExisting(NewNetworkNode networkNode) {
    Document node = nodes().find(and(eq("name", networkNode.name), eq("parentId", networkNode.parentId))).first();

    return node != null;
  }

  private MongoCollection<Document> nodes() {
    return db.getCollection("nodes");
  }

  /**
   * Sets the isParent field of a network node.
   *
   * @param isParent true if the node has children, false otherwise.
   * @param id       of the node.
   */
  private void setParent(String id, boolean isParent) {
    boolean isNotRoot = !"root".equals(id);

    if (isNotRoot) {
      nodes().updateOne(new Document("_id", new ObjectId(id)), new Document("$set", new Document("isParent", isParent)));
    }
  }

  private boolean hasChildren(String id) {
    Document child = nodes().find(eq("parentId", id)).first();
    return child != null;
  }

  private NetworkNode adapt(Document entity) {
    String name = entity.getString("name");

    String nodeId = entity.getObjectId("_id").toString();

    String parentId = entity.getString("parentId");

    Boolean isParent = entity.getBoolean("isParent");

    return new NetworkNode(nodeId, parentId, name,isParent);
  }

}