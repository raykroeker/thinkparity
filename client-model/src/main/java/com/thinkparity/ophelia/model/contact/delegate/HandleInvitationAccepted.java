/**
 * Created On: 14-Dec-07 4:21:43 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.contact.delegate;

import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;

import com.thinkparity.ophelia.model.contact.ContactDelegate;
import com.thinkparity.ophelia.model.index.InternalIndexModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;

/**
 * <b>Title:</b>thinkParity Ophelia Model Handle Invitation Accepted
 * Contact Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public class HandleInvitationAccepted extends ContactDelegate {

    /** An event. */
    private ContactInvitationAcceptedEvent event;

    /** A List of <code>IncomingEMailInvitation</code>. */
    private List<IncomingEMailInvitation> incomingEMailInvitations;

    /** An <code>IncomingUserInvitation</code>. */
    private IncomingUserInvitation incomingUserInvitation;

    /** A new <code>Contact</code>. */
    private Contact localContact;

    /** A List of <code>OutgoingEMailInvitation</code>. */
    private List<OutgoingEMailInvitation> outgoingEMailInvitations;

    /** An <code>OutgoingUserInvitation</code>. */
    private OutgoingUserInvitation outgoingUserInvitation;

    /**
     * Create HandleInvitationAccepted.
     *
     */
    public HandleInvitationAccepted() {
        super();
    }

    /**
     * Obtain the contact.
     *
     * @return A <code>Contact</code>.
     */
    public Contact getContact() {
        return localContact;
    }

    /**
     * Obtain the list of incoming email invitations.
     * 
     * @return A List of <code>IncomingEMailInvitation</code>.
     */
    public List<IncomingEMailInvitation> getIncomingEMailInvitations() {
        return incomingEMailInvitations;
    }

    /**
     * Obtain the incoming user invitation.
     *
     * @return A <code>IncomingUserInvitation</code>.
     */
    public IncomingUserInvitation getIncomingUserInvitation() {
        return incomingUserInvitation;
    }

    /**
     * Obtain the list of outgoing email invitations.
     * 
     * @return A List of <code>OutgoingEMailInvitation</code>.
     */
    public List<OutgoingEMailInvitation> getOutgoingEMailInvitations() {
        return outgoingEMailInvitations;
    }

    /**
     * Obtain the outgoing user invitation.
     *
     * @return A <code>OutgoingUserInvitation</code>.
     */
    public OutgoingUserInvitation getOutgoingUserInvitation() {
        return outgoingUserInvitation;
    }

    /**
     * Handle an invitation accepted event.
     * 
     */
    public void handleInvitationAccepted() {
        final InternalIndexModel indexModel = getIndexModel();
        final InternalSessionModel sessionModel = getSessionModel();
        final User acceptedBy = getUserModel().read(event.getAcceptedBy());
        final Contact contact = sessionModel.readContact(event.getAcceptedBy());

        /* if the accepted by user is not null, there exists the possibility
         * of incoming e-mail invitations; incoming user invitations and
         * outgoing user invitations; if it is null these invitations cannot
         * exist
         */
        if (null != acceptedBy) {
            incomingEMailInvitations =
                contactIO.readIncomingEMailInvitations(acceptedBy);
            for (final IncomingEMailInvitation iei : incomingEMailInvitations) {
                contactIO.deleteInvitation(iei);
                indexModel.deleteIncomingEMailInvitation(iei.getId());
            }

            // delete incoming user invitation and index
            incomingUserInvitation = contactIO.readIncomingUserInvitation(
                    acceptedBy);
            if (null != incomingUserInvitation) {
                contactIO.deleteInvitation(incomingUserInvitation);
                indexModel.deleteIncomingUserInvitation(
                        incomingUserInvitation.getId());
            }

            // delete outgoing user invitation and index
            outgoingUserInvitation = contactIO.readOutgoingUserInvitation(
                    acceptedBy);
            if (null != outgoingUserInvitation) {
                contactIO.deleteInvitation(outgoingUserInvitation);
                indexModel.deleteOutgoingUserInvitation(
                        outgoingUserInvitation.getId());
            }
        } else {
            incomingEMailInvitations = Collections.emptyList();
            incomingUserInvitation = null;
            outgoingUserInvitation = null;
        }

        // delete outgoing e-mail invitations and indicies
        outgoingEMailInvitations =
            contactIO.readOutgoingEMailInvitations(contact.getEmails());
        for (final OutgoingEMailInvitation oei : outgoingEMailInvitations) {
            // delete the published to reference
            getContainerModel().deletePublishedTo(oei.getInvitationEMail());
            // delete invitation and e-mail
            contactIO.deleteInvitation(oei);
            indexModel.deleteOutgoingEMailInvitation(oei.getId());
        }

        // create contact data
        if (null == getContactModel().read(event.getAcceptedBy())) {
            localContact = createLocal(contact);
        } else {
            logger.logInfo("Contact {0} already exists.", contact.getId());
        }
    }

    /**
     * Determine if the contact is set.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean isSetContact() {
        return null != localContact;
    }

    /**
     * Determine if the incoming user invitation is set.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean isSetIncomingUserInvitation() {
        return null != incomingUserInvitation;
    }

    /**
     * Determine if the outgoing user invitation is set.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean isSetOutgoingUserInvitation() {
        return null != outgoingUserInvitation;
    }

    /**
     * Set the event.
     *
     * @param event
     *      A <code>ContactInvitationAcceptedEvent</code>.
     */
    public void setEvent(final ContactInvitationAcceptedEvent event) {
        this.event = event;
    }
}
