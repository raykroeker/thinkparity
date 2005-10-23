/*
 * Mar 27, 2005
 */
package com.thinkparity.model.parity.model.project;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.tree.DataTreeNode;

public class ProjectTreeNode extends DataTreeNode {

	public ProjectTreeNode(final Project project) {
		super(project.getId().toString(), NodeType.Project, project
				.getCustomName(), project.getMetaDataFile());
		for(Project subProject : project.getProjects()) {
			addChild(DataTreeNode.createInstance(subProject));
		}
		for(Document subDocument : project.getDocuments()) {
			addChild(DataTreeNode.createInstance(subDocument));
		}
	}
}
