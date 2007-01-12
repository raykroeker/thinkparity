/*
 * Created On: Jan 6, 2006
 */
package com.thinkparity.ophelia.browser.util;


import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.archive.ArchiveModel;
import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.download.DownloadModel;
import com.thinkparity.ophelia.model.install.InstallModel;
import com.thinkparity.ophelia.model.migrator.ReleaseModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.user.UserModel;
import com.thinkparity.ophelia.model.workspace.Preferences;
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

    /** The thinkParity preferences. */
	private Preferences preferences;

    /** The thinkParity workspace. */
	private Workspace workspace;

    /**
     * Create ModelFactory.
     *
     */
	private ModelFactory() {
		super();
	}

    public ArchiveModel getArchiveModel(final Class clasz) {
        return modelFactory.getArchiveModel();
    }

    public ArtifactModel getArtifactModel(final Class clasz) {
        return modelFactory.getArtifactModel();
    }
    
    public ContactModel getContactModel(final Class clasz) {
        return modelFactory.getContactModel();
    }
    
    public ContainerModel getContainerModel(final Class clasz) {
        return modelFactory.getContainerModel();
    }

	public DocumentModel getDocumentModel(final Class clasz) {
		return modelFactory.getDocumentModel();
	}
    
    /**
     * Obtain a download model.
     * 
     * @param clasz
     *            A calling <code>Class</code>.
     * @return A <code>DownloadModel</code>.
     */
    public DownloadModel getDownloadModel(final Class clasz) {
        return modelFactory.getDownloadModel();
    }

    public InstallModel getInstallModel(final Class clasz) {
        return modelFactory.getInstallModel();
    }

    public Preferences getPreferences(final Class clasz) {
		return preferences;
	}

	/**
     * Obtain the thinkParity profileModel interface.
     * 
     * @param clasz
     *            The model consumer.
     * @return The thinkParity profileModel interface.
     */
    public ProfileModel getProfileModel(final Class clasz) {
        return modelFactory.getProfileModel();
    }

	public ReleaseModel getReleaseModel(final Class clasz) {
        return modelFactory.getReleaseModel();
    }

	public SessionModel getSessionModel(final Class clasz) {
		return modelFactory.getSessionModel();
	}

	/**
     * Obtain a parity user interface.
     *
     * @param clasz
     *      The name of the class will be used to obtain the user model.
     * @return A parity user interface.
     */
    public UserModel getUserModel(final Class clasz) {
        return modelFactory.getUserModel();
    }

	public Workspace getWorkspace(final Class clasz) {
		return workspace;
	}

    /**
	 * Initialize the model factory.
	 * 
	 */
	public void initialize(final Environment environment,
            final Workspace workspace) {
        Assert.assertIsNull("The model factory has already been initialized.",
                this.workspace);
        this.workspace = workspace;
        this.preferences = workspace.getPreferences();
        this.modelFactory = com.thinkparity.ophelia.model.ModelFactory.getInstance(environment, workspace);
    }
}
