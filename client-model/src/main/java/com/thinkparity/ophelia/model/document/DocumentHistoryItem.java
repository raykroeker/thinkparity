/*
 * Created On: Jul 6, 2006 8:58:01 PM
 */
package com.thinkparity.ophelia.model.document;

import com.thinkparity.ophelia.model.audit.HistoryItem;

/**
 * <b>Title:</b>thinkParity Document History Item<br>
 * <b>Description:</b>A thinkParity document history implementation.
 * 
 * @author raymond@thinkparity.com
 * @see HistoryItem
 */
public class DocumentHistoryItem extends HistoryItem {

    /** A document id. */
    private Long documentId;

    /** A document version id. */
    private Long versionId;

    /** Create DocumentHistoryItem. */
    public DocumentHistoryItem() { super(); }

    /**
     * Obtain the documentId
     *
     * @return The Long.
     */
    public Long getDocumentId() { return documentId; }

    /**
     * Obtain the versionId
     *
     * @return The Long.
     */
    public Long getVersionId() { return versionId; }

    /**
     * Determine if the version id is set.
     * 
     * @return True if the version id is set; false otherwise.
     */
    public Boolean isSetVersionId() { return null != versionId; }

    /**
     * Set documentId.
     *
     * @param documentId The Long.
     */
    public void setDocumentId(final Long documentId) {
        this.documentId = documentId;
    }

    /**
     * Set versionId.
     *
     * @param versionId The Long.
     */
    public void setVersionId(final Long versionId) {
        this.versionId = versionId;
    }
}
