/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.platform.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assertion;

import com.thinkparity.browser.BrowserException;
import com.thinkparity.browser.platform.util.l10n.ActionLocalization;
import com.thinkparity.browser.platform.util.model.ModelFactory;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.index.IndexModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.profile.ProfileModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.user.UserModel;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractAction {

	/** Action localization. */
	protected final ActionLocalization localization;

	/** An apache logger. */
	protected final Logger logger =
		Logger.getLogger(getClass());

	/** The thinkParity model factory. */
	protected final ModelFactory modelFactory = ModelFactory.getInstance();

	/** The action icon. */
	private Icon icon;

	/** The action id. */
	private ActionId id;

	/** The action mnemonic. */
    private String mnemonic;
    
    /** The action accelerator. */
    private String accelerator;
    
    /** The action name. */
	private String name;

    /**
     * Create AbstractAction.
     * 
     * @param id
     *            An action id.
     */
    protected AbstractAction(final ActionId id) {
        super();
        this.id = id;
        this.icon = null;
        this.localization = new ActionLocalization(id.toString());
        this.name = localization.getString("NAME");
        this.mnemonic = localization.getString("MNEMONIC").substring(0,1);
        this.accelerator = localization.getString("ACCELERATOR");
    }

	/**
	 * Obtain the action ICON.
	 * 
	 * @return The action ICON.
	 */
	public Icon getIcon() { return icon; }

	/**
	 * Obtain the action id.
	 * 
	 * @return The action id.
	 */
	public ActionId getId() { return id; }

    /**
     * Obtain the action MNEMONIC.
     * 
     * @return The action MNEMONIC.
     */
    public String getMnemonic() { return mnemonic; }
    
    /**
     * Obtain the action ACCELERATOR.
     * 
     * @return The action ACCELERATOR.
     */
    public String getAccelerator() { return accelerator; }
    
    /**
	 * Obtain the action NAME.
	 * 
	 * @return The action NAME.
	 */
	public String getName() { return name; }

	/**
	 * Invoke the action.
	 * 
	 * @param data
	 *            The action data.
	 */
	public abstract void invoke(final Data data);

	/**
     * Determine if the mnemonic is set.
     * 
     * @return True if the mnemonic is set; false otherwise.
     */
    public Boolean isSetMnemonic() {
        return ((null != mnemonic) && (mnemonic.charAt(0) != '!'));
    }
    
    /**
     * Determine if the accelerator is set.
     * 
     * @return True if the accelerator is set; false otherwise.
     */
    public Boolean isSetAccelerator() {
        return ((null != accelerator) && (accelerator.charAt(0) != '!'));
    }
    
    /**
     * Determine if the name is set.
     * 
     * @return True if the name is set; false otherwise.
     */
	public Boolean isSetName() {
        return null != name;
	}

	/**
     * Set the icon.
     * 
     * @param icon
     *            The <code>Icon</code>.
     */
	public void setIcon(final Icon icon) {
        this.icon = icon;
    }

	/**
     * Set the action mnemonic.
     * 
     * @param mnemonic
     *            The action mnemonic.
     */
    public void setMnemonic(final String mnemonic) {
        this.mnemonic = mnemonic;
    }
    
    /**
     * Set the accelerator.
     * 
     * @param accelerator
     *             The action accelerator.
     */
    public void setAccelerator(final String accelerator) {
        this.accelerator = accelerator;
    }
    
    /**
	 * Set the name.
	 * 
	 * @param name
	 *            A name <code>String</code>.
	 */
	public void setName(final String name) {
        this.name = name;
	}

	/**
	 * Obtain a thinkParity artifact interface.
	 * 
	 * @return A <code>ArtifactModel</code>.
	 */
	protected ArtifactModel getArtifactModel() {
		return modelFactory.getArtifactModel(getClass());
	}

    /**
     * Obtain the contact model api.
     * 
     * @return The contact model api.
     */
    protected ContactModel getContactModel() {
        return modelFactory.getContactModel(getClass());
    }

	/**
     * Obtain the container model api.
     * 
     * @return The container model api.
     */
    protected ContainerModel getContainerModel() {
        return modelFactory.getContainerModel(getClass());
    }

    /**
     * Convert the data element foudn at the given key to a list of files.
     * 
     * @param data
     *            The action data.
     * @param key
     *            The data element key.
     * @return A list of files.
     */
    protected List<File> getDataFiles(final Data data, final Enum<?> key) {
        final List<?> list = (List<?>) data.get(key);
        if(null == list) { return null; }
        final List<File> files = new ArrayList<File>();
        for(final Object o : list) { files.add((File) o); }
        return files;
    }

    /**
     * Convert the data element found at the given key to a list of jabber ids.
     * 
     * @param data
     *            The action data.
     * @param key
     *            The data element key.
     * @return A list of jabber ids.
     */
    protected List<JabberId> getDataJabberIds(final Data data, final Enum<?> key) {
        final List<?> list = (List<?>) data.get(key);
        if(null == list) { return null; }
        final List<JabberId> jabberIds = new ArrayList<JabberId>();
        for(final Object o : list) { jabberIds.add((JabberId) o); }
        return jabberIds;
    }

    /**
     * Convert the data element found at the given key to a list of users.
     * 
     * @param data
     *            The action data.
     * @param key
     *            The data element key.
     * @return A list of users.
     */
	protected List<User> getDataUsers(final Data data, final Enum<?> key) {
        final List<?> list = (List<?>) data.get(key);
        if(null == list) { return null; }
        final List<User> users = new ArrayList<User>();
        for(final Object o : list) { users.add((User) o); }
        return users;
    }
    
    /**
	 * Obtain the document model api.
	 * 
	 * @return The document model api.
	 */
	protected DocumentModel getDocumentModel() {
		return modelFactory.getDocumentModel(getClass());
	}  

	/**
     * Obtain the parity index interface.
     * 
     * @return The parity index interface.
     */
	protected IndexModel getIndexModel() {
		return modelFactory.getIndexModel(getClass());
	}   
    
    /**
     * Obtain a thinkParity profile interface.
     * 
     * @return A <code>ProfileModel</code>.
     */
    protected ProfileModel getProfileModel() {
        return modelFactory.getProfileModel(getClass());
    }

	/**
     * Obtain the parity session interface.
     * 
     * @return The parity session interface.
     */
	protected SessionModel getSessionModel() {
		return modelFactory.getSessionModel(getClass());
	}

	/**
	 * Obtain localized text.
	 * 
	 * @param localKey
	 *            The local key.
	 * @return The localized text.
	 */
	protected String getString(final String localKey) {
		return localization.getString(localKey);
	}
    
	/**
	 * Obtain localized text.
	 * 
	 * @param localKey
	 *            The local key.
	 * @param arguments
	 *            The text arguments.
	 * @return The localized text.
	 */
	protected String getString(final String localKey, final Object[] arguments) {
		return localization.getString(localKey, arguments);
	}

	protected SystemMessageModel getSystemMessageModel() {
		return modelFactory.getSystemMessageModel(getClass());
	}

	/**
     * Obtain the thinkParity user interface.
     * 
     * @return A thinkParity user interface.
     */
    protected UserModel getUserModel() {
        return modelFactory.getUserModel(getClass());
    }

    /**
     * Translate an error into a browser runtime exception.
     * 
     * @param t
     *            An error.
     * @return A browser error.
     */
    protected RuntimeException translateError(final Throwable t) {
        if (BrowserException.class.isAssignableFrom(t.getClass())) {
            return (BrowserException) t;
        } else if (Assertion.class.isAssignableFrom(t.getClass())) {
            return (Assertion) t;
        }
        else {
            final String internalErrorId = new StringBuffer()
                    .append(getId()).append(" - ")
                    .append(t.getMessage())
                    .toString();
            logger.error(internalErrorId, t);
            return new BrowserException(internalErrorId, t);
        }

    }
}
