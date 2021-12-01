/*
 * Jun 5, 2005
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;


/**
 * A document event describes the local and remote events generated by modifications
 * to the document via the model interface.
 *
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class DocumentEvent {

    /** The source document. */
	private final Document document;

    /** The source document version. */
    private final DocumentVersion documentVersion;

    /** The event source. */
    private final Source source;

    /** User info. */
    private final User user;

    /**
     * Create a DocumentEvent.
     *
     * @param source
     *      The source.
     */
    public DocumentEvent(final Source source, final Document document) {
        this(source, null, document, null);
    }

    /**
     * Create a DocumentEvent.
     * 
     * @param source
     *            The event source.
     * @param document
     *            A document.
     * @param documentVersion
     *            A document version.
     */
    public DocumentEvent(final Source source, final Document document,
            final DocumentVersion documentVersion) {
        this(source, null, document, documentVersion);
    }

    /**
     * Create a DocumentEvent.
     * 
     * @param source
     *            The event source.
     * @param user
     *            The user.
     * @param document
     *            The document.
     */
    public DocumentEvent(final Source source, final User user,
            final Document document) {
        this(source, user, document, null);
    }

    /**
     * Create a DocumentEvent.
     * 
     * @param source
     *            The event source.
     * @param user
     *            The user.
     * @param document
     *            The document.
     * @param documentVersion
     *            The document version.
     */
	public DocumentEvent(final Source source, final User user,
            final Document document, final DocumentVersion documentVersion) {
        super();
		this.document = document;
        this.documentVersion = documentVersion;
        this.source = source;
        this.user = user;
	}

	/**
	 * Obtain the source.
     *
	 * @return A document.
	 */
	public Document getDocument() { return document; }

    /**
     * Obtain the source version.
     *
     * @return A document version.
     */
    public DocumentVersion getDocumentVersion() { return documentVersion; }

    /**
     * Obtain the user.
     * 
     * @return The user.
     */
    public User getUser() { return user; }

    /**
     * Determine whether or not the event is a local event.
     * 
     * @return True if the event is a remote event.
     */
    public Boolean isLocal() { return Source.LOCAL == source; }

    /**
     * Determine whether or not the event is a remote event.
     * 
     * @return True if the event is a remote event.
     */
    public Boolean isRemote() { return Source.REMOTE == source; }

    /** A definition of event sources. */
    public enum Source { LOCAL, REMOTE }
}
