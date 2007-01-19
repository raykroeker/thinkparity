/*
 * Created On: Fri May 12 2006 09:11 PDT
 * $Id$
 */
package com.thinkparity.ophelia.model.migrator;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ReleaseIOHandler;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The parity bootstrap's release model implementation.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public final class ReleaseModelImpl extends Model implements
        ReleaseModel, InternalReleaseModel {

    /** The release xmpp io interface. */
    private ReleaseIOHandler releaseIO;

    /**
     * Create ReleaseModelImpl.
     *
     */
    public ReleaseModelImpl() {
        super();
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
        logger.logApiId();
        logger.logVariable("variable", artifactId);
        logger.logVariable("variable", groupId);
        logger.logVariable("variable", version);
        logger.logVariable("variable", libraries);
        final Long releaseId = releaseIO.create(
                artifactId, groupId, version, extractLibraryIds(libraries));
        return read(releaseId);
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
        return releaseIO.read(artifactId, groupId, version);
    }

    /**
     * Read all releases.
     * 
     * @return A list of releases.
     */
    public List<Release> readAll() {
        return releaseIO.readAll();
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
        return releaseIO.readLatest(artifactId, groupId);
    }

    /**
     * Read the libraries for the release.
     * 
     * @param releaseId
     *            A release id.
     * @return A list of libraries belonging to the release.
     */
    public List<Library> readLibraries(final Long releaseId) {
        return releaseIO.readLibraries(releaseId);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.releaseIO = IOFactory.getXMPP(workspace).createReleaseHandler();
    }

    /**
     * Extract a list of library ids from a list of libraries.
     * 
     * @param libraries
     *            A list of libraries.
     * @return A list of library ids.
     */
    private List<Long> extractLibraryIds(final List<Library> libraries) {
        final List<Long> libraryIds = new LinkedList<Long>();
        for(final Library library : libraries) { libraryIds.add(library.getId()); }
        return libraryIds;
    }

    /**
     * Read a release.
     * 
     * @param releaseId
     *            A release id.
     * @return A release.
     */
    private Release read(final Long releaseId) {
        return releaseIO.read(releaseId);
    }
}
