/*
 * Generated On: Oct 04 06 09:40:46 AM
 */
package com.thinkparity.ophelia.model.backup;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Backup Model<br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
public class BackupModel extends AbstractModel<BackupModelImpl> {

	/**
	 * Create a thinkParity Backup interface.
	 * 
	 * @param context
     *      A thinkParity model <code>Context</code>.
     * @param environment
     *      A thinkParity <code>Environment</code>.
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @return A thinkParity Backup interface.
	 */
	public static InternalBackupModel getInternalModel(final Context context,
            final Environment environment, final Workspace workspace) {
		return new InternalBackupModel(context, environment, workspace);
	}

	/**
	 * Create a thinkParity Backup interface.
	 * 
     * @param environment
     *      A thinkParity <code>Environment</code>.
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @return A thinkParity Backup interface.
	 */
	public static BackupModel getModel(final Environment environment,
        final Workspace workspace) {
		return new BackupModel(environment, workspace);
	}

	/**
	 * Create BackupModel.
	 *
     * @param environment
     *      A thinkParity <code>Environment</code>.
	 * @param workspace
	 *		The thinkParity <code>Workspace</code>.
	 */
	protected BackupModel(final Environment environment, final Workspace workspace) {
		super(new BackupModelImpl(environment, workspace));
	}
}
