/*
 * Created On: Aug 23, 2006 5:59:44 PM
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.util.Iterator;
import java.util.List;

import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.ophelia.model.user.TeamMember;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

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
        if (browser.confirm("Publish.Temp")) {
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

            getContainerModel().publishVersion(containerId, versionId, contacts);
            getArtifactModel().applyFlagSeen(containerId);
        }
    }

    public enum DataKey { CONTACTS, CONTAINER_ID, VERSION_ID }
}
