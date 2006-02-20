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

	public void close(final UUID documentUniqueId) throws ParityException {
		synchronized(getImplLock()) { getImpl().close(documentUniqueId); }
	}
}
