/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.haofeng.apps.dst.utils.clusterutil.clustering;

import java.util.Collection;

import com.baidu.mapapi.model.LatLng;

/**
 * A collection of ClusterItems that are nearby each other.
 */
public interface Cluster<T extends ClusterItem> {
	public LatLng getPosition();

	Collection<T> getItems();

	int getSize();
}