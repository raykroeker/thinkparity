/*
 * Created On:  27-Aug-07 12:56:00 PM
 */
package com.thinkparity.ophelia.model.container.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.document.DocumentVersion;

/**
 * <b>Title:</b>thinkParity Ophelia Model Container Monitor Publish Data<br>
 * <b>Description:</b>The data produced by the model and consumed by the UI
 * when displaying publish progress.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PublishData {

    /** An intermediate value for a number of bytes processed. */
    private Integer bytes;

    /** A document version. */
    private DocumentVersion documentVersion;

    /** A list of document versions. */
    private final List<DocumentVersion> documentVersions;

    /**
     * Create PublishData.
     *
     */
    public PublishData() {
        super();
        this.documentVersions = new ArrayList<DocumentVersion>();
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
     * Obtain the document version.
     *
     * @return A <code>DocumentVersion</code>.
     */
    public DocumentVersion getDocumentVersion() {
        return documentVersion;
    }

    /**
     * Obtain the document versions.
     *
     * @return A <code>List<DocumentVersion></code>.
     */
    public List<DocumentVersion> getDocumentVersions() {
        return Collections.unmodifiableList(documentVersions);
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
     * Set the document version.
     *
     * @param documentVersion
     *		A <code>DocumentVersion</code>.
     */
    public void setDocumentVersion(final DocumentVersion documentVersion) {
        this.documentVersion = documentVersion;
    }

    /**
     * Set the document versions.
     * 
     * @param documentVersions
     *            A <code>List<DocumentVersion></code>.
     */
    public void setDocumentVersions(final List<DocumentVersion> documentVersions) {
        this.documentVersions.clear();
        this.documentVersions.addAll(documentVersions);
    }
}
