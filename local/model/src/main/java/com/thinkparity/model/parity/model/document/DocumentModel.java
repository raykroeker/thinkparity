/*
 * Created On: Mar 6, 2005
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Comparator;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.DocumentListener;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.progress.ProgressIndicator;
import com.thinkparity.model.parity.model.sort.ComparatorBuilder;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.JabberId;

/**
 * The parity document interface.
 *
 * @author raykroeker@gmail.com
 * @version 
 */
public class DocumentModel {

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
	protected DocumentModel(final Workspace workspace) {
		super();
		this.impl = new DocumentModelImpl(workspace);
		this.implLock = new Object();
	}

    /**
     * Add a document listener.  If the listener is already registered
     * nothing is done.
     *
     * @param l
     *      The document listener.
     */
	public void addListener(final DocumentListener l) {
		synchronized(implLock) { impl.addListener(l); }
	}


    /**
	 * Archive a document.
	 * 
	 * @param documentId
	 *            The document to archive.
	 * @throws ParityException
	 */
	public File archive(final Long documentId) {
		throw Assert.createUnreachable("DocumentModel#archive(java.lang.Long) => ContainerModel#archive(java.lang.Long)");
	}

	/**
	 * Archive a document.
	 * 
	 * @param documentId
	 *            The document to archive.
	 * @throws ParityException
	 */
	public File archive(final Long documentId,
			final ProgressIndicator progressIndicator) {
        throw Assert.createUnreachable("DocumentModel#archive(java.lang.Long) => ContainerModel#archive(java.lang.Long,com.thinkparity.model.parity.model.progress.ProgressIndicator)");
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
	public void close(final Long documentId) {
        throw Assert.createUnreachable("DocumentModel#close(java.lang.Long) => ContainerModel#close(java.lang.Long)");
	}

	/**
     * Create a document and attach it to a container. This will take a name,
     * and input stream of a file and create a document.
     * 
     * @param name
     *            The document name.
     * @param inputStream
     *            The document content's input stream.
     * @return The document.
     * @throws ParityException
     */
	public Document create(final Long containerId, final String name,
            final InputStream inputStream) throws ParityException {
		synchronized(implLock) { return impl.create(containerId, name, inputStream); }
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
	    throw Assert.createUnreachable("DocumentModel#delete(java.lang.Long)) => ContainerModel#removeDocument(java.lang.Long)");
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
     * Determine whether or not the document has been distributed.
     * 
     * @param documentId
     *            The document id.
     * @return True if the document has been distributed; false otherwise.
     */
    public Boolean isDistributed(final Long documentId) {
        synchronized(implLock) { return impl.isDistributed(documentId); }
    }

    /**
     * Determine whether or not the working version of the document is equal to
     * the last version.
     * 
     * @param documentId
     *            The document id.
     * @return True if the working version is different from the last version.
     * @throws ParityException
     */
    public Boolean isWorkingVersionEqual(final Long documentId)
            throws ParityException {
        synchronized(implLock) { return impl.isWorkingVersionEqual(documentId); }
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
        throw Assert.createUnreachable("DocumentModel#list() => ContainerModel#readDocuments(java.lang.Long)");
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
        throw Assert.createUnreachable("DocumentModel#list(Comparator<Artifact>) => ContainerModel#readDocuments(java.lang.Long,Comparator<Artifact>)");
	}

	/**
     * Obtain a filtered and sorted list of documents.
     * 
     * @param comparator
     *            The comparator.
     * @param filter
     *            The document filter.
     * @return A list of documents.
     * @throws ParityException
     */
	public Collection<Document> list(final Comparator<Artifact> comaprator,
			final Filter<? super Artifact> filter) throws ParityException {
        throw Assert.createUnreachable("DocumentModel#list(Comparator<Artifact>,Filter<? super Artifact>) => ContainerModel#readDocuments(java.lang.Long,Comparator<Artifact>,Filter<? super Artifact>)");
	}

	/**
     * Obtain a filtered list of documents.
     * 
     * @param filter
     *            The document filter.
     * @return A list of documents.
     * @throws ParityException
     */
	public Collection<Document> list(final Filter<? super Artifact> filter)
			throws ParityException {
        throw Assert.createUnreachable("DocumentModel#list(Filter<? super Artifact>) => ContainerModel#readDocuments(java.lang.Long,Filter<? super Artifact>)");
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
     * Publish a document.  Publishing a document involves the following
     * process:<ol>
     *  <li>Check if the working version differs from the latest version. If<ul>
     *      <li>True:  Create a new version.
     *      <li>False:  Do nothing.
     *  <li>Send the latest version to all team members.
     * </ol>
     *
     * @param documentId
     *      The document id.
     */
    public void publish(final Long documentId) throws ParityException {
        throw Assert.createUnreachable("DocumentModel#publish(java.lang.Long) => ContainerModel#publish(java.lang.Long)");
    }

    /**
     * Reactivate a document.
     * 
     * @param documentId
     *            The document id.
     * @throws ParityException
     */
    public void reactivate(final Long documentId) throws ParityException {
        throw Assert.createUnreachable("DocumentModel#reactivate(java.lang.Long) => ContainerModel#reactivate(java.lang.Long)");
    }

	/**
	 * Read the document's history.
	 * 
	 * @param documentId
	 *            The document id.
	 * @return A list of a history items.
	 * @throws ParityException
	 */
	public Collection<HistoryItem> readHistory(final Long documentId)
			throws ParityException {
		synchronized(implLock) { return impl.readHistory(documentId); }
	}

	/**
	 * Read the document's history.
	 * 
	 * @param documentId
	 *            The document id.
	 * @param comparator
	 *            The sort to use when returning the history.
	 * @return A list of a history items.
	 * @throws ParityException
	 */
	public Collection<HistoryItem> readHistory(final Long documentId,
			final Comparator<HistoryItem> comparator)
			throws ParityException {
		synchronized(implLock) {
			return impl.readHistory(documentId, comparator);
		}
	}

	/**
     * Obtain the latest document version.
     * 
     * @param documentId
     *            The document id.
     * @return The latest document version.
     * @throws ParityException
     */
    public DocumentVersion readLatestVersion(final Long documentId)
            throws ParityException {
        synchronized(implLock) {
            return impl.readLatestVersion(documentId);
        }
    }

	/**
	 * Remove a document listener.
	 * 
	 * @param l
	 *        The document listener.
	 */
	public void removeListener(final DocumentListener l) {
		synchronized(implLock) { impl.removeListener(l); }
	}

    /**
     * Rename a document.
     *
     * @param documentId
     *      A document id.
     * @param documentName
     *      A document name.
     */
    public void rename(final Long documentId, final String documentName)
            throws ParityException {
        synchronized(implLock) { impl.rename(documentId, documentName); }
    }

    /**
     * Share a document with a user.  The user will become a member of the document
     * team.
     * 
     * @param documentId
     *      The document id.
     * @param jabberId
     *      The user id to add.
     */
    public void share(final Long documentId, final JabberId jabberId) throws ParityException {
        throw Assert.createUnreachable("DocumentModel#share(java.lang.Long,com.thinkparity.model.xmpp.JabberId) => ContainerModel#share(java.lang.Long,com.thinkparity.model.xmpp.JabberId)");
    }

    /**
     * Update the working version of a document.
     * 
     * @param documentId
     *            The document id.
     * @param updateFile
     *            The new working version.
     * @throws ParityException
     */
    public void updateWorkingVersion(final Long documentId,
            final File updateFile) throws ParityException {
        synchronized(implLock) {
            impl.updateWorkingVersion(documentId, updateFile);
        }
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
