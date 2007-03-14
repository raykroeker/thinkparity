/*
 * Nov 29, 2005
 */
package com.thinkparity.desdemona.model.contact;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.session.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.7
 */
public class ContactModel extends AbstractModel<ContactModelImpl> {

    /**
	 * Obtain a handle to the artifact model.
	 * 
	 * @return A handle to the artifact model.
	 */
	public static ContactModel getModel(final Session session) {
		final ContactModel contactModel = new ContactModel(session);
		return contactModel;
	}

    /**
     * Create ContactModel.
     * 
     * @param session
     *            The user's session.
     */
	private ContactModel(final Session session) {
		super(new ContactModelImpl(session));
	}

    /**
     * Accept the incoming invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitationId
     *            An invitation id.
     * @param acceptedOn
     *            An accceptance timestamp <code>Calendar</code>.
     */
    public void acceptInvitation(final JabberId userId,
            final IncomingEMailInvitation invitation, final Calendar acceptedOn) {
		synchronized (getImplLock()) {
            getImpl().acceptInvitation(userId, invitation, acceptedOn);
		}
	}

    /**
     * Accept the incoming invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitationId
     *            An invitation id.
     * @param acceptedOn
     *            An accceptance timestamp <code>Calendar</code>.
     */
    public void acceptInvitation(final JabberId userId,
            final IncomingUserInvitation invitation, final Calendar acceptedOn) {
        synchronized (getImplLock()) {
            getImpl().acceptInvitation(userId, invitation, acceptedOn);
        }
    }

	/**
     * Create an e-mail contact invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    public void createInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation) {
        synchronized (getImplLock()) {
            getImpl().createInvitation(userId, invitation);
        }
    }

    /**
     * Create a contact invitation.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     */
    public void createInvitation(final JabberId userId,
            final OutgoingUserInvitation invitation) {
        synchronized (getImplLock()) {
            getImpl().createInvitation(userId, invitation);
        }
    }

    /**
     * Decline an e-mail invitation. Delete the invitation and send a
     * notification (which will delete that invitation).
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedBy
     *            Who invited the user.
     * @param invitedAs
     *            To which <code>EMail</code> the invitation was sent.
     * @param declinedOn
     *            When the invitation was declined.
     */
    public void declineInvitation(final JabberId userId,
            final IncomingEMailInvitation invitation, final Calendar declinedOn) {
        synchronized (getImplLock()) {
            getImpl().declineInvitation(userId, invitation, declinedOn);
        }
    }

    /**
     * Decline an e-mail invitation. Delete the invitation and send a
     * notification (which will delete that invitation).
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedBy
     *            Who invited the user.
     * @param invitedAs
     *            To which <code>EMail</code> the invitation was sent.
     * @param declinedOn
     *            When the invitation was declined.
     */
    public void declineInvitation(final JabberId userId,
            final IncomingUserInvitation invitation, final Calendar declinedOn) {
        synchronized (getImplLock()) {
            getImpl().declineInvitation(userId, invitation, declinedOn);
        }
    }

    /**
     * Delete a contact for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void delete(final JabberId userId, final JabberId contactId) {
        synchronized (getImplLock()) {
            getImpl().delete(userId, contactId);
        }
    }

    /**
     * Delete a user's invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedAs
     *            The <code>Email</code> the invitation was created for.
     */
    public void deleteInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation, final Calendar deletedOn) {
        synchronized (getImplLock()) {
            getImpl().deleteInvitation(userId, invitation, deletedOn);
        }
    }

    /**
     * Delete a user's invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedAs
     *            The <code>Email</code> the invitation was created for.
     */
    public void deleteOutgoingUserInvitation(final JabberId userId,
            final OutgoingUserInvitation invitation, final Calendar deletedOn) {
        synchronized (getImplLock()) {
            getImpl().deleteOutgoingUserInvitation(userId, invitation,
                    deletedOn);
        }
    }

    /**
     * Read a user's contacts.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Contact&gt;</code>.
     */
	public List<Contact> read(final JabberId userId) {
		synchronized (getImplLock()) {
            return getImpl().read(userId);
		}
	}

    /**
     * Read a user's contact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     * @return The contact info.
     */
	public Contact read(final JabberId userId, final JabberId contactId) {
		synchronized (getImplLock()) {
            return getImpl().read(userId, contactId);
		}
	}

    public List<IncomingEMailInvitation> readIncomingEMailInvitations(
            final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readIncomingEMailInvitations(userId);
        }
    }

    public List<IncomingUserInvitation> readIncomingUserInvitations(
            final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readIncomingUserInvitations(userId);
        }
    }

    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readOutgoingEMailInvitations(userId);
        }
    }

	public List<OutgoingUserInvitation> readOutgoingUserInvitations(
            final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readOutgoingUserInvitations(userId);
        }
    }
}
