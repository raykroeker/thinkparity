/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.model.parity.model.download;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

import com.thinkparity.migrator.Release;

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
