package com.clouway.subnets.core;

import java.util.List;

/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 */
public interface NetworkNodeRepository {

  void register(String name, String parentId);

  void updateState(String id);

  List<NetworkNode> findAllByParent(String id);

}
