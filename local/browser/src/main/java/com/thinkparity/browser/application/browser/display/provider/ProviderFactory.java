/*
 * Created On: Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.provider.contact.ContactInfoProvider;
import com.thinkparity.browser.application.browser.display.provider.contact.ManageContactsProvider;
import com.thinkparity.browser.application.browser.display.provider.document.HistoryProvider;
import com.thinkparity.browser.application.browser.display.provider.main.InfoProvider;
import com.thinkparity.browser.application.browser.display.provider.main.MainProvider;
import com.thinkparity.browser.application.browser.display.provider.session.SendArtifactProvider;
import com.thinkparity.browser.application.browser.display.provider.session.SendVersionProvider;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.user.UserModel;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

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
     * Obtain the main provider.
     * 
     * @return The main provider.
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

	/** The contact interface. */
    protected final ContactModel cModel;
    
    /** The parity document interface. */
	protected final DocumentModel dModel;

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

    /** The local user. */
    private final User localUser;

	/** The local user id. */
	private final JabberId localUserId;
    
	/** The main provider. */
	private final ContentProvider mainProvider;
    
    /** The contacts provider. */
	private final ContentProvider manageContactsProvider;

	/** The send artifact provider. */
	private final ContentProvider sendArtifactProvider;

	/** The Send artifact version provider. */
	private final ContentProvider sendVersionProvider;

    /** The parity user interface. */
    private final UserModel uModel;

	/** Create a ProviderFactory. */
    // PATTERN Singleton,Factory
	private ProviderFactory() {
		super();
		final ModelFactory modelFactory = ModelFactory.getInstance();
		this.artifactModel = modelFactory.getArtifactModel(getClass());
		this.dModel = modelFactory.getDocumentModel(getClass());
        this.cModel = modelFactory.getContactModel(getClass());
		this.logger = LoggerFactory.getLogger(getClass());
		this.sModel = modelFactory.getSessionModel(getClass());
		this.systemMessageModel = modelFactory.getSystemMessageModel(getClass());
        this.uModel = modelFactory.getUserModel(getClass());

        this.localUser = uModel.read();
        this.localUserId = localUser.getId();

		this.historyProvider = new HistoryProvider(localUserId, artifactModel, dModel, sModel);
		this.infoProvider = new InfoProvider(localUser, dModel);
		this.mainProvider = new MainProvider(artifactModel, cModel, dModel, systemMessageModel, localUserId);
		this.manageContactsProvider = new ManageContactsProvider(cModel);
        this.contactInfoProvider = new ContactInfoProvider(cModel);
		this.sendArtifactProvider = new SendArtifactProvider(artifactModel, cModel, dModel, localUserId);
		this.sendVersionProvider = new SendVersionProvider(artifactModel, dModel, sModel, localUserId);
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
	 * Obtain the main provider.
	 * 
	 * @return The main provider.
	 */
	private ContentProvider doGetMainProvider() { return mainProvider; }

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
