/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.model.parity.model.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.profile.ProfileEMail;

/**
 * <b>Title:</b>thinkParity Profile Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 
 */
public class ProfileModel {

    /**
	 * Create a Profile interface.
	 * 
	 * @param context
	 *            A thinkParity internal context.
	 * @return The internal Profile interface.
	 */
	public static InternalProfileModel getInternalModel(final Context context) {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		return new InternalProfileModel(workspace, context);
	}

    /**
	 * Create a Profile interface.
	 * 
	 * @return The Profile interface.
	 */
	public static ProfileModel getModel() {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		return new ProfileModel(workspace);
	}

	/** The model implementation. */
	private final ProfileModelImpl impl;

	/** The model implementation synchronization lock. */
	private final Object implLock;

	/**
	 * Create ProfileModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ProfileModel(final Workspace workspace) {
		super();
		this.impl = new ProfileModelImpl(workspace);
		this.implLock = new Object();
	}

    /**
     * Add an email to the profile.
     * 
     * @param email
     *            An <code>EMail</code>.
     */
	public void addEmail(final EMail email) {
        synchronized (getImplLock()) {
            getImpl().addEmail(email);
        }
    }

    /**
     * Read the logged in user's profile.
     * 
     * @return A profile.
     */
    public Profile read() { synchronized(implLock) { return impl.read(); } }

    /**
     * Read a profile email.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     * @return A <code>ProfileEmail</code>.
     */
    public ProfileEMail readEmail(final Long emailId) {
        synchronized (getImplLock()) {
            return getImpl().readEmail(emailId);
        }
    }

    /**
     * Read a list of profile email addresses.
     * 
     * @return A list of email addresses.
     */
    public List<ProfileEMail> readEmails() {
        synchronized (getImplLock()) {
            return getImpl().readEmails();
        }
    }

	/**
     * Remove an email.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     */
	public void removeEmail(final Long emailId) {
        synchronized (getImplLock()) {
            getImpl().removeEmail(emailId);
        }
    }

    /**
     * Update the logged in user's profile.
     * 
     * @param profile
     *            A profile.
     */
    public void update(final Profile profile) {
        synchronized (getImplLock()) {
            getImpl().update(profile);
        }
    }

    /**
     * Verify an email.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @param key
     *            A verification key <code>String</code>.
     */
    public void verifyEmail(final Long emailId, final String key) {
        synchronized (getImplLock()) {
            getImpl().verifyEmail(emailId, key);
        }
    }

    /**
	 * Obtain the model implementation.
	 * 
	 * @return The model implementation.
	 */
	protected ProfileModelImpl getImpl() { return impl; }

    /**
	 * Obtain the model implementation synchronization lock.
	 * 
	 * @return The model implementation synchrnoization lock.
	 */
	protected Object getImplLock() { return implLock; }
}
