/*
 * Mar 6, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;
import java.util.UUID;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.CreationListener;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.model.note.Note;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.document.XMPPDocument;

/**
 * DocumentModel
 * @author raykroeker@gmail.com
 * @version 1.5.2.17
 */
public class DocumentModel {

	/**
	 * Obtain a handle to a document model.
	 * 
	 * @return The handle to the model.
	 */
	public static DocumentModel getModel() {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		final DocumentModel documentModel = new DocumentModel(workspace);
		return documentModel;
	}

	/**
	 * Internal implementation class.
	 */
	private final DocumentModelImpl impl;

	/**
	 * Synchronization lock for the implementation class.
	 */
	private final Object implLock;

	/**
	 * Create a DocumentModel [Singleton]
	 */
	private DocumentModel(final Workspace workspace) {
		super();
		this.impl = new DocumentModelImpl(workspace);
		this.implLock = new Object();
	}

	/**
	 * Add a creation listener for documents.
	 * 
	 * @param listener
	 *            The creation listener to add.
	 */
	public void addListener(final CreationListener listener) {
		synchronized(implLock) { impl.addListener(listener); }
	}

	/**
	 * Add a an update listener for documents.
	 * 
	 * @param listener
	 *            The update listener to add.
	 */
	public void addListener(final UpdateListener listener) {
		synchronized(implLock) { impl.addListener(listener); }
	}

	/**
	 * Add a note to a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param note
	 *            The note.
	 * @throws ParityException
	 */
	public Note addNote(final UUID documentId, final String note)
			throws ParityException {
		synchronized(implLock) { return impl.addNote(documentId, note); }
	}

	/**
	 * Import a document into a project. This will take a name, description and
	 * location of a document and copy the document into an internal store for
	 * the project, then returns the newly created document.
	 * 
	 * @param projectId
	 *            The parent project unique id.
	 * @param name
	 *            Name of the document you wish to import.
	 * @param description
	 *            Description of the document you wish to import.
	 * @param file
	 *            File content of the document
	 * @return The newly created document.
	 * @throws ParityException
	 */
	public Document create(final UUID projectId, final String name,
			final String description, final File file) throws ParityException {
		synchronized(implLock) {
			return impl.create(projectId, name, description, file);
		}
	}

	/**
	 * Create a new document version based upon an existing document. This will
	 * check the cache for updates to the document, write the updates to the
	 * document, then create a new version based upon that document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param action
	 *            The action causing the version creation.
	 * @param actionData
	 *            The data associated with the version creation action.
	 * @return The newly created version.
	 * @throws ParityException
	 */
	public DocumentVersion createVersion(final UUID documentId,
			final DocumentAction action, final DocumentActionData actionData)
			throws ParityException {
		synchronized(implLock) {
			return impl.createVersion(documentId, action, actionData);
		}
	}

	/**
	 * Delete a document.
	 * 
	 * @param document
	 *            The document to delete.
	 * @throws ParityException
	 */
	public void delete(final Document document) throws ParityException {
		synchronized(implLock) { impl.delete(document); }
	}

	/**
	 * Take the given document, and export it to the specified file. This will
	 * obtain the document's content, and save it to the file. Note that if file
	 * exists, it will be overwritten.
	 * 
	 * @param file
	 *            The file to export the document to.
	 * @param document
	 *            The document to export.
	 * @throws ParityException
	 */
	public void export(final Document document, final File file)
			throws ParityException {
		synchronized(implLock) { impl.export(document, file); }
	}

	/**
	 * Obtain a document with a specified id.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The document.
	 * @throws ParityException
	 */
	public Document get(final UUID documentId) throws ParityException {
		synchronized(implLock) { return impl.get(documentId); }
	}

	/**
	 * Obtain the document content for a given document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The document's content.
	 * @throws ParityException
	 */
	public DocumentContent getContent(final UUID documentId)
			throws ParityException {
		synchronized(implLock) { return impl.getContent(documentId); }
	}

	/**
	 * Obtain a document version.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version id.
	 * @return The document version.
	 * @throws ParityException
	 */
	public DocumentVersion getVersion(final UUID documentId,
			final String versionId) throws ParityException {
		synchronized(implLock) { return impl.getVersion(documentId, versionId); }
	}

	/**
	 * Obtain the content for a specific version.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version id.
	 * @return The content.
	 * @throws ParityException
	 */
	public DocumentVersionContent getVersionContent(final UUID documentId,
			final String versionId) throws ParityException {
		synchronized(implLock) {
			return impl.getVersionContent(documentId, versionId);
		}
	}

	/**
	 * Obtain a list of documents for a project.
	 * 
	 * @param projectId
	 *            The project unique id.
	 * @return A list of documents for a project.
	 * @throws ParityException
	 */
	public Collection<Document> list(final UUID projectId)
			throws ParityException {
		synchronized(implLock) { return impl.list(projectId); }
	}

	/**
	 * Obtain a list of document versions for a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return A list of document versions.
	 * @throws ParityException
	 */
	public Collection<DocumentVersion> listVersions(final UUID documentId)
			throws ParityException {
		synchronized(implLock) { return impl.listVersions(documentId); }
	}

	/**
	 * Lock a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	public void lock(final UUID documentId) throws ParityException {
		synchronized(implLock) { impl.lock(documentId); }
	}

	/**
	 * Move the document to an another project.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param destinationProjectId
	 *            The project unique id.
	 * @throws ParityException
	 */
	public void move(final UUID documentId, final UUID destinationProjectId)
			throws ParityException {
		synchronized(implLock) { impl.move(documentId, destinationProjectId); }
	}

	/**
	 * Open a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	public void open(final UUID documentId) throws ParityException {
		synchronized(implLock) { impl.open(documentId); }
	}

	/**
	 * Open a document version.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version to open.
	 * @throws ParityException
	 */
	public void openVersion(final UUID documentId, final String versionId) throws ParityException {
		synchronized(implLock) { impl.openVersion(documentId, versionId); }
	}

	/**
	 * Use the document model to receive a document from another parity user.
	 * 
	 * @param xmppDocument
	 *            The xmpp document received from another parity user.
	 * @throws ParityException
	 */
	public void receive(final XMPPDocument xmppDocument) throws ParityException {
		synchronized(implLock) { impl.receive(xmppDocument); }
	}

	/**
	 * Remove a creation listener.
	 * 
	 * @param listener
	 *            The creation listener to remove.
	 */
	public void removeListener(final CreationListener listener) {
		synchronized(implLock) { impl.removeListener(listener); }
	}

	/**
	 * Remove an update listener.
	 * 
	 * @param listener
	 *            The update listener to remove.
	 */
	public void removeListener(final UpdateListener listener) {
		synchronized(implLock) { impl.removeListener(listener); }
	}

	/**
	 * Unlock a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	public void unlock(final UUID documentId) throws ParityException {
		synchronized(implLock) { impl.unlock(documentId); }
	}

	/**
	 * Update the document.
	 * 
	 * @param document
	 *            The document to update.
	 * @throws ParityException
	 */
	public void update(final Document document) throws ParityException {
		synchronized(implLock) { impl.update(document); }
	}
}
