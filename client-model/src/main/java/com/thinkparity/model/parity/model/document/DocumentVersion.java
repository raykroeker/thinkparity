/*
 * Apr 16, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.UUID;

import com.thinkparity.model.parity.api.ParityObjectVersion;

/**
 * DocumentVersion
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersion extends ParityObjectVersion {

	/**
	 * The action causing the version creation.
	 */
	private final DocumentAction action;

	/**
	 * Additional action data for the version.
	 */
	private final DocumentActionData actionData;

	/**
	 * Reference to the main document.
	 */
	private UUID documentId;

	/**
	 * Reference to the document snapshot representd by this version.
	 */
	private final Document snapshot;

	/**
	 * Create a DocumentVersion
	 * 
	 * @param document
	 *            The document upon which to base the new version.
	 * @param versionId
	 *            The version id of the new version.
	 */
	public DocumentVersion(final UUID documentId, final String versionId,
			final Document snapshot, final DocumentAction action,
			final DocumentActionData actionData) {
		super(versionId);
		this.action = action;
		this.actionData = actionData;
		this.documentId = documentId;
		this.snapshot = snapshot;
	}

	/**
	 * Obtain the action for this version.
	 * 
	 * @return The action for this version.
	 */
	public DocumentAction getAction() { return action; }

	/**
	 * Obtain the action data for this version.
	 * 
	 * @return The action data for this version.
	 */
	public DocumentActionData getActionData() { return actionData; }

	/**
	 * Obtain the document.
	 * 
	 * @return The document this version belongs to.
	 */
	public UUID getDocumentId() { return documentId; }

	/**
	 * Obtain the snapshot of the document for this version.
	 * 
	 * @return The snapshot;
	 */
	public Document getSnapshot() { return snapshot; }

	/**
	 * Set the document reference.
	 * @param document The document reference.
	 */
	public void setDocumentId(final UUID documentId) { this.documentId = documentId; }
}
