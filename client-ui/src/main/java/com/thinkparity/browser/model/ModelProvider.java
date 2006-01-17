/*
 * Jan 6, 2006
 */
package com.thinkparity.browser.model;

import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelProvider {

	public static DocumentModel getDocumentModel(final Class clasz) {
		return DocumentModel.getModel();
	}

	public static ProjectModel getProjectModel(final Class clasz) {
		return ProjectModel.getModel();
	}

	public static WorkspaceModel getWorkspaceModel(final Class clasz) {
		return WorkspaceModel.getModel();
	}

	/**
	 * Create a ModelProvider [Singleton]
	 * 
	 */
	private ModelProvider() { super(); }
}
