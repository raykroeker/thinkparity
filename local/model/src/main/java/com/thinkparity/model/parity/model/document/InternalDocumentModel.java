/*
 * Feb 13, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.audit.event.AuditEvent;
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

	/**
     * Confirm that the document sent previously has been received by the
     * specified user.
     * 
     * @param documentId
     *      The document id.
     * @param versionId
     *      The document version id.
     * @param confirmedBy
     *      To whom the document was sent.
     * @throws ParityException
     */
    public void confirmSend(final Long documentId, final Long versionId,
            final JabberId confirmedBy)
            throws ParityException {
        synchronized(getImplLock()) {
            getImpl().confirmSend(documentId, versionId, confirmedBy);
        }
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
     * A key request for a document was accepted.
     * 
     * @param documentId
     *            The document id.
     * @param acceptedBy
     *            By whom the request was accepted.
     * @throws ParityException
     */
	public void keyRequestAccepted(final Long documentId,
            final JabberId acceptedBy) throws ParityException {
        synchronized(getImplLock()) {
            getImpl().keyRequestAccepted(documentId, acceptedBy);
        }
    }

    /**
     * A key request for a document was declined.
     * 
     * @param documentId
     *            The document id.
     * @param declinedBy
     *            By whom the request was declined.
     * @throws ParityException
     */
	public void keyRequestDeclined(final Long documentId, final JabberId declinedBy) throws ParityException {
	    synchronized(getImplLock()) {
            getImpl().keyRequestDeclined(documentId, declinedBy);
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
     * Open an input stream to read a document version. Note: It is a good idea
     * to buffer the input stream.
     * 
     * @param documentId
     *            A document id.
     * @param versionId
     *            A version id.
     * @return An input stream.
     */
	public InputStream openStream(final Long documentId, final Long versionId) {
	    synchronized(getImplLock()) {
            return getImpl().openStream(documentId, versionId);
        }
    }

	/**
     * Read a document.
     * 
     * @param documentId
     *            A document id.
     * @return A document.
     */
    public Document read(final Long documentId) {
        synchronized(getImplLock()) { return getImpl().read(documentId); }
    }

	/**
     * Read a list of audit events for a document.
     * 
     * @param documentId
     *            A document id.
     * @return A list of audit events.
     */
	public List<AuditEvent> readAuditEvents(final Long documentId) {
	    synchronized(getImplLock()) {
            return getImpl().readAuditEvents(documentId);
        }
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
}
