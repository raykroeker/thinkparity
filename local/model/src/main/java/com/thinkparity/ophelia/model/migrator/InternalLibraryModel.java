/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.migrator;


import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.migrator.LibraryBytes;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class InternalLibraryModel extends LibraryModel {

    /** Create InternalLibraryModel. */
    InternalLibraryModel(final Context context, final Environment environment,
            final Workspace workspace) {
        super(environment, workspace);
    }

    public LibraryBytes readBytes(final Long libraryId) {
        synchronized(getImplLock()) { return getImpl().readBytes(libraryId); }
    }
}
