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
import java.util.UUID;

import com.thinkparity.codebase.FileUtil;
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

import com.thinkparity.ophelia.model.AbstractModelImpl;
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
class DocumentModelImpl extends AbstractModelImpl<DocumentListener> {

	/** A document auditor. */
	private final DocumentModelAuditor auditor;

	/** The default document comparator. */
	private final Comparator<Artifact> defaultComparator;

	/** The default history comparator. */
	private final Comparator<? super HistoryItem> defaultHistoryComparator;

	/** The default history filter. */
    private final Filter<? super HistoryItem> defaultHistoryFilter;

    /** The default document version comparator. */
	private final Comparator<ArtifactVersion> defaultVersionComparator;

	/** A document reader/writer. */
	private final DocumentIOHandler documentIO;

    /** A document event generator for local events. */
    private final DocumentModelEventGenerator localEventGen;

	/**
	 * Create a DocumentModelImpl
	 * 
	 * @param workspace
	 *            The workspace to work within.
	 */
	DocumentModelImpl(final Environment environment, final Workspace workspace) {
		super(environment, workspace);
		final ComparatorBuilder comparatorBuilder = new ComparatorBuilder();
		this.auditor = new DocumentModelAuditor(internalModelFactory);
		this.defaultComparator = comparatorBuilder.createByName(Boolean.TRUE);
        this.defaultHistoryComparator = new ComparatorBuilder().createIdDescending();
        this.defaultHistoryFilter = new DefaultFilter();
		this.defaultVersionComparator =
			comparatorBuilder.createVersionById(Boolean.TRUE);
		this.documentIO = IOFactory.getDefault(workspace).createDocumentHandler();
        this.localEventGen = new DocumentModelEventGenerator(DocumentEvent.Source.LOCAL);
	}

    /**
     * @see com.thinkparity.ophelia.model.AbstractModelImpl#addListener(com.thinkparity.ophelia.model.util.EventListener)
     */
    @Override
    protected boolean addListener(final DocumentListener listener) {
        return super.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.AbstractModelImpl#removeListener(com.thinkparity.ophelia.model.util.EventListener)
     */
    @Override
    protected boolean removeListener(final DocumentListener listener) {
        return super.removeListener(listener);
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
     * Create a document.
     * 
     * @param name
     *            The document name.
     * @param content
     *            The document's content input stream.
     * @return The document.
     */
    Document create(final String name, final InputStream content) {
        logger.logApiId();
        logger.logVariable("name", name);
        logger.logVariable("content", content);
        assertIsSetCredentials();
        // create
        final Document document = create(UUIDGenerator.nextUUID(), name,
                content, localUserId(), currentDateTime());
        // fire event
        notifyDocumentCreated(document, localEventGen);
        return document;
    }

    /**
     * Create a duplicate document version.
     * 
     * @param documentId
     *            A document id.
     * @return The new version.
     * @throws ParityException
     */
    DocumentVersion createVersion(final Long documentId) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        try {
            assertDraftIsModified("Draft is not modified.", documentId);
            final LocalFile localFile = getLocalFile(read(documentId));
            final InputStream content = localFile.openStream();
            try {
                return createVersion(documentId, readNextVersionId(documentId),
                        content, localUserId(), currentDateTime());
            } finally {
                content.close();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete a document.
     * 
     * @param documentId
     *                The document unique id.
     * @throws ParityException
     */
    void delete(final Long documentId) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        deleteLocal(documentId);
        // fire event
        notifyDocumentDeleted(null, localEventGen);
    }

    /**
     * Obtain a document name generator.
     * 
     * @return A <code>DocumentNameGenerator</code>.
     */
    DocumentNameGenerator getNameGenerator() {
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
    DocumentVersion handleDocumentPublished(final JabberId publishedBy,
            final Calendar publishedOn, final UUID uniqueId, final Long versionId,
            final String name, final String checksum, final String streamId) {
        logger.logApiId();
        logger.logVariable("publishedBy", publishedBy);
        logger.logVariable("publishedOn", publishedOn);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("name", name);
        logger.logVariable("checksum", checksum);
        logger.logVariable("streamId", streamId);
        return handleDocumentSent(publishedBy, publishedOn, uniqueId,
                versionId, name, checksum, streamId);
    }

    /**
     * Handle the receipt of a document from the thinkParity network. If the
     * document does not exist; it will be created; if the version does not
     * exist it will be created.
     * 
     * @param sentBy
     *            By whom the document was sent.
     * @param sentOn
     *            When the document was sent.
     * @param uniqueId
     *            The document unique id.
     * @param versionId
     *            The document version id.
     * @param name
     *            The document name.
     * @param content
     *            The document content.
     */
    private DocumentVersion handleDocumentSent(final JabberId sentBy,
            final Calendar sentOn, final UUID uniqueId, final Long versionId,
            final String name, final String checksum, final String streamId) {
        logger.logApiId();
        logger.logVariable("sentBy", sentBy);
        logger.logVariable("sentOn", sentOn);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("name", name);
        logger.logVariable("checksum", checksum);
        logger.logVariable("streamId", streamId);
        try {
            final File streamFile = downloadStream(streamId);
            final InternalArtifactModel artifactModel  = getInternalArtifactModel();
            final Document document;
            final DocumentVersion version;
            if (artifactModel.doesExist(uniqueId)) {
                logger.logWarning("Document {0} already exists.", uniqueId);
                document = read(uniqueId);
                if (artifactModel.doesVersionExist(document.getId(), versionId)) {
                    logger.logWarning(
                            "Document version {0}:{1} already exists.",
                            uniqueId, versionId);
                    version = readVersion(document.getId(), versionId);
                }
                else {
                    final InputStream stream = new FileInputStream(streamFile);
                    try {
                        version = createVersion(document.getId(), versionId,
                                stream, sentBy, sentOn);
                    } finally {
                        stream.close();
                    }
                }
            }
            else {
                InputStream stream = new FileInputStream(streamFile);
                try {
                    document = create(uniqueId, name, stream, sentBy, sentOn);
                } finally {
                    stream.close();
                }
                stream = new FileInputStream(streamFile);
                try {
                    version = createVersion(document.getId(), versionId,
                            stream, sentBy, sentOn);
                } finally {
                    stream.close();
                }
            }
            return version;
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
	Boolean isDraftModified(final Long documentId) {
		logger.logApiId();
		logger.logVariable("documentId", documentId);
        try {
            final List<DocumentVersion> versions = listVersions(documentId);
            if (0 == versions.size()) {
                return Boolean.TRUE;
            } else {
                final Document document = read(documentId);
                final LocalFile localFile = getLocalFile(document);
                localFile.read();
                final String draftChecksum = localFile.getFileChecksum();
                return !versions.get(versions.size() - 1).getChecksum().equals(draftChecksum);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
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
	 * Open a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	void open(final Long documentId, final Opener opener) {
		logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("opener", opener);
		try {
			final Document document = read(documentId);

			// open the local file
			final LocalFile localFile = getLocalFile(document);
            if (!localFile.exists()) {
                final DocumentVersion latestVersion = readLatestVersion(documentId);
                final InputStream stream = openVersionStream(documentId,
                        latestVersion.getVersionId());
                try {
                    localFile.write(stream);
                } finally {
                    stream.close();
                }
            }
            
            localFile.open(opener);
		} catch (final Throwable t) {
            throw translateError(t);
		}
	}

	/**
	 * Open a document version. Extract the version's content and open it.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version id.
	 */
	void openVersion(final Long documentId, final Long versionId,
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
                final InputStream stream = openVersionStream(
                        documentId, versionId);
                try {
                    localFile.write(stream);
                } finally {
                    stream.close();
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
    InputStream openVersionStream(final Long documentId, final Long versionId) {
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
    void printDraft(final Long documentId, final Printer printer) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("printer", printer);
        try {
            final Document document = read(documentId);
            final LocalFile localFile = getLocalFile(document);
            printer.print(localFile.createTempClone(workspace));
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
    void printVersion(final Long documentId, final Long versionId,
            final Printer printer) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("printer", printer);
        try {
            final Document document = read(documentId);
            final DocumentVersion version = readVersion(documentId, versionId);
            final LocalFile localFile = getLocalFile(document, version);
            printer.print(localFile.createTempClone(workspace));
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
    Document read(final Long documentId) {
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
	Document read(final UUID uniqueId) {
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
    List<AuditEvent> readAuditEvents(final Long documentId) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        return getInternalAuditModel().read(documentId);
    }

    /**
     * Read the document history.
     * 
     * @param documentId
     *            A document id.
     * @return A list of history items.
     */
    List<DocumentHistoryItem> readHistory(final Long documentId) {
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
    List<DocumentHistoryItem> readHistory(final Long documentId,
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
    List<DocumentHistoryItem> readHistory(final Long documentId,
            final Comparator<? super HistoryItem> comparator,
            final Filter<? super HistoryItem> filter) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
		final DocumentHistoryBuilder historyBuilder =
		        new DocumentHistoryBuilder(getInternalDocumentModel(), l18n);
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
    List<DocumentHistoryItem> readHistory(final Long documentId,
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
	DocumentVersion readLatestVersion(final Long documentId) {
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
    DocumentVersion readVersion(final Long documentId, final Long versionId) {
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
     * Read the version size.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return The version size <code>Integer</code>.
     */
    Long readVersionSize(final Long documentId, final Long versionId) {
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
     * Rename a document.
     *
     * @param documentId
     *      A document id.
     * @param documentName
     *      A document name.
     */
    void rename(final Long documentId, final String documentName) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("documentName", documentName);
        try {
            final Document document = read(documentId);
            final LocalFile localFile = getLocalFile(document);
    
            // rename the document
            document.setName(documentName);
            documentIO.update(document);
    
            // rename the local file
            localFile.rename(documentName);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Revert a document draft to a version.
     * 
     * @param documentId
     *            A document id.
     * @param artifactVersionId
     *            A version id.
     */
    void revertDraft(final Long documentId) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        
        revertDraft(documentId, readLatestVersion(documentId).getVersionId());
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
	void updateDraft(final Long documentId, final InputStream content) {
	    logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("content", content);
        final LocalFile localFile = getLocalFile(read(documentId));
        try {
            localFile.write(content);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Assert that the document's draft is modified.
     * 
     * @param assertion
     *            An assertion.
     * @param documentId
     *            A document id.
     * @see DocumentModelImpl#isDraftModified(Long)
     */
    private void assertDraftIsModified(final Object assertion, final Long documentId) {
        Assert.assertTrue(assertion, isDraftModified(documentId));
    }

    /**
     * Audit the document's creation.
     * 
     * @param document
     *            A document.
     * @param createdBy
     *            The created by user id <code>JabberId</code>.
     * @param createdOn
     *            The created on date <code>Calendar</code>.
     */
    private void auditDocumentCreated(final Document document,
            final JabberId createdBy, final Calendar createdOn) {
        auditor.create(document, createdBy, createdOn);
    }

    /**
     * Create a document.
     * 
     * @param uniqueId
     *            A unique id.
     * @param name
     *            A name.
     * @param content
     *            The content.
     * @param createdBy
     *            The creator.
     * @param createdOn
     *            The creation date.
     * @return A document.
     */
    private Document create(final UUID uniqueId, final String name,
            final InputStream content, final JabberId createdBy,
            final Calendar createdOn) {
        try {
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
            // create remote info
            final InternalArtifactModel aModel = getInternalArtifactModel();
            aModel.createRemoteInfo(document.getId(), createdBy, createdOn);
            // create local file
            final LocalFile localFile = getLocalFile(document);
            localFile.write(StreamUtil.read(content));

            final Document postCreation = read(document.getId());
            // audit
            this.auditDocumentCreated(postCreation, createdBy, createdOn);
            return postCreation;
        } catch (final Throwable t) {
            throw translateError(t);
        }
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
            try { StreamUtil.copy(content, tempOutput); }
            finally { tempOutput.close(); }

    		// create version
            final Document document = read(documentId);
    		final DocumentVersion version = new DocumentVersion();
    		version.setArtifactId(documentId);
    		version.setArtifactType(document.getType());
    		version.setArtifactUniqueId(document.getUniqueId());
    		version.setChecksum(MD5Util.md5Hex(FileUtil.readBytes(tempContentFile)));
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
            final InputStream stream = new FileInputStream(tempContentFile);
            try {
                documentIO.createVersion(version, stream);
            } finally {
                stream.close();
            }
    		// write local version file
    		final LocalFile localFile = getLocalFile(document, version);
            final InputStream tempInput = new BufferedInputStream(new FileInputStream(tempContentFile));
    		try { localFile.write(tempInput); }
            finally { tempInput.close(); }
    		localFile.lock();
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
    private void deleteLocal(final Long documentId) {
        final Document document = read(documentId);
        // delete audit
        final InternalAuditModel iAuditModel = getInternalAuditModel();
        iAuditModel.delete(documentId);
        // delete versions
        final Collection<DocumentVersion> versions = listVersions(documentId);
		for(final DocumentVersion version : versions) {
            getLocalFile(document, version).delete();
			documentIO.deleteVersion(version.getArtifactId(), version.getVersionId());
        }
        // delete  index
        getIndexModel().deleteDocument(documentId);
        // delete document
        final LocalFile localFile = getLocalFile(document);
		localFile.delete();
		localFile.deleteParent();
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
		return new LocalFile(workspace, document);
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
		return new LocalFile(workspace, document, version);
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
     * Revert a document draft to a version.
     * 
     * @param documentId
     *            A document id.
     * @param versionId
     *            A version id.
     */
    private void revertDraft(final Long documentId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("versionId", versionId);
        assertDraftIsModified("DRAFT IS NOT MODIFIED", documentId);
        try {
            final Document document = read(documentId);
            final LocalFile draftFile = getLocalFile(document);
            draftFile.delete();
            final InputStream inputStream = openVersionStream(documentId, versionId);
            try {
                draftFile.write(inputStream);
            } finally {
                inputStream.close();
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

}
