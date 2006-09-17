/*
 * Created On: Aug 31, 2006 6:17:22 PM
 */
package com.thinkparity.ophelia.model.index.document;

import com.thinkparity.codebase.model.document.Document;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DocumentIndexEntry {

    private final Long containerId;

    private final Document document;
    
    public DocumentIndexEntry(final Document document) {
        this(null, document);
    }

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
