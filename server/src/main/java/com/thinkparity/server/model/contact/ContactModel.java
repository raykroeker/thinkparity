/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.contact;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.session.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.7
 */
public class ContactModel extends AbstractModel {

	/**
	 * Obtain a handle to the artifact model.
	 * 
	 * @return A handle to the artifact model.
	 */
	public static ContactModel getModel(final Session session) {
		final ContactModel contactModel = new ContactModel(session);
		return contactModel;
	}

    /** Model implementation. */
	private final ContactModelImpl impl;

	/** Model implementation synchronization lock. */
	private final Object implLock;

    /**
     * Create ContactModel.
     * 
     * @param session
     *            The user's session.
     */
	private ContactModel(final Session session) {
		super();
		this.impl = new ContactModelImpl(session);
		this.implLock = new Object();
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
		synchronized (implLock) {
            impl.acceptInvitation(userId, invitedBy, acceptedOn);
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
		synchronized (implLock) {
            impl.declineInvitation(userId, invitedBy, invitedAs, declinedOn);
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
        synchronized (implLock) {
            impl.delete(userId, contactId);
        }
    }

	/**
     * Delete a user's invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedUserId
     *            The invited user id <code>JabberId</code>.
     */
    public void deleteInvitation(final JabberId userId, final EMail invitedAs,
            final Calendar deletedOn) {
        synchronized (implLock) {
            impl.deleteInvitation(userId, invitedAs, deletedOn);
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
        synchronized (implLock) {
            impl.extendInvitation(userId, extendedTo, extendedOn);
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
		synchronized (implLock) {
            return impl.read(userId);
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
		synchronized (implLock) {
            return impl.read(userId, contactId);
		}
	}

	/**
	 * Read the invitation.
	 * 
	 * @param to
	 *            To whom the invitation is addressed.
	 * @return The invitation.
	 * @throws ParityServerModelException
	 */
	public Invitation readInvitation(final JabberId from) {
		synchronized (implLock) {
            return impl.readInvitation(from);
		}
	}
}
