/*
 * Created On: Mar 6, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.document.DocumentVersionContent;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.audit.HistoryItem;
import com.thinkparity.ophelia.model.events.DocumentListener;
import com.thinkparity.ophelia.model.util.Printer;
import com.thinkparity.ophelia.model.util.ProgressIndicator;
import com.thinkparity.ophelia.model.util.filter.Filter;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The parity document interface.
 *
 * @author raykroeker@gmail.com
 * @version 1.5.2.43
 */
public class DocumentModel extends AbstractModel<DocumentModelImpl> {

    /**
	 * Obtain a handle to an internal document model.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @param context
	 *            The parity context.
	 * @return The internal document model.
	 */
	public static InternalDocumentModel getInternalModel(final Context context,
            final Environment environment, final Workspace workspace) {
		return new InternalDocumentModel(context, environment, workspace);
	}

	/**
	 * Obtain a handle to a document model.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @return The handle to the model.
	 */
	public static DocumentModel getModel(final Environment environment,
            final Workspace workspace) {
		return new DocumentModel(environment, workspace);
	}

	/**
	 * Create a DocumentModel [Singleton]
	 */
	protected DocumentModel(final Environment environment,
            final Workspace workspace) {
		super(new DocumentModelImpl(environment, workspace));
	}

	/**
     * Add a document listener.  If the listener is already registered
     * nothing is done.
     *
     * @param l
     *      The document listener.
     */
	public void addListener(final DocumentListener l) {
		synchronized(getImplLock()) { getImpl().addListener(l); }
	}

    /**
     * @deprecated =>
     *             {@link com.thinkparity.ophelia.model.container.ContainerModel#archive(java.lang.Long)}
     */
    @Deprecated
    public File archive(final Long documentId) {
        throw Assert.createUnreachable("DocumentModel#archive(java.lang.Long) => ContainerModel#archive(java.lang.Long)");
    }


    /**
     * @deprecated =>
     *             {@link com.thinkparity.ophelia.model.container.ContainerModel#archive(Long, ProgressIndicator)}
     */
    @Deprecated
    public File archive(final Long documentId,
            final ProgressIndicator progressIndicator) {
        throw Assert.createUnreachable("DocumentModel#archive(java.lang.Long) => ContainerModel#archive(java.lang.Long,com.thinkparity.model.parity.model.progress.ProgressIndicator)");
    }

    /**
     * @deprecated =>
     *             {@link com.thinkparity.ophelia.model.container.ContainerModel#close(Long)}
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
		synchronized(getImplLock()) {
			return getImpl().createVersion(documentId);
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
		synchronized(getImplLock()) { return getImpl().read(documentId); }
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
		synchronized(getImplLock()) { return getImpl().readVersion(documentId, versionId); }
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
        synchronized(getImplLock()) { return getImpl().isDraftModified(documentId); }
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
	public List<DocumentVersion> readVersions(final Long documentId) {
		synchronized(getImplLock()) { return getImpl().listVersions(documentId); }
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
	public Collection<DocumentVersion> readVersions(final Long documentId,
			final Comparator<ArtifactVersion> comparator) {
		synchronized(getImplLock()) {
			return getImpl().listVersions(documentId, comparator);
		}
	}

	/**
	 * Open a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 */
	public void open(final Long documentId) {
		synchronized(getImplLock()) { getImpl().open(documentId); }
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
		synchronized(getImplLock()) { getImpl().openVersion(documentId, versionId); }
	}

	/**
     * Print a document draft.
     * 
     * @param documentId
     *            A document id.
     * @param printer
     *            A document printer.
     */
	public void printDraft(final Long documentId, final Printer printer) {
	    synchronized (getImplLock()) {
            getImpl().printDraft(documentId, printer);
        }
    }

	/**
     * Print a document version.
     * 
     * @param documentId
     *            A document id.
     * @param versionId
     *            A document version id.
     * @param printer
     *            A document printer.
     */
    public void printVersion(final Long documentId, final Long versionId,
            final Printer printer) {
        synchronized (getImplLock()) {
            getImpl().printVersion(documentId, versionId, printer);
        }
    }

    /**
	 * Read the document's history.
	 * 
	 * @param documentId
	 *            The document id.
	 * @return A list of a history items.
	 */
	public List<DocumentHistoryItem> readHistory(final Long documentId) {
		synchronized(getImplLock()) { return getImpl().readHistory(documentId); }
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
		synchronized(getImplLock()) {
			return getImpl().readHistory(documentId, comparator);
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
        synchronized(getImplLock()) {
            return getImpl().readHistory(documentId, comparator, filter);
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
        synchronized(getImplLock()) { return getImpl().readHistory(documentId, filter); }
    }

    /**
     * Obtain the latest document version.
     * 
     * @param documentId
     *            The document id.
     * @return The latest document version.
     */
    public DocumentVersion readLatestVersion(final Long documentId) {
        synchronized(getImplLock()) {
            return getImpl().readLatestVersion(documentId);
        }
    }

	/**
	 * Remove a document listener.
	 * 
	 * @param l
	 *        The document listener.
	 */
	public void removeListener(final DocumentListener l) {
		synchronized(getImplLock()) { getImpl().removeListener(l); }
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
        synchronized(getImplLock()) { getImpl().rename(documentId, documentName); }
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
        synchronized(getImplLock()) {
            getImpl().updateDraft(documentId, content);
        }
    }
}
