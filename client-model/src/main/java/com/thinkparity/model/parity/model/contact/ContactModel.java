/*
 * Generated On: Jun 27 06 04:14:53 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.contact;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * <b>Title:</b>thinkParity Contact Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version $Revision$
 */
public class ContactModel {

	/**
	 * Create a Contact interface.
	 * 
	 * @param context
	 *            A thinkParity internal context.
	 * @return The internal Contact interface.
	 */
	public static InternalContactModel getInternalModel(final Context context) {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		return new InternalContactModel(workspace, context);
	}

	/**
	 * Create a Contact interface.
	 * 
	 * @return The Contact interface.
	 */
	public static ContactModel getModel() {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		return new ContactModel(workspace);
	}

	/** The model implementation. */
	private final ContactModelImpl impl;

	/** The model implementation synchronization lock. */
	private final Object implLock;

	/**
	 * Create ContactModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ContactModel(final Workspace workspace) {
		super();
		this.impl = new ContactModelImpl(workspace);
		this.implLock = new Object();
	}

    /**
     * Create an e-mail contact.
     * 
     * @param email
     *            An e-mail address.
     */
    public Contact create(final String email) {
        synchronized(implLock) { return impl.create(email); }
    }

    /**
     * Delete a contact.
     * 
     * @param contactId
     *            A contact jabber id.
     * @return A contact.
     */
    public void delete(final JabberId contactId) {
        synchronized(implLock) { impl.delete(contactId); }
    }

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact jabber id.
     * @return A contact.
     */
    public Contact read(final JabberId contactId) {
        synchronized(implLock) { return impl.read(contactId); }
    }

	/**
	 * Obtain the model implementation.
	 * 
	 * @return The model implementation.
	 */
	protected ContactModelImpl getImpl() { return impl; }

	/**
	 * Obtain the model implementation synchronization lock.
	 * 
	 * @return The model implementation synchrnoization lock.
	 */
	protected Object getImplLock() { return implLock; }
}
