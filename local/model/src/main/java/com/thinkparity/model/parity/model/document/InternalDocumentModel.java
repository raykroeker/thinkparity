/*
 * Feb 13, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.JabberId;

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

	public void auditRecieveKey(final Long artifactId,
            final JabberId createdBy, final Calendar createdOn,
            final JabberId receivedFrom) throws ParityException {
		synchronized(getImplLock()) {
			getImpl().auditKeyRecieved(artifactId, createdBy, createdOn, receivedFrom);
		}
	}

	public void close(final UUID documentUniqueId, final JabberId closedBy)
			throws ParityException {
		synchronized(getImplLock()) { getImpl().close(documentUniqueId, closedBy); }
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
	 * Use the document model to receive a document from another parity user.
	 * 
	 * @param xmppDocument
	 *            The xmpp document received from another parity user.
	 * @throws ParityException
	 */
	public void receive(final JabberId receivedFrom,
            final UUID documentUniqueId, final Long versionId,
            final String name, final byte[] content) throws ParityException,
            SmackException {
		synchronized(getImplLock()) {
            getImpl().receive(
                    receivedFrom, documentUniqueId, versionId, name, content);
        }
	}

	public void requestKey(final Long documentId, final JabberId requestedBy)
			throws ParityException {
		synchronized(getImplLock()) { getImpl().requestKey(documentId, requestedBy); }
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
     * Update the index for a document.
     * 
     * @param documentId
     *            The document id.
     */
	public void updateIndex(final Long documentId) throws ParityException {
		synchronized(getImplLock()) { getImpl().updateIndex(documentId); }
	}
}
