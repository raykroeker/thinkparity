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
 * @version 1.1
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

    /**
	 * Contact model implementation.
	 * 
	 */
	private final ContactModelImpl impl;

	/**
	 * Synchronization lock for the implementation.
	 */
	private final Object implLock;

	/**
	 * Create a ArtifactModel.
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
	 * Create an invitation from the session user to the jabber id.
	 * 
	 * @param to
	 *            To whom the invitation is extended.
	 * @return An invitation or null if one does not exist.
	 */
	public Invitation createInvitation(final JabberId to)
			throws ParityServerModelException {
		synchronized(implLock) { return impl.createInvitation(to); }
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
	 * Delete an invitation.
	 * 
	 * @param from
	 *            From whom the invitation originated.
	 * @throws ParityServerModelException
	 */
	public void deleteInvitation(final JabberId from)
			throws ParityServerModelException {
		synchronized(implLock) { impl.deleteInvitation(from); }
	}

	/**
     * 
     * @param email
     */
    public void invite(final EMail email, final Calendar invitedOn) {
        synchronized(implLock) { impl.invite(email, invitedOn); }
    }

	/**
     * Read the contact info for the jabber id.
     * 
     * @param jabberId
     *            The jabber id.
     * @return The contact info.
     * @throws ParityServerModelException
     */
	public Contact readContact(final JabberId jabberId) {
		synchronized(implLock) { return impl.readContact(jabberId); }
	}

	public List<Contact> readContacts() throws ParityServerModelException {
		synchronized(implLock) { return impl.readContacts(); }
	}

	/**
	 * Read the invitation.
	 * 
	 * @param to
	 *            To whom the invitation is addressed.
	 * @return The invitation.
	 * @throws ParityServerModelException
	 */
	public Invitation readInvitation(final JabberId from)
			throws ParityServerModelException {
		synchronized(implLock) { return impl.readInvitation(from); }
	}
}
