/*
 * Feb 6, 2006
 */
package com.thinkparity.model.parity.model.io.handler;

import java.util.List;
import java.util.UUID;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.parity.model.io.IOHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface DocumentIOHandler extends IOHandler {

    /**
     * Create a document.
     * 
     * @param document
     *            A document.
     */
	public void create(final Document document);

	/**
     * Create a new document version.
     * 
     * @param version
     *            The version.
     * @param versionContent
     *            The version content.
     */
    public void createVersion(final DocumentVersion version,
            final DocumentVersionContent versionContent);

	/**
	 * Create a specific artifact version.
	 * 
	 * @param versionId
	 *            The artifact version id.
	 * @param version
	 *            The version.
	 * @param versionContent
	 *            The version content.
	 */
	public void createVersion(final Long versionId,
			final DocumentVersion version,
			final DocumentVersionContent versionContent);

	public void delete(final Long documentId);

	public void deleteVersion(final Long documentId, final Long versionId);

	public Document get(final Long documentId);

	public Document get(final UUID documentUniqueId);
	
    public DocumentVersion getVersion(final Long documentId,
			final Long versionId);

    public List<Document> list();

	public List<DocumentVersion> listVersions(final Long documentId);

    /**
     * Read the latest version for a document.
     * 
     * @param documentId
     *            A document id.
     * @return A document version.
     */
	public DocumentVersion readLatestVersion(final Long documentId);

	
	/**
     * Read the latest version content for a document.
     * 
     * @param documentId
     *            A document id.
     * @return A document version content.
     */
	public DocumentVersionContent readLatestVersionContent(final Long documentId);
	
	/**
     * Read a document's version content.
     * 
     * @param documentId
     *            A document id.
     * @param versionId
     *            A version id.
     * @return A document version content.
     */
    public DocumentVersionContent readVersionContent(final Long documentId, final Long versionId);
	
	public void update(final Document document);

	public void updateVersion(final DocumentVersion documentVersion);
}
