/*
 * Created On: Aug 14, 2006 1:30:01 PM
 */
package com.thinkparity.ophelia.browser.platform.action.contact;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DeleteOutgoingInvitation extends AbstractAction {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The browser application. */
    private final Browser browser;
    
    /** Create DeleteInvitation. */
    public DeleteOutgoingInvitation(final Browser browser) {
        super(ActionId.CONTACT_DELETE_OUTGOING_INVITATION);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Long invitationId = (Long) data.get(DataKey.INVITATION_ID);

        final ContactModel contactModel = getContactModel();
        final OutgoingInvitation outgoing = contactModel.readOutgoingInvitation(invitationId);
        if(browser.confirm("ContactOutgoingInvitationDelete.ConfirmDeleteMessage",
                new Object[] {outgoing.getEmail()})) {
            contactModel.deleteOutgoingInvitation(invitationId);
        }
    }

    /** The action data keys. */
    public enum DataKey { INVITATION_ID }
}
