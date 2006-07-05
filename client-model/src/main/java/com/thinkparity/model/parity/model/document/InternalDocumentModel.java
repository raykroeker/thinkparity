/*
 * Feb 13, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.util.Calendar;
import java.util.List;
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

	/**
     * Add a team member to the document.
     * 
     * @param documentId
     *            The document id.
     * @param jabberId
     *            The team member.
     * @throws ParityException
     */
    public void addTeamMember(final Long documentId, final JabberId jabberId)
            throws ParityException {
        synchronized(getImplLock()) {
            getImpl().addTeamMember(documentId, jabberId);
        }
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
     * Handle a reactivate request from the remote model.
     * 
     * @param reactivatedBy
     *            By whom the document was reactivated.
     * @param team
     *            The team.
     * @param uniqueId
     *            The unique id.
     * @param versionId
     *            The version id.
     * @param name
     *            The name.
     * @param content
     *            The content.
     */
    public void handleReactivate(final JabberId reactivatedBy,
            final List<JabberId> team, final UUID uniqueId,
            final Long versionId, final String name, final byte[] content)
            throws ParityException {
        synchronized(getImplLock()) {
            getImpl().handleReactivate(reactivatedBy, team, uniqueId, versionId,
                    name, content);
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

	/**
     * Remove a team member from the document.
     * 
     * @param documentId
     *            The document id.
     * @param jabberId
     *            The team member.
     * @throws ParityException
     */
    public void removeTeamMember(final Long documentId, final JabberId jabberId)
            throws ParityException {
        synchronized(getImplLock()) {
            getImpl().removeTeamMember(documentId, jabberId);
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
