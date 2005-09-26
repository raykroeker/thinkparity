/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.tree;

import com.thinkparity.model.parity.model.AbstractView;

/**
 * Tree
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Tree extends AbstractView {

	/**
	 * Create a new tree structure.
	 * @param rootNode
	 * @return <code>Tree</code.
	 */
	public static Tree createTree(final TreeNode rootNode) {
		return new Tree(rootNode);
	}

	/**
	 * Root of the tree.
	 */
	private TreeNode rootNode;

	/**
	 * Create a DataTree
	 */
	private Tree(final TreeNode rootNode) {
		super();
		this.rootNode = rootNode;
	}

	/**
	 * Obtain the root node.
	 * @return <code>TreeNode</code>
	 */
	public TreeNode getRootNode() { return rootNode; }
}
