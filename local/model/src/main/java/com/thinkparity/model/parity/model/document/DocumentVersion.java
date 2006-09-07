/*
 * Created On: Apr 16, 2005
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import com.thinkparity.model.artifact.ArtifactVersion;

/**
 * <b>Title:</b>thinkParity Document Version<br>
 * <b>Description:</b>The thinkParity document version encompasses the
 * document's binary content as well as the checksum; compression; and encoding
 * information used to store the content.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 * @see DocumentVersionContent
 */
public class DocumentVersion extends ArtifactVersion {

    /** The content checksum. */
    private String checksum;

    /** The level of compression. */
    private Integer compression;

    /** The content encoding. */
    private String encoding;

	/** Create DocumentVersion. */
	public DocumentVersion() { super(); }

    /**
     * Obtain the checksum
     *
     * @return The String.
     */
    public String getChecksum() { return checksum; }

    /**
     * Obtain the compression
     *
     * @return The Integer.
     */
    public Integer getCompression() { return compression; }

    /**
     * Obtain the encoding
     *
     * @return The String.
     */
    public String getEncoding() { return encoding; }

    /**
     * Set checksum.
     *
     * @param checksum The String.
     */
    public void setChecksum(final String checksum) { this.checksum = checksum; }

    /**
     * Set compression.
     *
     * @param compression The Integer.
     */
    public void setCompression(final Integer compression) {
        this.compression = compression;
    }

    /**
     * Set encoding.
     *
     * @param encoding The String.
     */
    public void setEncoding(final String encoding) { this.encoding = encoding; }
}
