/*
 * Apr 5, 2006
 */
package com.thinkparity.ophelia.model.util.smackx.packet.document;

import java.io.IOException;
import java.util.UUID;

import com.thinkparity.codebase.CompressionUtil;
import com.thinkparity.codebase.CompressionUtil.Level;

import com.thinkparity.ophelia.model.util.Base64;
import com.thinkparity.ophelia.model.util.smackx.packet.IQParity;

/**
 * An xmpp internet query containing a document.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class IQDocument extends IQParity {

    /**
     * A document content.
     * 
     */
    protected byte[] content;

    /**
     * A document name.
     * 
     */
    protected String name;

    /**
     * A document unique id.
     * 
     */
    protected UUID uniqueId;

    /**
     * A document version id.
     * 
     */
    protected Long versionId;

    /**
     * Create an IQDocument.
     * 
     * @param document
     *            A parity document.
     */
    protected IQDocument(final Action action) { super(action); }

    /**
     * Obtain the document content.
     * 
     * @return The document content bytes.
     */
    public byte[] getContent() { return content; }

    /**
     * Obtain the document name.
     * 
     * @return The document name.
     */
    public String getName() { return name; }

    /**
     * Obtain the document unique id.
     * 
     * @return The document unique id.
     */
    public UUID getUniqueId() { return uniqueId; }

    /**
     * Obtain the document version id.
     * 
     * @return The document version id.
     */
    public Long getVersionId() { return versionId; }

    /**
     * Set the document content.
     * 
     * @param content
     *            The document content bytes.
     */
    public void setContent(final byte[] content) {
        this.content = new byte[content.length];
        System.arraycopy(content, 0, this.content, 0, content.length);
    }

    /**
     * Set the document name.
     * 
     * @param name
     *            The document name.
     */
    public void setName(final String name) { this.name = name; }

    /**
     * Set the document unique id.
     * 
     * @param uniqueId
     *            The document unique id.
     */
    public void setUniqueId(final UUID uniqueId) { this.uniqueId = uniqueId; }

    /**
     * Set the document version id.
     * 
     * @param versionId
     *            The document version id.
     */
    public void setVersionId(final Long versionId) {
        this.versionId = versionId;
    }

    /**
     * Obtain the xml for the document.
     * 
     * @return An xml string.
     */
    protected String getDocumentXML() {
        try {
            return new StringBuffer()
                .append("<uuid>").append(uniqueId.toString()).append("</uuid>")
                .append("<versionid>").append(versionId.toString()).append("</versionid>")
                .append("<name>").append(name).append("</name>")
                .append("<bytes>").append(Base64.encodeBytes(CompressionUtil.compress(content, Level.Nine))).append("</bytes>")
                .toString();
        }
        catch(final IOException iox) { throw new RuntimeException(iox); }
    }
}
