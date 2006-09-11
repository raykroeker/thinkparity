/*
 * Generated On: Sep 04 06 04:29:31 PM
 */
package com.thinkparity.desdemona.model.archive;

import com.thinkparity.codebase.model.artifact.ArtifactVersion;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity Archive Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
class ArchiveModelImpl<T extends ArtifactVersion> extends AbstractModelImpl {

    /** Create ArchiveModelImpl. */
    ArchiveModelImpl() {
        super();
    }

    /**
     * Backup an artifact.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     */
    void backup(final T version) {
        logApiId();
        logVariable("version", version);
    }

    /**
     * Create ArchiveModelImpl.
     *
     * @param session
     *      The user's session.
     */
    ArchiveModelImpl(final Session session) {
        super(session);
    }

    /**
     * Start the archive.  This involves starting all of the archive
     * databases.
     *
     */
    void start() {
        logApiId();
    }

    /**
     * Stop the archive.  This involves stopping all of the archive
     * databases.
     *
     */
    void stop() {
        logApiId();
    }
}
