/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.platform.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.l10n.ActionLocalization;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.index.IndexModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractAction {

	/**
	 * Action localization.
	 * 
	 */
	protected final ActionLocalization localization;
	
	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger =
		LoggerFactory.getLogger(getClass());

	/**
	 * Parity model factory.
	 * 
	 */
	protected final ModelFactory modelFactory = ModelFactory.getInstance();

	/**
	 * The main controller.
	 * 
	 */
	private Browser controller;

	/**
	 * The action ICON.
	 * 
	 */
	private Icon icon;

	/**
	 * The action id.
	 * 
	 */
	private ActionId id;

	/**
	 * The action NAME.
	 * 
	 */
	private String name;

	/**
	 * Create a AbstractAction.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 * @param id
	 *            The action id.
	 * @param name
	 *            The action name.
	 * @param icon
	 *            The action icon.
	 */
	protected AbstractAction(final String l18nContext, final ActionId id,
			final String name, final Icon icon) {
		super();
		this.id = id;
		this.icon = icon;
		this.localization = new ActionLocalization(l18nContext);
		this.name = name;
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
	public abstract void invoke(final Data data) throws Exception;

	/**
	 * Set the action ICON.
	 * 
	 * @param ICON
	 *            The action ICON.
	 */
	public void setIcon(Icon icon) { this.icon = icon; }

	/**
	 * Set the action NAME.
	 * 
	 * @param NAME
	 *            The action NAME.
	 */
	public void setName(String name) { this.name = name; }

	/**
	 * Obtain the parity artifact interface.
	 * 
	 * @return The parity artifact interface.
	 */
	protected ArtifactModel getArtifactModel() {
		return modelFactory.getArtifactModel(getClass());
	}

	/**
	 * Obtain the main controller.
	 * 
	 * @return The main controller.
	 */
	protected Browser getController() {
		if(null == controller) { controller = Browser.getInstance(); }
		return controller;
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
	 * Register a parity error.
	 * 
	 * @param px
	 *            The parity error.
	 */
	protected void registerError(final ParityException px) {
		registerError((Throwable) px);
	}

    /**
	 * Register an error.
	 * 
	 * @param t
	 *            The error.
	 */
	private void registerError(final Throwable t) {
		// NOTE Error Handler Code
		logger.error("", t);
	}
}
