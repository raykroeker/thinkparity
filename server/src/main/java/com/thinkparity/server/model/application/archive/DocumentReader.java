/*
 * Created On: Sep 16, 2006 12:57:18 PM
 */
package com.thinkparity.desdemona.model.archive;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

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

    /** Create DocumentReader. */
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
        return containerModel.readDocuments(containerId, containerVersionId);
    }

    /**
     * @see com.thinkparity.desdemona.model.archive.ArchiveReader#read(java.util.UUID)
     */
    @Override
    public Document read(final UUID uniqueId) {
        final Long documentId = readArtifactId(uniqueId);
        return documentModel.read(documentId);
    }

    /**
     * @see com.thinkparity.desdemona.model.archive.ArchiveReader#readVersions(java.util.UUID)
     */
    @Override
    public List<DocumentVersion> readVersions(final UUID uniqueId) {
        // bit of a wierd workflow to ensure the container is archived
        readArchivedArtifactId(containerUniqueId);
        final Long documentId = readArtifactId(uniqueId);
        return documentModel.readVersions(documentId);
    }
}
