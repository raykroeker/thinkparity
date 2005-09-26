/*
 * Aug 7, 2005
 */
package com.thinkparity.model.parity.model.tree;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * TreeModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class TreeModelImpl extends AbstractModelImpl {

	/**
	 * Create a TreeModelImpl
	 */
	TreeModelImpl() { super(); }

	Tree getTree(final Workspace workspace) throws ParityException {
		final Project rootProject = ProjectModel.getRootProject(workspace);
		return createTree(rootProject);
	}

	private Tree createTree(final Project rootProject) {
		final TreeNode rootNode = TreeNode.createTreeNode(new ProjectContentProvider(rootProject));
		return Tree.createTree(rootNode);
	}

	/**
	 * ProjectContentProvider
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class ProjectContentProvider implements TreeNodeContentProvider {
		/**
		 * Project for the tree node.
		 */
		private Project project;

		/**
		 * Create a ProjectContentProvider
		 * @param project
		 */
		private ProjectContentProvider(final Project project) {
			super();
			this.project = project;
		}

		/**
		 * @see com.thinkparity.model.parity.model.tree.TreeNodeContentProvider#getName()
		 */
		public String getName() { return project.getName(); }
	}
}
