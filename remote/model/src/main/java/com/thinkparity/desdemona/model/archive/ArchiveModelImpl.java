/*
 * Generated On: Sep 04 06 04:29:31 PM
 */
package com.thinkparity.desdemona.model.archive;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.jabber.JabberId;
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

    /** A map of the archive sessions. */
    private static final Map<JabberId, Object> SESSIONS;

    static {
        SESSIONS = new HashMap<JabberId, Object>();
    }

    /** Create ArchiveModelImpl. */
    ArchiveModelImpl() {
        super();
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
     * Start the archive.  This involves starting all of the archive
     * databases.
     *
     */
    void start() {
        logApiId();
//        try {
//            final UserModel userModel = getUserModel();
//            final List<User> users = userModel.read();
//            List<JabberId> archiveIds;
//            for (final User user : users) {
//                archiveIds = userModel.readArchiveIds(user.getId());
//                for (final JabberId archiveId : archiveIds) {
//                    start(archiveId);
//                }
//            }
//        } catch (final Throwable t) {
//            throw translateError(t);
//        }
    }

    /**
     * Stop the archive.  This involves stopping all of the archive
     * databases.
     *
     */
    void stop() {
        logApiId();
//        try {
//            final UserModel userModel = getUserModel();
//            final List<User> users = userModel.read();
//            List<JabberId> archiveIds;
//            for (final User user : users) {
//                archiveIds = userModel.readArchiveIds(user.getId());
//                for (final JabberId archiveId : archiveIds) {
//                    stop(archiveId);
//                }
//            }
//        } catch (final Throwable t) {
//            throw translateError(t);
//        }
    }

    /**
     * Start an individual archive.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     */
    private void start(final JabberId archiveId) {
        synchronized (SESSIONS) {
            final com.thinkparity.ophelia.model.session.SessionModel
                    sessionModel = com.thinkparity.ophelia.model.session.SessionModel.getModel();
            sessionModel.login(getUserModel().readArchiveCredentials(archiveId));
            SESSIONS.put(archiveId, sessionModel.readSession());
        }
    }

    /**
     * Start an individual archive.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     */
    private void stop(final JabberId archiveId) {
        synchronized (SESSIONS) {
            final com.thinkparity.ophelia.model.session.SessionModel
                    sessionModel =
                        com.thinkparity.ophelia.model.session.SessionModel.getModel();
            sessionModel.logout();
        }
    }
}
