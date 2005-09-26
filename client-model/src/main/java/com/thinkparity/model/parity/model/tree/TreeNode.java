/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.tree;

import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

/**
 * DataTreeNode
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TreeNode {

	/**
	 * Create an instance of the appropriate tree node for the given parity
	 * object.
	 * 
	 * @param treeNodeContentProvider
	 * @return A new tree node based upon the tree node's content provider.
	 */
	public static TreeNode createTreeNode(
			final TreeNodeContentProvider treeNodeContentProvider) {
		return new TreeNode(treeNodeContentProvider);
	}

	/**
	 * List of the children of the tree node.
	 */
	private final Vector<TreeNode> children;

	/**
	 * Underlying content provider for the tree node.
	 */
	private final TreeNodeContentProvider contentProvider;

	/**
	 * Parent tree node.
	 */
	private TreeNode parent;

	/**
	 * Create a TreeNode
	 * @param contentProvider
	 */
	private TreeNode(final TreeNodeContentProvider contentProvider) {
		super();
		this.children = new Vector<TreeNode>(3);
		this.contentProvider = contentProvider;
		this.parent = null;
	}

	/**
	 * Add a tree node to the list of children for this node.
	 * @param treeNode <code>TreeNode</code>
	 */
	public void addChild(final TreeNode treeNode) {
		treeNode.setParent(this);
		children.add(treeNode);
	}

	/**
	 * Obtain the number of children for this tree node.
	 * @return <code>Integer</code>
	 */
	public Integer getChildCount() { return children.size(); }

	/**
	 * Obtain the children of this tree node.
	 * 
	 * @return <code>Vector&lt;TreeNode&gt;</code>
	 */
	public Vector<TreeNode> getChildren() {
		final Vector<TreeNode> newChildren =
			new Vector<TreeNode>(children.size());
		newChildren.addAll(children);
		return newChildren;
	}

	/**
	 * Obtain the children of this tree node as an array.
	 * @return <code>TreeNode[]</code>
	 */
	public TreeNode[] getChildrenArray() {
		return getChildren().toArray(new TreeNode[] {});
	}

	/**
	 * Obtain the name of the node.
	 * @return <code>String</code>
	 */
	public String getName() { return contentProvider.getName(); }

	/**
	 * Obtain the parent node.
	 * @return <code>TreeNode</code>
	 */
	public TreeNode getParent() { return parent; }

	/**
	 * Obtain the path for this tree node.  Each node will have a unique path.
	 * @return <code>TreeNodePath</code>
	 */
	public TreeNodePath getTreeNodePath() { return new TreeNodePath(this); }

	/**
	 * Determine whether or not this node has any children.
	 * @return <code>java.lang.Boolean</code>
	 */
	public Boolean hasChildren() { return children.size() > 0; }

	/**
	 * Determine whether or not the parent for this node is set.
	 * @return <code>Boolean</code>
	 */
	public Boolean isSetParent() { return null != parent; }

	/**
	 * Set the parent reference for this node.
	 * @param parent <code>TreeNode</code>
	 */
	private void setParent(final TreeNode parent) {
		Assert.assertNotNull("", parent);
		Assert.assertNotTrue("", this == parent);
		Assert.assertNotTrue("", this.equals(parent));
		this.parent = parent;
	}
}
