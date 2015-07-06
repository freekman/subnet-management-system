package com.clouway.subnets.persistence;

import com.clouway.subnets.core.NetworkNode;
import com.clouway.subnets.core.NetworkNodeException;
import com.clouway.subnets.core.NetworkNodeStore;
import com.clouway.subnets.core.NewNetworkNode;
import com.github.fakemongo.junit.FongoRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public class PersistentNetworkNodeRepositoryTest {

  @Rule
  public FongoRule fongo = new FongoRule();

  private PersistentNetworkNodeRepository repository;

  @Before
  public void setUp() throws Exception {
    repository = new PersistentNetworkNodeRepository(fongo.getDatabase("subnets"));
  }

  @Test
  public void registerRootNode() throws Exception {
    String parentId = "root";

    NewNetworkNode node = new NewNetworkNode("Sofia", parentId);

    String id = repository.register(node);

    List<NetworkNode> nodes = repository.findAllWithParent(parentId);

    List<NetworkNode> expected = newArrayList(new NetworkNode(id, parentId, "Sofia",false));

    assertThat(nodes, is(equalTo(expected)));
  }

  @Test(expected = NetworkNodeException.class)
  public void registerDuplicateSiblingNodes() throws Exception {
    String foo = "foo", parentId = dummyId();

    NewNetworkNode node = new NewNetworkNode(foo, parentId);

    repository.register(node);

    repository.register(node);
  }

  @Test
  public void registerExistingNodeOnDifferentLevels() throws Exception {
    String foo = "foo";
    String dummyId = dummyId();
    String scndDummyId = "559b9192c631aa6b3a4630ea";

    String idOne = repository.register(new NewNetworkNode(foo, dummyId));
    String idTwo = repository.register(new NewNetworkNode(foo, scndDummyId));

    List<NetworkNode> lvlOneNodes = repository.findAllWithParent(dummyId);
    List<NetworkNode> lvlTwoNodes = repository.findAllWithParent(scndDummyId);

    List<NetworkNode> firstExpected = newArrayList(new NetworkNode(idOne, dummyId, foo, false));
    List<NetworkNode> secondExpected = newArrayList(new NetworkNode(idTwo, scndDummyId, foo,false));

    assertThat(firstExpected, is(equalTo(lvlOneNodes)));
    assertThat(secondExpected, is(equalTo(lvlTwoNodes)));
  }

  @Test
  public void registerChildNode() throws Exception {
    String rootId = "root";
    String parentId = repository.register(new NewNetworkNode("parent", rootId));
    String childId = repository.register(new NewNetworkNode("child", parentId));

    List<NetworkNode> parents = repository.findAllWithParent(rootId);
    List<NetworkNode> expectedParents = newArrayList(new NetworkNode(parentId, rootId,"parent", true));

    List<NetworkNode> children = repository.findAllWithParent(parentId);
    List<NetworkNode> expectedChildren = newArrayList(new NetworkNode(childId, parentId, "child",false));

    assertThat(parents, is(equalTo(expectedParents)));
    assertThat(children, is(equalTo(expectedChildren)));
  }

  @Test
  public void deleteLeafNode() throws Exception {
    String parentId = repository.register(new NewNetworkNode("parent", dummyId()));
    String childId = repository.register(new NewNetworkNode("child", parentId));

    repository.delete(childId);

    List<NetworkNode> children = repository.findAllWithParent(parentId);
    List<NetworkNode> expectedChildren = newArrayList();

    List<NetworkNode> parents = repository.findAllWithParent(dummyId());
    List<NetworkNode> expectedParents = newArrayList(new NetworkNode(parentId, dummyId(),"parent", false));

    assertThat(children, is(equalTo(expectedChildren)));
    assertThat(parents,is(equalTo(expectedParents)));
  }

  @Test
  public void deleteSingleLeafNode() throws Exception {
    String parentId = repository.register(new NewNetworkNode("parent",dummyId()));
    String childId = repository.register(new NewNetworkNode("child",parentId));
    String childId2 = repository.register(new NewNetworkNode("child2",parentId));

    repository.delete(childId);

    List<NetworkNode> parents = repository.findAllWithParent(dummyId());
    List<NetworkNode> expectedParents = newArrayList(new NetworkNode(parentId, dummyId(),"parent", true));

    List<NetworkNode> children = repository.findAllWithParent(parentId);
    List<NetworkNode> expectedChildren = newArrayList(new NetworkNode(childId2, parentId, "child2", false));

    assertThat(parents, is(equalTo(expectedParents)));
    assertThat(children, is(equalTo(expectedChildren)));
  }

  @Test
  public void deleteRegisteredNode() throws Exception {
    String id = repository.register(new NewNetworkNode("foo", dummyId()));

    repository.delete(id);

    List<NetworkNode> nodes = repository.findAllWithParent(dummyId());
    assertThat(nodes.isEmpty(), is(true));
  }

  @Test(expected = NetworkNodeException.class)
  public void deleteParentNode() throws Exception {
    String fatherId = repository.register(new NewNetworkNode("father", "root"));
    String childId = repository.register(new NewNetworkNode("child", fatherId));

    repository.delete(fatherId);
  }

  private String dummyId() {
    return "559b9192c631aa6b3a4625ea";
  }

}