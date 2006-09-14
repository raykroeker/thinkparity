/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.install;


import com.thinkparity.codebase.model.migrator.Release;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.workspace.Workspace;

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
    public static InstallModel getModel(final Workspace workspace) {
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
