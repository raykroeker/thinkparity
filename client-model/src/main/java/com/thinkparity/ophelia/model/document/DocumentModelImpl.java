/*
 * Created On:  Sun Mar 06, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.DownloadMonitor;
import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.Constants.Compression;
import com.thinkparity.ophelia.model.Constants.Encoding;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.audit.HistoryItem;
import com.thinkparity.ophelia.model.audit.InternalAuditModel;
import com.thinkparity.ophelia.model.audit.event.AuditEvent;
import com.thinkparity.ophelia.model.events.DocumentEvent;
import com.thinkparity.ophelia.model.events.DocumentListener;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.DocumentIOHandler;
import com.thinkparity.ophelia.model.util.MD5Util;
import com.thinkparity.ophelia.model.util.Opener;
import com.thinkparity.ophelia.model.util.Printer;
import com.thinkparity.ophelia.model.util.UUIDGenerator;
import com.thinkparity.ophelia.model.util.filter.ArtifactFilterManager;
import com.thinkparity.ophelia.model.util.filter.Filter;
import com.thinkparity.ophelia.model.util.filter.history.DefaultFilter;
import com.thinkparity.ophelia.model.util.filter.history.HistoryFilterManager;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;
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

    /** The size of the buffer used by create draft when writing a file. */
    private static final Integer CREATE_DRAFT_WRITE_BUFFER;

    /** The size of the buffer used by create version when writing a file. */
    private static final Integer CREATE_VERSION_WRITE_BUFFER;

    /** The size of the buffer used by create when writing a file. */
    private static final Integer CREATE_WRITE_BUFFER;

    /** The size of the buffer used by open version when writing a file. */
    private static final Integer OPEN_VERSION_WRITE_BUFFER;

    /** The size of the buffer used by open when writing a file. */
    private static final Integer OPEN_WRITE_BUFFER;

    /** The size of the buffer used by print draft when copying a file. */
    private static final Integer PRINT_DRAFT_COPY_BUFFER;

    /** The size of the buffer used by print version when copying a file. */
    private static final Integer PRINT_VERSION_COPY_BUFFER;

    /** The size of the buffer used by revert draft when copying a file. */
    private static final Integer REVERT_DRAFT_WRITE_BUFFER;

    /** The size of the buffer used by update draft when copying a file. */
    private static final Integer UPDATE_DRAFT_WRITE_BUFFER;

    static {
        final Integer buffer = 1024;
        CREATE_DRAFT_WRITE_BUFFER = buffer;
        CREATE_WRITE_BUFFER = buffer;
        CREATE_VERSION_WRITE_BUFFER = buffer;
        OPEN_WRITE_BUFFER = buffer;
        OPEN_VERSION_WRITE_BUFFER = buffer;
        PRINT_DRAFT_COPY_BUFFER = buffer;
        PRINT_VERSION_COPY_BUFFER = buffer;
        REVERT_DRAFT_WRITE_BUFFER = buffer;
        UPDATE_DRAFT_WRITE_BUFFER = buffer;
    }

    /** A document auditor. */
	private DocumentModelAuditor auditor;

	/** The default document comparator. */
	private final Comparator<Artifact> defaultComparator;

	/** The default history comparator. */
	private final Comparator<? super HistoryItem> defaultHistoryComparator;

	/** The default history filter. */
    private final Filter<? super HistoryItem> defaultHistoryFilter;

	/** The default document version comparator. */
	private final Comparator<ArtifactVersion> defaultVersionComparator;

    /** A document reader/writer. */
	private DocumentIOHandler documentIO;

	/** A document event generator for local events. */
    private final DocumentModelEventGenerator localEventGen;

    /**
	 * Create a DocumentModelImpl
	 * 
	 * @param workspace
	 *            The workspace to work within.
	 */
	public DocumentModelImpl() {
		super();
		final ComparatorBuilder comparatorBuilder = new ComparatorBuilder();
		this.defaultComparator = comparatorBuilder.createByName(Boolean.TRUE);
        this.defaultHistoryComparator = new ComparatorBuilder().createIdDescending();
        this.defaultHistoryFilter = new DefaultFilter();
		this.defaultVersionComparator = comparatorBuilder.createVersionById(Boolean.TRUE);
        this.localEventGen = new DocumentModelEventGenerator(DocumentEvent.Source.LOCAL);
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
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#createDraft(com.thinkparity.codebase.model.document.DocumentLock,
     *      java.lang.Long)
     * 
     */
    public void createDraft(final DocumentLock lock, final Long documentId) {
        try {
            final Document document = read(documentId);
            final DocumentVersion latestVersion = readLatestVersion(documentId);

            final LocalFile draftFile = getLocalFile(document);
            final InputStream versionStream = openVersionStream(
                    document.getId(), latestVersion.getVersionId());
            try {
                draftFile.write(lock, versionStream,
                        latestVersion.getCreatedOn().getTimeInMillis(),
                        CREATE_DRAFT_WRITE_BUFFER);
            } finally {
                versionStream.close();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#createVersion(java.lang.Long,
     *      java.util.Calendar)
     * 
     */
    public DocumentVersion createVersion(final Long documentId,
            final Calendar createdOn) {
        try {
            assertDraftIsModified(documentId, "Draft has not been modified.");
            final LocalFile localFile = getLocalFile(read(documentId));
            final InputStream content = localFile.openInputStream();
            try {
                return createVersion(documentId, readNextVersionId(documentId),
                        content, localUserId(), createdOn);
            } finally {
                content.close();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#delete(com.thinkparity.codebase.model.document.DocumentLock,
     *      java.lang.Long)
     * 
     */
    public void delete(final DocumentLock lock,
            final Map<DocumentVersion, DocumentVersionLock> versionLocks,
            final Long documentId) {
        try {
            deleteLocal(lock, versionLocks, documentId);
            // fire event
            notifyDocumentDeleted(null, localEventGen);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#deleteDraft(com.thinkparity.codebase.model.document.DocumentLock,
     *      java.lang.Long)
     * 
     */
    public void deleteDraft(final DocumentLock lock, final Long documentId) {
        try {
            assertDoesExistDraft(documentId, "Draft does not exist.");
            final Document document = read(documentId);
            final LocalFile draftFile = getLocalFile(document);
            draftFile.delete(lock);
        } catch (final Throwable t) {
            throw translateError(t);
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
            final Document document = read(documentId);
            final LocalFile draftFile = getLocalFile(document);
            return draftFile.exists();
        } catch (final Throwable t) {
            throw translateError(t);
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
            throw translateError(t);
        }
    }

    /**
     * Handle the publish of a document from the thinkParity network. The
     * implementation is identical to sending a document.
     * 
     * @param publishedBy
     *            By whom the document was published.
     * @param publishedOn
     *            When the document was published.
     * @param uniqueId
     *            The document unique id.
     * @param versionId
     *            The document version id.
     * @param name
     *            The document name.
     * @param content
     *            The document content.
     */
    public DocumentVersion handleDocumentPublished(
            final DocumentVersion version, final String streamId,
            final JabberId publishedBy, final Calendar publishedOn) {
        logger.logApiId();
        logger.logVariable("version", version);
        logger.logVariable("streamId", streamId);
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
                    final File streamFile = downloadStream(new DownloadMonitor() {
                        public void chunkDownloaded(final int chunkSize) {
                            logger.logInfo("Downloaded {0} bytes", chunkSize);
                        }
                    }, streamId);
                    final InputStream stream = new FileInputStream(streamFile);
                    try {
                        localVersion = createVersion(document.getId(),
                                version.getVersionId(), stream, publishedBy,
                                publishedOn);
                    } finally {
                        stream.close();
                    }
                }
            }
            else {
                document = create(version.getArtifactUniqueId(),
                        version.getName(), publishedBy, publishedOn);
                final File streamFile = downloadStream(new DownloadMonitor() {
                    public void chunkDownloaded(final int chunkSize) {}
                }, streamId);
                final InputStream stream = new FileInputStream(streamFile);
                try {
                    localVersion = createVersion(document.getId(),
                            version.getVersionId(), stream, publishedBy,
                            publishedOn);
                } finally {
                    stream.close();
                }
                // index
                final Long containerId = artifactModel.readId(version.getArtifactUniqueId());
                getIndexModel().indexDocument(containerId, document.getId());
            }
            return localVersion;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Determine whether or not the draft of the document is different from the
     * latest version.
     * 
     * @return True if the draft is different from the latest version.
     */
    public Boolean isDraftModified(final Long documentId) {
		logger.logApiId();
		logger.logVariable("documentId", documentId);
        try {
            final Long latestVersionId = getArtifactModel().readLatestVersionId(documentId);
            if (null == latestVersionId) {
                return Boolean.TRUE;
            } else {
                final Document document = read(documentId);
                final DocumentVersion latestVersion =
                    readVersion(documentId, latestVersionId);

                final LocalFile draftFile = getLocalFile(document);
                final long latestVersionModifiedTime =
                    latestVersion.getCreatedOn().getTimeInMillis();

                /* the time stamp is checked first because it is fast;
                 * however documents are considered different only if the
                 * checksums are different */
                if (draftFile.lastModified() == latestVersionModifiedTime) {
                    return Boolean.FALSE;
                } else {
                    return !latestVersion.getChecksum().equals(
                            draftFile.readChecksum());
                }
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#lock(com.thinkparity.codebase.model.document.Document)
     *
     */
    public DocumentLock lock(final Document document) {
        try {
            final LocalFile localFile = getLocalFile(document);
            final DocumentLock lock = new DocumentLock();
            lock.setDocumentId(document.getId());
            return localFile.lock(lock);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#lockVersion(com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public DocumentVersionLock lockVersion(final DocumentVersion version) {
        try {
            final Document document = read(version.getArtifactId());
            final LocalFile localFile = getLocalFile(document, version);
            final DocumentVersionLock lock = new DocumentVersionLock();
            lock.setDocumentId(version.getArtifactId());
            lock.setVersionId(version.getVersionId());
            return localFile.lock(lock);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
	 * Open a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
    public void open(final Long documentId, final Opener opener) {
		logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("opener", opener);
		try {
			final Document document = read(documentId);

			// open the local file
			final LocalFile localFile = getLocalFile(document);
            if (!localFile.exists()) {
                final DocumentVersion latestVersion = readLatestVersion(documentId);
                final DocumentLock lock = lock(document);
                try {
                    final InputStream stream = openVersionStream(documentId,
                            latestVersion.getVersionId());
                    try {
                        localFile.write(lock, stream,
                                latestVersion.getCreatedOn().getTimeInMillis(),
                                OPEN_WRITE_BUFFER);
                    } finally {
                        stream.close();
                    }
                } finally {
                    release(lock);
                }
            }
            
            localFile.open(opener);
		} catch (final Throwable t) {
            throw translateError(t);
		}
	}

	/**
     * @see com.thinkparity.ophelia.model.document.DocumentModel#openVersion(java.lang.Long,
     *      java.lang.Long, com.thinkparity.ophelia.model.util.Opener)
     * 
     */
    public void openVersion(final Long documentId, final Long versionId,
            final Opener opener) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("opener", opener);
		try {
			final Document document = read(documentId);
			final DocumentVersion version = readVersion(documentId, versionId);
			final LocalFile localFile = getLocalFile(document, version);
            if (!localFile.exists()) {
                final DocumentVersionLock lock = lockVersion(version);
                try {
                    final InputStream stream = openVersionStream(
                            documentId, versionId);
                    try {
                        localFile.write(lock, stream,
                                version.getCreatedOn().getTimeInMillis(),
                                OPEN_VERSION_WRITE_BUFFER);
                    } finally {
                        stream.close();
                    }
                } finally {
                    release(lock);
                }
            }

			localFile.open(opener);
		} catch (final Throwable t) {
            throw translateError(t);
		}
	}

    /**
     * Open an input stream to read the document version. Note: It is a good
     * idea to buffer the input stream.
     * 
     * @param documentId
     *            A document id.
     * @param versionId
     *            A document version id.
     * @return A list of document versions and their streams.
     */
    public InputStream openVersionStream(final Long documentId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("versionId", versionId);
        return documentIO.openStream(documentId, versionId);
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
            final LocalFile localFile = getLocalFile(document);
            final File copyTo = workspace.createTempFile(document.getName());
            try {
                localFile.copy(copyTo, PRINT_DRAFT_COPY_BUFFER);
                printer.print(copyTo);
            } finally {
                Assert.assertTrue(copyTo.delete(),
                        "Could not delete temporary file {0}.", copyTo);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
            final LocalFile localFile = getLocalFile(document, version);

            final File copyTo = workspace.createTempFile(document.getName());
            try {
                localFile.copy(copyTo, PRINT_VERSION_COPY_BUFFER);
                printer.print(copyTo);
            } finally {
                Assert.assertTrue(copyTo.delete(),
                        "Could not delete temporary file {0}.", copyTo);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        return documentIO.get(documentId);
    }

	/**
	 * Obtain a document with the specified unique id.
	 * 
	 * @param documentUniqueId
	 *            The document unique id.
	 * @return The document.
	 */
    public Document read(final UUID uniqueId) {
		logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
		try {
            return documentIO.get(uniqueId);
		} catch (final Throwable t) {
            throw translateError(t);
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
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        return getAuditModel().read(documentId);
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
            throw translateError(t);
        }
    }

	/**
     * Read the document history.
     * 
     * @param documentId
     *            A document id.
     * @return A list of history items.
     */
    public List<DocumentHistoryItem> readHistory(final Long documentId) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
		return readHistory(documentId, defaultHistoryComparator);
	}

	/**
     * Read the document history.
     * 
     * @param documentId
     *            A document id.
     * @param comparator
     *            A history comparator.
     * @return A list of history items.
     */
    public List<DocumentHistoryItem> readHistory(final Long documentId,
            final Comparator<? super HistoryItem> comparator) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("comparator", comparator);
        return readHistory(documentId, comparator, defaultHistoryFilter);
    }

    /**
     * Read the document history.
     * 
     * @param documentId
     *            A document id.
     * @param comparator
     *            A history item comparator.
     * @param filter
     *            A history item filter.
     * @return A list of history items.
     * @throws ParityException
     */
    public List<DocumentHistoryItem> readHistory(final Long documentId,
            final Comparator<? super HistoryItem> comparator,
            final Filter<? super HistoryItem> filter) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
		final DocumentHistoryBuilder historyBuilder =
		        new DocumentHistoryBuilder(getDocumentModel(), l18n);
		final List<DocumentHistoryItem> history =
                historyBuilder.createHistory(documentId);
        HistoryFilterManager.filter(history, filter);
		ModelSorter.sortHistory(history, comparator);
		return history;
	}

    /**
     * Read the document history.
     * 
     * @param documentId
     *            A document id.
     * @param comparator
     *            A history comparator.
     * @return A list of history items.
     */
    public List<DocumentHistoryItem> readHistory(final Long documentId,
            final Filter<? super HistoryItem> filter) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("filter", filter);
        return readHistory(documentId, defaultHistoryComparator, filter);
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
            throw translateError(t);
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
            throw translateError(t);
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
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#release(com.thinkparity.codebase.model.document.Document)
     *
     */
    public void release(final DocumentLock lock) {
        try {
            final Document document = read(lock.getDocumentId());
            final LocalFile localFile = getLocalFile(document);
            localFile.release(lock);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#remove(com.thinkparity.codebase.model.document.DocumentLock,
     *      java.lang.Long)
     * 
     */
    public void remove(final DocumentLock lock, final Long documentId) {
        try {
            final LocalFile localFile = getLocalFile(get(documentId));
            localFile.setReadOnly(lock);
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
    public void rename(final Long documentId, final String documentName) {
        try {
            final Document document = read(documentId);
            final LocalFile localFile = getLocalFile(document);
            final DocumentLock lock = lock(document);
            try {
                // rename the document
                document.setName(documentName);
                documentIO.update(document);

                // rename the local file
                localFile.rename(lock, documentName);
            } finally {
                release(lock);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.document.InternalDocumentModel#revertDraft(com.thinkparity.codebase.model.document.DocumentLock,
     *      java.lang.Long)
     * 
     */
    public void revertDraft(final DocumentLock lock, final Long documentId) {
        try {
            revertDraft(lock, documentId, readLatestVersion(documentId).getVersionId());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Update the working version of a document. Note that the content stream is
     * not closed.
     * 
     * @param documentId
     *            The document id.
     * @param content
     *            The new content.
     */
    public void updateDraft(final Long documentId, final InputStream content) {
	    logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("content", content);
        try {
            final Document document = read(documentId);
            final LocalFile localFile = getLocalFile(document);
            final DocumentLock lock = lock(document);
            try {
                localFile.write(lock, content,
                        currentDateTime().getTimeInMillis(),
                        UPDATE_DRAFT_WRITE_BUFFER);
            } finally {
                release(lock);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
        this.auditor = new DocumentModelAuditor(modelFactory);
    }

    /**
	 * Audit a key received event.
	 * 
	 * @param artifactId
	 *            The document id.
	 * @param createdBy
	 *            The creator.
	 * @param createdOn
	 *            The creation date.
	 * @param receivedFrom
	 *            The user the key was received from.
	 */
	void auditKeyRecieved(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn, final JabberId receivedFrom)
            throws ParityException {
		auditor.receiveKey(artifactId, createdBy, createdOn, receivedFrom);
	}

    /**
	 * Obtain a list of documents.
	 * 
	 * @return A list of documents sorted by name.
	 * @throws ParityException
	 * 
	 * @see ComparatorBuilder
	 * @see #list(UUID, Comparator)
	 */
	Collection<Document> list() throws ParityException {
		logger.logApiId();
		return list(defaultComparator);
	}

    /**
	 * Obtain a list of sorted documents.
	 * 
	 * @param comparator
	 *            The comparator.
	 * @return A sorted list of documents.
	 * @throws ParityException
	 * 
	 * @see ComparatorBuilder
	 */
	Collection<Document> list(final Comparator<Artifact> comparator)
			throws ParityException {
        logger.logApiId();
		logger.logVariable("comparator", comparator);
		try {
			final List<Document> documents = documentIO.list();
			ModelSorter.sortDocuments(documents, comparator);
			return documents;
		} catch (final Throwable t) {
            throw translateError(t);
		}
	}

    /**
     * Obtain a filtered and sorted list of documents.
     * 
     * @param comparator
     *            The comparator.
     * @param filter
     *            The document filter.
     * @return A list of documents.
     * @throws ParityException
     */
	Collection<Document> list(final Comparator<Artifact> comparator,
			final Filter<? super Artifact> filter) throws ParityException {
	    logger.logApiId();
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
		final List<Document> documents = documentIO.list();
		ArtifactFilterManager.filter(documents, filter);
		ModelSorter.sortDocuments(documents, comparator);
		return documents;
	}

    /**
     * Obtain a filtered list of documents.
     * 
     * @param filter
     *            The document filter.
     * @return A list of documents.
     * @throws ParityException
     */
	Collection<Document> list(final Filter<? super Artifact> filter)
			throws ParityException {
        logger.logApiId();
        logger.logVariable("filter", filter);
		return list(defaultComparator, filter);
	}

	/**
	 * Obtain a list of document versions for a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The list of document versions; ordered by the version id
	 *         ascending.
	 * @throws ParityException
	 * 
	 * @see #listVersions(UUID, Comparator)
	 */
	List<DocumentVersion> listVersions(final Long documentId) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
		return listVersions(documentId, defaultVersionComparator);
	}

    /**
	 * Obtain a list of document versions for a document; ordered by the
	 * specified comparator.
	 * 
	 * @param documentId
	 *            The document id.
	 * @param comparator
	 *            The document version sorter.
	 * @return The list of document versions.
	 * @throws ParityException
	 * 
	 * @see ComparatorBuilder
	 */
	List<DocumentVersion> listVersions(final Long documentId,
			final Comparator<ArtifactVersion> comparator) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("comparator", comparator);
		final List<DocumentVersion> versions = documentIO.listVersions(documentId);
		ModelSorter.sortDocumentVersions(versions, comparator);
		return versions;
	}

    /**
     * Fire a local document updated event.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    void notifyDocumentUpdated(final Long documentId) {
        notifyDocumentUpdated(read(documentId), localEventGen);
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
            final Calendar createdOn) throws IOException {
        // create document
        final Document document = create(uniqueId, name, createdBy, createdOn);
        // create local file
        final LocalFile localFile = getLocalFile(document);
        final DocumentLock lock = lock(document);
        try {
            localFile.write(lock, content, createdOn.getTimeInMillis(),
                    CREATE_WRITE_BUFFER);
        } finally {
            release(lock);
        }
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
        // create artifact remote info
        final InternalArtifactModel aModel = getArtifactModel();
        aModel.createRemoteInfo(document.getId(), createdBy, createdOn);
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
            final Long versionId, final InputStream content,
            final JabberId createdBy, final Calendar createdOn)
            throws IOException {
	    final File tempContentFile = workspace.createTempFile();
        try {
            // create a temp file with the content
            final OutputStream tempOutput = new BufferedOutputStream(new FileOutputStream(tempContentFile));
            try {
                StreamUtil.copy(content, tempOutput);
            } finally {
                tempOutput.close();
            }

    		// create version
            final Document document = read(documentId);
    		final DocumentVersion version = new DocumentVersion();
    		version.setArtifactId(documentId);
    		version.setArtifactType(document.getType());
    		version.setArtifactUniqueId(document.getUniqueId());
            final InputStream checksumStream = new FileInputStream(tempContentFile);
		    try {
                version.setChecksum(MD5Util.md5Hex(checksumStream));
            } finally {
                checksumStream.close();
            }
    		version.setCompression(Compression.NONE);
    		version.setCreatedBy(createdBy);
    		version.setCreatedOn(createdOn);
    		version.setEncoding(Encoding.BASE_64);
    		version.setName(document.getName());
    		version.setUpdatedBy(version.getCreatedBy());
    		version.setUpdatedOn(version.getCreatedOn());
            version.setSize(tempContentFile.length());
            version.setVersionId(versionId);
            // create version content
            final InputStream versionStream = new FileInputStream(tempContentFile);
            try {
                documentIO.createVersion(version, versionStream);
            } finally {
                versionStream.close();
            }
    		// write local version file
    		final LocalFile localFile = getLocalFile(document, version);
            final DocumentVersionLock lock = lockVersion(version);
            try {
                final InputStream tempInput = new BufferedInputStream(new FileInputStream(tempContentFile));
                try {
                    localFile.write(lock, tempInput,
                            version.getCreatedOn().getTimeInMillis(),
                            CREATE_VERSION_WRITE_BUFFER);
                } finally {
                    tempInput.close();
                }
        		localFile.setReadOnly(lock);
            } finally {
                release(lock);
            }
    		// update document
    		document.setUpdatedBy(version.getUpdatedBy());
    		document.setUpdatedOn(version.getUpdatedOn());
    		documentIO.update(document);
    		return readVersion(documentId, versionId);
        } finally {
            Assert.assertTrue(
                    "[CANNOT DELETE TEMPORARY FILE]",
                    tempContentFile.delete());
        }
	}

    /**
     * Delete only the local document data.
     * 
     * @param document
     *            A document.
     * @throws ParityException
     */
    private void deleteLocal(final DocumentLock lock,
            final Map<DocumentVersion, DocumentVersionLock> versionLocks,
            final Long documentId) {
        final Document document = read(documentId);
        // delete audit
        final InternalAuditModel iAuditModel = getAuditModel();
        iAuditModel.delete(documentId);
        // delete versions
        final Collection<DocumentVersion> versions = listVersions(documentId);
		for(final DocumentVersion version : versions) {
            getLocalFile(document, version).delete(versionLocks.get(version));
			documentIO.deleteVersion(version.getArtifactId(), version.getVersionId());
        }
        // delete  index
        getIndexModel().deleteDocument(documentId);
        // delete document
        final LocalFile localFile = getLocalFile(document);
		if (localFile.exists())
            localFile.delete(lock);
		documentIO.delete(documentId);
    }

    /**
	 * Create a document local file reference for a given document.
	 * 
	 * @param document
	 *            The document.
	 * @return The document local file reference.
	 */
	private LocalFile getLocalFile(final Document document) {
		return new LocalFile(this, workspace, document);
	}

    /**
	 * Create a document local file reference for a given version.
	 * 
	 * @param version
	 *            The version.
	 * @return The document local file reference.
	 */
	private LocalFile getLocalFile(final Document document,
			final DocumentVersion version) {
		return new LocalFile(this, workspace, document, version);
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
     * Fire document updated.
     * 
     * @param document
     *            A <code>Document</code>.
     * @param eventGen
     *            A <code>DocumentModelEventGenerator</code>.
     */
    private void notifyDocumentUpdated(final Document document,
            final DocumentModelEventGenerator eventGen) {
        notifyListeners(new EventNotifier<DocumentListener>() {
            public void notifyListener(final DocumentListener listener) {
                listener.documentUpdated(eventGen.generate(document));
            }
        });
    }

    /**
     * Release a document version lock.
     * 
     * @param lock
     *            A <code>DocumentVersionLock</code>.
     */
    private void release(final DocumentVersionLock lock) {
        try {
            final Document document = read(lock.getDocumentId());
            final DocumentVersion version = readVersion(lock.getDocumentId(), lock.getVersionId());
            final LocalFile localFile = getLocalFile(document, version);
            localFile.release(lock);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Revert a document draft to a version's content.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     */
    private void revertDraft(final DocumentLock lock, final Long documentId,
            final Long versionId) throws IOException {
        final Document document = read(documentId);
        final LocalFile draftFile = getLocalFile(document);
        draftFile.delete(lock);
        final InputStream inputStream = openVersionStream(documentId, versionId);
        final DocumentVersion version = readVersion(documentId, versionId);
        try {
            draftFile.write(lock, inputStream,
                    version.getCreatedOn().getTimeInMillis(),
                    REVERT_DRAFT_WRITE_BUFFER);
        } finally {
            inputStream.close();
        }
    }

}
