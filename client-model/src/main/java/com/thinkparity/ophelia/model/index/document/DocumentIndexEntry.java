/*
 * Created On: Aug 31, 2006 6:17:22 PM
 */
package com.thinkparity.ophelia.model.index.document;

import com.thinkparity.codebase.model.document.Document;

/**
 * <b>Title:</b>thinkParity OpheliaModel Index Document Entry<br>
 * <b>Description:</b>A wrapper around a container id and document that allows
 * the document index to create/delete entries.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
public final class DocumentIndexEntry {

    /** A container id <code>Long</code>. */
    private final Long containerId;

    /** A <code>Document</code>. */
    private final Document document;

    /**
     * Create DocumentIndexEntry.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param document
     *            A <code>Document</code>.
     */
    public DocumentIndexEntry(final Long containerId, final Document document) {
        super();
        this.containerId = containerId;
        this.document = document;
    }

    /**
     * Obtain the containerId
     *
     * @return The Long.
     */
    public Long getContainerId() {
        return containerId;
    }

    /**
     * Obtain the document
     *
     * @return The Document.
     */
    public Document getDocument() {
        return document;
    }

    
}
