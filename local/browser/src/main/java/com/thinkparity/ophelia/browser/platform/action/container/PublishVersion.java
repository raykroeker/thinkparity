/*
 * Created On: Aug 23, 2006 5:59:44 PM
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
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PublishVersion extends AbstractAction {

    /** A thinkParity browser application. */
    private final Browser browser;

    /**
     * Create PublishVersion.
     * 
     * @param browser
     *            A thinkParity browser application.
     */
    public PublishVersion(final Browser browser) {
        super(ActionId.CONTAINER_PUBLISH_VERSION);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long versionId = (Long) data.get(DataKey.VERSION_ID);
        final List<User> contactsIn = getDataUsers(data, DataKey.CONTACTS);
        final List<User> teamMembersIn = getDataUsers(data, DataKey.TEAM_MEMBERS);
        
        if (((null==contactsIn) || contactsIn.isEmpty()) &&
            ((null==teamMembersIn) || teamMembersIn.isEmpty())) {
                // Launch publish dialog
                browser.displayPublishContainerDialog(containerId, versionId);
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
            
            // TODO This should allow team members too.
            getContainerModel().publishVersion(containerId, versionId, contacts);
            getArtifactModel().applyFlagSeen(containerId);
        }
    }

    public enum DataKey { CONTAINER_ID, VERSION_ID, CONTACTS, TEAM_MEMBERS }
}
