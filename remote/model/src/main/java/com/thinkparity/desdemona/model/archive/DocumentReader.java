/*
 * Created On: Sep 16, 2006 12:57:18 PM
 */
package com.thinkparity.desdemona.model.archive;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DocumentReader extends ArchiveReader<Document, DocumentVersion> {

    /** An internal thinkParity client container interface. */
    private final InternalContainerModel containerModel;

    /** A container unique id. */
    private final UUID containerUniqueId;

    /** A container version id. */
    private final Long containerVersionId;

    /** An internal thinkParity client document interface. */
    private final InternalDocumentModel documentModel;

    /**
     * Create DocumentReader.
     * 
     * @param modelFactory
     *            A <code>ClientModelFactory</code>.
     * @param containerUniqueId
     *            A container unique id <code>UUID</code>.
     */
    DocumentReader(final ClientModelFactory modelFactory,
            final UUID containerUniqueId) {
        this(modelFactory, containerUniqueId, null);
    }

    /**
     * Create DocumentReader.
     * 
     * @param modelFactory
     *            A <code>ClientModelFactory</code>.
     * @param containerUniqueId
     *            A container unique id <code>UUID</code>.
     * @param containerVersionId
     *            A container version id <code>Long</code>.
     */
    DocumentReader(final ClientModelFactory modelFactory,
            final UUID containerUniqueId, final Long containerVersionId) {
        super(modelFactory);
        this.containerModel = getContainerModel();
        this.containerUniqueId = containerUniqueId;
        this.containerVersionId = containerVersionId;
        this.documentModel = getDocumentModel();
    }

    /**
     * @see com.thinkparity.desdemona.model.archive.ArchiveReader#read()
     */
    @Override
    public List<Document> read() {
        final Long containerId = readArchivedArtifactId(containerUniqueId);
        if (null == containerId) {
            return Collections.emptyList();
        } else {
            return containerModel.readDocuments(containerId, containerVersionId);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.archive.ArchiveReader#read(java.util.UUID)
     */
    @Override
    public Document read(final UUID uniqueId) {
        // bit of a wierd workflow to ensure the container is archived
        final Long containerId = readArchivedArtifactId(containerUniqueId);
        if (null == containerId) {
            return null;
        } else {
            final Long documentId = readArtifactId(uniqueId);
            return documentModel.read(documentId);
        }
    }

    @Override
    public Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId, final Long versionId) {
        return Collections.emptyMap();
    }

    /**
     * @see com.thinkparity.desdemona.model.archive.ArchiveReader#readVersion(java.util.UUID, java.lang.Long)
     *
     */
    @Override
    public DocumentVersion readVersion(final UUID uniqueId, final Long versionId) {
        final Long containerId = readArchivedArtifactId(containerUniqueId);
        if (null == containerId) {
            return null;
        } else {
            final Long documentId = artifactModel.readId(uniqueId);
            if (null == documentId) {
                return null;
            } else {
                return documentModel.readVersion(documentId, versionId);
            }
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.archive.ArchiveReader#readVersionDeltas(java.util.UUID)
     *
     */
    @Override
    public Map<DocumentVersion, Delta> readVersionDeltas(final UUID uniqueId) {
        // bit of a wierd workflow to ensure the container is archived
        final Long containerId = readArchivedArtifactId(containerUniqueId);
        if (null == containerId) {
            return Collections.emptyMap();
        } else {
            return containerModel.readDocumentVersionDeltas(containerId,
                    containerVersionId);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.archive.ArchiveReader#readVersionDeltas(java.util.UUID, java.lang.Long)
     *
     */
    @Override
    public Map<DocumentVersion, Delta> readVersionDeltas(final UUID uniqueId,
            final Long compareToVersionId) {
        // bit of a wierd workflow to ensure the container is archived
        final Long containerId = readArchivedArtifactId(containerUniqueId);
        if (null == containerId) {
            return Collections.emptyMap();
        } else {
            return containerModel.readDocumentVersionDeltas(containerId,
                    containerVersionId, compareToVersionId);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.archive.ArchiveReader#readVersions(java.util.UUID)
     */
    @Override
    public List<DocumentVersion> readVersions(final UUID uniqueId) {
        // bit of a wierd workflow to ensure the container is archived
        final Long containerId = readArchivedArtifactId(containerUniqueId);
        if (null == containerId) {
            return Collections.emptyList();
        } else {
            return containerModel.readDocumentVersions(containerId,
                    containerVersionId);
        }
    }
}
