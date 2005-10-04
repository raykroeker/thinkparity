/*
 * Mar 6, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.parity.api.events.CreationListener;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.api.note.Note;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.user.User;

/**
 * DocumentModel
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentModel {

	/**
	 * Handle to the document api's implementation.
	 */
	private static final DocumentModelImpl impl = new DocumentModelImpl();

	/**
	 * Add a listener that is notified whenever a document is created.
	 * 
	 * @param creationListener
	 *            <code>com.thinkparity.model.parity.api.events.CreationListener</code>
	 */
	public static void addCreationListener(
			final CreationListener creationListener) {
		impl.addCreationListener(creationListener);
	}

	/**
	 * Add a listener that is notified whenever a document is updated.
	 * 
	 * @param updateListener
	 *            <code>com.thinkparity.model.parity.api.events.UpdateListener</code>
	 */
	public static void addUpdateListener(final UpdateListener updateListener) {
		impl.addUpdateListener(updateListener);
	}

	public static void closeDocument(final Document document)
			throws ParityException {
		Assert.assertNotYetImplemented("DocumentModel.closeDocument()");
	}

	public static void deleteDocument(final Document document)
			throws ParityException {
		Assert.assertNotYetImplemented("DocumentModel.deleteDocument()");
	}

	/**
	 * Take the given document, and export it to the specified file. This will
	 * obtain the document's content, and save it to the file.
	 * 
	 * @param file
	 *            <code>java.io.File</code>
	 * @param document
	 *            <code>com.thinkparity.model.parity.api.document.Document</code>
	 * @throws ParityException
	 */
	public static void exportDocument(final File file, final Document document)
			throws ParityException {
		impl.exportDocument(file, document);
	}

	public static Document getDocument(final File documentMetaDataFile) throws ParityException {
		return impl.getDocument(documentMetaDataFile);
	}

	/**
	 * Obtain a handle to a document model.
	 * 
	 * @return The handle to the model.
	 */
	public static DocumentModel getModel() { return new DocumentModel(); }

	public static StringBuffer getRelativePath(final Document document)
			throws ParityException {
		return impl.getRelativePath(document);
	}

	public static Boolean isDocumentClosable(final Document document)
			throws ParityException {
		return impl.isDocumentClosable(document);
	}

	public static Boolean isDocumentDeletable(final Document document)
			throws ParityException {
		return impl.isDocumentDeletable(document);
	}

	public static void openDocument(final Document document)
			throws ParityException {
		impl.openDocument(document);
	}

	/**
	 * Remove a creation listener.
	 * @see <code>com.thinkparity.model.parity.api.document.DocumentApi#addCreationListener</code>
	 * @param creationListener <code>com.thinkparity.model.parity.api.events.CreationListener</code>
	 */
	public static void removeCreationListener(
			final CreationListener creationListener) {
		impl.removeCreationListener(creationListener);
	}

	public static void removeUpdateListener(final UpdateListener updateListener) {
		impl.removeUpdateListener(updateListener);
	}

	public static void sendDocument(final Collection<User> users,
			final Document document) throws ParityException {
		impl.sendDocument(users, document);
	}

	public static void updateDocument(final Document document)
			throws ParityException {
		impl.updateDocument(document);
	}

	/**
	 * Handle to the document api implementation.
	 */
	private final DocumentModelImpl impl2;

	/**
	 * Synchronization lock for access to impl2.
	 */
	private final Object impl2Lock = new Object();

	/**
	 * Handle to the parity workspace.
	 */
	private final Workspace workspace;

	/**
	 * Create a DocumentModel [Singleton]
	 */
	private DocumentModel() {
		super();
		this.workspace = WorkspaceModel.getModel().getWorkspace();
		this.impl2 = new DocumentModelImpl(workspace);
	}

	public void addNote(final Document document, final Note note)
			throws ParityException {
		synchronized(impl2Lock) { impl2.addNote(document, note); }
	}

	/**
	 * Import a document version into a project. This will take an existing
	 * version of a document and create it within the root project.
	 * 
	 * @param documentVersion
	 *            <code>com.thinkparity.model.parity.api.document.DocumentVersion</code>
	 * @return <code>com.thinkparity.model.parity.api.document.Document</code>
	 */
	public Document createDocument(final DocumentVersion documentVersion)
			throws ParityException {
		return impl.createDocument(documentVersion);
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
		synchronized(impl2Lock) {
			return impl2.createDocument(project, name, description, documentFile);
		}
	}
}
