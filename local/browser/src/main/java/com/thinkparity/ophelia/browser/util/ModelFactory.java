/*
 * Created On: Jan 6, 2006
 */
package com.thinkparity.ophelia.browser.util;


import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.backup.BackupModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.migrator.MigratorModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.user.UserModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

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

    /** The thinkParity <code>ModelFactory</code>. */
    private com.thinkparity.ophelia.model.ModelFactory modelFactory;

    /** The thinkParity workspace. */
	private Workspace workspace;

    /**
     * Create ModelFactory.
     *
     */
	private ModelFactory() {
		super();
	}

    /**
     * Obtain an artifact model.
     * 
     * @param clasz
     *            The name of clasz will be used to obtain an appropriate model.
     * @return An instance of <code>ArtifactModel</code>.
     */
    public final ArtifactModel getArtifactModel(final Class clasz) {
        return modelFactory.getArtifactModel();
    }
    /**
     * Obtain a backup model.
     * 
     * @param clasz
     *            The name of clasz will be used to obtain an appropriate model.
     * @return An instance of <code>BackupModel</code>.
     */
    public final BackupModel getBackupModel(final Class clasz) {
        return modelFactory.getBackupModel();
    }

    /**
     * Obtain a contact model.
     * 
     * @param clasz
     *            The name of clasz will be used to obtain an appropriate model.
     * @return An instance of <code>ContactModel</code>.
     */
    public final ContactModel getContactModel(final Class clasz) {
        return modelFactory.getContactModel();
    }


    /**
     * Obtain a container model.
     * 
     * @param clasz
     *            The name of clasz will be used to obtain an appropriate model.
     * @return An instance of <code>ContainerModel</code>.
     */
    public final ContainerModel getContainerModel(final Class clasz) {
        return modelFactory.getContainerModel();
    }

    /**
     * Obtain a document model.
     * 
     * @param clasz
     *            The name of clasz will be used to obtain an appropriate model.
     * @return An instance of <code>DocumentModel</code>.
     */
	public final DocumentModel getDocumentModel(final Class clasz) {
		return modelFactory.getDocumentModel();
	}
    
    /**
     * Obtain a migrator model.
     * 
     * @param clasz
     *            The name of clasz will be used to obtain an appropriate model.
     * @return An instance of <code>MigratorModel</code>.
     */
    public final MigratorModel getMigratorModel(final Class clasz) {
        return modelFactory.getMigratorModel();
    }

    /**
     * Obtain a profile model.
     * 
     * @param clasz
     *            The name of clasz will be used to obtain an appropriate model.
     * @return An instance of <code>ProfileModel</code>.
     */
    public final ProfileModel getProfileModel(final Class clasz) {
        return modelFactory.getProfileModel();
    }

    /**
     * Obtain a session model.
     * 
     * @param clasz
     *            The name of clasz will be used to obtain an appropriate model.
     * @return An instance of <code>SessionModel</code>.
     */
	public final SessionModel getSessionModel(final Class clasz) {
		return modelFactory.getSessionModel();
	}

    /**
     * Obtain a user model.
     * 
     * @param clasz
     *            The name of clasz will be used to obtain an appropriate model.
     * @return An instance of <code>UserModel</code>.
     */
    public final UserModel getUserModel(final Class clasz) {
        return modelFactory.getUserModel();
    }

    /**
     * Obtain workspace.
     * 
     * @param clasz
     *            The name of clasz will be used to obtain an appropriate
     *            workspace.
     * @return An instance of <code>Workspace</code>.
     */
	public final Workspace getWorkspace(final Class clasz) {
		return workspace;
	}

	/**
     * Initialize the model factory.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
	public final void initialize(final Environment environment,
            final Workspace workspace) {
        Assert.assertIsNull("The model factory has already been initialized.",
                this.workspace);
        this.workspace = workspace;
        this.modelFactory = com.thinkparity.ophelia.model.ModelFactory.getInstance(environment, workspace);
    }
}
