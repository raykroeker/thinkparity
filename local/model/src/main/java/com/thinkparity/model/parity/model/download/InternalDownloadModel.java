/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.model.parity.model.download;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;

import com.thinkparity.migrator.Release;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class InternalDownloadModel extends DownloadModel {

    /** Create InternalDownloadModel. */
    InternalDownloadModel(final Context context, final Workspace workspace) {
        super(workspace);
    }

    /**
     * Read the latest release that has been downloaded.
     * 
     * @return A release.
     */
    public Release read() throws ParityException {
        synchronized(getImplLock()) {return getImpl().read(); }
    }
}
