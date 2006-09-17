/*
 * Jan 31, 2006
 */
package com.thinkparity.ophelia.model.message;

import com.thinkparity.codebase.model.Context;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessageModel extends AbstractModel<SystemMessageModelImpl> {

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

	/**
     * Create SystemMessageModel.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
	protected SystemMessageModel(final Workspace workspace) {
		super(new SystemMessageModelImpl(workspace));
	}

}
