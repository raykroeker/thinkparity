/*
 * Created On:  27-Aug-07 12:56:00 PM
 */
package com.thinkparity.ophelia.model.container.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

/**
 * <b>Title:</b>thinkParity Ophelia Model Container Monitor Publish Data<br>
 * <b>Description:</b>The data produced by the model and consumed by the UI
 * when displaying publish progress.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RestoreBackupData {

    /** A list of all restored document versions. */
    private final List<DocumentVersion> allRestoredDocumentVersions;

    /** An intermediate number of bytes processed. */
    private Integer bytes;

    /** A container to delete. */
    private Container deleteContainer;

    /** A list of containers to delete. */
    private final List<Container> deleteContainers;

    /** The restore container. */
    private Container restoreContainer;

    /** The restore containers. */
    private final List<Container> restoreContainers;

    /** The restore documents. */
    private final List<Document> restoreDocuments;

    /** The restore document version. */
    private DocumentVersion restoreDocumentVersion;

    /** The list of document versions to restore. */
    private final List<DocumentVersion> restoreDocumentVersions;

    /**
     * Create PublishData.
     *
     */
    public RestoreBackupData() {
        super();
        this.allRestoredDocumentVersions = new ArrayList<DocumentVersion>();
        this.deleteContainers = new ArrayList<Container>();
        this.restoreContainers = new ArrayList<Container>();
        this.restoreDocuments = new ArrayList<Document>();
        this.restoreDocumentVersions = new ArrayList<DocumentVersion>();
    }

    /**
     * Obtain the bytes.
     *
     * @return A <code>Integer</code>.
     */
    public Integer getBytes() {
        return bytes;
    }

    /**
     * Obtain the deleteContainer.
     *
     * @return A <code>Container</code>.
     */
    public Container getDeleteContainer() {
        return deleteContainer;
    }

    /**
     * Obtain the delete containers.
     *
     * @return A <code>List<Container></code>.
     */
    public List<Container> getDeleteContainers() {
        return Collections.unmodifiableList(deleteContainers);
    }

    /**
     * Obtain the restoreContainer.
     *
     * @return A <code>Container</code>.
     */
    public Container getRestoreContainer() {
        return restoreContainer;
    }

    /**
     * Obtain the restoreContainers.
     *
     * @return A <code>List<Container></code>.
     */
    public List<Container> getRestoreContainers() {
        return Collections.unmodifiableList(restoreContainers);
    }

    /**
     * Obtain the restoreDocuments.
     *
     * @return A <code>List<Document></code>.
     */
    public List<Document> getRestoreDocuments() {
        return Collections.unmodifiableList(restoreDocuments);
    }

    /**
     * Obtain the restoreDocumentVersion.
     *
     * @return A <code>DocumentVersion</code>.
     */
    public DocumentVersion getRestoreDocumentVersion() {
        return restoreDocumentVersion;
    }

    /**
     * Obtain the restoreDocumentVersions.
     *
     * @return A <code>List<DocumentVersion></code>.
     */
    public List<DocumentVersion> getRestoreDocumentVersions() {
        return Collections.unmodifiableList(restoreDocumentVersions);
    }

    /**
     * Set the bytes.
     *
     * @param bytes
     *		A <code>Integer</code>.
     */
    public void setBytes(final Integer bytes) {
        this.bytes = bytes;
    }

    /**
     * Set the delete container.
     *
     * @param deleteContainer
     *		A <code>Container</code>.
     */
    public void setDeleteContainer(final Container deleteContainer) {
        this.deleteContainer = deleteContainer;
    }

    /**
     * Set the delete containers.
     * 
     * @param deleteContainers
     *            A <code>List<Container></code>.
     */
    public void setDeleteContainers(final List<Container> deleteContainers) {
        this.deleteContainers.clear();
        this.deleteContainers.addAll(deleteContainers);
    }

    /**
     * Set the restoreContainer.
     *
     * @param restoreContainer
     *		A <code>Container</code>.
     */
    public void setRestoreContainer(final Container restoreContainer) {
        this.restoreContainer = restoreContainer;
    }

    /**
     * Set the restore containers.
     * 
     * @param restoreContainers
     *            A <code>List<Container></code>.
     */
    public void setRestoreContainers(final List<Container> restoreContainers) {
        this.restoreContainers.clear();
        this.restoreContainers.addAll(restoreContainers);
    }

    /**
     * Set the restoreDocuments.
     *
     * @param restoreDocuments
     *		A <code>List<Document></code>.
     */
    public void setRestoreDocuments(final List<Document> restoreDocuments) {
        this.restoreDocuments.clear();
        this.restoreDocuments.addAll(restoreDocuments);
    }

    /**
     * Set the restoreDocumentVersion.
     *
     * @param restoreDocumentVersion
     *		A <code>DocumentVersion</code>.
     */
    public void setRestoreDocumentVersion(
            final DocumentVersion restoreDocumentVersion) {
        this.restoreDocumentVersion = restoreDocumentVersion;
    }

    /**
     * Set the restore document versions.
     * 
     * @param restoreDocumentVersions
     *            A <code>List<DocumentVersion></code>.
     */
    public void setRestoreDocumentVersions(final List<DocumentVersion> restoreDocumentVersions) {
        this.restoreDocumentVersions.clear();
        for (final DocumentVersion restoreDocumentVersion : restoreDocumentVersions) {
            if (this.allRestoredDocumentVersions.contains(restoreDocumentVersion)) {
                continue;
            } else {
                this.allRestoredDocumentVersions.add(restoreDocumentVersion);
                this.restoreDocumentVersions.add(restoreDocumentVersion);
            }
        }
    }
}
