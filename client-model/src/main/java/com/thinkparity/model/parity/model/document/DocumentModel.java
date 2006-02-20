/*
 * Mar 6, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.CreationListener;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.sort.ComparatorBuilder;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.document.XMPPDocument;

/**
 * DocumentModel
 * @author raykroeker@gmail.com
 * @version 1.5.2.17
 * 
 * TODO The history should highlight updates\new versions as well as the document
 * list.
 *    - ie the versions should also contain flags
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
	 * Obtain a handle to an internal document model.
	 * 
	 * @param context
	 *            The parity context.
	 * @return The internal document model.
	 */
	public static InternalDocumentModel getInternalModel(final Context context) {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		final InternalDocumentModel internalModel = new InternalDocumentModel(workspace, context);
		return internalModel;
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
	protected DocumentModel(final Workspace workspace) {
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
	 * Close a document.
	 * 
	 * @param documentId
	 *            The document id.
	 * @throws NotTrueAssertion
	 *             <ul>
	 *             <li>If the user is offline.
	 *             <li>If the logged in user is not the key holder.
	 *             </ul>
	 * @throws ParityException
	 */
	public void close(final Long documentId) throws ParityException {
		synchronized(implLock) { impl.close(documentId); }
	}

	/**
	 * Import a document. This will take a name, description and location of a
	 * document and copy the document into an internal store, then returns the
	 * newly created document.
	 * 
	 * @param name
	 *            Name of the document you wish to import.
	 * @param description
	 *            Description of the document you wish to import.
	 * @param file
	 *            File content of the document
	 * @return The newly created document.
	 * @throws ParityException
	 */
	public Document create(final String name, final String description,
			final File file) throws ParityException {
		synchronized(implLock) { return impl.create(name, description, file); }
	}

	/**
	 * Create a new document version based upon an existing document. This will
	 * check the cache for updates to the document, write the updates to the
	 * document, then create a new version based upon that document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The newly created version.
	 * @throws ParityException
	 */
	public DocumentVersion createVersion(final Long documentId)
			throws ParityException {
		synchronized(implLock) {
			return impl.createVersion(documentId);
		}
	}

	/**
	 * Delete a document.
	 * 
	 * @param document
	 *            The document unique id.
	 * @throws ParityException
	 */
	public void delete(final Long documentId) throws ParityException {
		synchronized(implLock) { impl.delete(documentId); }
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
	 *            The document id.
	 * @return The document.
	 * @throws ParityException
	 */
	public Document get(final Long documentId) throws ParityException {
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
	public DocumentContent getContent(final Long documentId)
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
	public DocumentVersion getVersion(final Long documentId,
			final Long versionId) throws ParityException {
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
	public DocumentVersionContent getVersionContent(final Long documentId,
			final Long versionId) throws ParityException {
		synchronized(implLock) {
			return impl.getVersionContent(documentId, versionId);
		}
	}

	/**
	 * Obtain a list of documents.
	 * 
	 * @return A list of documents sorted by name.
	 * @throws ParityException
	 * 
	 * @see ComparatorBuilder
	 * @see #list(Long, Comparator)
	 */
	public Collection<Document> list() throws ParityException {
		synchronized(implLock) { return impl.list(); }
	}

	/**
	 * Obtain a list of sorted documents.
	 * 
	 * @param comparator
	 *            The comparator.
	 * @return A sorted list of documents.
	 * @throws ParityException
	 * 
	 * @see ComparatorBuilder
	 */
	public Collection<Document> list(final Comparator<Artifact> comparator)
			throws ParityException {
		synchronized(implLock) { return impl.list(comparator); }
	}

	/**
	 * Obtain a list of document versions for a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The list of document versions; ordered by the version id
	 *         ascending.
	 * @throws ParityException
	 * 
	 * @see ComparatorBuilder
	 * @see #listVersions(Long, Comparator)
	 */
	public Collection<DocumentVersion> listVersions(final Long documentId)
			throws ParityException {
		synchronized(implLock) { return impl.listVersions(documentId); }
	}

	/**
	 * Obtain a list of document versions for a document; ordered by the
	 * specified comparator.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param comparator
	 *            The document version sorter.
	 * @return The list of document versions.
	 * @throws ParityException
	 * 
	 * @see ComparatorBuilder
	 */
	public Collection<DocumentVersion> listVersions(final Long documentId,
			final Comparator<ArtifactVersion> comparator)
			throws ParityException {
		synchronized(implLock) {
			return impl.listVersions(documentId, comparator);
		}
	}

	/**
	 * Open a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	public void open(final Long documentId) throws ParityException {
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
	public void openVersion(final Long documentId, final Long versionId)
			throws ParityException {
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

	// USED BY THE ABSTRACT MODEL
	public void update(final Document document) throws ParityException {
		synchronized(implLock) { impl.update(document); }
	}

	/**
	 * Obtain the implementation.
	 * 
	 * @return The implementation.
	 */
	protected DocumentModelImpl getImpl() { return impl; }

	/**
	 * Obtain the implementation synchronization lock.
	 * 
	 * @return The implementation synchrnoization lock.
	 */
	protected Object getImplLock() { return implLock; }
}
