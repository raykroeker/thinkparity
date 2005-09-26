/*
 * Aug 7, 2005
 */
package com.thinkparity.model.parity.model.tree;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * TreeModel
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TreeModel extends AbstractModel {

	/**
	 * Handle to the internal implementation.
	 */
	private final TreeModelImpl impl;

	/**
	 * Create a TreeModel
	 */
	public TreeModel() {
		super();
		impl = new TreeModelImpl();
	}

	/**
	 * Obtain the tree.
	 * @return <code>DataTree</code>
	 */
	public Tree getTree(final Workspace workspace) throws ParityException {
		return impl.getTree(workspace);
	}
}
