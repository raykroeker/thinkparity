/*
 * Created On: Apr 16, 2005
 * $Id$
 */
package com.thinkparity.codebase.model.document;

import com.thinkparity.codebase.model.artifact.ArtifactVersion;

/**
 * <b>Title:</b>thinkParity Document Version<br>
 * <b>Description:</b>The thinkParity document version encompasses the
 * document's binary content as well as the checksum; compression; and encoding
 * information used to store the content.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.4
 * @see DocumentVersionContent
 */
public class DocumentVersion extends ArtifactVersion {

    /** The content checksum. */
    private String checksum;
    
    /** The checksum algorithm. */
    private String checksumAlgorithm;

    /** The content size. */
    private Long size;

	/**
     * Create DocumentVersion.
     *
	 */
	public DocumentVersion() {
        super();
	}

    /**
     * Obtain the checksum
     *
     * @return The String.
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
     * Obtain size.
     *
     * @return A Integer.
     */
    public Long getSize() {
        return size;
    }

    /**
     * Set checksum.
     *
     * @param checksum The String.
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
     * Set size.
     *
     * @param size
     *		A Integer.
     */
    public void setSize(final Long size) {
        this.size = size;
    }
}
