/*
 * Created On: Jan 6, 2006
 */
package com.thinkparity.browser.platform.util.model;

import com.thinkparity.model.parity.model.archive.ArchiveModel;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.download.DownloadModel;
import com.thinkparity.model.parity.model.index.IndexModel;
import com.thinkparity.model.parity.model.install.InstallModel;
import com.thinkparity.model.parity.model.profile.ProfileModel;
import com.thinkparity.model.parity.model.release.ReleaseModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.user.UserModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
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

    /** A thinkParity download interface. */
    private DownloadModel download;

    /** A thinkParity index interface. */
	private IndexModel indexModel;

    /** A thinkParity install interface. */
    private InstallModel install;

    /** Flag indicating whether or not the factory is initialized. */
	private boolean isInitialized;

    /** The thinkParity preferences. */
	private Preferences preferences;

    /** A thinkParity profile interface. */
    private ProfileModel profile;

    /** A thinkParity release interface. */
    private ReleaseModel release;

    /** A thinkParity session interface. */
	private SessionModel sessionModel;

	/** A thinkParity user interface. */
    private UserModel uModel;

    /** The thinkParity workspace. */
	private Workspace workspace;

    /** A thinkParity workspace interface. */
	private WorkspaceModel workspaceModel;

	/** Create ModelFactory. */
	private ModelFactory() {
		super();
		this.isInitialized = false;
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
     * Obtain the parity download interface.
     * 
     * @param clasz
     *            The model consumer.
     * @return The parity download interface.
     */
    public DownloadModel getDownload(final Class clasz) { return download; }

    public IndexModel getIndexModel(final Class clasz) {
		return indexModel;
	}

    /**
     * Obtain the parity install interface.
     * 
     * @param clasz
     *            The model consumer.
     * @return The parity install interface.
     */
    public InstallModel getInstall(final Class clasz) { return install; }

	public Preferences getPreferences(final Class clasz) {
		return preferences;
	}

	/**
     * Obtain the thinkParity profile interface.
     * 
     * @param clasz
     *            The model consumer.
     * @return The thinkParity profile interface.
     */
    public ProfileModel getProfileModel(final Class clasz) { return profile; }

	/**
     * Obtain the parity release interface.
     * 
     * @param clasz
     *            The model consumer.
     * @return The parity release interface.
     */
	public ReleaseModel getRelease(final Class clasz) { return release; }

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
            archiveModel = ArchiveModel.getModel();
			artifactModel = ArtifactModel.getModel();
            containerModel = ContainerModel.getModel();            
			documentModel = DocumentModel.getModel();
            contactModel = ContactModel.getModel();
            download = DownloadModel.getModel();
            indexModel = IndexModel.getModel();
            install = InstallModel.getModel();
            profile = ProfileModel.getModel();
            release = ReleaseModel.getModel();
            sessionModel = SessionModel.getModel();
            uModel = UserModel.getModel();
            workspaceModel = WorkspaceModel.getModel();
            workspace = workspaceModel.getWorkspace();
            preferences = workspace.getPreferences();
            isInitialized = true;
        }
    }
}
