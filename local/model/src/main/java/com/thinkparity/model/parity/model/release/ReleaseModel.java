/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import java.util.List;

import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * The parity bootstrap's release model.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReleaseModel extends AbstractModel {

    /**
     * Obtain the parity bootstrap's internal release model interface.
     *
     * @return The parity bootstrap's internal release model interface.
     */
    public static InternalReleaseModel getInternalModel(final Context context) {
        final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
        return new InternalReleaseModel(context, workspace);
    }

    /**
     * Obtain the parity bootstrap's release model interface.
     *
     * @return The parity bootstrap's release model interface.
     */
    public static ReleaseModel getModel() {
        final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
        return new ReleaseModel(workspace);
    }

    /** The implementation. */
    private final ReleaseModelImpl impl;

    /** The implementation lock. */
    private final Object implLock;

    /** Create ReleaseModel. */
    protected ReleaseModel(final Workspace workspace) {
        super();
        this.impl = new ReleaseModelImpl(workspace);
        this.implLock = new Object();
    }

    /**
     * Create a release.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param version
     *            A version.
     * @param libraries
     *            A list of libraries.
     * @return A release.
     */
    public Release create(final String artifactId, final String groupId,
            final String version, final List<Library> libraries) {
        synchronized(implLock) {
            return impl.create(artifactId, groupId, version, libraries);
        }
    }

    /**
     * Read a release.
     *
     * @param artifactId
     *      An artifact id.
     * @param groupId
     *      A group id.
     * @param version
     *      A version.
     * @return A release.
     */
    public Release read(final String artifactId, final String groupId,
            final String version) {
        synchronized(implLock) {
            return impl.read(artifactId, groupId, version);
        }
    }

    /**
     * Read the latest release.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @return A release.
     */
    public Release readLatest(final String artifactId, final String groupId) {
        synchronized(getImplLock()) {
            return getImpl().readLatest(artifactId, groupId);
        }
    }

    /**
     * Read a list of libraries belonging to a release.
     * 
     * @param releaseId
     *            A release id.
     * @return A list of libraries.
     */
    public List<Library> readLibraries(final Long releaseId) {
        synchronized(implLock) { return impl.readLibraries(releaseId); }
    }

    /**
     * Obtain the implementation.
     *
     * @return The implementation.
     */
    protected ReleaseModelImpl getImpl() { return impl; }

    /**
     * Obtain the implementation lock.
     *
     * @return The implementation lock.
     */
    protected Object getImplLock() { return implLock; }
}
