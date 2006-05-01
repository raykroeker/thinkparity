/*
 * Jan 6, 2006
 */
package com.thinkparity.browser.model;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.index.IndexModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.user.UserModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelFactory {

    /** The singleton instance of the model factory. */
	private static final ModelFactory INSTANCE;

	static { INSTANCE = new ModelFactory(); }

    /**
     * Obtain a model factory instance.
     *
     * @return A model factory instance.
     */
	public static ModelFactory getInstance() { return INSTANCE; }

    /** The parity artifact interface. */
	private ArtifactModel artifactModel;

    /** The parity document interface. */
	private DocumentModel documentModel;

    /** The parity index interface. */
	private IndexModel indexModel;

    /** Flag indicating whether or not the factory is initialized. */
	private boolean isInitialized;

    /** The parity preferences. */
	private Preferences preferences;

    /** The parity session interface. */
	private SessionModel sessionModel;

    /** The parity user interface. */
    private UserModel uModel;

	/** The parity system message interface. */
	private SystemMessageModel systemMessageModel;

    /** The parity workspace. */
	private Workspace workspace;

    /** The parity workspace interface. */
	private WorkspaceModel workspaceModel;

	/** Create a ModelFactory. */
    // [Singleton,Factory]
	private ModelFactory() {
		super();
		this.isInitialized = false;
	}

	public ArtifactModel getArtifactModel(final Class clasz) {
		return artifactModel;
	}

	public DocumentModel getDocumentModel(final Class clasz) {
		return documentModel;
	}

	public IndexModel getIndexModel(final Class clasz) {
		return indexModel;
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

    /**
     * Obtain a parity user interface.
     *
     * @param clasz
     *      The name of the class will be used to obtain the user model.
     * @return A parity user interface.
     */
    public UserModel getUserModel(final Class clasz) { return uModel; }

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
			artifactModel = ArtifactModel.getModel();
			documentModel = DocumentModel.getModel();
			indexModel = IndexModel.getModel();
			sessionModel = SessionModel.getModel();
            uModel = UserModel.getModel();
			workspaceModel = WorkspaceModel.getModel();
			workspace = workspaceModel.getWorkspace();
			preferences = workspace.getPreferences();
			isInitialized = true;
		}
	}
}
