/*
 * Created On:  27-Nov-07 6:01:09 PM
 */
package com.thinkparity.ophelia.model.contact.delegate;

import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationExtendedEvent;

import com.thinkparity.ophelia.model.contact.ContactDelegate;

/**
 * <b>Title:</b>thinkParity Ophelia Model Handle EMail Invitation Extended
 * Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HandleEMailInvitationExtended extends ContactDelegate {

    /** An event. */
    private ContactEMailInvitationExtendedEvent event;

    /** The new invitation. */
    private IncomingEMailInvitation invitation;

    /**
     * Create HandleEMailInvitationExtended.
     *
     */
    public HandleEMailInvitationExtended() {
        super();
    }

    /**
     * Obtain the invitation.
     *
     * @return A <code>IncomingEMailInvitation</code>.
     */
    public IncomingEMailInvitation getInvitation() {
        return invitation;
    }

    /**
     * Handle e-mail invitation extended.
     * 
     */
    public void handleEMailInvitationExtended() {
        final User extendedBy = getUserModel().readLazyCreate(event.getInvitedBy());
        if (contactIO.doesIncomingInvitationExist(event.getInvitedAs(), extendedBy)) {
            logger.logInfo("E-Mail invitation from {0} to {1} exists.",
                    extendedBy, event.getInvitedAs());
        } else {
            logger.logInfo("E-Mail invitation from {0} to {1} does not exist.",
                    extendedBy, event.getInvitedAs());
            // create incoming e-mail invitation
            invitation = new IncomingEMailInvitation();
            invitation.setCreatedBy(extendedBy);
            invitation.setCreatedOn(event.getInvitedOn());
            invitation.setExtendedBy(extendedBy);
            invitation.setInvitationEMail(event.getInvitedAs());
            contactIO.createInvitation(invitation);
    
            // index
            getIndexModel().indexIncomingEMailInvitation(invitation.getId());
        }
    }

    /**
     * Determine if the invitation has been set.
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
     *		A <code>ContactEMailInvitationExtendedEvent</code>.
     */
    public void setEvent(final ContactEMailInvitationExtendedEvent event) {
        this.event = event;
    }
}
