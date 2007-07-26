/*
 * Created On:  Sun Mar 06, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.Constants.ChecksumAlgorithm;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.io.StreamOpener;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.artifact.ArtifactVersionFlag;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentDraft;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.download.DownloadFile;

import com.thinkparity.ophelia.model.Delegate;
import com.thinkparity.ophelia.model.DownloadHelper;
import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.Constants.DirectoryNames;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.document.delegate.UploadStream;
import com.thinkparity.ophelia.model.events.DocumentEvent;
import com.thinkparity.ophelia.model.events.DocumentListener;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.DocumentIOHandler;
import com.thinkparity.ophelia.model.util.Opener;
import com.thinkparity.ophelia.model.util.Printer;
import com.thinkparity.ophelia.model.util.UUIDGenerator;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * Implementation of the document model interface.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public final class DocumentModelImpl extends
        Model<DocumentListener> implements DocumentModel,
        InternalDocumentModel {

    /** The default document version comparator. */
	private final Comparator<ArtifactVersion> defaultVersionComparator;

    /** A document reader/writer. */
	private DocumentIOHandler documentIO;

    /** A document event generator for local events. */
    private final DocumentModelEventGenerator localEventGen;

	/** The directory beneath which all files are stored. */
    private File localFilesDirectory;

    private final DocumentNameGenerator nameGenerator;

    /**
	 * Create a DocumentModelImpl
	 * 
	 * @param workspace
	 *            The workspace to work within.
	 */
	public DocumentModelImpl() {
		super();
		final ComparatorBuilder comparatorBuilder = new ComparatorBuilder();
		this.defaultVersionComparator = comparatorBuilder.createVersionById(Boolean.TRUE);
        this.localEventGen = new DocumentModelEventGenerator(DocumentEvent.Source.LOCAL);
        this.nameGenerator = new DocumentNameGenerator();
	}

    /**
     * @see com.thinkparity.ophelia.model.Model#addListener(com.thinkparity.codebase.event.EventListener)
     *
	 */
    @Override
    public void addListener(final DocumentListener listener) {
        super.addListener(listener);
    }

    /**
     * Create a document.
     * 
     * @param name
     *            The document name.
     * @param content
     *            The document's content input stream.
     * @return The document.
     */
    public Document create(final String name, final InputStream content) {
        try {
            assertIsSetCredentials();
            // create
            final Document document = create(UUIDGenerator.nextUUID(), name,
                    content, localUserId(), currentDateTime());
            // fire event
            notifyDocumentCreated(document, localEventGen);
            return document;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#createDraft(com.thinkparity.ophelia.model.document.DocumentFileLock,
     *      java.lang.Long)
     * 
     */
    public DocumentDraft createDraft(final DocumentFileLock lock,
            final Long documentId) throws CannotLockException {
        try {
            final DocumentVersion latestVersion = readLatestVersion(documentId);
            writeVersion(latestVersion, lock);
            lock.getFile().setWritable(true, true);
            lock.release();
            try {
                Assert.assertTrue(lock.getFile().setLastModified(
                        latestVersion.getCreatedOn().getTimeInMillis()),
                        "Could not set last modified for document {0}.",
                        latestVersion.getName());
            } finally {
                lock(lock, read(documentId));
            }
            return readDraft(lock, documentId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#createVersion(com.thinkparity.ophelia.model.document.DocumentFileLock, java.lang.Long, java.io.InputStream, java.lang.Integer, java.util.Calendar)
     *
     */
    public DocumentVersion createVersion(final DocumentFileLock lock,
            final Long documentId, final InputStream stream,
            final Calendar createdOn) {
        try {
            assertDraftIsModified(lock, documentId, "Draft has not been modified.");

            return createVersion(documentId, readNextVersionId(documentId),
                        stream, localUserId(), createdOn);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#createVersion(java.lang.Long,
     *      java.io.InputStream, java.lang.Integer, java.util.Calendar)
     * 
     */
    public DocumentVersion createVersion(final Long documentId,
            final InputStream stream, final Calendar createdOn) {
        try {
            assertDraftIsModified(documentId, "Draft has not been modified.");

            return createVersion(documentId, readNextVersionId(documentId),
                        stream, localUserId(), createdOn);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#delete(com.thinkparity.codebase.model.document.DocumentLock,
     *      java.lang.Long)
     * 
     */
    public void delete(final DocumentFileLock lock,
            final Map<DocumentVersion, DocumentFileLock> versionLocks,
            final Long documentId) {
        try {
            release(lock);
            deleteLocal(lock, versionLocks, documentId);
            // fire event
            notifyDocumentDeleted(null, localEventGen);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#delete(java.lang.Long)
     * 
     */
    public void delete(final Long documentId) {
        try {
            final Document document = read(documentId);
            final File draftFile = getDraftFile(document);
            Assert.assertNotTrue(draftFile.exists(),
                    "Draft file {0} for document {1} still exists.", draftFile,
                    document.getName());
            // delete local data
            deleteLocal(documentId);
            // fire event
            notifyDocumentDeleted(document, localEventGen);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#deleteDraft(com.thinkparity.codebase.model.document.DocumentLock,
     *      java.lang.Long)
     * 
     */
    public void deleteDraft(final DocumentFileLock lock, final Long documentId) {
        try {
            assertDoesExistDraft(documentId, "Draft does not exist.");
            release(lock);
            Assert.assertTrue(lock.getFile().delete(),
                    "Could not delete draft file {0}.", lock.getFile());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Determine whether or not a draft exists.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @return True if the draft exists.
     */
    public Boolean doesExistDraft(final Long documentId) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        try {
            return getDraftFile(read(documentId)).exists();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.DocumentModel#get(java.lang.Long)
     *
     */
    public Document get(final Long documentId) {
        return read(documentId);
    }

    /**
     * Obtain a document name generator.
     * 
     * @return A <code>DocumentNameGenerator</code>.
     */
    public DocumentNameGenerator getNameGenerator() {
        logger.logApiId();
        try {
            return new DocumentNameGenerator();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#handleDocumentPublished(com.thinkparity.codebase.model.container.ContainerVersion,
     *      com.thinkparity.codebase.model.document.DocumentVersion,
     *      java.io.File, com.thinkparity.codebase.jabber.JabberId,
     *      java.util.Calendar)
     * 
     */
    public DocumentVersion handleDocumentPublished(
            final ContainerVersion containerVersion,
            final DocumentVersion version, final File versionFile,
            final JabberId publishedBy, final Calendar publishedOn) {
        try {
            final InternalArtifactModel artifactModel  = getArtifactModel();
            final Document document;
            final DocumentVersion localVersion;
            if (artifactModel.doesExist(version.getArtifactUniqueId())) {
                logger.logInfo("Document {0} already exists.",
                        version.getArtifactUniqueId());
                document = read(version.getArtifactUniqueId());
                if (artifactModel.doesVersionExist(document.getId(),
                        version.getVersionId())) {
                    logger.logWarning(
                            "Document version {0}:{1} already exists.",
                            version.getArtifactUniqueId(), version.getVersionId());
                    localVersion = readVersion(document.getId(), version.getVersionId());
                } else {
                    Assert.assertNotNull(versionFile,
                            "Version file {0} cannot be null for version {1}.",
                            version);
                    final InputStream input = new FileInputStream(versionFile);
                    try {
                        localVersion = createVersion(document.getId(),
                                version.getVersionId(), input, publishedBy,
                                publishedOn);
                    } finally {
                        input.close();
                    }
                }
            }
            else {
                Assert.assertNotNull(versionFile,
                        "Version file {0} cannot be null for version {1}.",
                        version);
                document = create(version.getArtifactUniqueId(),
                        version.getArtifactName(), publishedBy, publishedOn);
                final InputStream stream = new FileInputStream(versionFile);
                try {
                    localVersion = createVersion(document.getId(),
                            version.getVersionId(), stream,
                            publishedBy, publishedOn);
                } finally {
                    stream.close();
                }
                getIndexModel().indexDocument(containerVersion.getArtifactId(),
                        document.getId());
            }
            return localVersion;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#isDraftModified(com.thinkparity.ophelia.model.document.DocumentFileLock, java.lang.Long)
     *
     */
    public Boolean isDraftModified(final DocumentFileLock lock,
            final Long documentId) {
        try {
            final File draftFile = getDraftFile(lock);
            if (draftFile.exists()) {
                final DocumentVersion latestVersion = readLatestVersion(documentId);
                if (null == latestVersion) {
                    return Boolean.TRUE;
                } else {
                    final long latestVersionCreatedOn =
                        latestVersion.getCreatedOn().getTimeInMillis();
                    /* the time stamp is checked first because it is fast;
                     * however documents are considered different only if the
                     * checksums are different */
                    if (draftFile.lastModified() == latestVersionCreatedOn) {
                        return Boolean.FALSE;
                    } else {
                        return !latestVersion.getChecksum().equals(
                                checksum(lock.getFileChannel(0L)));
                    }
                }
            } else {
                return Boolean.FALSE;
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Determine whether or not the draft of the document is different from the
     * latest version.
     * 
     * @return True if the draft is different from the latest version.
     */
    public Boolean isDraftModified(final Long documentId) {
        try {
            final File draftFile = getDraftFile(read(documentId));
            if (draftFile.exists()) {
                final DocumentVersion latestVersion = readLatestVersion(documentId);
                if (null == latestVersion) {
                    return Boolean.TRUE;
                } else {
                    final long latestVersionCreatedOn =
                        latestVersion.getCreatedOn().getTimeInMillis();
                    /* the time stamp is checked first because it is fast;
                     * however documents are considered different only if the
                     * checksums are different */
                    if (draftFile.lastModified() == latestVersionCreatedOn) {
                        return Boolean.FALSE;
                    } else {
                        return !latestVersion.getChecksum().equals(
                                checksum(draftFile));
                    }
                }
            } else {
                return Boolean.FALSE;
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#lock(com.thinkparity.codebase.model.document.Document)
     *
     */
    public DocumentFileLock lock(final Document document)
            throws CannotLockException {
        try {
            final DocumentFileLock lock = new DocumentFileLock();
            lock(lock, document);
            return lock;
        } catch (final CannotLockException clx) {
            throw clx;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#lockVersion(com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public DocumentFileLock lockVersion(final DocumentVersion version)
            throws CannotLockException {
        try {
            final DocumentFileLock lock = new DocumentFileLock();
            lock(lock, read(version.getArtifactId()), version);
            return lock;
        } catch (final CannotLockException clx) {
            throw clx;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#newDownloadHelper(com.thinkparity.codebase.model.document.DocumentVersion)
     *
     */
    public DownloadHelper newDownloadHelper(final DocumentVersion version) {
        try {
            final StreamSession session = getStreamModel().newDownstreamSession(
                    version);
            final DownloadFile downloadFile = new DownloadFile(session);
            return new DownloadHelper() {
                public void download(final File target) throws IOException {
                    downloadFile.download(target);
                }
            };
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.DocumentModel#open(java.lang.Long,
     *      com.thinkparity.ophelia.model.util.Opener)
     * 
     */
    public void open(final Long documentId, final Opener opener) {
		try {
            opener.open(getDraftFile(read(documentId)));
		} catch (final Throwable t) {
            throw panic(t);
		}
	}

    /**
     * Open an input stream to read the document draft. Note: It is a good
     * idea to buffer the input stream.
     * 
     * @param documentId
     *            A document id.
     * @return A document draft content <code>InputStream</code>.
     */
    public InputStream openDraft(final Long documentId) {
        try {
            final Document document = read(documentId);
            final File localFile = getDraftFile(document);
            return openFile(localFile);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.DocumentModel#openVersion(java.lang.Long,
     *      java.lang.Long, com.thinkparity.ophelia.model.util.Opener)
     * 
     */
    public void openVersion(final Long documentId, final Long versionId,
            final Opener opener) {
		try {
			opener.open(getVersionFile(read(documentId),
                    readVersion(documentId, versionId)));
		} catch (final Throwable t) {
            throw panic(t);
		}
	}

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#openVersion(java.lang.Long,
     *      java.lang.Long, com.thinkparity.codebase.io.StreamOpener)
     * 
     */
    public void openVersion(final Long documentId, final Long versionId,
            final StreamOpener opener) {
        try {
            documentIO.openStream(documentId, versionId, opener);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Print a document draft.
     * 
     * @param documentId
     *            A document id.
     * @param printer
     *            A document printer.
     */
    public void printDraft(final Long documentId, final Printer printer) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("printer", printer);
        try {
            final Document document = read(documentId);
            final File draftFile = getDraftFile(document);
            final File copyTo = workspace.createTempFile(document.getName());
            try {
                fileToFile(draftFile, copyTo);
                printer.print(copyTo);
            } finally {
                Assert.assertTrue(copyTo.delete(),
                        "Could not delete temporary file {0}.", copyTo);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }        
        
    /**
     * Print a document version.
     * 
     * @param documentId
     *            A document id.
     * @param versionId
     *            A document version id.
     * @param printer
     *            A document printer.
     */
    public void printVersion(final Long documentId, final Long versionId,
            final Printer printer) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("printer", printer);
        try {
            final Document document = read(documentId);
            final DocumentVersion version = readVersion(documentId, versionId);
            final File versionFile = getVersionFile(document, version);

            final File copyTo = workspace.createTempFile(document.getName());
            try {
                fileToFile(versionFile, copyTo);
                printer.print(copyTo);
            } finally {
                Assert.assertTrue(copyTo.delete(),
                        "Could not delete temporary file {0}.", copyTo);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#read()
     *
     */
    public List<Document> read() {
        try {
            return documentIO.read();
        } catch (final Throwable t) {
            throw panic(t);
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
        try {
            return documentIO.read(documentId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
	 * Obtain a document with the specified unique id.
	 * 
	 * @param documentUniqueId
	 *            The document unique id.
	 * @return The document.
	 */
    public Document read(final UUID uniqueId) {
		try {
            return documentIO.read(uniqueId);
		} catch (final Throwable t) {
            throw panic(t);
		}
	}

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#readDraft(com.thinkparity.ophelia.model.document.DocumentFileLock)
     * 
     */
    public DocumentDraft readDraft(final DocumentFileLock lock,
            final Long documentId) {
        try {
            if (doesExistDraft(lock)) {
                final File draftFile = getDraftFile(lock);
                final DocumentDraft draft = new DocumentDraft();
                draft.setChecksum(checksum(lock.getFileChannel(0L)));
                draft.setChecksumAlgorithm(getChecksumAlgorithm());
                draft.setDocumentId(documentId);
                draft.setSize(draftFile.length());
                return draft;
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#readDraft(java.lang.Long)
     *
     */
    public DocumentDraft readDraft(final Long documentId) {
        try {
            if (doesExistDraft(documentId)) {
                final File draftFile = getDraftFile(read(documentId));
                final DocumentDraft draft = new DocumentDraft();
                draft.setChecksum(checksum(draftFile));
                draft.setChecksumAlgorithm(getChecksumAlgorithm());
                draft.setDocumentId(documentId);
                draft.setSize(draftFile.length());
                return draft;
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    

    /**
     * Obtain the first available version.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @return A <code>DocumentVersion</code>.
     */
    public DocumentVersion readEarliestVersion(final Long documentId) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        try {
            final Long versionId = getArtifactModel().readEarliestVersionId(documentId);
            if (null == versionId) {
                return null;
            } else {
                return readVersion(documentId, versionId);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
	 * Obtain the latest document version.
	 * 
	 * @param documentId
	 *            The document id.
	 * @return The latest version.
	 */
    public DocumentVersion readLatestVersion(final Long documentId) {
		logger.logApiId();
		logger.logVariable("documentId", documentId);
		try {
            if (doesExistLatestVersion(documentId)) {
                return documentIO.readLatestVersion(documentId);
            } else {
                return null;
            }
		} catch (final Throwable t) {
            throw panic(t);
		}
	}

    /**
     * Read a document version.
     * 
     * @param documentId
     *            A document id.
     * @param versionId
     *            A version id.
     * @return A document version.
     */
    public DocumentVersion readVersion(final Long documentId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("versionId", versionId);
        try {
            return documentIO.getVersion(documentId, versionId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#readVersions(java.lang.Long)
     *
     */
    public List<DocumentVersion> readVersions(final Long documentId) {
        try {
            return readVersions(documentId, defaultVersionComparator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#readVersions(java.lang.Long, java.util.Comparator)
     *
     */
    public List<DocumentVersion> readVersions(Long documentId,
            Comparator<? super ArtifactVersion> comparator) {
        try {
            final List<DocumentVersion> versions =
                documentIO.listVersions(documentId);
            Collections.sort(versions, comparator);
            return versions;
        } catch (final Throwable t) {
            throw panic(t);
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
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("versionId", versionId);
        try {
            return documentIO.readVersionSize(documentId, versionId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#release(com.thinkparity.codebase.model.document.Document)
     *
     */
    public void release(final DocumentFileLock lock) {
        try {
            lock.release();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#remove(com.thinkparity.codebase.model.document.DocumentLock,
     *      java.lang.Long)
     * 
     */
    public void remove(final DocumentFileLock lock, final Long documentId) {
        try {
            final Document document = read(documentId);
            Assert.assertTrue(lock.getFile().setReadOnly(),
                    "Cannot remove document {0}.", document.getName());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#removeListener(com.thinkparity.ophelia.model.util.EventListener)
     */
    @Override
    public void removeListener(final DocumentListener listener) {
        super.removeListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.document.DocumentModel#rename(java.lang.Long,
     *      java.lang.String)
     * 
     */
    public void rename(final Long documentId, final String renameTo)
            throws CannotLockException {
        try {
            final Document document = read(documentId);
            final DocumentFileLock lock = lock(document);
            try {
                // rename the document
                document.setName(renameTo);
                documentIO.update(document);
            } finally {
                release(lock);
            }

            // rename the local file
            renameFile(lock, renameTo);
        } catch (final CannotLockException clx) {
            throw clx;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#revertDraft(com.thinkparity.codebase.model.document.DocumentLock,
     *      java.lang.Long)
     * 
     */
    public void revertDraft(final DocumentFileLock lock, final Long documentId) {
        try {
            final Document document = read(documentId);
            final DocumentVersion latestVersion = readLatestVersion(documentId);
            deleteFile(lock);
            lock(lock, document);
            writeVersion(latestVersion, lock);
            lock.getFile().setWritable(true, true);
            lock.release();
            try {
                Assert.assertTrue(lock.getFile().setLastModified(
                        latestVersion.getCreatedOn().getTimeInMillis()),
                        "Could not set last modified for document {0}.",
                        latestVersion.getName());
            } finally {
                lock(lock, document);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#updateDraft(com.thinkparity.ophelia.model.document.DocumentFileLock,
     *      java.lang.Long, java.io.InputStream)
     * 
     */
    public void updateDraft(final DocumentFileLock lock, final Long documentId,
            final InputStream content) {
        try {
            final Document document = read(documentId);
            deleteFile(lock);
            lock(lock, document);
            streamToChannel(content, lock.getFileChannel(0L));
            lock.release();
            try {
                Assert.assertTrue(lock.getFile().setLastModified(
                        currentDateTime().getTimeInMillis()),
                        "Could not set last modified for document {0}.",
                        document.getName());
            } finally {
                lock(lock, document);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.DocumentModel#updateDraft(java.lang.Long,
     *      java.io.InputStream)
     * 
     */
    public void updateDraft(final Long documentId, final InputStream content)
            throws CannotLockException {
        try {
            final Document document = read(documentId);
            final DocumentFileLock lock = lock(document);
            try {
                updateDraft(lock, documentId, content);
            } finally {
                release(lock);
            }
        } catch (final CannotLockException clx) {
            throw clx;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#uploadStream(com.thinkparity.codebase.model.stream.StreamMonitor,
     *      com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public void uploadStream(final StreamMonitor monitor,
            final DocumentVersion version) {
        try {
            final UploadStream delegate = newDelegate(UploadStream.class);
            delegate.setMonitor(monitor);
            
            delegate.setVersion(version);
            delegate.uploadStream();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.documentIO = IOFactory.getDefault(workspace).createDocumentHandler();
        this.localFilesDirectory = new File(workspace.getDataDirectory(), DirectoryNames.Workspace.Data.LOCAL);
        if(!localFilesDirectory.exists())
            Assert.assertTrue(localFilesDirectory.mkdir(),
                    "Cannot create directory {0}.", localFilesDirectory);
    }

    /**
     * Obtain the document persistence io.
     * 
     * @return A <code>DocumentIOHandler</code>.
     */
    DocumentIOHandler getDocumentIO() {
        return documentIO;
    }

	/**
     * Assert that the document's draft exists.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param assertMessage
     *            An assertion message <code>String</code>.
     * @param assertArguments
     *            An assertion message's arguments <code>Object...</code>.
     */
    private void assertDoesExistDraft(final Long documentId,
            final String assertMessage, final Object... assertArguments) {
        Assert.assertTrue(doesExistDraft(documentId), assertMessage,
                assertArguments);
    }

    /**
     * Assert that the document's draft is modified.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param assertMessage
     *            An assertion message <code>String</code>.
     * @param assertArguments
     *            An assertion message's arguments <code>Object...</code>.
     */
    private void assertDraftIsModified(final DocumentFileLock lock,
            final Long documentId, final String assertMessage,
            final Object... assertArguments) {
        Assert.assertTrue(isDraftModified(lock, documentId), assertMessage,
                assertArguments);
    }

    /**
     * Assert that the document's draft is modified.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param assertMessage
     *            An assertion message <code>String</code>.
     * @param assertArguments
     *            An assertion message's arguments <code>Object...</code>.
     */
    private void assertDraftIsModified(final Long documentId,
            final String assertMessage, final Object... assertArguments) {
        Assert.assertTrue(isDraftModified(documentId), assertMessage,
                assertArguments);
    }

    /**
     * Create a document. Simply create the document; the artifact remote info
     * and stream the content to the draft file.
     * 
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     * @param name
     *            A name <code>String</code>.
     * @param content
     *            The draft content <code>InputStream</code>.
     * @param createdBy
     *            A created by user id <code>JabberId</code>.
     * @param createdOn
     *            A created on <code>Calendar</code>.
     * @return A <code>Document</code>.
     */
    private Document create(final UUID uniqueId, final String name,
            final InputStream content, final JabberId createdBy,
            final Calendar createdOn) throws CannotLockException, IOException {
        // create document
        final Document document = create(uniqueId, name, createdBy, createdOn);
        final DocumentFileLock lock = lock(document);
        try {
            streamToChannel(content, lock.getFileChannel(0L));
        } finally {
            release(lock);
        }
        Assert.assertTrue(lock.getFile().setLastModified(
                createdOn.getTimeInMillis()),
                "Could not set last modified for document {0}.", name);
        return read(document.getId());
    }

    /**
     * Create a document. Simply create the document and the artifact remote
     * info object in the database.
     * 
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     * @param name
     *            A name <code>String</code>.
     * @param createdBy
     *            A created by user id <code>JabberId</code>.
     * @param createdOn
     *            A created on <code>Calendar</code>.
     * @return A <code>Document</code>.
     */
    private Document create(final UUID uniqueId, final String name,
            final JabberId createdBy, final Calendar createdOn) {
        // create document
        final Document document = new Document();
        document.setCreatedBy(createdBy);
        document.setCreatedOn(createdOn);
        document.setFlags(Collections.<ArtifactFlag>emptyList());
        document.setUniqueId(uniqueId);
        document.setName(name);
        document.setUpdatedBy(document.getCreatedBy());
        document.setUpdatedOn(document.getCreatedOn());
        document.setState(ArtifactState.ACTIVE);
        documentIO.create(document);
        return read(document.getId());
    }

    /**
     * Create a document version.
     * 
     * @param documentId
     *            A document id.
     * @param versionId
     *            A document version id.
     * @param content
     *            The document's content.
     * @param createdBy
     *            Who creatd the version.
     * @param createdOn
     *            When the version was created.
     * @return A new document version.
     */
	private DocumentVersion createVersion(final Long documentId,
            final Long versionId, final InputStream stream,
            final JabberId createdBy, final Calendar createdOn)
            throws CannotLockException, IOException {
	    final File tempFile = workspace.createTempFile();
        try {
            // create a temp file containing the stream
            streamToFile(stream, tempFile);

    		// create version
            final Document document = read(documentId);
    		final DocumentVersion version = new DocumentVersion();
    		version.setArtifactId(documentId);
    		version.setArtifactName(document.getName());
    		version.setArtifactType(document.getType());
    		version.setArtifactUniqueId(document.getUniqueId());
            version.setChecksum(checksum(tempFile));
            version.setChecksumAlgorithm(ChecksumAlgorithm.MD5.name());
            version.setComment(null);
    		version.setCreatedBy(createdBy);
    		version.setCreatedOn(createdOn);
            version.setFlags(Collections.<ArtifactVersionFlag>emptyList());
            version.setName(null);
    		version.setUpdatedBy(version.getCreatedBy());
    		version.setUpdatedOn(version.getCreatedOn());
            version.setSize(tempFile.length());
            version.setVersionId(versionId);
            // create version content
            final InputStream databaseStream = new FileInputStream(tempFile);
            try {
                documentIO.createVersion(version, databaseStream, getBufferSize());
            } finally {
                databaseStream.close();
            }
    		// write local version file
            final DocumentFileLock versionLock = lockVersion(version);
            try {
                fileToChannel(tempFile, versionLock.getFileChannel(0L));
            } finally {
                release(versionLock);
            }
            Assert.assertTrue(versionLock.getFile().setLastModified(
                    version.getCreatedOn().getTimeInMillis()),
                    "Could not set last modified for document {0} version {1}.",
                    document.getName(), version.getVersionId());
            versionLock.getFile().setReadOnly();
    		// update document
    		document.setUpdatedBy(version.getUpdatedBy());
    		document.setUpdatedOn(version.getUpdatedOn());
    		documentIO.update(document);
    		return readVersion(documentId, versionId);
        } finally {
            Assert.assertTrue(tempFile.delete(),
                    "Cannot delete temp file {0}.", tempFile);
        }
	}

    /**
     * Delete a file.
     * 
     * @param lock
     *            A <code>DocumentFileLock</code>.
     */
    private void deleteFile(final DocumentFileLock lock) {
    	release(lock);
        Assert.assertTrue(lock.getFile().delete(), "Could not delete file {0}.", lock.getFile());
        final File parent = lock.getFile().getParentFile();
        if (0 == parent.list().length)
            Assert.assertTrue(parent.delete(), "Could not delete directory {0}.", parent);
    }

    /**
     * Delete the local document data.
     * 
     * @param lock
     *            A <code>DocumentFileLock</code>.
     * @param versionLocks
     *            A <code>Map</code> of <code>DocumentVersion</code>s to
     *            their <code>DocumentFileLock</code>s.
     * @param documentId
     *            A document id <code>Long</code>.
     */
    private void deleteLocal(final DocumentFileLock lock,
            final Map<DocumentVersion, DocumentFileLock> versionLocks,
            final Long documentId) {
        // delete versions
        final List<DocumentVersion> versions = readVersions(documentId);
        for (final DocumentVersion version : versions) {
            deleteFile(versionLocks.get(version));
            documentIO.deleteVersion(version.getArtifactId(), version.getVersionId());
        }
        // delete  index
        getIndexModel().deleteDocument(documentId);
        // delete document
        deleteFile(lock);
        documentIO.delete(documentId);
    }

    /**
     * Delete the local document data.  This will delete only the index data
     * and the document meta-data.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    private void deleteLocal(final Long documentId) {
        // delete  index
        getIndexModel().deleteDocument(documentId);
        documentIO.delete(documentId);
    }

    /**
     * Determine whether or not a draft exists.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @return True if the draft exists.
     */
    private Boolean doesExistDraft(final DocumentFileLock lock) {
        final File draftFile = getDraftFile(lock);
        return draftFile.exists();
    }

    /**
     * Obtain the document root directory.
     * 
     * @param document
     *            A <code>Document</code>.
     * @return A directory <code>File</code>.
     */
    private File getDocumentRoot(final Document document) {
        final File parent = new File(localFilesDirectory,
                nameGenerator.localDirectoryName(document));
        if(!parent.exists()) {
            Assert.assertTrue(parent.mkdir(),
                    "Cannot create directory {0}.", parent);
        }
        return parent;
    }

    /**
     * Obtain the draft file.
     * 
     * @param document
     *            A <code>Document</code>.
     * @return A draft <code>File</code>.
     */
    private File getDraftFile(final Document document) {
        final String child = nameGenerator.fileName(document);
        return new File(getDocumentRoot(document), child);
    }

    /**
     * Obtain the draft file.
     * 
     * @param document
     *            A <code>Document</code>.
     * @return A draft <code>File</code>.
     */
    private File getDraftFile(final DocumentFileLock lock) {
        return lock.getFile();
    }

    /**
     * Obtain the version file.
     * 
     * @param document
     *            A <code>Document</code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A draft <code>File</code>.
     */
    private File getVersionFile(final Document document,
            final DocumentVersion version) {
        final String child = nameGenerator.localFileName(version);
        return new File(getDocumentRoot(document), child);
    }

    /**
     * Lock the document and set the appropriate lock members.
     * 
     * @param lock
     *            A <code>DocumentFileLock</code>.
     * @param document
     *            A <code>Document</code>.
     * @throws CannotLockException
     */
    private void lock(final DocumentFileLock lock, final Document document)
            throws CannotLockException {
        final File file = getDraftFile(document);
        lock(lock, file, document.getName());
    }

    /**
     * Obtain an exclusive file lock on a document version file.
     * 
     * @param lock
     *            A <code>DocumentFileLock</code>.
     * @param document
     *            A <code>Document</code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     */
    private void lock(final DocumentFileLock lock, final Document document,
            final DocumentVersion version) throws CannotLockException {
        final File file = getVersionFile(document, version);
        lock(lock, file, new StringBuilder(document.getName()).append(':')
                .append(version.getVersionId()).toString());
    }

    /**
     * Obtain an exclusive lock on a file.
     * 
     * @param lock
     *            A <code>DocumentFileLock</code>.
     * @param file
     *            A <code>File</code>.
     * @param name
     *            A lock name <code>String</code>.
     * @throws CannotLockException
     */
    private void lock(final DocumentFileLock lock, final File file,
            final String name) throws CannotLockException {
        try {
            file.setWritable(true, true);
            final FileChannel channel = new RandomAccessFile(file, "rws").getChannel();
            final FileLock fileLock = channel.tryLock();
            if (null == fileLock) {
                channel.close();
                throw new CannotLockException(name);
            }
            lock.setFile(file);
            lock.setFileChannel(channel);
            lock.setFileLock(fileLock);
        } catch (final IOException iox) {
            throw new CannotLockException(name);
        }
    }

    /**
     * Create an instance of a delegate.
     * 
     * @param <D>
     *            A delegate type.
     * @param type
     *            The delegate type <code>Class</code>.
     * @return An instance of <code>D</code>.
     */
    private <D extends Delegate<DocumentModelImpl>> D newDelegate(
            final Class<D> type) {
        try {
            final D instance = type.newInstance();
            instance.initialize(this);
            return instance;
        } catch (final IllegalAccessException iax) {
            throw new ThinkParityException("Could not create delegate.", iax);
        } catch (final InstantiationException ix) {
            throw new ThinkParityException("Could not create delegate.", ix);
        }
    }

    /**
	 * Fire document created.
	 * 
	 * @param document
     *      A document.
     * @param eventGen
     *      The event generator.
	 */
	private void notifyDocumentCreated(final Document document,
            final DocumentModelEventGenerator eventGen) {
        notifyListeners(new EventNotifier<DocumentListener>() {
            public void notifyListener(final DocumentListener listener) {
				listener.documentCreated(eventGen.generate(document));
			}
		});
	}

    /**
	 * Fire document deleted.
	 * 
	 * @param document
     *      The document.
     * @param eventGen
     *      The event generator.
	 */
	private void notifyDocumentDeleted(final Document document,
            final DocumentModelEventGenerator eventGen) {
        notifyListeners(new EventNotifier<DocumentListener>() {
            public void notifyListener(final DocumentListener listener) {
				listener.documentDeleted(eventGen.generate(document));
			}
		});
	}

    /**
     * Open a file in an input stream.
     * 
     * @param file
     *            A <code>File</code>.
     * @return An <code>InputStream</code>.
     * @throws IOException
     */
    private InputStream openFile(final File file) throws IOException {
        return new FileInputStream(file);
    }

    /**
     * Rename a file.
     * 
     * @param lock
     *            A <code>DocumentFileLock</code> lock.
     * @param renameTo
     *            The new name <code>String</code>.
     */
    private void renameFile(final DocumentFileLock lock, final String renameTo) {
        final File renameToFile = new File(
                lock.getFile().getParentFile(), renameTo);
        Assert.assertTrue(lock.getFile().renameTo(renameToFile),
                "Could not rename file from {0} to {1}.", lock.getFile(),
                renameToFile);
    }

    /**
     * Write the contents of the document version to the document file lock's
     * underlying file channel.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @param lock
     *            A <code>DocumentFileLock<code>.
     */
    private void writeVersion(final DocumentVersion version,
            final DocumentFileLock lock) {
        openVersion(version.getArtifactId(), version.getVersionId(), new StreamOpener() {
            public void open(final InputStream stream) throws IOException {
                streamToChannel(stream, lock.getFileChannel(0L));
            }
        });
    }
}
