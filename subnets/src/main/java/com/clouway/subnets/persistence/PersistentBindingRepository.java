package com.clouway.subnets.persistence;

import com.clouway.subnets.core.Binding;
import com.clouway.subnets.core.BindingFinder;
import com.google.inject.Inject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
class PersistentBindingRepository implements BindingFinder {
  private MongoDatabase database;

  @Inject
  public PersistentBindingRepository(MongoDatabase database) {
    this.database = database;
  }

  @Override
  public List<Binding> findAllBySubnetID(String id) {
    Document query = new Document()
            .append("_id", new ObjectId(id));
    FindIterable<Document> documents = nets().find(query);
    return getAllBindings(documents);
  }

  private List<Binding> getAllBindings(FindIterable<Document> documents) {
    List<Binding> bindings = new ArrayList<>();
    List<Document> documentList = new ArrayList<>();
    for (Document doc : documents) {
      documentList = (List<Document>) doc.get("bindings");
    }
    for (Document document : documentList) {
      String id = document.getString("_id");
      String ip = (String) document.get("ip");
      String description = (String) document.get("description");
      bindings.add(new Binding(id, description, ip));
    }
    return bindings;
  }

  private MongoCollection<Document> nets() {
    return database.getCollection("nets");
  }
}
