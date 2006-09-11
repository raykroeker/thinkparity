/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.library;


import com.thinkparity.codebase.model.migrator.Library;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * The parity bootstrap library interface.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class LibraryModel extends AbstractModel {

    /**
     * Obtain the internal parity library interface.
     * 
     * @param context
     *            A parity calling context.
     * @return The internal parity library interface.
     */
    public static InternalLibraryModel getInternalModel(final Context context) {
        final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
        return new InternalLibraryModel(context, workspace);
    }

    /**
     * Obtain the parity library interface.
     * 
     * @return The parity library interface.
     */
    public static LibraryModel getModel() {
        final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
        return new LibraryModel(workspace);
    }

    /** The parity bootstrap library implementation. */
    private final LibraryModelImpl impl;

    /** The parity bootstrap library implementation synchronization lock. */
    private final Object implLock;

    /** Create LibraryModel. */
    protected LibraryModel(final Workspace workspace) {
        super();
        this.impl = new LibraryModelImpl(workspace);
        this.implLock = new Object();
    }

    /**
     * Create a library.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param path
     *            A path.
     * @param type
     *            A type.
     * @param version
     *            A version.
     * @param bytes
     *            A byte array.
     * @return A library.
     */
    public Library create(final String artifactId, final String groupId,
            final String path, final Library.Type type, final String version,
            final byte[] bytes) {
        synchronized(implLock) {
            return impl.create(artifactId, groupId, path, type, version, bytes);
        }
    }

    /**
     * Read a library.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param type
     *            A type.
     * @param version
     *            A version.
     * @return A library.
     */
    public Library read(final String artifactId, final String groupId,
            final Library.Type type, final String version) {
        synchronized(implLock) {
            return impl.read(artifactId, groupId, type, version);
        }
    }

    /**
     * Obtain parity bootstrap library implementation.
     *
     * @return The parity bootstrap library implementation.
     */
    protected LibraryModelImpl getImpl() { return impl; }

    /**
     * Obtain parity bootstrap library implementation synchronization lock.
     * 
     * @return The parity bootstrap library implementation synchronization lock.
     */
    protected Object getImplLock() { return implLock; }

}
