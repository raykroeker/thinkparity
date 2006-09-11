/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.download;


import com.thinkparity.codebase.model.migrator.Release;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * The parity bootstrap download interface.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class DownloadModel extends AbstractModel {

    /**
     * Obtain the parity bootstrap internal download interface.
     * 
     * @return The parity bootstrap internal download interface.
     */
    public static InternalDownloadModel getInternalModel(final Context context) {
        final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
        return new InternalDownloadModel(context, workspace);
    }

    /**
     * Obtain the parity bootstrap download interface.
     * 
     * @return The parity bootstrap download interface.
     */
    public static DownloadModel getModel() {
        final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
        return new DownloadModel(workspace);
    }

    /** The parity bootstrap download interface. */
    private final DownloadModelImpl impl;

    /** The parity bootstrap download implementation synchronization lock. */
    private final Object implLock;

    /** Create DownloadModel. */
    protected DownloadModel(final Workspace workspace) {
        super();
        this.impl = new DownloadModelImpl(workspace);
        this.implLock = new Object();
    }

    /**
     * Download a release.
     * 
     * @param release
     *            A release.
     * @throws ParityException
     */
    public void download(final Release release) throws ParityException {
        synchronized(implLock) { impl.download(release); }
    }

    /**
     * Determine the download for release is complete.
     * 
     * @return True if the download for the latest version is complete; false
     *         otherwise.
     */
    public Boolean isComplete(final Release release) throws ParityException {
        synchronized(implLock) { return impl.isComplete(release); }
    }

    /**
     * Obtain the impl
     *
     * @return The DownloadModelImpl.
     */
    protected DownloadModelImpl getImpl() { return impl; }

    /**
     * Obtain the implLock
     *
     * @return The Object.
     */
    protected Object getImplLock() { return implLock; }
}
