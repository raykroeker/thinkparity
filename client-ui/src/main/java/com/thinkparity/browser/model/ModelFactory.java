/*
 * Jan 6, 2006
 */
package com.thinkparity.browser.model;

import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelFactory {

	private static final ModelFactory INSTANCE;

	static { INSTANCE = new ModelFactory(); }

	public static ModelFactory getInstance() { return INSTANCE; }

	private DocumentModel documentModel;
	private boolean isInitialized;
	private Preferences preferences;
	private SessionModel sessionModel;

	/**
	 * The system message interface.
	 * 
	 */
	private SystemMessageModel systemMessageModel;

	private Workspace workspace;

	private WorkspaceModel workspaceModel;

	/**
	 * Create a ModelFactory [Singleton]
	 * 
	 */
	private ModelFactory() {
		super();
		this.isInitialized = false;
	}

	public DocumentModel getDocumentModel(final Class clasz) {
		return documentModel;
	}

	public Preferences getPreferences(final Class clasz) {
		return preferences;
	}

	public SessionModel getSessionModel(final Class clasz) {
		return sessionModel;
	}

	public SystemMessageModel getSystemMessageModel(final Class clasz) {
		if(null == systemMessageModel) {
			systemMessageModel = SystemMessageModel.getModel();
		}
		return systemMessageModel;
	}

	public Workspace getWorkspace(final Class clasz) {
		return getWorkspaceModel(clasz).getWorkspace();
	}

	public WorkspaceModel getWorkspaceModel(final Class clasz) {
		if(null == workspaceModel) {
			// NOTE We do this because of a circular dependency
			workspaceModel = WorkspaceModel.getModel();
		}
		return workspaceModel;
	}

	/**
	 * Initialize the model factory.
	 * 
	 */
	public void initialize() {
		if(!isInitialized) {
			documentModel = DocumentModel.getModel();
			sessionModel = SessionModel.getModel();
			workspaceModel = WorkspaceModel.getModel();
			workspace = workspaceModel.getWorkspace();
			preferences = workspace.getPreferences();
			isInitialized = true;
		}
	}
}
