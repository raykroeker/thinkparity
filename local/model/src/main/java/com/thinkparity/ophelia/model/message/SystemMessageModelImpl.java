/*
 * Jan 31, 2006
 */
package com.thinkparity.ophelia.model.message;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class SystemMessageModelImpl extends AbstractModelImpl {

	/**
     * Create SystemMessageModelImpl.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
	SystemMessageModelImpl(final Environment environment, final Workspace workspace) {
		super(environment, workspace);
	}
}
