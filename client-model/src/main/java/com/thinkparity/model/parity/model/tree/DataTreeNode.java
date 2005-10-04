/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.tree;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentTreeNode;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectTreeNode;

/**
 * DataTreeNode
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class DataTreeNode {

	/**
	 * Create an instance of the appropriate tree node for the given parity
	 * object.
	 * 
	 * @param parityObject
	 *            <code>com.thinkparity.model.parity.model.ParityObject</code>
	 * @return <code>com.thinkparity.model.parity.model.ParityTreeNode</code>
	 */
	public static DataTreeNode createInstance(final ParityObject parityObject) {
		switch(parityObject.getType()) {
			case DOCUMENT:
				return new DocumentTreeNode((Document) parityObject);
			case PROJECT:
				return new ProjectTreeNode((Project) parityObject);
			default:
				throw Assert.createUnreachable("DataTreeNode.createInstance(ParityObject)");
		}
	}

	public enum NodeType { Document, Project }

	private String id;
	private File metaDataFile;
	private String name;
	private Collection<DataTreeNode> nodeChildren;
	private DataTreeNode nodeParent;
	private NodeType nodeType;

	/**
	 * Create a DataTreeNode
	 */
	protected DataTreeNode(final String id, final NodeType nodeType,
			final String name, final File metaDataFile) {
		super();
		this.id = id;
		this.nodeType = nodeType;
		this.name = name;
		this.metaDataFile = metaDataFile;
		this.nodeChildren = new Vector<DataTreeNode>(0);
		this.nodeParent = null;
	}

	public final void addChild(final DataTreeNode projectTreeNode) {
		projectTreeNode.setParent(this);
		nodeChildren.add(projectTreeNode);
	}

	public final int getChildCount() { return null == nodeChildren ? 0 : nodeChildren.size(); }

	public final Iterator<DataTreeNode> getChildren() {
		final Collection<DataTreeNode> children =
			new Vector<DataTreeNode>(nodeChildren.size());
		children.addAll(nodeChildren);
		return children.iterator();
	}

	public final DataTreeNode[] getChildrenArray() {
		return nodeChildren.toArray(new DataTreeNode[] {});
	}

	/**
	 * Obtain the value of id.
	 * @return <code>String</code>
	 */
	public String getId() {
		return id;
	}

	public final File getMetaDataFile() { return metaDataFile; }

	public final String getName() { return name; }

	public final DataTreeNode getParent() { return nodeParent; }

	public final Boolean hasChildren() {
		final boolean hasChildren =
			null != nodeChildren && 0 < nodeChildren.size();
		return hasChildren;
	}

	public final Boolean isRootNode() {
		final boolean isRootNode = null == nodeParent;
		return isRootNode;
	}

	public NodeType getNodeType() { return nodeType; }

	private void setParent(final DataTreeNode parent) {
		this.nodeParent = parent;
	}
}
