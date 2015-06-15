package com.clouway.subnets.persistence;

import com.clouway.subnets.core.Category;
import com.clouway.subnets.core.CategoryException;
import com.clouway.subnets.core.CategoryRepository;
import com.clouway.subnets.core.NewCategory;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

import static com.google.common.collect.Lists.*;
import static com.mongodb.client.model.Filters.eq;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
class PersistentCategoryRepository implements CategoryRepository {

  private MongoDatabase db;

  @Inject
  PersistentCategoryRepository(MongoDatabase db) {
    this.db = db;
  }

  @Override
  public String register(NewCategory category) {
    if (isExisting(category)) {
      throw new CategoryException();
    }

    Document newCategory = new Document("type", category.type);

    categories().insertOne(newCategory);

    return newCategory.getObjectId("_id").toString();
  }

  @Override
  public void delete(String id) {

    categories().deleteOne(eq("_id", new ObjectId(id)));
  }

  @Override
  public List<Category> findAll() {
    FindIterable<Document> documents = categories().find();

    MongoCursor<Document> iterator = documents.iterator();

    List<Category> categories = newArrayList();

    while (iterator.hasNext()) {
      Document category = iterator.next();

      String type = category.getString("type");

      String id = category.getObjectId("_id").toString();

      categories.add(new Category(id, type));
    }

    return categories;
  }

  private MongoCollection<Document> categories() {
    return db.getCollection("categories");
  }

  private boolean isExisting(NewCategory category) {
    Document first = categories().find(eq("type", category.type)).first();

    return first != null;
  }

}
