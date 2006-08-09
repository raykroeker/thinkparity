/*
 * Created On:  Sun Mar 06, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    /** A method id. */
    private static final String LOG_UWV = "[LMODEL] [DOCUMENT] [UPDATE WORKING VERSION] ";

    /** A logger info statement. */
    private static final String LOG_UWV_INFO = LOG_UWV;

	private static StringBuffer getApiId(final String api) {
        return getModelId("DOCUMENT").append(" ").append(api);
    }

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
                versionId, currentUserId(), currentDateTime(), confirmedBy);
        // fire event
        notifyConfirmationReceived(get(documentId),
                getVersion(documentId, versionId), remoteEventGen);
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
        debugVariable("name", name);
        debugVariable("content", content);
        assertIsSetCredentials();
        // create
        final Document document = create(UUIDGenerator.nextUUID(), 1L, name,
                content, currentUserId(), currentDateTime());
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
        debugVariable("documentId", documentId);
        final DocumentVersion version = readLatestVersion(documentId);
        return createVersion(documentId, nextVersionId(documentId),
                openVersionStream(version.getArtifactId(), version
                        .getVersionId()), currentUserId(), currentDateTime());
    }

	/**
	 * Delete a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	void delete(final Long documentId) throws ParityException {
		logger.info("[LMODEL] [DOCUMENT MODEL] [DELETE]");
		logger.debug(documentId);
        assertOnline("[LMODEL] [DOCUMENT MODEL] [DELETE] [USER IS NOT ONLINE]");

        final Document document = get(documentId);
        deleteLocal(document);

        // fire event
		notifyDocumentDeleted(null, localEventGen);
	}

    /**
	 * Obtain a document with a specified id.
	 * 
	 * @param id
	 *            The id of the document.
	 * @return The document
	 * @throws ParityException
	 */
	Document get(final Long id) {
		logger.info("get(Long)");
		logger.debug(id);
		return documentIO.get(id);
	}

    /**
	 * Obtain a document with the specified unique id.
	 * 
	 * @param documentUniqueId
	 *            The document unique id.
	 * @return The document.
	 */
	Document get(final UUID documentUniqueId) {
		logger.info("get(UUID)");
		logger.debug(documentUniqueId);
		try { return documentIO.get(documentUniqueId); }
		catch(final Throwable t) { throw translateError(getApiId("[GET]"), t); }
	}

	/**
	 * Obtain a document version.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version id.
	 * @return The document version.
	 * @throws ParityException
	 */
	DocumentVersion getVersion(final Long documentId, final Long versionId) {
		logger.info("getVersion(Long,Long)");
		logger.debug(documentId);
		logger.debug(versionId);
		try { return documentIO.getVersion(documentId, versionId); }
		catch(final Throwable t) {
            throw translateError(getApiId("[GET VERSION]"), t);
		}
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
		try { return documentIO.readVersionContent(documentId, versionId); }
		catch(final Throwable t) {
			throw translateError(getApiId("[GET VERSION CONTENT]"), t);
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
            final String name, final InputStream content) {
        logApiId();
        debugVariable("publishedBy", publishedBy);
        debugVariable("publishedOn", publishedOn);
        debugVariable("uniqueId", uniqueId);
        debugVariable("versionId", versionId);
        debugVariable("name", name);
        debugVariable("content", content);
        return handleDocumentSent(publishedBy, publishedOn, uniqueId,
                versionId, name, content);
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
            final String name, final InputStream content) {
        logApiId();
        debugVariable("sentBy", sentBy);
        debugVariable("sentOn", sentOn);
        debugVariable("uniqueId", uniqueId);
        debugVariable("versionId", versionId);
        debugVariable("name", name);
        debugVariable("content", content);
        final InternalArtifactModel artifactModel  = getInternalArtifactModel();
        final Document document;
        final DocumentVersion version;
        if(artifactModel.doesExist(uniqueId)) {
            logWarning("[DOCUMENT EXISTS]");
            document = get(uniqueId);
            if(artifactModel.doesVersionExist(document.getId(), versionId)) {
                logWarning("[DOCUMENT VERSION EXISTS]");
                version = getVersion(document.getId(), versionId);
            }
            else {
                version = createVersion(document.getId(), versionId, content,
                        sentBy, sentOn);
            }
        }
        else {
            document = create(uniqueId, versionId, name, content,
                    sentBy, sentOn);
            version = getVersion(document.getId(), versionId);
        }
        return version;
    }

	/**
	 * Determine whether or not the working version of the document is different
	 * from the last version.
	 * 
	 * @return True if the working version is different from the last version.
	 * @throws ParityException
	 */
	Boolean isDraftModified(final Long documentId) {
		logger.info(getApiId("[IS DRAFT MODIFIED]"));
		logger.debug(documentId);
        try { return determineIsDraftModified(documentId); }
        catch(final ParityException px) {
            throw translateError(getApiId("[IS DRAFT MODIFIED]"), px);
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
        // unlock
        unlock(documentId);

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
        auditKeyRecieved(documentId, currentUserId(), currentDateTime, acceptedBy);

        // fire the key request accepted event
        notifyKeyRequestAccepted(readUser(acceptedBy), get(documentId), remoteEventGen);
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
        iAModel.auditKeyRequestDenied(documentId, currentUserId(),
                currentDateTime, declinedBy);

        // fire the key request declined event
        notifyKeyRequestDeclined(
                readUser(declinedBy), get(documentId), remoteEventGen);
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
	 * Lock a document.
	 * 
	 * @param documentId
	 *            The document id.
	 * @throws ParityException
	 */
	void lock(final Long documentId) throws ParityException {
		logger.info("lock(Long)");
		logger.debug(documentId);
		try {
			// re-create the local file from the meta-data
			final Document document = get(documentId);
			final LocalFile localFile = getLocalFile(document);
			localFile.delete();
			localFile.write(readLatestVersionContent(documentId).getContent());
			localFile.lock();
		}
		catch(IOException iox) {
			logger.error("lock(Long)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("lock(Long)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Open a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	void open(final Long documentId) {
		logger.info("[LMODEL] [DOCUMENT] [OPEN]");
		logger.debug(documentId);
		try {
			final Document document = get(documentId);

			// open the local file
			final LocalFile localFile = getLocalFile(document);
			localFile.open();
		}
		catch(final Throwable t) {
            throw translateError(getApiId("[OPEN]"), t);
		}
	}

	/**
	 * Open a document version. Extract the version's content and open it.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version id.
	 * @throws ParityException
	 */
	void openVersion(final Long documentId, final Long versionId)
			throws ParityException {
		logger.info("openVersion(UUID,String)");
		logger.debug(documentId);
		logger.debug(versionId);
		try {
			final Document document = get(documentId);
			final DocumentVersion version = getVersion(documentId, versionId);

			final LocalFile localFile = getLocalFile(document, version);
			localFile.open();
		}
		catch(IOException iox) {
			logger.error("openVersion(UUID,String)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("openVersion(UUID,String)", rx);
			throw ParityErrorTranslator.translate(rx);
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
        logger.info(getApiId("[OPEN VERSION STREAM]"));
        logger.debug(documentId);
        logger.debug(versionId);
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
        logger.info(getApiId("[READ]"));
        logger.debug(documentId);
        return get(documentId);
    }

    /**
     * Read a list of audit events for a document.
     * 
     * @param documentId
     *            A document id.
     * @return A list of audit events.
     */
    List<AuditEvent> readAuditEvents(final Long documentId) {
        logger.info(getApiId("[READ AUDIT EVENTS]"));
        logger.debug(documentId);
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
		logger.info(getApiId("[READ HISTORY]"));
		logger.debug(documentId);
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
        logger.info(getApiId("[READ HISTORY]"));
        logger.debug(documentId);
        logger.debug(comparator);
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
		logger.info(getApiId("[READ HISTORY]"));
		logger.debug(documentId);
        logger.debug(comparator);
        logger.debug(filter);
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
        logger.info(getApiId("[READ HISTORY]"));
        logger.debug(documentId);
        logger.debug(filter);
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
		logger.info("getLatestVersion(Long)");
		logger.debug(documentId);
		try { return documentIO.readLatestVersion(documentId); }
		catch(final Throwable t) {
            throw translateError(getApiId("[READ LATEST VERSION]"), t);
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
        logger.info("[LMODEL] [DOCUMENT] [RENAME]");
        logger.debug(documentId);
        logger.debug(documentName);
        try {
            final Collection<DocumentVersion> dVersions = listVersions(documentId);
            Assert.assertTrue(
                    "[LMODEL] [DOCUMENT] [RENAME] [CANNOT RENAME A PUBLISHED DOCUMENT]",
                    dVersions.size() == 1);
            final Document d = get(documentId);
            final DocumentVersion dVersion = dVersions.iterator().next();
            final String originalName = d.getName();
    
            // delete the local files
            LocalFile dFile = getLocalFile(d);
            LocalFile dVersionFile = getLocalFile(d, dVersion);
    		dFile.delete();
            dVersionFile.delete();
    
            // rename the document and version
            d.setName(documentName);
            dVersion.setName(documentName);
            documentIO.update(d);
            documentIO.updateVersion(dVersion);
    
            // write the local files
            dFile = getLocalFile(d);
            final DocumentVersionContent versionContent =
                    documentIO.readVersionContent(documentId, dVersion.getVersionId());
            dVersionFile = getLocalFile(d, dVersion);
    
            dFile.write(versionContent.getContent());
            dVersionFile.write(versionContent.getContent());

            // audit the rename
            auditor.rename(documentId, currentDateTime(), currentUserId(),
                    originalName,documentName);
        }
        catch(final Throwable t) {
            throw translateError(getApiId("[RENAME]"), t);
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
		final Document d = get(documentId);
		d.setUpdatedOn(currentDateTime());
		documentIO.update(d);

        // fire event
        notifyKeyRequested(readUser(requestedBy), d, remoteEventGen);
	}

	/**
	 * Unlock a document.
	 * 
	 * @param documentId
	 *            The document id.
	 * @throws ParityException
	 */
	void unlock(final Long documentId) throws ParityException {
		logger.info("unlock(Long)");
		logger.debug(documentId);
		try {
			// re-create the local file from the meta-data
			final Document d = get(documentId);
			final LocalFile localFile = getLocalFile(d);
			localFile.delete();
			localFile.write(readLatestVersionContent(documentId).getContent());
		}
		catch(IOException iox) {
			logger.error("unlock(UUID)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("unlock(UUID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
     * Update the working version of a document.
     * 
     * @param documentId
     *            The document id.
     * @param updateFile
     *            The new working version.
     */
	void updateWorkingVersion(final Long documentId, final File updateFile)
            throws ParityException {
	    logger.info(LOG_UWV_INFO);
        logger.debug(documentId);
        logger.debug(updateFile);
        assertOnline(LOG_UWV_INFO + " [USER IS NOT ONLINE]");
        final LocalFile localFile = getLocalFile(get(documentId));
        InputStream is = null;
        try {
            is = openInputStream(updateFile);
            localFile.write(is);
        }
        catch(final Throwable t) {
            throw translateError(getApiId("[UPDATE WORKING VERSION]"), t);
        }
        finally {
            try { is.close(); }
            catch(final Throwable t) {
                throw translateError(getApiId("[UPDATE WORKING VERSION]"), t);
            }
        }
    }

    /**
     * Create a document.
     * @param uniqueId
     */
    private Document create(final UUID uniqueId, final Long versionId,
            final String name, final InputStream content,
            final JabberId createdBy, final Calendar createdOn) {
        try {
            // create the document
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

            // create the local file
            final LocalFile localFile = getLocalFile(document);
            localFile.write(StreamUtil.read(content));

            // create a version
            createVersion(document.getId(), versionId, content, createdBy,
                    createdOn);

            // create remote info
            final InternalArtifactModel aModel = getInternalArtifactModel();
            aModel.createRemoteInfo(document.getId(), createdBy, createdOn);

            // audit the creation
            auditor.create(document.getId(), createdBy, document.getCreatedOn());

            // index the document
            indexor.create(document.getId(), document.getName());

            return read(document.getId());
        }
        catch(final Throwable t) {
            throw translateError(getApiId("[CREATE]"), t);
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
            final JabberId createdBy, final Calendar createdOn) {
		logApiId();
        debugVariable("documentId", documentId);
        debugVariable("versionId", versionId);
        debugVariable("content", content);
        assertOnline("[USER NOT ONLINE]");
		try {
			final Document document = get(documentId);

			// read the document local file
			final LocalFile localFile = getLocalFile(document);
			localFile.read();

			// create a new version
			final DocumentVersion version = new DocumentVersion();
			version.setArtifactId(documentId);
			version.setArtifactType(document.getType());
			version.setArtifactUniqueId(document.getUniqueId());
			version.setChecksum(MD5Util.md5Hex(StreamUtil.read(content)));
			version.setCompression(Compression.NONE);
			version.setCreatedBy(createdBy.getUsername());
			version.setCreatedOn(createdOn);
			version.setEncoding(Encoding.BASE_64);
			version.setName(document.getName());
			version.setUpdatedBy(version.getCreatedBy());
			version.setUpdatedOn(version.getCreatedOn());
            version.setVersionId(versionId);
			final DocumentVersionContent versionContent = new DocumentVersionContent();
			versionContent.setContent(StreamUtil.read(content));
			versionContent.setVersion(version);
            documentIO.createVersion(version, versionContent);

			// create the version local file
			final LocalFile versionLocalFile = getLocalFile(document, version);
			versionLocalFile.write(content);
			versionLocalFile.lock();

			// update the document updated by\on
			document.setUpdatedBy(version.getCreatedBy());
			document.setUpdatedOn(version.getUpdatedOn());
			documentIO.update(document);

			return version;
		}
		catch(final Throwable t) {
			throw translateError(getApiId("[CREATE VERSION]"), t);
		}
	}

    /**
     * Delete only the local document data.
     * 
     * @param document
     *            A document.
     * @throws ParityException
     */
    private void deleteLocal(final Document document) throws ParityException {
        // delete team
        final InternalArtifactModel iAModel = getInternalArtifactModel();
        iAModel.deleteTeam(document.getId());

        // delete audit
        final InternalAuditModel iAuditModel = getInternalAuditModel();
        iAuditModel.delete(document.getId());

        // delete versions
        final Collection<DocumentVersion> versions = listVersions(document.getId());
		for(final DocumentVersion version : versions) {
            getLocalFile(document, version).delete();
			documentIO.deleteVersion(document.getId(), version.getVersionId());
        }

        // delete  index
        indexor.delete(document.getId());

        // delete document
        final LocalFile localFile = getLocalFile(document);
		localFile.delete();
		localFile.deleteParent();
		documentIO.delete(document.getId());
    }

    /** {@link #isDraftModified(Long)} */
	private Boolean determineIsDraftModified(final Long documentId)
            throws ParityException {
		try {
			final Document document = get(documentId);

			final LocalFile localFile = getLocalFile(document);
			localFile.read();
			final String workingVersionChecksum = localFile.getFileChecksum();

			final DocumentVersion version = readLatestVersion(documentId);
			// we might have no recorded versions (initial point)
			if(null == version) { return Boolean.TRUE; }
			else {
				return !version.getChecksum().equals(workingVersionChecksum);
			}
		}
		catch(final IOException iox) {
			logger.error("[LMODEL] [DOCUMENT] [IS WORKING VERSION EQUAL] [IO ERROR]", iox);
            logger.error(documentId);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(final RuntimeException rx) {
			logger.error("[LMODEL] [DOCUMENT] [IS WORKING VERSION EQUAL] [UNKNOWN ERROR]", rx);
            logger.error(documentId);
			throw ParityErrorTranslator.translate(rx);
		}
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
     * Obtain the next version id for a document.
     * 
     * @param documentId
     *            A document id.
     * @return The next version id.
     */
    private Long nextVersionId(final Long documentId) {
        final DocumentVersion latestVersion = readLatestVersion(documentId);
        return latestVersion.getVersionId() + 1;
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

    /**
     * Create an input stream from the input file.
     * 
     * @param inputFile
     *            The input file.
     * @return An input stream.
     * @throws FileNotFoundException
     */
    private InputStream openInputStream(final File inputFile)
            throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(inputFile), 512);
    }

	/**
     * Read the latest document version content.
     * 
     * @param documentId
     *            A document id.
     * @return The document version content.
     * @throws ParityException
     */
    private DocumentVersionContent readLatestVersionContent(
            final Long documentId) {
        return documentIO.readLatestVersionContent(documentId);
    }

    private User readUser(final JabberId jabberId) throws ParityException {
        return getInternalSessionModel().readUser(jabberId);
    }
}
