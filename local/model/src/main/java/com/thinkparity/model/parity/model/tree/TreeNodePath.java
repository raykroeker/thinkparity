/*
 * Aug 7, 2005
 */
package com.thinkparity.model.parity.model.tree;

/**
 * TreeNodePath
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TreeNodePath {

	/**
	 * The node for which this path belongs.
	 */
	private TreeNode treeNode;

	/**
	 * Create a TreeNodePath
	 */
	TreeNodePath(final TreeNode treeNode) {
		super();
		this.treeNode = treeNode;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		final StringBuffer path = new StringBuffer("/");
		if(!treeNode.isSetParent()) { path.append(treeNode.getName()); }
		else {
			path.append(treeNode.getParent().getTreeNodePath())
				.append("/")
				.append(treeNode.getName());
		}
		return path.toString();
	}
}
