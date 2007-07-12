/*
 * Created On: Aug 23, 2006 5:59:44 PM
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.session.OfflineException;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PublishVersion extends AbstractBrowserAction {

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
        final Boolean displayAvatar = (Boolean) data.get(DataKey.DISPLAY_AVATAR);
        final Long versionId = (Long) data.get(DataKey.VERSION_ID);
        final List<EMail> emails = data.getList(DataKey.EMAILS);
        final List<User> contactsIn = getDataUsers(data, DataKey.CONTACTS);
        final List<User> teamMembersIn = getDataUsers(data, DataKey.TEAM_MEMBERS);
        final ContainerVersion version = getContainerModel().readVersion(
                containerId, versionId);
        if (displayAvatar) {
            // Launch publish dialog
            browser.displayPublishContainerDialog(containerId, versionId);
        } else {
            final Profile profile = getProfileModel().read();
            final List<Contact> contacts = new ArrayList<Contact>();
            final List<TeamMember> teamMembers = new ArrayList<TeamMember>();

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

            /* if any e-mail addresses are contact e-mails, convert them to
             * contact references */
            Contact contact;
            final EMail[] duplicateEmails = (EMail[])emails.toArray(new EMail[0]);
            for (final EMail email : duplicateEmails) {
                if (getContactModel().doesExist(email)) {
                    contact = getContactModel().read(email);
                    if (!contacts.contains(contact)) {
                        contacts.add(contact);
                        emails.remove(email);
                    }
                }
            }

            try {
                getContainerModel().publishVersion(version.getArtifactId(),
                        version.getVersionId(), emails, contacts, teamMembers);
                getContainerModel().applyFlagSeen(version.getArtifactId());
            } catch (final OfflineException ox) {
                browser.displayErrorDialog("ErrorOffline");
            }
        }
    }

    /** Data keys. */
    public enum DataKey {
        CONTACTS, CONTAINER_ID, DISPLAY_AVATAR, EMAILS, TEAM_MEMBERS, VERSION_ID
    }
}
