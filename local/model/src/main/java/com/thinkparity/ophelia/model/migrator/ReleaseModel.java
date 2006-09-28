/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.migrator;

import java.util.List;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The parity bootstrap's release model.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReleaseModel extends AbstractModel<ReleaseModelImpl> {

    /**
     * Obtain the parity bootstrap's internal release model interface.
     *
     * @return The parity bootstrap's internal release model interface.
     */
    public static InternalReleaseModel getInternalModel(final Context context,
            final Environment environment, final Workspace workspace) {
        return new InternalReleaseModel(context, environment, workspace);
    }

    /**
     * Obtain the parity bootstrap's release model interface.
     *
     * @return The parity bootstrap's release model interface.
     */
    public static ReleaseModel getModel(final Environment environment,
            final Workspace workspace) {
        return new ReleaseModel(environment, workspace);
    }

    /** Create ReleaseModel. */
    protected ReleaseModel(final Environment environment,
            final Workspace workspace) {
        super(new ReleaseModelImpl(environment, workspace));
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
        synchronized(getImplLock()) {
            return getImpl().create(artifactId, groupId, version, libraries);
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
        synchronized(getImplLock()) {
            return getImpl().read(artifactId, groupId, version);
        }
    }

    /**
     * Read all releases.
     * 
     * @return A list of all releases.
     */
    public List<Release> readAll() {
        synchronized(getImplLock()) { return getImpl().readAll(); }
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
        synchronized(getImplLock()) { return getImpl().readLibraries(releaseId); }
    }
}
