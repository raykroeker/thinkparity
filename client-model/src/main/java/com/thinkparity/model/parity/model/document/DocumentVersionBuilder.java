/*
 * Jun 18, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.UUID;

import com.thinkparity.model.parity.ParityException;



/**
 * DocumentVersionBuilder
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersionBuilder {

	/**
	 * Singleton instance of the builder.
	 * @see DocumentVersionBuilder#singletonLock
	 */
	private static final DocumentVersionBuilder singleton;

	/**
	 * Synchronization lock.
	 * @see DocumentVersionBuilder#singleton
	 */
	private static final Object singletonLock;

	static {
		singleton = new DocumentVersionBuilder();
		singletonLock = new Object();
	}

	/**
	 * Create a new version.
	 * 
	 * @param document
	 *            The document.
	 * @param content
	 *            The document content.
	 * @param action
	 *            The action.
	 * @param actionData
	 *            The action data.
	 * @return The new version.
	 * @throws ParityException
	 */
	public static DocumentVersion create(final Document document,
			final DocumentContent content, final DocumentAction action,
			final DocumentActionData actionData) throws ParityException {
		synchronized(singletonLock) {
			return singleton.createImpl(document, content, action, actionData);
		}
	}

	/**
	 * Obtain a reference to a version.
	 * 
	 * @param contentSnapshot
	 *            The content snapshot.
	 * @param documentId
	 *            The document id.
	 * @param versionId
	 *            The version id.
	 * @param snapshot
	 *            The snapshot.
	 * @param action
	 *            The action.
	 * @param actionData
	 *            The action data.
	 * @return A reference to a version.
	 */
	public static DocumentVersion get(final DocumentContent contentSnapshot,
			final UUID documentId, final String versionId,
			final Document snapshot, final DocumentAction action,
			final DocumentActionData actionData) {
		synchronized(singletonLock) {
			return singleton.getVersionImpl(contentSnapshot, documentId,
					versionId, snapshot, action, actionData);
		}
	}

	/**
	 * Create a DocumentVersionBuilder [Builder, Singleton]
	 */
	private DocumentVersionBuilder() { super(); }

	/**
	 * Create a reference to a new version.
	 * 
	 * @param document
	 *            The document.
	 * @param content
	 *            The document content.
	 * @param action
	 *            The action.
	 * @param actionData
	 *            The action data.
	 * @return The new version.
	 * @throws ParityException
	 */
	private DocumentVersion createImpl(final Document document,
			final DocumentContent content, final DocumentAction action,
			final DocumentActionData actionData) throws ParityException {
		final String newVersion = createNextVersion(document);
		return new DocumentVersion(content, document.getId(), newVersion,
				document, action, actionData);
	}

	/**
	 * Obtain the next version in the sequence for a document.
	 * 
	 * @param document
	 *            The document to obtain the version for.
	 * @return The next version in the sequence.
	 */
	private String createNextVersion(final Document document)
			throws ParityException {
		final DocumentModel documentModel = DocumentModel.getModel();
		final Integer numberOfVersions = documentModel.listVersions(document).size();
		return new StringBuffer("v").append(numberOfVersions + 1).toString();
	}

	/**
	 * Obtain a reference to a version.
	 * 
	 * @param contentSnapshot
	 *            The content snapshot.
	 * @param documentId
	 *            The document id.
	 * @param versionId
	 *            The version id.
	 * @param snapshot
	 *            The snapshot.
	 * @param action
	 *            The action.
	 * @param actionData
	 *            The action data.
	 * @return The version reference.
	 */
	private DocumentVersion getVersionImpl(
			final DocumentContent contentSnapshot, final UUID documentId,
			final String versionId, final Document snapshot,
			final DocumentAction action, final DocumentActionData actionData) {
		return new DocumentVersion(contentSnapshot, documentId, versionId,
				snapshot, action, actionData);
	}
}
