/*
 * Jul 23, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import com.thinkparity.model.parity.model.AbstractModelImpl;

/**
 * WorkspaceModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see com.thinkparity.model.parity.model.workspace.WorkspaceModel
 */
class WorkspaceModelImpl extends AbstractModelImpl {

	/**
	 * Singleton instance.
	 */
	private static final Workspace cachedWorkspace;

	static { cachedWorkspace = new WorkspaceHelper().openWorkspace(); }

	/**
     * Obtain a logging id for an api.
     * 
     * @param api
     *            The api.
     * @return A logging id.
     */
    private static StringBuffer getApiId(final String api) {
        return getModelId("[WORKSPACE]").append(" ").append(api);
    }

	/**
	 * Create a WorkspaceModelImpl.
	 * 
	 */
	WorkspaceModelImpl() { super(null); }

    /**
	 * Obtain the workspace for the parity model software.
	 * 
	 * @return The workspace.
	 */
	Workspace getWorkspace() { return WorkspaceModelImpl.cachedWorkspace; }

    /**
     * Determine if this is the first run of the workspace.
     * 
     * @return True if this is the first run of the workspace; false otherwise.
     */
    Boolean isFirstRun() {
        logger.info(getApiId("[IS FIRST RUN]"));
        return null == readCredentials();
    }
}

