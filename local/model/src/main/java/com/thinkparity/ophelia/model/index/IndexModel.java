/*
 * Created On: Mar 6, 2006
 */
package com.thinkparity.ophelia.model.index;


import com.thinkparity.codebase.model.Context;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.5
 */
public class IndexModel extends AbstractModel<IndexModelImpl> {

	/**
	 * Obtain an internal index model.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @param context
	 *            The parity context.
	 * @return An internal index model.
	 */
	public static InternalIndexModel getInternalModel(final Context context,
            final Workspace workspace) {
		return new InternalIndexModel(workspace, context);
	}

	/**
	 * Obtain an index model.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @return The index model.
	 */
	public static IndexModel getModel(final Workspace workspace) {
		return new IndexModel(workspace);
	}

	/**
	 * Create a IndexModel.
	 */
	protected IndexModel(final Workspace workspace) {
		super(new IndexModelImpl(workspace));
	}
}
