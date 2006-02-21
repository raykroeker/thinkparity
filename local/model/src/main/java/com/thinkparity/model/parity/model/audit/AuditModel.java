/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit;

import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AuditModel extends AbstractModel {

	/**
	 * Obtain an internal audit model.
	 * 
	 * @param context
	 *            The parity model context.
	 * @return An internal audit model.
	 */
	public static InternalAuditModel getInternalModel(final Context context) {
		final Workspace workspace = getWorkspaceModel().getWorkspace();
		final InternalAuditModel iAModel = new InternalAuditModel(workspace, context);
		return iAModel;
	}

	/**
	 * Obtain an audit model.
	 * 
	 * @return An audit model.
	 */
	public static AuditModel getModel() {
		final Workspace workspace = getWorkspaceModel().getWorkspace();
		final AuditModel aModel = new AuditModel(workspace);
		return aModel;
	}

	/**
	 * The implementation.
	 * 
	 */
	private final AuditModelImpl impl;

	/**
	 * The implementation synchrnization lock.
	 * 
	 */
	private final Object implLock;

	/**
	 * Create a AuditModel.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	protected AuditModel(final Workspace workspace) {
		super();
		this.impl = new AuditModelImpl(workspace);
		this.implLock = new Object();
	}

	/**
	 * Obtain the implementation.
	 * 
	 * @return The implementation.
	 */
	protected AuditModelImpl getImpl() { return impl; }

	/**
	 * Obtain the implementation lock.
	 * 
	 * @return The implementation lock.
	 */
	protected Object getImplLock() { return implLock; }
}
