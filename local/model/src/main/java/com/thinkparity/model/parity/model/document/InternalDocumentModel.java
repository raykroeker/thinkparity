/*
 * Feb 13, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.util.UUID;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalDocumentModel extends DocumentModel implements
		InternalModel {

	/**
	 * Create a InternalDocumentModel.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 * @param context
	 *            The parity context.
	 */
	InternalDocumentModel(final Workspace workspace,
			final Context context) {
		super(workspace);
		context.assertContextIsValid();
	}

	public void close(final UUID documentUniqueId) throws ParityException {
		synchronized(getImplLock()) { getImpl().close(documentUniqueId); }
	}

	/**
	 * Obtain a document with a specified id.
	 * 
	 * @param documentUniqueId
	 *            The document unique id.
	 * @return The document.
	 * @throws ParityException
	 */
	public Document get(final UUID documentUniqueId) throws ParityException {
		synchronized(getImplLock()) { return getImpl().get(documentUniqueId); }
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
		synchronized(getImplLock()) {
			return getImpl().isWorkingVersionEqual(documentId);
		}
	}

	/**
	 * Lock a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	public void lock(final Long documentId) throws ParityException {
		synchronized(getImplLock()) { getImpl().lock(documentId); }
	}

	/**
	 * Unlock a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	public void unlock(final Long documentId) throws ParityException {
		synchronized(getImplLock()) { getImpl().unlock(documentId); }
	}

	/**
	 * Obtain the latest document version.
	 * 
	 * @param documentId
	 *            The document id.
	 * @return The latest document version.
	 * @throws ParityException
	 */
	public DocumentVersion getLatestVersion(final Long documentId)
			throws ParityException {
		synchronized(getImplLock()) {
			return getImpl().getLatestVersion(documentId);
		}
	}
}
