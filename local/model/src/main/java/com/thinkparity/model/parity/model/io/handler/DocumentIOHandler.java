/*
 * Feb 6, 2006
 */
package com.thinkparity.model.parity.model.io.handler;

import java.util.List;
import java.util.UUID;

import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.parity.model.io.IOHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface DocumentIOHandler extends IOHandler {

	public void create(final Document document, final DocumentContent content);

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
	
	public DocumentContent getContent(final Long documentId);
	
	public DocumentVersion getLatestVersion(final Long documentId);

	public DocumentVersion getVersion(final Long documentId,
			final Long versionId);

	public DocumentVersionContent getVersionContent(final Long documentId,
			final Long versionId);
	
	public List<Document> list();
	
	public List<DocumentVersion> listVersions(final Long documentId);
	
	public void update(final Document document);
	
	public void updateContent(final DocumentContent content);
	
	public void updateState(final Long documentId, final ArtifactState state);
}
