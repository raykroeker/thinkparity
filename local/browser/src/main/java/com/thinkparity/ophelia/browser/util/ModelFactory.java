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
import com.thinkparity.ophelia.model.index.IndexModel;
import com.thinkparity.ophelia.model.install.InstallModel;
import com.thinkparity.ophelia.model.migrator.ReleaseModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.user.UserModel;
import com.thinkparity.ophelia.model.workspace.Preferences;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

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

    /** A thinkParity archive interface. */
    private ArchiveModel archiveModel;

    /** A thinkParity artifact interface. */
    private ArtifactModel artifactModel;
    
    /** A thinkParity contact interface. */
    private ContactModel contactModel;   
    
    /** A thinkParity container interface. */
    private ContainerModel containerModel;
      
    /** A thinkParity document interface. */
	private DocumentModel documentModel;  

    /** A thinkParity downloadModel interface. */
    private DownloadModel downloadModel;

    /** A thinkParity index interface. */
	private IndexModel indexModel;

    /** A thinkParity installModel interface. */
    private InstallModel installModel;

    /** The thinkParity preferences. */
	private Preferences preferences;

    /** A thinkParity profileModel interface. */
    private ProfileModel profileModel;

    /** A thinkParity releaseModel interface. */
    private ReleaseModel releaseModel;

    /** A thinkParity session interface. */
	private SessionModel sessionModel;

	/** A thinkParity user interface. */
    private UserModel userModel;

    /** The thinkParity workspace. */
	private Workspace workspace;

    /** A thinkParity workspace interface. */
	private WorkspaceModel workspaceModel;

	/** Create ModelFactory. */
	private ModelFactory() {
		super();
	}

    public ArchiveModel getArchiveModel(final Class clasz) {
        return archiveModel;
    }
    
    public ArtifactModel getArtifactModel(final Class clasz) {
        return artifactModel;
    }
    
    public ContactModel getContactModel(final Class clasz) {
        return contactModel;
    }

	public ContainerModel getContainerModel(final Class clasz) {
        return containerModel;
    }
    
    public DocumentModel getDocumentModel(final Class clasz) {
		return documentModel;
	}

    /**
     * Obtain the parity downloadModel interface.
     * 
     * @param clasz
     *            The model consumer.
     * @return The parity downloadModel interface.
     */
    public DownloadModel getDownloadModel(final Class clasz) {
        return downloadModel;
    }

    public IndexModel getIndexModel(final Class clasz) {
		return indexModel;
	}

    /**
     * Obtain the parity installModel interface.
     * 
     * @param clasz
     *            The model consumer.
     * @return The parity installModel interface.
     */
    public InstallModel getInstall(final Class clasz) {
        return installModel;
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
        return profileModel;
    }

	/**
     * Obtain the parity releaseModel interface.
     * 
     * @param clasz
     *            The model consumer.
     * @return The parity releaseModel interface.
     */
	public ReleaseModel getReleaseModel(final Class clasz) {
        return releaseModel;
	}

	public SessionModel getSessionModel(final Class clasz) {
		return sessionModel;
	}

	/**
     * Obtain a parity user interface.
     *
     * @param clasz
     *      The name of the class will be used to obtain the user model.
     * @return A parity user interface.
     */
    public UserModel getUserModel(final Class clasz) {
        return userModel;
    }

	public Workspace getWorkspace(final Class clasz) {
		return workspace;
	}

	public WorkspaceModel getWorkspaceModel(final Class clasz) {
		return workspaceModel;
	}

    /**
	 * Initialize the model factory.
	 * 
	 */
	public void initialize(final Environment environment,
            final Workspace workspace) {
        Assert.assertIsNull("MODEL FACTORY ALREADY INITIALIZED", this.workspace);
        this.workspace = workspace;
        this.preferences = workspace.getPreferences();
        initializeModels(environment);
    }

    /**
     * Initialize the models.
     *
     */
    private void initializeModels(final Environment environment) {
        archiveModel = ArchiveModel.getModel(environment, workspace);
		artifactModel = ArtifactModel.getModel(environment, workspace);
        containerModel = ContainerModel.getModel(environment, workspace);
		documentModel = DocumentModel.getModel(environment, workspace);
        contactModel = ContactModel.getModel(environment, workspace);
        downloadModel = DownloadModel.getModel(environment, workspace);
        indexModel = IndexModel.getModel(environment, workspace);
        installModel = InstallModel.getModel(environment, workspace);
        profileModel = ProfileModel.getModel(environment, workspace);
        releaseModel = ReleaseModel.getModel(environment, workspace);
        sessionModel = SessionModel.getModel(environment, workspace);
        userModel = UserModel.getModel(environment, workspace);
        workspaceModel = WorkspaceModel.getModel(environment);
    }
}
