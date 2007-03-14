/*
 * Created On: Sep 17, 2006 2:55:25 PM
 */
package com.thinkparity.desdemona.model.backup;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.desdemona.model.session.Session;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InternalBackupModel extends BackupModel {

    /**
     * Create InternalBackupModel.
     * 
     * @param context
     *            A thinkParity model <code>Context</code>.
     * @param session
     *            A thinkParity <code>Session</code>.
     */
    InternalBackupModel(final Context context, final Session session) {
        super(session);
    }

    /**
     * Obtain a container reader for the archive.
     * 
     * @return A container archive reader.
     */
    public BackupReader<Container, ContainerVersion> getContainerReader(
            final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().getContainerReader(userId);
        }
    }

    public Statistics readStatistics(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readStatisitcs(userId);
        }
    }

    /**
     * Obtain a document archive reader.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return An <code>ArchiveReader&lt;Document, DocumentVersion&gt;</code>.
     */
    public BackupReader<Document, DocumentVersion> getDocumentReader(
            final JabberId userId, final UUID containerUniqueId,
            final Long containerVersionId) {
        synchronized (getImplLock()) {
            return getImpl().getDocumentReader(userId, containerUniqueId,
                    containerVersionId);
        }
    }
}
