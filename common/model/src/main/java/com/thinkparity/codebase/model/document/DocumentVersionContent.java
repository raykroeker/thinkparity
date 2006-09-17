/*
 * Created On: Nov 19, 2005
 * $Id$
 */
package com.thinkparity.codebase.model.document;

/**
 * <b>Title:</b>thinkParity Document Version Content<br>
 * <b>Description:</b>The document version content represents only the content
 * of a document version. The content is separated to maintain a high level of
 * performance when reading lists of document versions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersionContent {

	/** The content. */
    private byte[] content;

    /** The document version. */
	private DocumentVersion version;

	/** Create DocumentVersionContent. */
	public DocumentVersionContent() { super(); }

    /**
     * Obtain the content
     *
     * @return The byte[].
     */
    public byte[] getContent() { return content; }

    /**
     * Obtain the documentVersion
     *
     * @return The DocumentVersion.
     */
    public DocumentVersion getVersion() { return version; }

    /**
     * Set content.
     *
     * @param content The byte[].
     */
    public void setContent(final byte[] content) { this.content = content; }

    /**
     * Set documentVersion.
     *
     * @param documentVersion The DocumentVersion.
     */
    public void setVersion(final DocumentVersion version) {
        this.version = version;
    }
}
