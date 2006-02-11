/*
 * Nov 19, 2005
 */
package com.thinkparity.model.parity.model.document;


/**
 * Represents the content snapshot of a document version.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersionContent {

	/**
	 * Snapshot of the document content.
	 * 
	 */
	private DocumentContent documentContent;

	/**
	 * The document id.
	 * 
	 */
	private Long documentId;

	/**
	 * The version id.
	 * 
	 */
	private Long versionId;

	/**
	 * Create a DocumentVersionContent.
	 */
	public DocumentVersionContent() { super(); }

	/**
	 * Obtain the document content.
	 * 
	 * @return The document content.
	 */
	public DocumentContent getDocumentContent() { return documentContent; }

	/**
	 * Obtain the document id.
	 * 
	 * @return The document id.
	 */
	public Long getDocumentId() { return documentId; }

	/**
	 * Obtain the version id.
	 * 
	 * @return The version id.
	 */
	public Long getVersionId() { return versionId; }

	/**
	 * Set the document content.
	 * 
	 * @param documentContent
	 *            The document content.
	 */
	public void setDocumentContent(final DocumentContent documentContent) {
		this.documentContent = documentContent;
	}

	/**
	 * @param documentId The documentId to set.
	 */
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	/**
	 * Set the version id.
	 * 
	 * @param versionId
	 *            The version id.
	 */
	public void setVersionId(final Long versionId) {
		this.versionId = versionId;
	}
}
