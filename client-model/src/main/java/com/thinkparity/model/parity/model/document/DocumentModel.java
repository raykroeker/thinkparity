/*
 * Mar 6, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;

import com.thinkparity.codebase.assertion.Assert;

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
	 * Add a listener that is notified whenever a document is created.
	 * 
	 * @param creationListener
	 *            <code>com.thinkparity.model.parity.api.events.CreationListener</code>
	 */
	public void addCreationListener(final CreationListener creationListener) {
		synchronized(implLock) { impl.addCreationListener(creationListener); }
	}

	public void addNote(final Document document, final Note note)
			throws ParityException {
		synchronized(implLock) { impl.addNote(document, note); }
	}

	/**
	 * Add a listener that is notified whenever a document is updated.
	 * 
	 * @param updateListener
	 *            <code>com.thinkparity.model.parity.api.events.UpdateListener</code>
	 */
	public void addUpdateListener(final UpdateListener updateListener) {
		synchronized(implLock) { impl.addUpdateListener(updateListener); }
	}

	public void closeDocument(final Document document) throws ParityException {
		Assert.assertNotYetImplemented("DocumentModel.closeDocument()");
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
	 * @param documentFile
	 *            File content of the document
	 * @return The newly created document.
	 * @throws ParityException
	 */
	public Document createDocument(final Project project,
			final String name, final String description, final File documentFile)
			throws ParityException {
		synchronized(implLock) {
			return impl.createDocument(project, name, description, documentFile);
		}
	}

	/**
	 * Delete a document.
	 * 
	 * @param document
	 *            The document to delete.
	 * @throws ParityException
	 */
	public void deleteDocument(final Document document) throws ParityException {
		synchronized(implLock) { impl.deleteDocument(document); }
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
	public void exportDocument(final File file, final Document document)
			throws ParityException {
		synchronized(implLock) { impl.exportDocument(file, document); }
	}

	public Document getDocument(final File documentMetaDataFile)
			throws ParityException {
		synchronized(implLock) {
			return impl.getDocument(documentMetaDataFile);
		}
	}

	public StringBuffer getRelativePath(final Document document)
			throws ParityException {
		synchronized(implLock) { return impl.getRelativePath(document); }
	}

	public Boolean isDocumentClosable(final Document document)
			throws ParityException {
		synchronized(implLock) { return impl.isDocumentClosable(document); }
	}

	public Boolean isDocumentDeletable(final Document document)
			throws ParityException {
		synchronized(implLock) { return impl.isDocumentDeletable(document); }
	}

	public void openDocument(final Document document)
			throws ParityException {
		synchronized(implLock) { impl.openDocument(document); }
	}

	/**
	 * Use the document model to receive a document from another parity user.
	 * 
	 * @param xmppDocument
	 *            The xmpp document received from another parity user.
	 * @throws ParityException
	 */
	public void receiveDocument(final XMPPDocument xmppDocument)
			throws ParityException {
		synchronized(implLock) { impl.receiveDocument(xmppDocument); }
	}

	/**
	 * Remove a creation listener.
	 * @see <code>com.thinkparity.model.parity.api.document.DocumentApi#addCreationListener</code>
	 * @param creationListener <code>com.thinkparity.model.parity.api.events.CreationListener</code>
	 */
	public void removeCreationListener(final CreationListener creationListener) {
		synchronized(implLock) {
			impl.removeCreationListener(creationListener);
		}
	}

	public void removeUpdateListener(final UpdateListener updateListener) {
		synchronized(implLock) { impl.removeUpdateListener(updateListener); }
	}

	public void updateDocument(final Document document) throws ParityException {
		synchronized(implLock) { impl.updateDocument(document); }
	}
}
