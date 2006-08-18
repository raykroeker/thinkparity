/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.platform.action.container;

import java.util.List;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * Publish a document.  This will send a given document version to
 * every member of the team.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Publish extends AbstractAction {

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

    /** The browser application. */
    private final Browser browser;

	/** Create Publish. */
	public Publish(final Browser browser) {
		super(ActionId.CONTAINER_PUBLISH);
        this.browser = browser;
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
		final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final List<Contact> contacts = getContactModel().read();
        final List<TeamMember> teamMembers = getContainerModel().readTeam(containerId);
		if (browser.confirm("Publish.Temp")) {
		    getContainerModel().publish(containerId, contacts, teamMembers);
		    getArtifactModel().applyFlagSeen(containerId);
        }
	}

	/**
	 * The key used to set\get the data.
	 * 
	 * @see Data
	 */
	public enum DataKey { CONTAINER_ID }
}
