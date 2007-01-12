/*
 * Created On: Mar 6, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.InputStream;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.audit.HistoryItem;
import com.thinkparity.ophelia.model.events.DocumentListener;
import com.thinkparity.ophelia.model.util.Opener;
import com.thinkparity.ophelia.model.util.Printer;
import com.thinkparity.ophelia.model.util.filter.Filter;

/**
 * <b>Title:</b>thinkParity Document Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.13
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface DocumentModel {

    /**
     * Add a document listener.  If the listener is already registered
     * nothing is done.
     *
     * @param listener
     *      The document listener.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
	public void addListener(final DocumentListener listener);

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
	public Document create(final String name, final InputStream inputStream);

    /**
	 * Create a new document version based upon an existing document. This will
	 * check the cache for updates to the document, write the updates to the
	 * document, then create a new version based upon that document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The newly created version.
	 */
	public DocumentVersion createVersion(final Long documentId);

    /**
	 * Obtain a document with a specified id.
	 * 
	 * @param documentId
	 *            The document id.
	 * @return The document.
	 */
	public Document get(final Long documentId);

	/**
     * Determine whether or not the draft of the document has been modified by
     * the user.
     * 
     * @param documentId
     *            The document id.
     * @return True if the draft of the document has been modified.
     */
    public Boolean isDraftModified(final Long documentId);

    /**
	 * Open a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 */
	public void open(final Long documentId, final Opener opener);

    /**
	 * Open a document version.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version to open.
	 */
	public void openVersion(final Long documentId, final Long versionId,
            final Opener opener);

    /**
     * Print a document draft.
     * 
     * @param documentId
     *            A document id.
     * @param printer
     *            A document printer.
     */
	public void printDraft(final Long documentId, final Printer printer);

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
            final Printer printer);

	/**
     * Obtain the first available version.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @return A <code>DocumentVersion</code>.
     */
    public DocumentVersion readEarliestVersion(final Long documentId);

	/**
	 * Read the document's history.
	 * 
	 * @param documentId
	 *            The document id.
	 * @return A list of a history items.
	 */
	public List<DocumentHistoryItem> readHistory(final Long documentId);

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
            final Comparator<? super HistoryItem> comparator);

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
            final Comparator<? super HistoryItem> comparator,
            final Filter<? super HistoryItem> filter);

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
            final Filter<? super HistoryItem> filter);
    /**
     * Obtain the latest document version.
     * 
     * @param documentId
     *            The document id.
     * @return The latest document version.
     */
    public DocumentVersion readLatestVersion(final Long documentId);

    /**
	 * Obtain a document version.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version id.
	 * @return The document version.
	 */
	public DocumentVersion readVersion(final Long documentId,
            final Long versionId);

    /**
	 * Remove a document listener.
	 * 
	 * @param listener
	 *        The document listener.
	 */
	public void removeListener(final DocumentListener listener);

	/**
     * Rename a document.
     *
     * @param documentId
     *      A document id.
     * @param documentName
     *      A document name.
     */
    public void rename(final Long documentId, final String documentName);

    /**
     * Update the draft of a document.
     * 
     * @param documentId
     *            The document id.
     * @param content
     *            The new content.
     */
    public void updateDraft(final Long documentId, final InputStream content); 
}
