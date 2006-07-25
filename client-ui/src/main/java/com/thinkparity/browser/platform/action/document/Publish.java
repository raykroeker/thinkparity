/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.platform.action.document;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * Publish a document.  This will send a given document version to
 * every member of the team.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Publish extends AbstractAction {

	/** An icon. */
	private static final Icon ICON;

	/** The id. */
	private static final ActionId ID;

	/** The name. */
	private static final String NAME;

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	static {
		ICON = null;
		ID = ActionId.CONTAINER_PUBLISH;
		NAME = "Publish";
	}

	/** The browser application. */
	private final Browser application;

	/** Create Publish. */
	public Publish(final Browser application) {
		super("Container.Publish", ID, NAME, ICON);
		this.application = application;
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
		getContainerModel().publish(containerId);
		getArtifactModel().applyFlagSeen(containerId);

		application.fireContainerUpdated(containerId);
	}

	/**
	 * The key used to set\get the data.
	 * 
	 * @see Data
	 */
	public enum DataKey { CONTAINER_ID }
}
