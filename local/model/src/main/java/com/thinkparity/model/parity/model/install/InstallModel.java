/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.model.parity.model.install;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

import com.thinkparity.migrator.Release;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class InstallModel extends AbstractModel {

    /**
     * Obtain the parity install interface.
     * 
     * @return The parity install interface.
     */
    public static InstallModel getModel() {
        final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
        return new InstallModel(workspace);
    }

    /** The parity install implementation. */
    private final InstallModelImpl impl;

    /** The parity install implementation synchronization lock. */
    private final Object implLock;

    /**
     * Create InstallModel.
     * 
     * @param workspace
     *            The parity workspace.
     */
    protected InstallModel(final Workspace workspace) {
        super();
        this.impl = new InstallModelImpl(workspace);
        this.implLock = new Object();
    }

    /**
     * Install a release.
     * 
     * @param release
     *            A release.
     */
    public void install(final Release release) throws ParityException {
        synchronized(implLock) { impl.install(release); }
    }
}
