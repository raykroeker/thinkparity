/*
 * Created On: Jul 23, 2005
 */
package com.thinkparity.ophelia.model.workspace;


import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.Constants.ShutdownHookNames;
import com.thinkparity.ophelia.model.Constants.ShutdownHookPriorities;
import com.thinkparity.ophelia.model.util.ShutdownHook;

/**
 * WorkspaceModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see com.thinkparity.ophelia.model.workspace.WorkspaceModel
 */
class WorkspaceModelImpl extends AbstractModelImpl {

	/** Singleton workspace. */
	private static final Workspace WORKSPACE;

    /** Singleton workspace helper. */
    private static final WorkspaceHelper WORKSPACE_HELPER;

	static {
        WORKSPACE_HELPER = new WorkspaceHelper();
        WORKSPACE = WORKSPACE_HELPER.openWorkspace();

        addShutdownHook(new ShutdownHook() {
            @Override
            public String getDescription() {
                return ShutdownHookNames.WORKSPACE;
            }
            @Override
            public String getName() {
                return ShutdownHookNames.WORKSPACE;
            }
            @Override
            public Integer getPriority() {
                return ShutdownHookPriorities.WORKSPACE;
            }
            @Override
            public void run() {
                WORKSPACE_HELPER.savePreferences();
                WORKSPACE_HELPER.deleteTempDirectory();
            }
        });
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
	Workspace getWorkspace() {
        return WorkspaceModelImpl.WORKSPACE;
	}

    /**
     * Determine if this is the first run of the workspace.
     * 
     * @return True if this is the first run of the workspace; false otherwise.
     */
    Boolean isFirstRun() {
        logApiId();
        return null == readCredentials();
    }
}

