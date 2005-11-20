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
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.document.XMPPDocument;

/**
 * DocumentModel
 * @author raykroeker@gmail.com
 * @version 1.1
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
	 * @param document
	 *            The document to add the note to.
	 * @param subject
	 *            The subject of the note.
	 * @param content
	 *            The content of the note (Optional).
	 * @throws ParityException
	 */
	public Note addNote(final Document document, final String note)
			throws ParityException {
		synchronized(implLock) { return impl.addNote(document, note); }
	}

	/**
	 * Import a document into a project. This will take a name, description and
	 * location of a document and copy the document into an internal store for
	 * the project, then returns the newly created document.
	 * 
	 * @param project
	 *            Project within which the new document will reside.
	 * @param name
	 *            Name of the document you wish to import.
	 * @param description
	 *            Description of the document you wish to import.
	 * @param file
	 *            File content of the document
	 * @return The newly created document.
	 * @throws ParityException
	 */
	public Document create(final Project project, final String name,
			final String description, final File file) throws ParityException {
		synchronized(implLock) {
			return impl.create(project, name, description, file);
		}
	}

	/**
	 * Create a new document version based upon an existing document. This will
	 * check the cache for updates to the document, write the updates to the
	 * document, then create a new version based upon that document.
	 * 
	 * @param document
	 *            The document to create the version for.
	 * @param action
	 *            The action causing the version creation.
	 * @param actionData
	 *            The data associated with the version creation action.
	 * @return The newly created version.
	 * @throws ParityException
	 */
	public DocumentVersion createVersion(final Document document,
			final DocumentAction action, final DocumentActionData actionData)
			throws ParityException {
		synchronized(implLock) {
			return impl.createVersion(document, action, actionData);
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
	 * @param id
	 *            The id of the document.
	 * @return The document
	 * @throws ParityException
	 */
	public Document get(final UUID id) throws ParityException {
		synchronized(implLock) { return impl.get(id); }
	}

	/**
	 * Obtain the document content for a given document.
	 * 
	 * @param document
	 *            The document.
	 * @return The document's content.
	 * @throws ParityException
	 */
	public DocumentContent getContent(final Document document)
			throws ParityException {
		synchronized(implLock) { return impl.getContent(document); }
	}

	/**
	 * Obtain the content for a specific version.
	 * 
	 * @param version
	 *            The version.
	 * @return The content.
	 * @throws ParityException
	 */
	public DocumentVersionContent getVersionContent(
			final DocumentVersion version) throws ParityException {
		synchronized(implLock) { return impl.getVersionContent(version); }
	}

	/**
	 * Obtain a list of documents for a project.
	 * 
	 * @param project
	 *            The project which contains the documents.
	 * @return A list of documents for a project.
	 * @throws ParityException
	 */
	public Collection<Document> list(final Project project)
			throws ParityException {
		synchronized(implLock) { return impl.list(project); }
	}

	/**
	 * Obtain a list of document versions for a document.
	 * 
	 * @param document
	 *            The document which contains the versions.
	 * @return The list of document versions.
	 * @throws ParityException
	 */
	public Collection<DocumentVersion> listVersions(final Document document)
			throws ParityException {
		synchronized(implLock) { return impl.listVersions(document); }
	}

	/**
	 * Move the document to an another project.
	 * 
	 * @param document
	 *            The document to move.
	 * @param destination
	 *            The project to move the document to.
	 * @throws ParityException
	 */
	public void move(final Document document, final Project destination) throws ParityException {
		synchronized(implLock) { impl.move(document, destination); }
	}

	/**
	 * Open a document.
	 * 
	 * @param document
	 *            The document to open.
	 * @throws ParityException
	 */
	public void open(final Document document) throws ParityException {
		synchronized(implLock) { impl.open(document); }
	}

	/**
	 * Open a document version.
	 * 
	 * @param version
	 *            The version to open.
	 * @throws ParityException
	 */
	public void openVersion(final DocumentVersion version) throws ParityException {
		synchronized(implLock) { impl.openVersion(version); }
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
