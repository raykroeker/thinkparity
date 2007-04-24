/*
 * Created On: Mar 6, 2005
 */
package com.thinkparity.ophelia.model.document;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.util.jta.TransactionType;

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
     * Read a document.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @return A <code>Document</code>.
     */
    public Document read(final Long documentId);

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
}
