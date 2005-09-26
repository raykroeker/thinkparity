/*
 * Aug 7, 2005
 */
package com.thinkparity.model.parity.model.tree;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.ParityTest;
import com.thinkparity.model.parity.model.tree.Tree;
import com.thinkparity.model.parity.model.tree.TreeModel;

/**
 * TreeModelTest
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TreeModelTest extends ParityTest {

	/**
	 * Handle to the actual tree model returned.
	 */
	private TreeModel treeModel;

	/**
	 * Create a TreeModelTest
	 * @param name
	 */
	public TreeModelTest() { super("Test:  Tree model."); }

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this.treeModel = new TreeModel();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		this.treeModel = null;
	}

	public void testGetTree() {
		try {
			final Tree tree = treeModel.getTree(workspace);
			TreeModelTest.assertNotNull(tree);
		}
		catch(ParityException px) { fail(px.getMessage()); }
	}
}
