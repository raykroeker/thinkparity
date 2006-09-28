/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.download;


import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class InternalDownloadModel extends DownloadModel {

    /** Create InternalDownloadModel. */
    InternalDownloadModel(final Context context, final Environment environment,
            final Workspace workspace) {
        super(environment, workspace);
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
