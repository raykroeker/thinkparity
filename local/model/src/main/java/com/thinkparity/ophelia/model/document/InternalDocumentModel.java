/*
 * Created On: Feb 13, 2006
 */
package com.thinkparity.ophelia.model.document;

import java.io.InputStream;
import java.util.List;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerArtifactPublishedEvent;

import com.thinkparity.ophelia.model.InternalModel;
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

    /**
     * Create a draft for a document.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void createDraft(final Long documentId) {
        synchronized (getImplLock()) {
            getImpl().createDraft(documentId);
        }
    }

    /**
     * Delete a document.
     * 
     * @param documentId
     *            A document id.
     */
    public void delete(final Long documentId) {
        synchronized (getImplLock()) {
            getImpl().delete(documentId);
        }
    }

	/**
     * Delete the document draft.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void deleteDraft(final Long documentId) {
        synchronized (getImplLock()) {
            getImpl().deleteDraft(documentId);
        }
    }

    /**
     * Determine whether or not a draft exists for a document.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public Boolean doesExistDraft(final Long documentId) {
        synchronized (getImplLock()) {
            return getImpl().doesExistDraft(documentId);
        }
    }

    /**
     * Obtain a document name generator.
     * 
     * @return A <code>DocumentNameGenerator</code>.
     */
    public DocumentNameGenerator getNameGenerator() {
        synchronized (getImplLock()) {
            return getImpl().getNameGenerator();
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
     * @param streamId
     *            The stream id <code>String</code>.
     * @return The document version.
     */
    public DocumentVersion handleDocumentPublished(
            final ContainerArtifactPublishedEvent event) {
        synchronized (getImplLock()) {
            return getImpl().handleDocumentPublished(event);
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
	    synchronized (getImplLock()) {
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
        synchronized (getImplLock()) {
            return getImpl().read(documentId);
        }
    }

    /**
     * Read a list of audit events for a document.
     * 
     * @param documentId
     *            A document id.
     * @return A list of audit events.
     */
	public List<AuditEvent> readAuditEvents(final Long documentId) {
	    synchronized (getImplLock()) {
            return getImpl().readAuditEvents(documentId);
        }
    }

    /**
     * Read the version size.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return The version size <code>Integer</code>.
     */
    public Long readVersionSize(final Long documentId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readVersionSize(documentId, versionId);
        }
    }

    /**
     * Revert the document draft to its previous state.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void revertDraft(final Long documentId) {
        synchronized (getImplLock()) {
            getImpl().revertDraft(documentId);
        }
    }
}
