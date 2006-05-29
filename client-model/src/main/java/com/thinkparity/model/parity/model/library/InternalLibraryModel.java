/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.model.parity.model.library;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;

import com.thinkparity.migrator.LibraryBytes;

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
