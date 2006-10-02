/*
 * Created On: Feb 13, 2006
 */
package com.thinkparity.ophelia.model.document;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.InternalModel;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.audit.event.AuditEvent;
import com.thinkparity.ophelia.model.workspace.Workspace;

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
	InternalDocumentModel(final Context context, final Environment environment,
            final Workspace workspace) {
		super(environment, workspace);
	}

    public void auditRecieveKey(final Long artifactId,
            final JabberId createdBy, final Calendar createdOn,
            final JabberId receivedFrom) throws ParityException {
		synchronized(getImplLock()) {
			getImpl().auditKeyRecieved(artifactId, createdBy, createdOn, receivedFrom);
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
            final String name, final String checksum, final InputStream content) {
        synchronized(getImplLock()) {
            return getImpl().handleDocumentPublished(publishedBy, publishedOn, uniqueId,
                    versionId, name, checksum, content);
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
            final String name, final String checksum, final InputStream content) {
        synchronized(getImplLock()) {
            return getImpl().handleDocumentSent(sentBy, sentOn, uniqueId,
                    versionId, name, checksum, content);
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

    /**
     * Revert the document draft to its previous state.
     * 
     * @param documentId
     *            A document id.
     */
    public void revertDraft(final Long documentId) {
        synchronized (getImplLock()) {
            getImpl().revertDraft(documentId);
        }
    }
}
