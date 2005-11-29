/*
 * Nov 28, 2005
 */
package com.thinkparity.server.model.subscription;

import com.thinkparity.server.model.AbstractModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SubscriptionModel extends AbstractModel {

	/**
	 * Obtain a handle to the subscription model.
	 * 
	 * @return A handle to the subscription model.
	 */
	public static SubscriptionModel getModel() {
		final SubscriptionModel subscriptionModel = new SubscriptionModel();
		return subscriptionModel;
	}

	/**
	 * The subscription model implementation.
	 */
	private final SubscriptionModelImpl impl;

	/**
	 * Synchronization lock for the implementation.
	 */
	private final Object implLock;

	/**
	 * Create a SubscriptionModel.
	 */
	private SubscriptionModel() {
		super();
		this.impl = new SubscriptionModelImpl();
		this.implLock = new Object();
	}
}
