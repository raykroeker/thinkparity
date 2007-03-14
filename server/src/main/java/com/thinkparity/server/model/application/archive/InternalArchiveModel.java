/*
 * Created On: Sep 17, 2006 2:55:25 PM
 */
package com.thinkparity.desdemona.model.archive;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.desdemona.model.session.Session;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InternalArchiveModel extends ArchiveModel {

    InternalArchiveModel(final Context context, final Session session) {
        super(session);
    }

    /**
     * Obtain a container reader for the archive.
     * 
     * @return A container archive reader.
     */
    public ArchiveReader<Container, ContainerVersion> getContainerReader(
            final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().getContainerReader(userId);
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
    public ArchiveReader<Document, DocumentVersion> getDocumentReader(
            final JabberId userId, final UUID containerUniqueId) {
        synchronized (getImplLock()) {
            return getImpl().getDocumentReader(userId, containerUniqueId);
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
    public ArchiveReader<Document, DocumentVersion> getDocumentReader(
            final JabberId userId, final UUID containerUniqueId,
            final Long containerVersionId) {
        synchronized (getImplLock()) {
            return getImpl().getDocumentReader(userId, containerUniqueId,
                    containerVersionId);
        }
    }

    /**
     * Obtain a model factory for the archive.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     * @return An archive's <code>ClientModelFactory</code>.
     */
    public ClientModelFactory getModelFactory(final JabberId archiveId) {
        synchronized (getImplLock()) {
            return getImpl().getModelFactory(archiveId);
        }
    }

    public Boolean isArchiveOnline(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().isArchiveOnline(userId); 
        }
    }
}
