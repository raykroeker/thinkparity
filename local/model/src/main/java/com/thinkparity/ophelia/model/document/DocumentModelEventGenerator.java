/*
 * Created April 22, 2006 - 10:36
 * DocumentModelEventGenerator.java
 */
package com.thinkparity.ophelia.model.document;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.events.DocumentEvent;

/**
 * A convenience class used to create events for the document listener.
 *
 * @author raymond@thinkparity.com
 */
class DocumentModelEventGenerator {

    /** The source to use when genrating the events. */
    private final DocumentEvent.Source source;

    /**
     * Create a DocumentModelEventGenerator.
     *
     * @param source
     *      The source to use for creating the events.
     */
    DocumentModelEventGenerator(final DocumentEvent.Source source) {
        super();
        this.source = source;
    }

    /**
     * Generate a document event for a document.
     *
     * @param document
     *      A document.
     * @return A document event.
     */
    DocumentEvent generate(final Document document) {
        return new DocumentEvent(source, document);
    }

    /**
     * Generate a document event for a document version.
     * 
     * @param document
     *            A document
     * @param documentVersion
     *            A document version.
     * @return A document event.
     */
    DocumentEvent generate(final Document document, final DocumentVersion documentVersion) {
        return new DocumentEvent(source, document, documentVersion);
    }

    /**
     * Generate a document event.
     * 
     * @param user
     *            A user.
     * @param document
     *            A document.
     * @return A document event.
     */
    DocumentEvent generate(final User user, final Document document) {
        return new DocumentEvent(source, user, document);
    }

    /**
     * Generate a document event for a document version.
     * 
     * @param user
     *            A user.
     * @param document
     *            A document
     * @param documentVersion
     *            A document version.
     * @return A document event.
     */
   DocumentEvent generate(final User user, final Document document,
            final DocumentVersion documentVersion) {
       return new DocumentEvent(source, user, document, documentVersion);
   }
}