/*
 * Mar 6, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;

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
	public void addListener(final CreationListener creationListener) {
		synchronized(implLock) { impl.addListener(creationListener); }
	}

	/**
	 * Add a listener that is notified whenever a document is updated.
	 * 
	 * @param updateListener
	 *            <code>com.thinkparity.model.parity.api.events.UpdateListener</code>
	 */
	public void addListener(final UpdateListener updateListener) {
		synchronized(implLock) { impl.addListener(updateListener); }
	}

	public void addNote(final Document document, final Note note)
			throws ParityException {
		synchronized(implLock) { impl.addNote(document, note); }
	}

	public void close(final Document document) throws ParityException {
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

	public String getRelativePath(final Document document) {
		synchronized(implLock) { return impl.getRelativePath(document); }
	}

	public Boolean isClosable(final Document document)
			throws ParityException {
		synchronized(implLock) { return impl.isClosable(document); }
	}

	public Boolean isDeletable(final Document document)
			throws ParityException {
		synchronized(implLock) { return impl.isDeletable(document); }
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

	public void open(final Document document) throws ParityException {
		synchronized(implLock) { impl.open(document); }
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
	 * @see <code>com.thinkparity.model.parity.api.document.DocumentApi#addCreationListener</code>
	 * @param creationListener <code>com.thinkparity.model.parity.api.events.CreationListener</code>
	 */
	public void removeListener(final CreationListener creationListener) {
		synchronized(implLock) {
			impl.removeListener(creationListener);
		}
	}

	public void removeListener(final UpdateListener updateListener) {
		synchronized(implLock) { impl.removeListener(updateListener); }
	}

	public void update(final Document document) throws ParityException {
		synchronized(implLock) { impl.update(document); }
	}
}
