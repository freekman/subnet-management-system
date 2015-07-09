package com.clouway.subnets.core;

import com.google.common.base.Optional;

import java.util.List;

/**
 * Created by ivan.genchev1989@gmail.com.
 */

public interface BindingFinder {

  Optional<BindingWithId> findByIP(String subnetId,String ip);
}
