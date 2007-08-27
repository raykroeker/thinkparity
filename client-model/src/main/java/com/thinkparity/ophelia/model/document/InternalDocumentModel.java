/*
 * Created On: Feb 13, 2006
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.io.StreamOpener;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentDraft;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.download.DownloadFile;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.util.ProcessMonitor;

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
            final Long documentId) throws CannotLockException;

    /**
     * Create an existing document version.
     * 
     * @param document
     *            A <code>Document</code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     * @param stream
     *            An <code>InputStream</code>.
     */
    public void createVersion(final Document document,
            final DocumentVersion version, final InputStream stream);

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
            final Calendar createdOn);

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
            final InputStream stream, final Calendar createdOn);

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
     * Delete a document.
     * 
     * @param lock
     *            A <code>DocumentFileLock</code>.
     * @param documentId
     *            A document id.
     */
    public void delete(final Long documentId);

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

    /**
     * Handle a document published event.
     * 
     * @param containerVersion
     *            A <code>ContainerVersion</code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     * @param versionFile
     *            The version's <code>File</code>.  If the document version
     *            already exists locally; file is null.
     * @param publishedBy
     *            A published by user id <code>JabberId</code>.
     * @param publishedOn
     *            A published on date/time <code>Calendar</code>.
     * @return A <code>DocumentVersion</code>.
     */
    public DocumentVersion handleDocumentPublished(
            final ContainerVersion containerVersion,
            final DocumentVersion version, final File versionFile,
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
     * Create a new instance of a document version download file.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>DownloadFile</code>.
     */
    public DownloadFile newDownloadFile(final DocumentVersion version);

    /**
     * Open the document draft input stream.
     *
     * @param documentId
     *      A document id <code>Long</code>.
     * @return A document draft content <code>InputStream</code>.
     */
    public InputStream openDraft(final Long documentId);

    /**
     * Open an input stream to read a document version.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param versionId
     *            A document version id <code>Long</code>.
     * @param opener
     *            A <code>StreamOpener</code>.
     */
	public void openVersion(final Long documentId, final Long versionId,
            final StreamOpener opener);

    /**
     * Read a list of documents.
     * 
     * @return A <code>List</code> of <code>Document</code>s.
     */
    public List<Document> read();

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
     * Rename a document.
     * 
     * @param documentId
     *            A document id.
     * @param documentName
     *            A document name.
     * @throws CannotLockException
     *             if the document cannot be locked
     */
    public void rename(final Long documentId, final String documentName)
            throws CannotLockException;

    /**
     * Revert the document draft to its previous state.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void revertDraft(final DocumentFileLock lock, final Long documentId);

    /**
     * Update the draft of a document.
     * 
     * @param lock
     *            A <code>DocumentFileLock</code>.
     * @param documentId
     *            The document id.
     * @param content
     *            The new content.
     */
    public void updateDraft(final DocumentFileLock lock, final Long documentId,
            final InputStream content);

    /**
     * Upload a document version to the streaming server.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     */
    public void uploadStream(final ProcessMonitor monitor,
            final DocumentVersion version);
}
