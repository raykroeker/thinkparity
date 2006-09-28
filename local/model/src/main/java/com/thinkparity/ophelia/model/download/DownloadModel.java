/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.download;


import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The parity bootstrap download interface.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class DownloadModel extends AbstractModel<DownloadModelImpl> {

    /**
     * Obtain the parity bootstrap internal download interface.
     * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
     * @return The parity bootstrap internal download interface.
     */
    public static InternalDownloadModel getInternalModel(final Context context,
            final Environment environment, final Workspace workspace) {
        return new InternalDownloadModel(context, environment, workspace);
    }

    /**
     * Obtain the parity bootstrap download interface.
     * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
     * @return The parity bootstrap download interface.
     */
    public static DownloadModel getModel(final Environment environment,
            final Workspace workspace) {
        return new DownloadModel(environment, workspace);
    }

    /** Create DownloadModel. */
    protected DownloadModel(final Environment environment,
            final Workspace workspace) {
        super(new DownloadModelImpl(environment, workspace));
    }

    /**
     * Download a release.
     * 
     * @param release
     *            A release.
     * @throws ParityException
     */
    public void download(final Release release) throws ParityException {
        synchronized(getImplLock()) { getImpl().download(release); }
    }

    /**
     * Determine the download for release is complete.
     * 
     * @return True if the download for the latest version is complete; false
     *         otherwise.
     */
    public Boolean isComplete(final Release release) throws ParityException {
        synchronized(getImplLock()) { return getImpl().isComplete(release); }
    }
}
