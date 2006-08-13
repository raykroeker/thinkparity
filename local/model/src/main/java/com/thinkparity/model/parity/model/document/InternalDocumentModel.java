/*
 * Created On: Feb 13, 2006
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
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.25
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
     * Delete a document.
     * 
     * @param documentId
     *            A document id.
     */
    public void delete(final Long documentId) {
        synchronized(getImplLock()) { getImpl().delete(documentId); }
    }

    /**
	 * Obtain a document with a specified id.
	 * 
	 * @param documentUniqueId
	 *            The document unique id.
	 * @return The document.
	 * @throws ParityException
	 */
	public Document get(final UUID documentUniqueId) {
		synchronized(getImplLock()) { return getImpl().read(documentUniqueId); }
	}

    /**
     * Handle the receipt of a document from the thinkParity network.
     * 
     * @param uniqueId
     *            A unique id.
     * @param versionId
     *            A version id.
     * @param name
     *            A name.
     * @param createdBy
     *            The creator.
     * @param createdOn
     *            The creation date.
     * @param content
     *            The content's input stream.
     * @return The document version.
     */
    public DocumentVersion handleDocumentPublished(final JabberId publishedBy,
            final Calendar publishedOn, final UUID uniqueId, final Long versionId,
            final String name, final InputStream content) {
        synchronized(getImplLock()) {
            return getImpl().handleDocumentPublished(publishedBy, publishedOn, uniqueId,
                    versionId, name, content);
        }
    }
    /**
     * Handle the receipt of a document from the thinkParity network.
     * 
     * @param uniqueId
     *            A unique id.
     * @param versionId
     *            A version id.
     * @param name
     *            A name.
     * @param createdBy
     *            The creator.
     * @param createdOn
     *            The creation date.
     * @param content
     *            The content's input stream.
     * @return The document version.
     */
    public DocumentVersion handleDocumentSent(final JabberId sentBy,
            final Calendar sentOn, final UUID uniqueId, final Long versionId,
            final String name, final InputStream content) {
        synchronized(getImplLock()) {
            return getImpl().handleDocumentSent(sentBy, sentOn, uniqueId,
                    versionId, name, content);
        }
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
     * Open an input stream to read a document version. Note: It is a good idea
     * to buffer the input stream.
     * 
     * @param documentId
     *            A document id.
     * @return An input stream.
     */
	public InputStream openVersionStream(final Long documentId,
            final Long versionId) {
	    synchronized(getImplLock()) {
            return getImpl().openVersionStream(documentId, versionId);
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
}
