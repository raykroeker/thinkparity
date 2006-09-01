/*
 * Created On: Mar 6, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.api.events.DocumentListener;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.audit.HistoryItem;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.progress.ProgressIndicator;
import com.thinkparity.model.parity.model.sort.ComparatorBuilder;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * The parity document interface.
 *
 * @author raykroeker@gmail.com
 * @version 1.5.2.43
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
     * @deprecated =>
     *             {@link com.thinkparity.model.parity.model.container.ContainerModel#archive(java.lang.Long)}
     */
    @Deprecated
    public File archive(final Long documentId) {
        throw Assert.createUnreachable("DocumentModel#archive(java.lang.Long) => ContainerModel#archive(java.lang.Long)");
    }

    /**
     * @deprecated =>
     *             {@link com.thinkparity.model.parity.model.container.ContainerModel#archive(Long, ProgressIndicator)}
     */
    @Deprecated
    public File archive(final Long documentId,
            final ProgressIndicator progressIndicator) {
        throw Assert.createUnreachable("DocumentModel#archive(java.lang.Long) => ContainerModel#archive(java.lang.Long,com.thinkparity.model.parity.model.progress.ProgressIndicator)");
    }

    /**
     * @deprecated =>
     *             {@link com.thinkparity.model.parity.model.container.ContainerModel#close(Long)}
     */
    @Deprecated
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
     */
	public Document create(final String name, final InputStream inputStream) {
		synchronized(getImplLock()) { return getImpl().create(name, inputStream); }
	}

	/**
	 * Create a new document version based upon an existing document. This will
	 * check the cache for updates to the document, write the updates to the
	 * document, then create a new version based upon that document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The newly created version.
	 */
	public DocumentVersion createVersion(final Long documentId) {
		synchronized(implLock) {
			return impl.createVersion(documentId);
		}
	}

    /**
	 * Obtain a document with a specified id.
	 * 
	 * @param documentId
	 *            The document id.
	 * @return The document.
	 */
	public Document get(final Long documentId) {
		synchronized(implLock) { return impl.read(documentId); }
	}

	/**
	 * Obtain a document version.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version id.
	 * @return The document version.
	 */
	public DocumentVersion getVersion(final Long documentId,
			final Long versionId) {
		synchronized(implLock) { return impl.readVersion(documentId, versionId); }
	}

    /**
	 * Obtain the content for a specific version.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version id.
	 * @return The content.
	 */
	public DocumentVersionContent getVersionContent(final Long documentId,
			final Long versionId) {
		synchronized(implLock) {
			return impl.getVersionContent(documentId, versionId);
		}
	}

    /**
     * Determine whether or not the draft of the document has been modified by
     * the user.
     * 
     * @param documentId
     *            The document id.
     * @return True if the draft of the document has been modified.
     */
    public Boolean isDraftModified(final Long documentId) {
        synchronized(implLock) { return impl.isDraftModified(documentId); }
    }

	/**
	 * Obtain a list of document versions for a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The list of document versions; ordered by the version id
	 *         ascending.
	 * 
	 * @see ComparatorBuilder
	 * @see #listVersions(Long, Comparator)
	 */
	public List<DocumentVersion> listVersions(final Long documentId) {
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
	 * 
	 * @see ComparatorBuilder
	 */
	public Collection<DocumentVersion> listVersions(final Long documentId,
			final Comparator<ArtifactVersion> comparator) {
		synchronized(implLock) {
			return impl.listVersions(documentId, comparator);
		}
	}

	/**
	 * Open a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 */
	public void open(final Long documentId) {
		synchronized(implLock) { impl.open(documentId); }
	}

	/**
	 * Open a document version.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version to open.
	 */
	public void openVersion(final Long documentId, final Long versionId) {
		synchronized(implLock) { impl.openVersion(documentId, versionId); }
	}

	/**
	 * Read the document's history.
	 * 
	 * @param documentId
	 *            The document id.
	 * @return A list of a history items.
	 */
	public List<DocumentHistoryItem> readHistory(final Long documentId) {
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
	 */
	public List<DocumentHistoryItem> readHistory(final Long documentId,
            final Comparator<HistoryItem> comparator) {
		synchronized(implLock) {
			return impl.readHistory(documentId, comparator);
		}
	}

    /**
     * Read the document's history.
     * 
     * @param documentId
     *            The document id.
     * @param comparator
     *            A history item comparator.
     * @param filter
     *            A history item filter.
     * @return A list of a history items.
     */
    public List<DocumentHistoryItem> readHistory(final Long documentId,
            final Comparator<HistoryItem> comparator,
            final Filter<? super HistoryItem> filter) {
        synchronized(implLock) {
            return impl.readHistory(documentId, comparator, filter);
        }
    }

    /**
     * Read the document's history.
     * 
     * @param documentId
     *            The document id.
     * @param filter
     *            A history filter.
     * @return A list of a history items.
     */
    public List<DocumentHistoryItem> readHistory(final Long documentId,
            final Filter<? super HistoryItem> filter) {
        synchronized(implLock) { return impl.readHistory(documentId, filter); }
    }

    /**
     * Obtain the latest document version.
     * 
     * @param documentId
     *            The document id.
     * @return The latest document version.
     */
    public DocumentVersion readLatestVersion(final Long documentId) {
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
    public void rename(final Long documentId, final String documentName) {
        synchronized(implLock) { impl.rename(documentId, documentName); }
    }

	/**
     * Update the draft of a document.
     * 
     * @param documentId
     *            The document id.
     * @param content
     *            The new content.
     */
    public void updateDraft(final Long documentId, final InputStream content) {
        synchronized(implLock) {
            impl.updateDraft(documentId, content);
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
