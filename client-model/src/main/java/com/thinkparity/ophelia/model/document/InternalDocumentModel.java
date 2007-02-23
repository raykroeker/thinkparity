/*
 * Created On: Feb 13, 2006
 */
package com.thinkparity.ophelia.model.document;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentDraft;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamUploader;
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
     * Create a draft for a document.
     * 
     * @param lock
     *            A <code>DocumentFileLock</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public DocumentDraft createDraft(final DocumentFileLock lock,
            final Long documentId);

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
    public DocumentVersion createVersion(final DocumentFileLock lock,
            final Long documentId, final InputStream stream,
            final Integer buffer, final Calendar createdOn);

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
            final InputStream stream, final Integer buffer,
            final Calendar createdOn);

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

    /**
     * Obtain a document name generator.
     * 
     * @return A <code>DocumentNameGenerator</code>.
     */
    public DocumentNameGenerator getNameGenerator();

    // TODO-javadoc InternalDocumentModel#handleDocumentPublished
    public DocumentVersion handleDocumentPublished(
            final DocumentVersion version, final String streamId,
            final JabberId publishedBy, final Calendar publishedOn);

    /**
     * Determine whether or not the draft of the document has been modified by
     * the user.
     * 
     * @param documentId
     *            The document id.
     * @return True if the draft of the document has been modified.
     */
    public Boolean isDraftModified(final DocumentFileLock lock,
            final Long documentId);

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
     * Open the document draft input stream.
     *
     * @param documentId
     *      A document id <code>Long</code>.
     * @return A document draft content <code>InputStream</code>.
     */
    public InputStream openDraft(final Long documentId);

    /**
     * Open an input stream to read a document version. Note: It is a good idea
     * to buffer the input stream.
     * 
     * @param documentId
     *      A document id <code>Long</code>.
     * @param versionId
     *      A document version id <code>Long</code>.
     * @return A document version content <code>InputStream</code>.
     */
	public InputStream openVersion(final Long documentId, final Long versionId);

    /**
     * Read a document.
     * 
     * @param documentId
     *            A document id.
     * @return A document.
     */
    public Document read(final Long documentId);

	/**
     * Obtain the document draft.
     * 
     * @param lock
     *            A <code>DocumentFileLock</code>.
     * @return A <code>DocumentDraft</code>.
     */
    public DocumentDraft readDraft(final DocumentFileLock lock,
            final Long documentId);

    /**
     * Obtain the document draft.
     * 
     * @param documentId
     *            A document draft <code>Long</code>.
     * @return A <code>DocumentDraft</code>.
     */
    public DocumentDraft readDraft(final Long documentId);

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
     * @param lock
     *            A <code>DocumentFileLock</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void remove(final DocumentFileLock lock, final Long documentId);

    /**
     * Revert the document draft to its previous state.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void revertDraft(final DocumentFileLock lock, final Long documentId);

    /**
     * Save a version to an output stream.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param uploader
     *            An <code>StreamUploader</code> to upload to.
     */
    public void uploadVersion(final Long documentId, final Long versionId,
            final StreamUploader uploader);
}
