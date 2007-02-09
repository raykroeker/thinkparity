/*
 * Created On: Feb 13, 2006
 */
package com.thinkparity.ophelia.model.document;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.audit.event.AuditEvent;

/**
 * <b>Title:</b>thinkParity Internal Document Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.14
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalDocumentModel extends DocumentModel {

    /**
     * Create a draft for a document.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void createDraft(final Long documentId);

    /**
     * Create a new document version based upon an existing document. This will
     * check the cache for updates to the document, write the updates to the
     * document, then create a new version based upon that document.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param createdOn
     *            A created on <code>Calendar</code>.
     * @return The newly created version.
     */
    public DocumentVersion createVersion(final Long documentId,
            final Calendar createdOn);

    /**
     * Delete a document.
     * 
     * @param documentId
     *            A document id.
     */
    public void delete(final Long documentId);

    /**
     * Delete the document draft.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void deleteDraft(final Long documentId);

    /**
     * Determine whether or not a draft exists for a document.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public Boolean doesExistDraft(final Long documentId);

	/**
     * Obtain a document name generator.
     * 
     * @return A <code>DocumentNameGenerator</code>.
     */
    public DocumentNameGenerator getNameGenerator();

    /**
     * Handle the receipt of a document version from the thinkParity network.
     * @param version
     * @param streamId
     * @param publishedBy
     * @param publishedOn
     * @return
     */
    public DocumentVersion handleDocumentPublished(
            final DocumentVersion version, final String streamId,
            final JabberId publishedBy, final Calendar publishedOn);

    /**
     * Open an input stream to read a document version. Note: It is a good idea
     * to buffer the input stream.
     * 
     * @param documentId
     *            A document id.
     * @return An input stream.
     */
	public InputStream openVersionStream(final Long documentId,
            final Long versionId);

	/**
     * Read a document.
     * 
     * @param documentId
     *            A document id.
     * @return A document.
     */
    public Document read(final Long documentId);

    /**
     * Read a list of audit events for a document.
     * 
     * @param documentId
     *            A document id.
     * @return A list of audit events.
     */
	public List<AuditEvent> readAuditEvents(final Long documentId);

    /**
     * Read a list of document versions.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @return A <code>List</code> of <code>DocumentVersion</code>s.
     */
    public List<DocumentVersion> readVersions(final Long documentId);

    /**
     * Read a list of document versions.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @return A <code>List</code> of <code>DocumentVersion</code>s.
     */
    public List<DocumentVersion> readVersions(final Long documentId,
            final Comparator<? super ArtifactVersion> comparator);

    /**
     * Read the version size.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return The version size <code>Integer</code>.
     */
    public Long readVersionSize(final Long documentId, final Long versionId);

    /**
     * Remove a document.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void remove(final Long documentId);

    /**
     * Revert the document draft to its previous state.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void revertDraft(final Long documentId);
}
