/*
 * Created On:  16-Feb-07 9:15:14 PM
 */
package com.thinkparity.codebase.model.container;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerDraftDocument {

    private String checksum;

    private String checksumAlgorithm;

    private Long containerDraftId;

    private Long documentId;

    private Long size;

    /**
     * Create DocumentDraft.
     *
     */
    public ContainerDraftDocument() {
        super();
    }

    /**
     * Obtain checksum.
     *
     * @return A String.
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * Obtain checksumAlgorithm.
     *
     * @return A String.
     */
    public String getChecksumAlgorithm() {
        return checksumAlgorithm;
    }

    /**
     * Obtain containerDraftId.
     *
     * @return A Long.
     */
    public Long getContainerDraftId() {
        return containerDraftId;
    }

    /**
     * Obtain documentId.
     *
     * @return A Long.
     */
    public Long getDocumentId() {
        return documentId;
    }

    /**
     * Obtain size.
     *
     * @return A Long.
     */
    public Long getSize() {
        return size;
    }

    /**
     * Set checksum.
     *
     * @param checksum
     *		A String.
     */
    public void setChecksum(final String checksum) {
        this.checksum = checksum;
    }

    /**
     * Set checksumAlgorithm.
     *
     * @param checksumAlgorithm
     *		A String.
     */
    public void setChecksumAlgorithm(final String checksumAlgorithm) {
        this.checksumAlgorithm = checksumAlgorithm;
    }

    /**
     * Set containerDraftId.
     *
     * @param containerDraftId
     *		A Long.
     */
    public void setContainerDraftId(final Long containerDraftId) {
        this.containerDraftId = containerDraftId;
    }

    /**
     * Set documentId.
     *
     * @param documentId
     *		A Long.
     */
    public void setDocumentId(final Long documentId) {
        this.documentId = documentId;
    }

    /**
     * Set size.
     *
     * @param size
     *		A Long.
     */
    public void setSize(final Long size) {
        this.size = size;
    }
}