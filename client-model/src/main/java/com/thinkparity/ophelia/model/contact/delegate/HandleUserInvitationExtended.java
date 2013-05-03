/*
 * Created On:  27-Nov-07 6:38:47 PM
 */
package com.thinkparity.ophelia.model.contact.delegate;

import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationExtendedEvent;

import com.thinkparity.ophelia.model.contact.ContactDelegate;
import com.thinkparity.ophelia.model.user.InternalUserModel;

/**
 * <b>Title:</b>thinkParity Ophelia Model Handle User Invitation Extended
 * Contact Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HandleUserInvitationExtended extends ContactDelegate {

    /** An event. */
    private ContactUserInvitationExtendedEvent event;

    /** A new invitation. */
    private IncomingUserInvitation invitation; 

    /**
     * Create HandleUserInvitationExtended.
     *
     */
    public HandleUserInvitationExtended() {
        super();
    }

    /**
     * Obtain the invitation.
     *
     * @return A <code>IncomingUserInvitation</code>.
     */
    public IncomingUserInvitation getInvitation() {
        return invitation;
    }

    /**
     * Handle the user invitation extended event.
     * 
     */
    public void handleUserInvitationExtended() {
        final InternalUserModel userModel = getUserModel();
        final User extendedBy = userModel.readLazyCreate(event.getInvitedBy());
        if (contactIO.doesIncomingInvitationExist(extendedBy)) {
            logger.logInfo("Incoming user invitation from {0} exists.",
                    extendedBy);
        } else {
            logger.logInfo("Incoming user invitation from {0} does not exist.",
                    extendedBy);
            // create incoming user invitation
            invitation = new IncomingUserInvitation();
            invitation.setCreatedBy(extendedBy);
            invitation.setCreatedOn(event.getInvitedOn());
            invitation.setExtendedBy(extendedBy);
            invitation.setInvitationUser(localUser());
            contactIO.createInvitation(invitation);
            // index
            getIndexModel().indexIncomingUserInvitation(invitation.getId());
        }
    }

    /**
     * Determine if the invitation is set.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean isSetInvitation() {
        return null != invitation;
    }

    /**
     * Set the event.
     *
     * @param event
     *		A <code>ContactUserInvitationExtendedEvent</code>.
     */
    public void setEvent(final ContactUserInvitationExtendedEvent event) {
        this.event = event;
    }
}
