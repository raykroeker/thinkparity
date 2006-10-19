/*
 * Created On: Jun 10, 2006 10:26:01 AM
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.session.LoginMonitor;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

import com.thinkparity.ophelia.browser.platform.Platform;

import org.apache.log4j.Logger;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public final class FirstRunHelper {

    /** An apache logger. */
    final Logger logger;

    /** The thinkParity <code>Workspace</code>. */
    private final Workspace workspace;

    /** The thinkParity workspace model. */
    private final WorkspaceModel workspaceModel;

    /** Create FirstRunHelper. */
    public FirstRunHelper(final Platform platform) {
        super();
        this.logger = platform.getLogger(getClass());
        this.workspace = platform.getModelFactory().getWorkspace(getClass());
        this.workspaceModel = platform.getModelFactory().getWorkspaceModel(getClass());
    }

    /**
     * Execute first run functionality for the browser platform.
     * 
     * @return True if first run completed.
     */
    public void firstRun() {
        final LoginWindow window = new LoginWindow();
        window.setVisibleAndWait();

        final String username = window.getUsername();
        final String password = window.getPassword();
        if(null != username && null != password) {
            final Credentials credentials = new Credentials();
            credentials.setPassword(password);
            credentials.setUsername(username);

            workspaceModel.initialize(workspace, new LoginMonitor() {
                public Boolean confirmSynchronize() {
                    final ConfirmSynchronizeWindow confirmWindow = new ConfirmSynchronizeWindow();
                    confirmWindow.setVisibleAndWait();
                    return confirmWindow.didConfirm();
                }
                public void notifyInvalidCredentials(final Credentials credentials) {
                }
            }, credentials);
        }
    }

    /**
     * Determine if this is the first time the platform has been run.
     * 
     * @return True if this is the first time the platform has been run.
     */
    public Boolean isFirstRun() {
        return !workspaceModel.isInitialized(workspace);
    }
}
