/*
 * Created On: Mar 6, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.InputStream;

import com.thinkparity.codebase.model.annotation.ThinkParityConcurrency;
import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.util.concurrent.Lock;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.events.DocumentListener;
import com.thinkparity.ophelia.model.util.Opener;
import com.thinkparity.ophelia.model.util.Printer;

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
    @ThinkParityConcurrency(Lock.LOCAL_READ)
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
     * Read a document.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @return A <code>Document</code>.
     */
    public Document read(final Long documentId);

	/**
     * Obtain the first available version.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @return A <code>DocumentVersion</code>.
     */
    public DocumentVersion readEarliestVersion(final Long documentId);

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
    @ThinkParityTransaction(TransactionType.NEVER)
	public void removeListener(final DocumentListener listener);

    /**
     * Update the draft of a document.
     * 
     * @param documentId
     *            The document id.
     * @param content
     *            The new content.
     */
    public void updateDraft(final Long documentId, final InputStream content)
            throws CannotLockException; 
}
