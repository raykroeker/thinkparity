/*
 * Created On:  17-Feb-07 12:35:02 AM
 */
package com.thinkparity.codebase.model.document;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DocumentDraft {

    private String checksum;

    private String checksumAlgorithm;

    private Long documentId;

    private Long size;

    /**
     * Create DocumentDraft.
     *
     */
    public DocumentDraft() {
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
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    /**
     * Set checksumAlgorithm.
     *
     * @param checksumAlgorithm
     *		A String.
     */
    public void setChecksumAlgorithm(String checksumAlgorithm) {
        this.checksumAlgorithm = checksumAlgorithm;
    }

    /**
     * Set documentId.
     *
     * @param documentId
     *		A Long.
     */
    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    /**
     * Set size.
     *
     * @param size
     *		A Long.
     */
    public void setSize(Long size) {
        this.size = size;
    }

}
