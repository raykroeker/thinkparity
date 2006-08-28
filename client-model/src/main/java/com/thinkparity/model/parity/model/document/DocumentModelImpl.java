/*
 * Created On:  Sun Mar 06, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.*;
import java.util.*;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.Constants.Compression;
import com.thinkparity.model.Constants.Encoding;
import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.DocumentEvent;
import com.thinkparity.model.parity.api.events.DocumentListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.artifact.InternalArtifactModel;
import com.thinkparity.model.parity.model.audit.HistoryItem;
import com.thinkparity.model.parity.model.audit.InternalAuditModel;
import com.thinkparity.model.parity.model.audit.event.AuditEvent;
import com.thinkparity.model.parity.model.filter.ArtifactFilterManager;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.history.DefaultFilter;
import com.thinkparity.model.parity.model.filter.history.HistoryFilterManager;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.DocumentIOHandler;
import com.thinkparity.model.parity.model.message.system.InternalSystemMessageModel;
import com.thinkparity.model.parity.model.progress.ProgressIndicator;
import com.thinkparity.model.parity.model.sort.ComparatorBuilder;
import com.thinkparity.model.parity.model.sort.ModelSorter;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.MD5Util;
import com.thinkparity.model.parity.util.UUIDGenerator;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * Implementation of the document model interface.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
class DocumentModelImpl extends AbstractModelImpl {

    /** A list of document listeners. */
	private static final List<DocumentListener> LISTENERS = new LinkedList<DocumentListener>();

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

    /** A document indexor. */
	private final DocumentIndexor indexor;

    /** A document event generator for local events. */
    private final DocumentModelEventGenerator localEventGen;

    /** A document event generator for remote events. */
    private final DocumentModelEventGenerator remoteEventGen;

	/**
	 * Create a DocumentModelImpl
	 * 
	 * @param workspace
	 *            The workspace to work within.
	 */
	DocumentModelImpl(final Workspace workspace) {
		super(workspace);
		final ComparatorBuilder comparatorBuilder = new ComparatorBuilder();
		this.auditor = new DocumentModelAuditor(getContext());
		this.defaultComparator = comparatorBuilder.createByName(Boolean.TRUE);
        this.defaultHistoryComparator = new ComparatorBuilder().createIdDescending();
        this.defaultHistoryFilter = new DefaultFilter();
		this.defaultVersionComparator =
			comparatorBuilder.createVersionById(Boolean.TRUE);
		this.documentIO = IOFactory.getDefault().createDocumentHandler();
		this.indexor = new DocumentIndexor(getContext());
        this.localEventGen = new DocumentModelEventGenerator(DocumentEvent.Source.LOCAL);
        this.remoteEventGen = new DocumentModelEventGenerator(DocumentEvent.Source.REMOTE);
	}

    /**
     * Add a document listener.  If the listener is already registered
     * nothing is done.
     *
     * @param l
     *      The document listener.
     */
	void addListener(final DocumentListener l) {
		logger.info("[LMODEL] [DOCUMENT] [ADD LISTENER]");
		logger.debug(l);
		Assert.assertNotNull("[LMODEL] [DOCUMENT] [ADD LISTENER] [NULL LISTENER]", l);
		synchronized (DocumentModelImpl.LISTENERS) {
			if(DocumentModelImpl.LISTENERS.contains(l)) { return; }
			DocumentModelImpl.LISTENERS.add(l);
		}
	}

	/**
	 * @param documentId
	 * @return
	 * @throws ParityException
	 */
	File archive(final Long documentId) throws ParityException {
		return archive(documentId, ProgressIndicator.emptyIndicator());
	}

	/**
	 * 
	 * @param documentId
	 * @param progressIndicator
	 * @return
	 * @throws ParityException
	 */
	File archive(final Long documentId,
			final ProgressIndicator progressIndicator) throws ParityException {
		logger.info("[LMODEL] [DOCUMENT] [ARCHIVE]");
		logger.debug(documentId);
		logger.debug(progressIndicator);
        throw Assert.createNotYetImplemented("DocumentModelImpl#archive(Long,ProgressIndicator)");
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
     * Confirm that the document sent previously has been received by the
     * specified user.
     * 
     * @param documentId
     *            The document id.
     * @param confirmedBy
     *            To whom the document was sent.
     * @throws ParityException
     */
    void confirmSend(final Long documentId, final Long versionId,
            final JabberId confirmedBy) throws ParityException {
        // audit the receipt
        getInternalArtifactModel().auditConfirmationReceipt(documentId,
                versionId, localUserId(), currentDateTime(), confirmedBy);
        // fire event
        notifyConfirmationReceived(read(documentId),
                readVersion(documentId, versionId), remoteEventGen);
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
        logApiId();
        logVariable("name", name);
        logVariable("content", content);
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
        logApiId();
        logVariable("documentId", documentId);
        try {
            assertDraftIsModified("[DRAFT IS NOT MODIFIED]", documentId);
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
        logApiId();
        logVariable("documentId", documentId);
        deleteLocal(documentId);
        // fire event
        notifyDocumentDeleted(null, localEventGen);
    }

    /**
	 * Obtain the content for a specific version.
	 * 
	 * @param documentId
	 *            The document id.
	 * @param versionId
	 *            The version id.
	 * @return The content.
	 * @throws ParityException
	 */
	DocumentVersionContent getVersionContent(final Long documentId,
			final Long versionId) {
		logger.info("getVersionContent(Long,Long)");
		logger.debug(documentId);
		logger.debug(versionId);
		try {
            return documentIO.readVersionContent(documentId, versionId);
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
            final String name, final String checksum, final InputStream content) {
        logApiId();
        logVariable("publishedBy", publishedBy);
        logVariable("publishedOn", publishedOn);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("name", name);
        logVariable("checksum", checksum);
        logVariable("content", content);
        return handleDocumentSent(publishedBy, publishedOn, uniqueId,
                versionId, name, checksum, content);
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
    DocumentVersion handleDocumentSent(final JabberId sentBy,
            final Calendar sentOn, final UUID uniqueId, final Long versionId,
            final String name, final String checksum, final InputStream content) {
        logApiId();
        logVariable("sentBy", sentBy);
        logVariable("sentOn", sentOn);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("name", name);
        logVariable("checksum", checksum);
        logVariable("content", content);
        try {
            final InternalArtifactModel artifactModel  = getInternalArtifactModel();
            final Document document;
            final DocumentVersion version;
            if(artifactModel.doesExist(uniqueId)) {
                logWarning("[DOCUMENT EXISTS]");
                document = read(uniqueId);
                if(artifactModel.doesVersionExist(document.getId(), versionId)) {
                    logWarning("[DOCUMENT VERSION EXISTS]");
                    version = readVersion(document.getId(), versionId);
                }
                else {
                    version = createVersion(document.getId(), versionId, content,
                            sentBy, sentOn);
                }
            }
            else {
                document = create(uniqueId, name, content, sentBy, sentOn);
                version = createVersion(document.getId(), versionId, content,
                        sentBy, sentOn);
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
		logApiId();
		logVariable("documentId", documentId);
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
     * A key request for a document was accepted.
     * 
     * @param documentId
     *            The document id.
     * @param acceptedBy
     *            By whom the request was accepted.
     * @throws ParityException
     */
    void keyRequestAccepted(final Long documentId, final JabberId acceptedBy)
            throws ParityException {
        // apply key flag
        final InternalArtifactModel iAModel = getInternalArtifactModel();
        iAModel.applyFlagKey(documentId);
        // remove seen flag
        iAModel.removeFlagSeen(documentId);

        // update the remote info row
        final Calendar currentDateTime = currentDateTime();
        iAModel.updateRemoteInfo(documentId, acceptedBy, currentDateTime);

        // create system message
        getInternalSystemMessageModel().
            createKeyResponse(documentId, Boolean.TRUE, acceptedBy);

        // audit receive key
        auditKeyRecieved(documentId, localUserId(), currentDateTime, acceptedBy);

        // fire the key request accepted event
        notifyKeyRequestAccepted(readUser(acceptedBy), read(documentId), remoteEventGen);
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
    void keyRequestDeclined(final Long documentId, final JabberId declinedBy)
            throws ParityException {
        final Calendar currentDateTime = currentDateTime();

        // remove seen flag
        final InternalArtifactModel iAModel = getInternalArtifactModel();
        iAModel.removeFlagSeen(documentId);

        // update the remote info row
        iAModel.updateRemoteInfo(documentId, declinedBy, currentDateTime);

        // create system message
        getInternalSystemMessageModel().
            createKeyResponse(documentId, Boolean.FALSE, declinedBy);

        // audit key request denied
        iAModel.auditKeyRequestDenied(documentId, localUserId(),
                currentDateTime, declinedBy);

        // fire the key request declined event
        notifyKeyRequestDeclined(
                readUser(declinedBy), read(documentId), remoteEventGen);
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
		logger.info("[LMODEL] [DOCUMENT] [LIST]");
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
		logger.info("[LMODEL] [DOCUMENT] [LIST SORTED]");
		logger.debug(comparator);
		try {
			final List<Document> documents = documentIO.list();
			ModelSorter.sortDocuments(documents, comparator);
			return documents;
		}
		catch(RuntimeException rx) {
			logger.error("list(UUID,Comparator<Artifact>)", rx);
			throw ParityErrorTranslator.translate(rx);
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
		logger.info("[LMODEL] [DOCUMENT] [LIST SORTED & FILTERED]");
		logger.debug(comparator);
		logger.debug(filter);
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
		logger.info("[LMODEL] [DOCUMENT] [LIST FILTERED]");
		logger.debug(filter);
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
		logger.info("listVersions(Long)");
		logger.debug(documentId);
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
		logger.info("listVersions(Document)");
		logger.debug(documentId);
		logger.debug(comparator);
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
	void open(final Long documentId) {
		logApiId();
        logVariable("documentId", documentId);
		try {
			final Document document = read(documentId);

			// open the local file
			final LocalFile localFile = getLocalFile(document);
			localFile.open();
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
	void openVersion(final Long documentId, final Long versionId) {
        logApiId();
        logVariable("documentId", documentId);
        logVariable("versionId", versionId);
		try {
			final Document document = read(documentId);
			final DocumentVersion version = readVersion(documentId, versionId);
			final LocalFile localFile = getLocalFile(document, version);
			localFile.open();
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
        logApiId();
        logVariable("documentId", documentId);
        logVariable("versionId", versionId);
        return documentIO.openStream(documentId, versionId);
    }

	/**
     * Read a document.
     * 
     * @param documentId
     *            A document id.
     * @return A document.
     */
    Document read(final Long documentId) {
        logApiId();
        logVariable("documentId", documentId);
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
		logApiId();
        logVariable("uniqueId", uniqueId);
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
        logApiId();
        logVariable("documentId", documentId);
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
        logApiId();
        logVariable("documentId", documentId);
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
        logApiId();
        logVariable("documentId", documentId);
        logVariable("comparator", comparator);
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
        logApiId();
        logVariable("documentId", documentId);
        logVariable("comparator", comparator);
        logVariable("filter", filter);
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
        logApiId();
        logVariable("documentId", documentId);
        logVariable("filter", filter);
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
		logApiId();
		logVariable("documentId", documentId);
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
        logApiId();
        logVariable("documentId", documentId);
        logVariable("versionId", versionId);
        try { return documentIO.getVersion(documentId, versionId); }
        catch(final Throwable t) {
            throw translateError(t);
        }
    }

    /**
	 * Remove a document listener.
	 * 
	 * @param l
	 *        A document listener.
	 */
	void removeListener(final DocumentListener l) {
		logger.info("[LMODEL] [DOCUMENT] [REMOVE LISTENER]");
		logger.debug(l);
		Assert.assertNotNull("[LMODEL] [DOCUMENT] [REMOVE LISTENER] [NULL LISTENER]", l);
		synchronized(DocumentModelImpl.LISTENERS) {
            if(!DocumentModelImpl.LISTENERS.contains(l)) { return; }
			DocumentModelImpl.LISTENERS.remove(l);
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
        logApiId();
        logVariable("documentId", documentId);
        logVariable("documentName", documentName);
        try {
            final Document document = read(documentId);
            final String originalName = document.getName();
            final LocalFile localFile = getLocalFile(document);
    
            // rename the document
            document.setName(documentName);
            documentIO.update(document);
    
            // rename the local file
            localFile.rename(documentName);

            // audit the rename
            auditor.rename(documentId, currentDateTime(), localUserId(),
                    originalName,documentName);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    
	void requestKey(final Long documentId, final JabberId requestedBy)
			throws ParityException {
		logger.info("[LMODEL] [DOCUMENT] [REQUEST KEY]");
		logger.debug(documentId);

		// create system message
		final InternalSystemMessageModel iSMModel = getInternalSystemMessageModel();
		iSMModel.createKeyRequest(documentId, requestedBy);

		// update the document's last update date
		final Document d = read(documentId);
		d.setUpdatedOn(currentDateTime());
		documentIO.update(d);

        // fire event
        notifyKeyRequested(readUser(requestedBy), d, remoteEventGen);
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
        logApiId();
        logVariable("documentId", documentId);
        
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
	    logApiId();
        logVariable("documentId", documentId);
        logVariable("content", content);
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
            document.setCreatedBy(createdBy.getUsername());
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
            // audit creation
            auditor.create(document.getId(), createdBy, document.getCreatedOn());
            // index document
            indexor.create(document.getId(), document.getName());
            // create local file
            final LocalFile localFile = getLocalFile(document);
            localFile.write(StreamUtil.read(content));
            return read(document.getId());
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
    		version.setCreatedBy(createdBy.getUsername());
    		version.setCreatedOn(createdOn);
    		version.setEncoding(Encoding.BASE_64);
    		version.setName(document.getName());
    		version.setUpdatedBy(version.getCreatedBy());
    		version.setUpdatedOn(version.getCreatedOn());
            version.setVersionId(versionId);
            // create version content
    		final DocumentVersionContent versionContent = new DocumentVersionContent();
    		versionContent.setContent(FileUtil.readBytes(tempContentFile));
    		versionContent.setVersion(version);
            documentIO.createVersion(version, versionContent);
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
        indexor.delete(documentId);
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
     * Fire confirmation received.
     *
     * @param document
     *      A document
     * @param eventGen
     *      The event generator.
     */
    private void notifyConfirmationReceived(final Document document,
            final DocumentVersion version,
            final DocumentModelEventGenerator eventGen) {
        synchronized(DocumentModelImpl.LISTENERS) {
            for(final DocumentListener l : DocumentModelImpl.LISTENERS) {
                l.confirmationReceived(eventGen.generate(document, version));
            }
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
	private void notifyDocumentCreated(final Document document, final DocumentModelEventGenerator eventGen) {
		synchronized(DocumentModelImpl.LISTENERS) {
			for(final DocumentListener l : DocumentModelImpl.LISTENERS) {
				l.documentCreated(eventGen.generate(document));
			}
		}
	}

    /**
	 * Fire document deleted.
	 * 
	 * @param document
     *      The document.
     * @param eventGen
     *      The event generator.
	 */
	private void notifyDocumentDeleted(final Document document, final DocumentModelEventGenerator eventGen) {
		synchronized(DocumentModelImpl.LISTENERS) {
			for(final DocumentListener l : DocumentModelImpl.LISTENERS) {
				l.documentDeleted(eventGen.generate(document));
			}
		}
	}

	/**
     * Fire key request accepted.
     * 
     * @param user
     *            A user.
     * @param document
     *            A document.
     * @param eventGen
     *            The event generator.
     */
    private void notifyKeyRequestAccepted(final User user,
            final Document document, final DocumentModelEventGenerator eventGen) {
        synchronized(DocumentModelImpl.LISTENERS) {
            for(final DocumentListener l : DocumentModelImpl.LISTENERS) {
                l.keyRequestAccepted(eventGen.generate(user, document));
            }
        }
    }

    /**
     * Fire key request declined.
     * 
     * @param user
     *            A user.
     * @param document
     *            A document.
     * @param eventGen
     *            The event generator.
     */
    private void notifyKeyRequestDeclined(final User user,
            final Document document, final DocumentModelEventGenerator eventGen) {
        synchronized(DocumentModelImpl.LISTENERS) {
            for(final DocumentListener l : DocumentModelImpl.LISTENERS) {
                l.keyRequestDeclined(eventGen.generate(user, document));
            }
        }
    }

    /**
     * Fire key requested.
     * 
     * @param user
     *            A user.
     * @param document
     *            A document
     * @param eventGen
     *            The event generator.
     */
    private void notifyKeyRequested(final User user, final Document document,
            final DocumentModelEventGenerator eventGen) {
        synchronized(DocumentModelImpl.LISTENERS) {
            for(final DocumentListener l : DocumentModelImpl.LISTENERS) {
                l.keyRequested(eventGen.generate(user, document));
            }
        }
    }

    private User readUser(final JabberId jabberId) throws ParityException {
        return getInternalSessionModel().readUser(jabberId);
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
        logApiId();
        logVariable("documentId", documentId);
        logVariable("versionId", versionId);
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
