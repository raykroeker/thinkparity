/*
 * Created On:  28-Jun-07 9:26:46 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.document.DocumentFileLock;
import com.thinkparity.ophelia.model.session.InternalSessionModel;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Delete extends ContainerDelegate {

    /** A container id. */
    private Long containerId;

    /**
     * Create Delete.
     *
     */
    public Delete() {
        super();
    }

    /**
     * Delete a container.
     *
     */
    public void delete() throws CannotLockException {
        final Container container = read(containerId);
        final List<Document> allDocuments = readAllDocuments(containerId);
        final Map<Document, DocumentFileLock> allDocumentsLocks = new HashMap<Document, DocumentFileLock>();
        final Map<DocumentVersion, DocumentFileLock> allDocumentVersionsLocks = new HashMap<DocumentVersion, DocumentFileLock>();
        try {
            allDocumentsLocks.putAll(lockDocuments(allDocuments));
            allDocumentVersionsLocks.putAll(lockDocumentVersions(allDocuments));

            if (isDistributed(container.getId())) {
                final InternalSessionModel sessionModel = getSessionModel();
                final Calendar deletedOn = sessionModel.readDateTime();
                // delete
                deleteLocal(container.getId(), allDocuments, allDocumentsLocks, allDocumentVersionsLocks);
                containerService.delete(getAuthToken(), container, deletedOn);
            } else {
                deleteLocal(container.getId(), allDocuments, allDocumentsLocks, allDocumentVersionsLocks);
            }
        } finally {
            try {
                releaseLocks(allDocumentsLocks.values());
            } finally {
                releaseLocks(allDocumentVersionsLocks.values());
            }
        }
    }

    /**
     * Set the container id.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void setContainerId(final Long containerId) {
        this.containerId = containerId;
    }
}
