/*
 * Feb 24, 2006
 */
package com.thinkparity.ophelia.model.message;

import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalSystemMessageModel extends SystemMessageModel {

	/**
	 * Create an InternalSystemMessageModel.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 * @param context
	 *            The parity context.
	 */
	InternalSystemMessageModel(final Workspace workspace, final Context context) {
		super(workspace);
		context.assertContextIsValid();
	}
}
