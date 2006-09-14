/*
 * Jan 31, 2006
 */
package com.thinkparity.ophelia.model.message;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessageModel extends AbstractModel {

	/**
	 * Obtain an internal system message model.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @param context
	 *            The parity context.
	 * @return The internal system message model.
	 */
	public static InternalSystemMessageModel getInternalModel(
            final Context context, final Workspace workspace) {
		return new InternalSystemMessageModel(workspace, context);
	}

	/**
	 * Obtain a <code>SystemMessageModel</code>
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @return A SystemMessageModel.
	 */
	public static SystemMessageModel getModel(final Workspace workspace) {
		return new SystemMessageModel(workspace);
	}

	/** The model implementation. */
	private final SystemMessageModelImpl impl;

	/** The model implementation syncrhonization lock. */
	private final Object implLock;

	/**
     * Create SystemMessageModel.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
	protected SystemMessageModel(final Workspace workspace) {
		super();
		this.impl = new SystemMessageModelImpl(workspace);
		this.implLock = new Object();
	}

	/**
	 * Obtain the implementation.
	 * 
	 * @return The implementation.
	 */
	protected SystemMessageModelImpl getImpl() { return impl; }

	/**
	 * Obtain the implementation lock.
	 * 
	 * @return The implementation lock.
	 */
	protected Object getImplLock() { return implLock; }
}
