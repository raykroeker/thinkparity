/**
 * Created On: 12-Nov-07 5:18:21 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public class CreateOutgoingEMailInvitation extends AbstractBrowserAction {

    /** The <code>Browser</code> application. */
    private final Browser browser;

    /**
     * Create CreateOutgoingEMailInvitation.
     * 
     * @param browser
     *            The <code>Browser</code> application.
     */
    public CreateOutgoingEMailInvitation(final Browser browser) {
        super(ActionId.CONTACT_CREATE_OUTGOING_EMAIL_INVITATION);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final EMail email = (EMail) data.get(DataKey.EMAIL);

        if (getProfileModel().isInviteAvailable()
                && getProfileModel().readIsActive()
                && getProfileModel().readEMail().isVerified()) {
            if (null == email) {
                final ProfileEMail profileEMail = getProfileModel().readEMail();
                final List<Contact> contacts = getContactModel().read();
                final List<OutgoingEMailInvitation> outgoingEMailInvitations = getContactModel().readOutgoingEMailInvitations();
                browser.displayCreateOutgoingEMailInvitationDialog(profileEMail, contacts, outgoingEMailInvitations);
            } else {
                getContactModel().createOutgoingEMailInvitation(email);

                // Clear search and filter
                browser.showAllTabPanels();
            }
        }
    }

    /**
     * The key used to set\get the data.
     * 
     * @see Data
     */
    public enum DataKey { EMAIL }
}
