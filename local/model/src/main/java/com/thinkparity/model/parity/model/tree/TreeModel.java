/*
 * Aug 7, 2005
 */
package com.thinkparity.model.parity.model.tree;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModel;

/**
 * TreeModel
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TreeModel extends AbstractModel {

	/**
	 * Obtain a handle to the tree model interface.
	 * 
	 * @return The tree model interface.
	 */
	public static TreeModel getModel() {
		final TreeModel treeModel = new TreeModel();
		return treeModel;
	}

	/**
	 * Implementation.
	 * @see TreeModel#implLock
	 */
	private final TreeModelImpl impl;

	/**
	 * Synchronization lock.
	 * @see TreeModel#impl
	 */
	private final Object implLock;

	/**
	 * Create a TreeModel
	 */
	private TreeModel() {
		super();
		this.impl = new TreeModelImpl();
		this.implLock = new Object();
	}

	/**
	 * Obtain the tree.
	 * 
	 * @return The parity tree.
	 */
	public Tree getTree() throws ParityException {
		synchronized(implLock) { return impl.getTree(); }
	}
}
