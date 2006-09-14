/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.migrator;


import com.thinkparity.codebase.model.migrator.LibraryBytes;

import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class InternalLibraryModel extends LibraryModel {

    /** Create InternalLibraryModel. */
    InternalLibraryModel(final Context context, final Workspace workspace) {
        super(workspace);
    }

    public LibraryBytes readBytes(final Long libraryId) {
        synchronized(getImplLock()) { return getImpl().readBytes(libraryId); }
    }
}
