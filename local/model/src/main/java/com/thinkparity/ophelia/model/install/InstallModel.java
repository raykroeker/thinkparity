/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.install;


import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class InstallModel extends AbstractModel<InstallModelImpl> {

    /**
     * Obtain the parity install interface.
     * 
     * @return The parity install interface.
     */
    public static InstallModel getModel(final Environment environment,
            final Workspace workspace) {
        return new InstallModel(environment, workspace);
    }

    /**
     * Create InstallModel.
     * 
     * @param workspace
     *            The parity workspace.
     */
    protected InstallModel(final Environment environment, final Workspace workspace) {
        super(new InstallModelImpl(environment, workspace));
    }

    /**
     * Install a release.
     * 
     * @param release
     *            A release.
     */
    public void install(final Release release) throws ParityException {
        synchronized (getImplLock()) { getImpl().install(release); }
    }
}
