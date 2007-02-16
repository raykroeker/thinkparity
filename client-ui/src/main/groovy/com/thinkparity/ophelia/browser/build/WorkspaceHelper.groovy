/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.ophelia.browser.build

import com.thinkparity.codebase.model.session.Credentials

import com.thinkparity.ophelia.model.ModelFactory
import com.thinkparity.ophelia.model.session.DefaultLoginMonitor
import com.thinkparity.ophelia.model.workspace.Workspace
import com.thinkparity.ophelia.model.workspace.WorkspaceModel

/**
 * <b>Title:</b>thinkParity OpheliaUI Build Task Workspace Helper<br>
 * <b>Description:</b><br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class WorkspaceHelper {

    /** A build task configuration <code>Map</code>. */
    Map configuration

    /** An instance of <code>WorkspaceModel</code>. */
    WorkspaceModel model

    /** A <code>Workspace</code>. */
    Workspace workspace

    /**
     * Initialize the workspace helper.  Obtain an instance of the model.
     *
     */
    void init() {
        if (null == model)
            model = WorkspaceModel.getInstance(configuration["thinkparity.environment"])
    }

    /**
     * Open a workspace.
     *
     */
    void open() {
        init()
        assert null == workspace
        // create the workspace
        workspace = model.getWorkspace(configuration["thinkparity.target.workspace-dir"])
        // initialize if required
        if (!model.isInitialized(workspace))
            model.initialize(workspace, new LoginMonitor(), configuration["thinkparity.credentials"])
        // save the workspace to the configuration
        configuration["thinkparity.workspace"] = workspace
        // save the model factory to the configuration
        configuration["thinkparity.modelfactory"] =
            ModelFactory.getInstance(configuration["thinkparity.environment"], workspace)
    }

    /**
     * Close a workspace.
     *
     */
    void close() {
        model.close(workspace)
        workspace = null
    }
}

/**
 * <b>Title:</b>Workspace Helper Login Monitor<br>
 * <b>Description:</b>A login monitor that will always synchronize.<br>
 */
class LoginMonitor extends DefaultLoginMonitor {
    public Boolean confirmSynchronize() {
        return Boolean.TRUE
    }
}
