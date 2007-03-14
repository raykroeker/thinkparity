/*
 * Created On: Aug 14, 2006 1:30:01 PM
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;

import com.thinkparity.ophelia.model.contact.ContactModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity OpheliaUI Browser Application Delete Outgoing
 * Contact EMail Invitation Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeleteOutgoingEMailInvitation extends AbstractAction {

    /** The browser application. */
    private final Browser browser;
    
    /**
     * Create DeleteOutgoingEMailInvitation.
     * 
     * @param browser
     *            The <code>Browser</code> application.
     */
    public DeleteOutgoingEMailInvitation(final Browser browser) {
        super(ActionId.CONTACT_DELETE_OUTGOING_EMAIL_INVITATION);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Long invitationId = (Long) data.get(DataKey.INVITATION_ID);

        final ContactModel contactModel = getContactModel();
        final OutgoingEMailInvitation outgoing = contactModel.readOutgoingEMailInvitation(invitationId);
        if (browser.confirm("ContactOutgoingEMailInvitationDelete.ConfirmDeleteMessage",
                new Object[] {outgoing.getInvitationEMail()})) {
            contactModel.deleteOutgoingEMailInvitation(invitationId);
        }
    }

    /** The action data keys. */
    public enum DataKey { INVITATION_ID }
}
