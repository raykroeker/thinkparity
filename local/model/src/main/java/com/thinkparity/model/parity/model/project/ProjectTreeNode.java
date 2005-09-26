/*
 * Mar 27, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.util.Iterator;

import com.thinkparity.model.parity.api.document.Document;
import com.thinkparity.model.parity.model.tree.DataTreeNode;

public class ProjectTreeNode extends DataTreeNode {

	public ProjectTreeNode(final Project project) {
		super(project.getId().toString(), NodeType.Project, project
				.getCustomName(), project.getMetaDataFile());
		Project childProject;
		for(Iterator<Project> iChildProjects = project.getProjects();
			iChildProjects.hasNext();) {
			childProject = iChildProjects.next();
			addChild(DataTreeNode.createInstance(childProject));
		}
		Document childDocument;
		for(Iterator<Document> iChildDocuments = project.getDocuments().iterator();
			iChildDocuments.hasNext();) {
			childDocument = iChildDocuments.next();
			addChild(DataTreeNode.createInstance(childDocument));
		}
	}
}
