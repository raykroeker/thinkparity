/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.migrator;

import java.util.List;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Release Model<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface ReleaseModel {

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
            final String version, final List<Library> libraries);

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
            final String version);

    /**
     * Read all releases.
     * 
     * @return A list of all releases.
     */
    public List<Release> readAll();

    /**
     * Read the latest release.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @return A release.
     */
    public Release readLatest(final String artifactId, final String groupId);

    /**
     * Read a list of libraries belonging to a release.
     * 
     * @param releaseId
     *            A release id.
     * @return A list of libraries.
     */
    public List<Library> readLibraries(final Long releaseId);
}
