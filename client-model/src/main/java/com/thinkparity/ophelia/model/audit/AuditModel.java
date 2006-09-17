/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.audit;


import com.thinkparity.codebase.model.Context;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AuditModel extends AbstractModel<AuditModelImpl> {

	/**
	 * Obtain an internal audit model.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @param context
	 *            The parity model context.
	 * @return An internal audit model.
	 */
	public static InternalAuditModel getInternalModel(final Context context,
            final Workspace workspace) {
		return new InternalAuditModel(workspace, context);
	}

	/**
	 * Obtain an audit model.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @return An audit model.
	 */
	public static AuditModel getModel(final Workspace workspace) {
		return new AuditModel(workspace);
	}

	/**
	 * Create a AuditModel.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	protected AuditModel(final Workspace workspace) {
		super(new AuditModelImpl(workspace));
	}
}
