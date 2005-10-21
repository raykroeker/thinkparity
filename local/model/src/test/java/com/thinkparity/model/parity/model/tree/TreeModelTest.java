/*
 * Aug 7, 2005
 */
package com.thinkparity.model.parity.model.tree;

import java.util.Vector;

import com.thinkparity.model.ModelTestCase;

/**
 * TreeModelTest
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TreeModelTest extends ModelTestCase {

	/**
	 * Get tree data structure.
	 */
	private class GetTreeData {
		private TreeModel treeModel;
		private GetTreeData(final TreeModel treeModel) {
			this.treeModel = treeModel;
		}
	}

	private Vector<GetTreeData> getTreeData;

	/**
	 * Create a TreeModelTest
	 * @param name
	 */
	public TreeModelTest() { super("Test:  Tree model."); }

	public void testGetTree() {
		try {
			Tree tree;
			for(GetTreeData data : getTreeData) {
				tree = data.treeModel.getTree();
				TreeModelTest.assertNotNull(tree);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		setUpGetTree();
	}

	protected void setUpGetTree() throws Exception {
		getTreeData = new Vector<GetTreeData>(1);
		getTreeData.add(new GetTreeData(getTreeModel()));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownGetTree();
	}
	
	protected void tearDownGetTree() throws Exception {
		getTreeData.clear();
		getTreeData = null;
	}
}
