/*
 * Jan 10, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.user.TeamMember;

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
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
		final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final List<User> contactsIn = getDataUsers(data, DataKey.CONTACTS);
        final List<User> teamMembersIn = getDataUsers(data, DataKey.TEAM_MEMBERS);
        final String comment = (String) data.get(DataKey.COMMENT);
        
        if (((null==contactsIn) || contactsIn.isEmpty()) &&
            ((null==teamMembersIn) || teamMembersIn.isEmpty())) {
            // Launch publish dialog
            browser.displayPublishContainerDialog(containerId);
        } else {
            final Profile profile = getProfileModel().read();
            final ArrayList<TeamMember> teamMembers = new ArrayList<TeamMember>();
            final ArrayList<Contact> contacts = new ArrayList<Contact>();
            
            // Build team members list, minus the current user
            for (final User teamMemberIn : teamMembersIn) {
                if (!teamMemberIn.getId().equals(profile.getId())) {
                    teamMembers.add((TeamMember)teamMemberIn);
                }
            }
            
            // Build contacts list, minus any overlap with team members
            for (final User contactIn : contactsIn) {
                Boolean found = Boolean.FALSE;
                for (final TeamMember teamMember : teamMembers) {
                    if (teamMember.getId().equals(contactIn.getId())) {
                        found = Boolean.TRUE;
                        break;
                    }
                }
                if (!found) {
                    contacts.add((Contact) contactIn);
                }
            }
            
            getContainerModel().publish(containerId, comment, contacts, teamMembers);
            getArtifactModel().applyFlagSeen(containerId);            
        }
	}

	/**
	 * The key used to set\get the data.
	 * 
	 * @see Data
	 */
	public enum DataKey { CONTAINER_ID, CONTACTS, TEAM_MEMBERS, COMMENT }
}
