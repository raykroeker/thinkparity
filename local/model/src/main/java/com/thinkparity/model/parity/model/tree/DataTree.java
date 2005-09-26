/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.tree;

import java.util.Iterator;

import com.thinkparity.model.parity.model.AbstractView;

/**
 * DataTree
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DataTree extends AbstractView {

	/**
	 * Create a new parity tree using node as the root element.
	 * 
	 * @param node
	 *            The node to use as a root element within the new tree.
	 * @return A fully constructed DataTree.
	 */
	public static DataTree createParityTree(final DataTreeNode node) {
		return new DataTree(node);
	}

	/**
	 * Root of the tree.
	 */
	private DataTreeNode root;

	/**
	 * Create a DataTree
	 */
	private DataTree(final DataTreeNode root) {
		super();
		this.root = root;
	}

	/**
	 * @return Iterator of DataTreeNodes.
	 * @deprecated - use getRoot().getChildren()
	 */
	public Iterator<DataTreeNode> getChildren() {
		return getRoot().getChildren();
	}

	public DataTreeNode getRoot() {
		return root;
	}

	public DataTreeNode getParent(final DataTreeNode parityTreeNode) {
		return null;
	}
}
