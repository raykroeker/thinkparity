/*
 * Created On: Feb 13, 2006
 */
package com.thinkparity.ophelia.model.document;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.util.jta.TransactionType;

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
     * Delete a document.
     * 
     * @param lock
     *            A <code>DocumentFileLock</code>.
     * @param documentId
     *            A document id.
     */
    public void delete(final DocumentFileLock lock,
            final Map<DocumentVersion, DocumentFileLock> versionLocks,
            final Long documentId);

    /**
     * Delete the document draft.
     * 
     * @param lock
     *            A <code>DocumentFileLock</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void deleteDraft(final DocumentFileLock lock, final Long documentId);

    /**
     * Determine whether or not a draft exists for a document.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public Boolean doesExistDraft(final Long documentId);

    // TODO-javadoc InternalDocumentModel#handleDocumentPublished
    public DocumentVersion handleDocumentPublished(final Long documentId,
            final DocumentVersion version, final JabberId publishedBy,
            final Calendar publishedOn);

    /**
     * Obtain an exclusive lock on a document.
     * 
     * @param document
     *            A <code>Document</code>.
     * @return A <code>DocumentFileLock</code>.
     * @throws CannotLockException
     *             if an exclusive lock cannot be obtained
     */
    public DocumentFileLock lock(final Document document)
            throws CannotLockException;

    /**
     * Obtain an exclusive lock on a document.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>DocumentVersionLock</code>.
     * @throws CannotLockException
     *             if an exclusive lock cannot be obtained
     */
    public DocumentFileLock lockVersion(final DocumentVersion version)
            throws CannotLockException;

    /**
     * Read a document.
     * 
     * @param documentId
     *            A document id.
     * @return A document.
     */
    public Document read(final Long documentId);

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
}
