/*
 * Created On: Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.provider.contact.ContactInfoProvider;
import com.thinkparity.browser.application.browser.display.provider.contact.ManageContactsProvider;
import com.thinkparity.browser.application.browser.display.provider.container.ContainerAvatarProvider;
import com.thinkparity.browser.application.browser.display.provider.container.ManageTeamProvider;
import com.thinkparity.browser.application.browser.display.provider.document.HistoryProvider;
import com.thinkparity.browser.application.browser.display.provider.main.InfoProvider;
import com.thinkparity.browser.application.browser.display.provider.main.MainProvider;
import com.thinkparity.browser.application.browser.display.provider.session.SendArtifactProvider;
import com.thinkparity.browser.application.browser.display.provider.session.SendVersionProvider;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.profile.ProfileModel;
import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProviderFactory {

	/** Singleton instance. */
	private static final ProviderFactory SINGLETON;

	static { SINGLETON = new ProviderFactory(); }

	/** Obtain the contact info provider.
     * 
     * @return The contact info provider.
     */
    public static ContentProvider getContactInfoProvider() {
        return SINGLETON.doGetContactInfoProvider();
    }

	/**
     * Obtain the container provider.
     * 
     * @return The container provider.
     */
    public static ContentProvider getContainerProvider() {
        return SINGLETON.doGetContainersProvider();
    }
    
    /**
	 * Obtain the history content provider.
	 * 
	 * @return The history content provider.
	 */
	public static ContentProvider getHistoryProvider() {
		return SINGLETON.doGetHistoryProvider();
	}
    
    /**
	 * Obtain the info provider.
	 * 
	 * @return The info provider.
	 */
	public static ContentProvider getInfoProvider() {
		return SINGLETON.doGetInfoProvider();
	}
    
	/**
     * Obtain the main (documents) provider.
     * 
     * @return The main (documents) provider.
     */
	public static ContentProvider getMainProvider() {
		return SINGLETON.doGetMainProvider();
	}
    
    /**
	 * Obtain the manage contacts provider.
	 * 
	 * @return The manage contacts provider.
	 */
	public static ContentProvider getManageContactsProvider() {
		return SINGLETON.doGetManageContactsProvider();
	}

    /**
     * Obtain the manage team provider.
     * 
     * @return The manage team provider.
     */
    public static ContentProvider getManageTeamProvider() {
        return SINGLETON.doGetManageTeamProvider();
    }

	public static ContentProvider getSendArtifactProvider() {
		return SINGLETON.doGetSendArtifactProvider();
	}

	public static ContentProvider getSendVersionProvider() {
		return SINGLETON.doGetSendVersionProvider();
	}

	/** The parity artifact interface. */
	protected final ArtifactModel artifactModel;
    
    /** The contact interface. */
    protected final ContactModel contactModel;
    
    /** The parity container (package) interface. */
    protected final ContainerModel containerModel;

    /** The parity document interface. */
	protected final DocumentModel documentModel;
    
	/** An apache logger. */
	protected final Logger logger;

	/** The parity session interface. */
	protected final SessionModel sessionModel;

	/** The parity system message interface. */
	protected final SystemMessageModel systemMessageModel;

	/** The contact info provider. */
    private final ContentProvider contactInfoProvider;

	/** The containers (packages) provider. */
    private final ContentProvider containersProvider;

	/** The document history provider. */
	private final ContentProvider historyProvider;

    /** The info pane provider. */
	private final ContentProvider infoProvider;
    
    /** The main (documents) provider. */
	private final ContentProvider mainProvider;
    
	/** The contacts provider. */
	private final ContentProvider manageContactsProvider;
    
    /** The manage team provider. */
    private final ContentProvider manageTeamProvider;
    
    /** The user's profile. */
    private final Profile profile;

	/** A thinkParity profile interface. */
    private final ProfileModel profileModel;

	/** The send artifact provider. */
	private final ContentProvider sendArtifactProvider;

    /** The Send artifact version provider. */
	private final ContentProvider sendVersionProvider;

	/** Create ProviderFactory. */
	private ProviderFactory() {
		super();
		final ModelFactory modelFactory = ModelFactory.getInstance();
		this.artifactModel = modelFactory.getArtifactModel(getClass());
        this.containerModel = modelFactory.getContainerModel(getClass());
		this.documentModel = modelFactory.getDocumentModel(getClass());
        this.contactModel = modelFactory.getContactModel(getClass());
		this.logger = LoggerFactory.getLogger(getClass());
        this.profileModel = modelFactory.getProfileModel(getClass());
		this.sessionModel = modelFactory.getSessionModel(getClass());
		this.systemMessageModel = modelFactory.getSystemMessageModel(getClass());

        this.profile = profileModel.read();
		this.historyProvider = new HistoryProvider(profile, artifactModel, documentModel, sessionModel);
		this.infoProvider = new InfoProvider(profile);
        this.containersProvider = new ContainerAvatarProvider(profile, containerModel, documentModel);
		this.mainProvider = new MainProvider(profile, artifactModel, containerModel, contactModel, documentModel, systemMessageModel);
        this.manageTeamProvider = new ManageTeamProvider(profile, containerModel, contactModel);
		this.manageContactsProvider = new ManageContactsProvider(profile, contactModel);
        this.contactInfoProvider = new ContactInfoProvider(profile, contactModel);
		this.sendArtifactProvider = new SendArtifactProvider(profile, artifactModel, contactModel, documentModel);
		this.sendVersionProvider = new SendVersionProvider(profile, artifactModel, documentModel, sessionModel);
	}

	/**
     * Obtain the contact info provider.
     * 
     * @return The contact info provider.
     */
    private ContentProvider doGetContactInfoProvider() {
        return contactInfoProvider;
    }

	/**
     * Obtain the containers provider.
     * 
     * @return The containers provider.
     */
    private ContentProvider doGetContainersProvider() {
        return containersProvider;
    }

	/**
	 * Obtain the history content provider.
	 * 
	 * @return The history content provider.
	 */
	private ContentProvider doGetHistoryProvider() { return historyProvider; }
    
    /**
	 * Obtain the info content provider.
	 * 
	 * @return The info content provider.
	 */
	private ContentProvider doGetInfoProvider() { return infoProvider; }
    
	/**
	 * Obtain the main (documents) provider.
	 * 
	 * @return The main (documents) provider.
	 */
	private ContentProvider doGetMainProvider() {
        return mainProvider;
    }
    
    /**
	 * Obtain the manage contacts provider.
	 * 
	 * @return The manage contacts provider.
	 */
	private ContentProvider doGetManageContactsProvider() {
		return manageContactsProvider;
	}

    /**
     * Obtain the manage team provider.
     * 
     * @return The manage team provider.
     */
    private ContentProvider doGetManageTeamProvider() {
        return manageTeamProvider;
    }

	/**
	 * Obtain the user provider.
	 * 
	 * @return The user provider.
	 */
	private ContentProvider doGetSendArtifactProvider() {
		return sendArtifactProvider;
	}

	private ContentProvider doGetSendVersionProvider() {
		return sendVersionProvider;
	}
}
