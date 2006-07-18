/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.model.parity.model.profile;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

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
     * Read the logged in user's profile.
     * 
     * @return A profile.
     */
    public Profile read() { synchronized(implLock) { return impl.read(); } }

     /**
     * Update the logged in user's profile.
     * 
     * @param profile
     *            A profile.
     */
    public void update(final Profile profile) {
        synchronized(implLock) { impl.update(profile); }
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
