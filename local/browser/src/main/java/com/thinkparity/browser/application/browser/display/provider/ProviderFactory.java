/*
 * Created On: Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.provider.contact.ContactInfoProvider;
import com.thinkparity.browser.application.browser.display.provider.contact.ManageContactsProvider;
import com.thinkparity.browser.application.browser.display.provider.container.ContainersProvider;
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
	private static final ProviderFactory singleton;

	static { singleton = new ProviderFactory(); }

	/** Obtain the contact info provider.
     * 
     * @return The contact info provider.
     */
    public static ContentProvider getContactInfoProvider() {
        return singleton.doGetContactInfoProvider();
    }

	/**
	 * Obtain the history content provider.
	 * 
	 * @return The history content provider.
	 */
	public static ContentProvider getHistoryProvider() {
		return singleton.doGetHistoryProvider();
	}
    
    /**
	 * Obtain the info provider.
	 * 
	 * @return The info provider.
	 */
	public static ContentProvider getInfoProvider() {
		return singleton.doGetInfoProvider();
	}
    
    /**
     * Obtain the containers provider.
     * 
     * @return The containers provider.
     */
    public static ContentProvider getContainersProvider() {
        return singleton.doGetContainersProvider();
    }
    
	/**
     * Obtain the main (documents) provider.
     * 
     * @return The main (documents) provider.
     */
	public static ContentProvider getMainProvider() {
		return singleton.doGetMainProvider();
	}

    /**
	 * Obtain the manage contacts provider.
	 * 
	 * @return The manage contacts provider.
	 */
	public static ContentProvider getManageContactsProvider() {
		return singleton.doGetManageContactsProvider();
	}

	public static ContentProvider getSendArtifactProvider() {
		return singleton.doGetSendArtifactProvider();
	}

	public static ContentProvider getSendVersionProvider() {
		return singleton.doGetSendVersionProvider();
	}

	/** The parity artifact interface. */
	protected final ArtifactModel artifactModel;
    
    /** The parity container (package) interface. */
    protected final ContainerModel ctrModel;
    
    /** The parity document interface. */
	protected final DocumentModel dModel;

    /** The contact interface. */
    protected final ContactModel cModel;
    
	/** An apache logger. */
	protected final Logger logger;

	/** The parity session interface. */
	protected final SessionModel sModel;

	/** The parity system message interface. */
	protected final SystemMessageModel systemMessageModel;

	/** The contact info provider. */
    private final ContentProvider contactInfoProvider;

	/** The document history provider. */
	private final ContentProvider historyProvider;

	/** The info pane provider. */
	private final ContentProvider infoProvider;

    /** The user's profile. */
    private final Profile profile;
    
    /** The containers (packages) provider. */
    private final ContentProvider containersProvider;
    
	/** The main (documents) provider. */
	private final ContentProvider mainProvider;
    
    /** The contacts provider. */
	private final ContentProvider manageContactsProvider;

	/** A thinkParity profile interface. */
    private final ProfileModel pModel;

	/** The send artifact provider. */
	private final ContentProvider sendArtifactProvider;

    /** The Send artifact version provider. */
	private final ContentProvider sendVersionProvider;

	/** Create ProviderFactory. */
	private ProviderFactory() {
		super();
		final ModelFactory modelFactory = ModelFactory.getInstance();
		this.artifactModel = modelFactory.getArtifactModel(getClass());
        this.ctrModel = modelFactory.getContainerModel(getClass());
		this.dModel = modelFactory.getDocumentModel(getClass());
        this.cModel = modelFactory.getContactModel(getClass());
		this.logger = LoggerFactory.getLogger(getClass());
        this.pModel = modelFactory.getProfileModel(getClass());
		this.sModel = modelFactory.getSessionModel(getClass());
		this.systemMessageModel = modelFactory.getSystemMessageModel(getClass());

        this.profile = pModel.read();
		this.historyProvider = new HistoryProvider(profile, artifactModel, dModel, sModel);
		this.infoProvider = new InfoProvider(profile);
        this.containersProvider = new ContainersProvider(profile, artifactModel, ctrModel, dModel, cModel, systemMessageModel);
		this.mainProvider = new MainProvider(profile, artifactModel, ctrModel, cModel, dModel, systemMessageModel);
		this.manageContactsProvider = new ManageContactsProvider(profile, cModel);
        this.contactInfoProvider = new ContactInfoProvider(profile, cModel);
		this.sendArtifactProvider = new SendArtifactProvider(profile, artifactModel, cModel, dModel);
		this.sendVersionProvider = new SendVersionProvider(profile, artifactModel, dModel, sModel);
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
     * Obtain the containers provider.
     * 
     * @return The containers provider.
     */
    private ContentProvider doGetContainersProvider() {
        return containersProvider;
    }
    
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
