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
     * @param invitedAs
     *            The original invitation e-mail.
     * @param invitedBy
     *            By whom the invitation was sent.
     * @param acceptedBy
     *            By whom the invitation was accepted
     * @param acceptedOn
     *            When the invitation was accepted.
     */
    public void acceptInvitation(final JabberId invitedBy,
            final JabberId acceptedBy, final Calendar acceptedOn) {
		synchronized (implLock) {
            impl.acceptInvitation(invitedBy, acceptedBy, acceptedOn);
		}
	}

    /**
     * Decline the invitation. Send the invitee a notifiaction.
     * 
     * @param invitedAs
     *            The original invitation e-mail.
     * @param invitedBy
     *            The invitor.
     * @param declinedBy
     *            The invitee.
     * @param declinedOn
     *            When the acceptance was made.
     */
	public void declineInvitation(final String invitedAs,
            final JabberId invitedBy, final JabberId declinedBy,
            final Calendar declinedOn) {
		synchronized (implLock) {
            impl.declineInvitation(invitedAs, invitedBy, declinedBy, declinedOn);
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
    public void deleteInvitation(final JabberId userId,
            final JabberId invitedUserId) {
        synchronized (implLock) {
            impl.deleteInvitation(userId, invitedUserId);
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
     * Read the contact info for the jabber id.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     * @return The contact info.
     */
	public Contact readContact(final JabberId contactId) {
		synchronized (implLock) {
            return impl.readContact(contactId);
		}
	}

    /**
     * Read all contacts.
     * 
     * @return A <code>List&lt;Contact&gt;</code>.
     * @throws ParityServerModelException
     */
	public List<Contact> readContacts() throws ParityServerModelException {
		synchronized (implLock) {
            return impl.readContacts();
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
