/*
 * Created On: Fri May 12 2006 09:11 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import java.util.List;

import com.thinkparity.model.parity.ParityException;
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
     * @param name
     *            A name.
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
     * Delete a release.
     *
     * @param releaseId
     *      A release id.
     */
    public void delete(final Long releaseId) {
        synchronized(implLock) { impl.delete(releaseId); }
    }

    /**
     * Download the latest release.
     *
     * @param artifactId
     *      An artifact id.
     * @param groupId
     *      A group id.
     */
    public void download(final String artifactId, final String groupId)
            throws ParityException {
        synchronized(implLock) { impl.download(artifactId, groupId); }
    }

    /**
     * Determine whether a release is available.
     *
     * @param artifactId
     *      An artifact id.
     * @param groupId
     *      A group id.
     * @param version
     *      A version
     * @return True if a release is available; false otherwise.
     */
    public Boolean isAvailable(final String artifactId, final String groupId,
            final String version) {
        synchronized(implLock) {
            return impl.isAvailable(artifactId, groupId, version);
        }
    }

    /**
     * Migrate to the latest release.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param version
     *            A version.
     */
    public void migrate(final String artifactId, final String groupId,
            final String version) throws ParityException {
        synchronized(implLock) {
            impl.migrate(artifactId, groupId,version);
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
