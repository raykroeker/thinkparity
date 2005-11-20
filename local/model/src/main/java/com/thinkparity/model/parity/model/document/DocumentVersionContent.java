/*
 * Nov 19, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.UUID;

/**
 * Represents the content snapshot of a document version.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersionContent {

	/**
	 * Snapshot of the document content.
	 */
	private DocumentContent snapshot;

	/**
	 * Document id.
	 */
	private UUID documentId;

	/**
	 * Version id.
	 */
	private String versionId;

	/**
	 * Create a DocumentVersionContent.
	 */
	public DocumentVersionContent(final DocumentContent contentSnapshot,
			final UUID documentId, final String versionId) {
		super();
		this.documentId = documentId;
		this.snapshot = contentSnapshot;
		this.versionId = versionId;
	}

	/**
	 * Obtain the content snapshot.
	 * 
	 * @return The content snapshot.
	 */
	public DocumentContent getSnapshot() { return snapshot; }

	/**
	 * Obtain the document id.
	 * 
	 * @return The document id.
	 */
	public UUID getDocumentId() { return documentId; }

	/**
	 * Obtain the version id.
	 * @return The version id.
	 */
	public String getVersionId() { return versionId; }
}
