/*
 * Jan 31, 2006
 */
package com.thinkparity.ophelia.model.message;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class SystemMessageModelImpl extends Model {

	/**
     * Create SystemMessageModelImpl.
     * 
     */
	public SystemMessageModelImpl() {
		super();
	}

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
    }
}
