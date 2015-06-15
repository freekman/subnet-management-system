package com.clouway.subnets.persistence;

import com.clouway.subnets.core.Category;
import com.clouway.subnets.core.CategoryException;
import com.clouway.subnets.core.NewCategory;
import com.github.fakemongo.junit.FongoRule;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class PersistentCategoryRepositoryTest {

  private PersistentCategoryRepository repository;

  @Rule
  public FongoRule fongo = new FongoRule();

  @Before
  public void setUp() throws Exception {
    repository = new PersistentCategoryRepository(fongo.getDatabase("nets"));
  }


  @Test
  public void findAll() throws Exception {
    NewCategory cars = new NewCategory("cars");
    NewCategory telephone = new NewCategory("telephone");

    String carId = repository.register(cars);

    String telephoneId = repository.register(telephone);

    List<Category> categories = repository.findAll();

    List<Category> expected = newArrayList(new Category(carId, "cars"), new Category(telephoneId, "telephone"));

    assertThat(categories, is(equalTo(expected)));
  }

  @Test(expected = CategoryException.class)
  public void duplicateCategoryInsertion() throws Exception {
    NewCategory cars = new NewCategory("cars");

    repository.register(cars);

    repository.register(cars);
  }

  @Test
  public void deleteExistingCategory() throws Exception {
    repository.register(new NewCategory("television"));

    List<Category> categories = repository.findAll();

    String id = categories.get(0).id;

    repository.delete(id);

    assertThat(repository.findAll().size(), is(0));
  }

  @Test
  public void attemptRemovalOfNonExistingCategory() throws Exception {
    String dummyId = "55896517c631aa3692d32b95";

    String categoryId = repository.register(new NewCategory("category1"));

    assertThat(categoryId, is(not(equalTo(dummyId))));

    repository.delete(dummyId);

    List<Category> expected = newArrayList(new Category(categoryId, "category1"));

    assertThat(repository.findAll(), is(equalTo(expected)));
  }


}