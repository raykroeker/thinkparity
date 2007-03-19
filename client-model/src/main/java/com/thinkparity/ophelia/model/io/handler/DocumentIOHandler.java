/*
 * Feb 6, 2006
 */
package com.thinkparity.ophelia.model.io.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamOpener;
import com.thinkparity.codebase.model.stream.StreamUploader;

import com.thinkparity.ophelia.model.io.IOHandler;

/**
 * <b>Title:</b>thinkParity OpheliaModel Document IO Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.16
 */
public interface DocumentIOHandler extends IOHandler {

    /**
     * Create a document.
     * 
     * @param document
     *            A document.
     */
	public void create(final Document document);

    // TODO-javadoc DocumentIOHandler#createVersion
    public void createVersion(final DocumentVersion version,
            final InputStream stream, final Integer bufferSize);

	public void delete(final Long documentId);

	public void deleteVersion(final Long documentId, final Long versionId);

	public DocumentVersion getVersion(final Long documentId,
			final Long versionId);

	public List<DocumentVersion> listVersions(final Long documentId);

	/**
     * Open an input stream to the document's content.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param opener
     *            A <code>StreamOpener</code>.
     */
    public void openStream(final Long documentId, final Long versionId,
            final StreamOpener opener) throws IOException;

	public List<Document> read();
	
    public Document read(final Long documentId);

    public Document read(final UUID documentUniqueId);

	/**
     * Read the latest version for a document.
     * 
     * @param documentId
     *            A document id.
     * @return A document version.
     */
	public DocumentVersion readLatestVersion(final Long documentId);

    /**
     * Read a document version's size.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param versionId
     *            A document version id <code>Long</code>.
     * @return A document version's size <code>Integer</code>.
     */
    public Long readVersionSize(final Long documentId, final Long versionId);

    /**
     * Update a document.
     * 
     * @param document
     *            A <code>Document</code>.
     */
	public void update(final Document document);

    /**
     * Upload a document version.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param upload
     *            An <code>Upload</code> target.
     */
    public void uploadVersion(final Long documentId, final Long versionId,
            final StreamUploader uploader) throws IOException;
}
