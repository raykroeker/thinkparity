/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.contact;

import java.util.List;

import com.thinkparity.server.JabberId;
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

	public void acceptInvitation(final JabberId from, final JabberId to)
		throws ParityServerModelException {
		synchronized(implLock) { impl.acceptInvitation(from, to); }
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

	public void declineInvitation(final JabberId from, final JabberId to)
		throws ParityServerModelException {
		synchronized(implLock) { impl.declineInvitation(from , to); }
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

	public List<Contact> readContacts() throws ParityServerModelException {
		synchronized(implLock) { return impl.readContacts(); }
	}
}
