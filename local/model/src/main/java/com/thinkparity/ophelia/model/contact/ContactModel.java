/*
 * Generated On: Jun 27 06 04:14:53 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.contact;

import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.events.ContactListener;
import com.thinkparity.ophelia.model.util.filter.Filter;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Contact Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version $Revision$
 */
public class ContactModel extends AbstractModel<ContactModelImpl> {

    /**
	 * Create a Contact interface.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @param context
	 *            A thinkParity internal context.
	 * @return The internal Contact interface.
	 */
	public static InternalContactModel getInternalModel(final Context context,
            final Workspace workspace) {
		return new InternalContactModel(workspace, context);
	}

    /**
	 * Create a Contact interface.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @return The Contact interface.
	 */
	public static ContactModel getModel(final Workspace workspace) {
		return new ContactModel(workspace);
	}

    /**
	 * Create ContactModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ContactModel(final Workspace workspace) {
		super(new ContactModelImpl(workspace));
	}

	/**
     * Accept the incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void acceptIncomingInvitation(final Long invitationId) {
        synchronized (getImplLock()) {
            getImpl().acceptIncomingInvitation(invitationId);
        }
    }

    /**
     * Add a contact listener.
     * 
     * @param listener
     *            A contact listener.
     */
    public void addListener(final ContactListener listener) {
        synchronized (getImplLock()) {
            getImpl().addListener(listener);
        }
    }

    /**
     * Create an e-mail contact invitation.
     * 
     * @param email
     *            An e-mail address.
     * @return The new contact invitation.
     */
    public OutgoingInvitation createOutgoingInvitation(final EMail email) {
        synchronized(getImplLock()) { return getImpl().createOutgoingInvitation(email); }
    }

	/**
     * Accept the incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void declineIncomingInvitation(final Long invitationId) {
        synchronized (getImplLock()) {
            getImpl().declineIncomingInvitation(invitationId);
        }
    }

	/**
     * Delete a contact.
     * 
     * @param contactId
     *            A contact jabber id.
     * @return A contact.
     */
    public void delete(final JabberId contactId) {
        synchronized(getImplLock()) { getImpl().delete(contactId); }
    }

	/**
     * Delete an outgoing invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void deleteOutgoingInvitation(final Long invitationId) {
        synchronized (getImplLock()) {
            getImpl().deleteOutgoingInvitation(invitationId);
        }
    }

    /**
     * Download the contacts from the server and create local contacts.
     *
     */
    public void download() {
        synchronized(getImplLock()) { getImpl().download(); }
    }

    /**
     * Read a list of contacts.
     * 
     * @return A list of contacts.
     */
    public List<Contact> read() {
        synchronized(getImplLock()) { return getImpl().read(); }
    }

    /**
     * Read a list of contacts.
     * 
     * @param comparator
     *            A contact comparator to sort the contacts.
     * @return A list of contacts.
     */
    public List<Contact> read(final Comparator<Contact> comparator) {
        synchronized(getImplLock()) { return getImpl().read(comparator); }
    }

    /**
     * Read a list of contacts.
     * 
     * @param comparator
     *            A contact comparator to sort the contacts.
     * @param filter
     *            A contact filter to scope the contacts.
     * @return A list of contacts.
     */
    public List<Contact> read(final Comparator<Contact> comparator,
            final Filter<? super Contact> filter) {
        synchronized(getImplLock()) { return getImpl().read(comparator, filter); }
    }

    /**
     * Read a list of contacts.
     * 
     * @param filter
     *            A contact filter to scope the contacts.
     * @return A list of contacts.
     */
    public List<Contact> read(final Filter<Contact> filter) {
        synchronized(getImplLock()) { return getImpl().read(filter); }
    }

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact jabber id.
     * @return A contact.
     */
    public Contact read(final JabberId contactId) {
        synchronized(getImplLock()) { return getImpl().read(contactId); }
    }

    /**
     * Read an outgoing invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An outgoing invitation.
     */
    public IncomingInvitation readIncomingInvitation(final Long invitationId) {
        synchronized (getImplLock()) {
            return getImpl().readIncomingInvitation(invitationId);
        }
    }

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    public List<IncomingInvitation> readIncomingInvitations() {
        synchronized (getImplLock()) {
            return getImpl().readIncomingInvitations();
        }
    }

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    public List<IncomingInvitation> readIncomingInvitations(
            final Comparator<ContactInvitation> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readIncomingInvitations(comparator);
        }
    }

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    public List<IncomingInvitation> readIncomingInvitations(
            final Comparator<ContactInvitation> comparator,
            final Filter<? super ContactInvitation> filter) {
        synchronized (getImplLock()) {
            return getImpl().readIncomingInvitations(comparator, filter);
        }
    }

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    public List<IncomingInvitation> readIncomingInvitations(
            final Filter<? super ContactInvitation> filter) {
        synchronized (getImplLock()) {
            return getImpl().readIncomingInvitations(filter);
        }
    }


    /**
     * Read an outgoing invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An outgoing invitation.
     */
    public OutgoingInvitation readOutgoingInvitation(final Long invitationId) {
        synchronized (getImplLock()) {
            return getImpl().readOutgoingInvitation(invitationId);
        }
    }

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingInvitation> readOutgoingInvitations() {
        synchronized (getImplLock()) {
            return getImpl().readOutgoingInvitations();
        }
    }

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingInvitation> readOutgoingInvitations(
            final Comparator<ContactInvitation> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readOutgoingInvitations(comparator);
        }
    }

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingInvitation> readOutgoingInvitations(
            final Comparator<ContactInvitation> comparator,
            final Filter<? super ContactInvitation> filter) {
        synchronized (getImplLock()) {
            return getImpl().readOutgoingInvitations(comparator, filter);
        }
    }


    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingInvitation> readOutgoingInvitations(
            final Filter<? super ContactInvitation> filter) {
        synchronized (getImplLock()) {
            return getImpl().readOutgoingInvitations(filter);
        }
    }

    /**
     * Remove a contact listener.
     * 
     * @param listener
     *            A contact listener.
     */
    public void removeListener(final ContactListener listener) {
        synchronized (getImplLock()) {
            getImpl().removeListener(listener);
        }
    }

    /**
     * Search for contacts.
     * 
     * @param expression
     *            A search expression.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
    public List<JabberId> search(final String expression) {
        synchronized (getImplLock()) {
            return getImpl().search(expression);
        }
    }
}
