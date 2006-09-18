/*
 * Nov 29, 2005
 */
package com.thinkparity.desdemona.model.contact;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.contact.Contact;

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
     * Accept an invitation. Create the contact relationship; and notify the
     * user.
     * 
     * @param userId
     *            The user id <code>JabberId</code>.
     * @param invitedBy
     *            By whom the invitation was sent.
     * @param acceptedOn
     *            When the invitation was accepted.
     */
    public void acceptInvitation(final JabberId userId,
            final JabberId invitedBy, final Calendar acceptedOn) {
		synchronized (getImplLock()) {
            getImpl().acceptInvitation(userId, invitedBy, acceptedOn);
		}
	}

    /**
     * Decline an invitation. Delete the invitation and send a notification
     * (which will delete that invitation).
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
            final JabberId invitedBy, final EMail invitedAs,
            final Calendar declinedOn) {
		synchronized (getImplLock()) {
            getImpl().declineInvitation(userId, invitedBy, invitedAs, declinedOn);
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
    public void deleteInvitation(final JabberId userId, final EMail invitedAs,
            final Calendar deletedOn) {
        synchronized (getImplLock()) {
            getImpl().deleteInvitation(userId, invitedAs, deletedOn);
        }
    }

	/**
     * Extend an invitation. If the email is registered within the thinkParity
     * community an invitation will be sent via thinkParity otherwise an
     * invitation will be sent via and email.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param extendedTo
     *            A <code>EMail</code> to invite.
     * @param extendedOn
     *            The date <code>Calendar</code> of the invitation.
     */
    public void extendInvitation(final JabberId userId, final EMail extendedTo,
            final Calendar extendedOn) {
        synchronized (getImplLock()) {
            getImpl().extendInvitation(userId, extendedTo, extendedOn);
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

	/**
	 * Read the invitation.
	 * 
	 * @param from
	 *            From whom the invitation is sent.
	 * @return The invitation.
	 */
	public Invitation readInvitation(final JabberId from) {
		synchronized (getImplLock()) {
            return getImpl().readInvitation(from);
		}
	}
}
