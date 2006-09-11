/*
 * Created On: Aug 23, 2006 5:59:44 PM
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.util.Iterator;
import java.util.List;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;



import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.user.TeamMember;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Share extends AbstractAction {

    /** A thinkParity browser application. */
    private final Browser browser;

    /**
     * Create Share.
     * 
     * @param browser
     *            A thinkParity browser application.
     */
    public Share(final Browser browser) {
        super(ActionId.CONTAINER_SHARE);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long versionId = (Long) data.get(DataKey.VERSION_ID);
        if (browser.confirm("Share.Temp")) {
            final Profile profile = ProfileModel.getModel().read();
            final List<Contact> contacts = getContactModel().read();
            final List<TeamMember> teamMembers = getContainerModel().readTeam(containerId);
            Contact contact;
            for (final Iterator<Contact> iContact = contacts.iterator(); iContact.hasNext(); ) {
                contact = iContact.next();
                for (final TeamMember teamMember : teamMembers) {
                    if (teamMember.getId().equals(contact.getId())) {
                        iContact.remove();
                    }
                }
            }
            TeamMember teamMember;
            for (final Iterator<TeamMember> iTeamMember = teamMembers.iterator(); iTeamMember.hasNext(); ) {
                teamMember = iTeamMember.next();
                if (teamMember.getId().equals(profile.getId())) {
                    iTeamMember.remove();
                }
            }

            getContainerModel().share(containerId, versionId, contacts, teamMembers);
            getArtifactModel().applyFlagSeen(containerId);
        }
    }

    public enum DataKey { CONTACTS, CONTAINER_ID, TEAM_MEMBERS, VERSION_ID }
}
