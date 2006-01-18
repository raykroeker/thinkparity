/*
 * Jan 6, 2006
 */
package com.thinkparity.browser.model;

import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelFactory {

	public static SessionModel getSessionModel(final Class clasz) {
		return SessionModel.getModel();
	}

	public static Preferences getPreferences(final Class clasz) {
		return getWorkspace(clasz).getPreferences();
	}

	public static Workspace getWorkspace(final Class clasz) {
		return getWorkspaceModel(clasz).getWorkspace();
	}

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
	 * Create a ModelFactory [Singleton]
	 * 
	 */
	private ModelFactory() { super(); }
}
