/*
 * Mar 6, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;

import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.DocumentEvent;
import com.thinkparity.model.parity.api.events.DocumentListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.L18nContext;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.artifact.InternalArtifactModel;
import com.thinkparity.model.parity.model.audit.InternalAuditModel;
import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.parity.model.document.history.HistoryItemBuilder;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.ModelFilterManager;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.DocumentHistoryIOHandler;
import com.thinkparity.model.parity.model.io.handler.DocumentIOHandler;
import com.thinkparity.model.parity.model.message.system.InternalSystemMessageModel;
import com.thinkparity.model.parity.model.progress.ProgressIndicator;
import com.thinkparity.model.parity.model.session.InternalSessionModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.sort.ComparatorBuilder;
import com.thinkparity.model.parity.model.sort.ModelSorter;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.MD5Util;
import com.thinkparity.model.parity.util.UUIDGenerator;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * Implementation of the document model interface.
 * 
 * @author raykroeker@gmail.com
 * @version 1.5.2.43
 */
class DocumentModelImpl extends AbstractModelImpl {

    /** A list of document listeners. */
	private static final List<DocumentListener> LISTENERS = new LinkedList<DocumentListener>();

    /** A method id. */
    private static final String LOG_UWV = "[LMODEL] [DOCUMENT] [UPDATE WORKING VERSION] ";

    /** A logger error statement. */
    private static final String LOG_UWV_ERROR_FNFX =
        LOG_UWV + "[FILE NOT FOUND]";

    /** A logger error statement. */
    private static final String LOG_UWV_ERROR_IOX =
        LOG_UWV + "[IO ERROR]";

    /** A logger error statement. */
    private static final String LOG_UWV_ERROR_IOX_2 =
        LOG_UWV + "[IO ERROR ON CLOSE]";

	/** A logger info statement. */
    private static final String LOG_UWV_INFO = LOG_UWV;

	/** A document auditor. */
	private final DocumentModelAuditor auditor;

	/** The default document comparator. */
	private final Comparator<Artifact> defaultComparator;

	/** The default document history comparator. */
	private Comparator<HistoryItem> defaultHistoryItemComparator;

	/** The default document version comparator. */
	private final Comparator<ArtifactVersion> defaultVersionComparator;

	/** A document history reader/writer. */
	private final DocumentHistoryIOHandler documentHistoryIO;

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
		super(workspace, L18nContext.DOCUMENT);
		final ComparatorBuilder comparatorBuilder = new ComparatorBuilder();
		this.auditor = new DocumentModelAuditor(getContext());
		this.defaultComparator = comparatorBuilder.createByName(Boolean.TRUE);
		this.defaultVersionComparator =
			comparatorBuilder.createVersionById(Boolean.TRUE);
		this.documentIO = IOFactory.getDefault().createDocumentHandler();
		this.documentHistoryIO = IOFactory.getPDF().createDocumentHistoryIOHandler();
		this.indexor = new DocumentIndexor(getContext());
        this.localEventGen = new DocumentModelEventGenerator(DocumentEvent.Source.LOCAL);
        this.remoteEventGen = new DocumentModelEventGenerator(DocumentEvent.Source.REMOTE);
	}

	/**
	 * Accept the key request.
	 * 
	 * @param keyRequestId
	 *            The key request id.
	 */
	void acceptKeyRequest(final Long keyRequestId) throws ParityException {
		getInternalArtifactModel().acceptKeyRequest(keyRequestId);
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
		assertValidOutputDirectory();

		// 1  Audit Archive
		auditor.archive(documentId, currentUserId(), currentDateTime());

		// 2  Archive History
		final File archive = documentHistoryIO.archive(documentId, readHistory(documentId));

        // fire event
        notifyDocumentArchived(get(documentId), localEventGen);

        return archive;
	}

    /**
     * Fire the document archived listener event.
     *
     * @param document
     *      A document,
     * @param eventGen
     *      The event generator.
     */
    private void notifyDocumentArchived(final Document document, final DocumentModelEventGenerator eventGen) {
        synchronized(LISTENERS) {
            for(final DocumentListener l : LISTENERS) {
                l.documentArchived(eventGen.generate(document));
            }
        }
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
	 * Close a document.
	 * 
	 * @param documentId
	 *            The document id.
	 * @throws NotTrueAssertion
	 *             <ul>
	 *             <li>If the user is offline.
	 *             <li>If the logged in user is not the key holder.
	 *             </ul>
	 * @throws ParityException
	 */
	void close(final Long documentId) throws ParityException {
		logger.info("[LMODEL] [DOCUMENT] [CLOSE]");
		logger.debug(documentId);
		assertLoggedInUserIsKeyHolder(documentId);
		try {
			// close the document
			final Document document = get(documentId);
			assertStateTransition(document.getState(), ArtifactState.CLOSED);
			documentIO.updateState(document.getId(), ArtifactState.CLOSED);

			// lock the document
			lock(documentId);

			// send the closeure to the server
			final InternalSessionModel iSModel = getInternalSessionModel();
			iSModel.sendClose(documentId);

			// audit the closeure
			final Document d = get(documentId);
			auditor.close(d.getId(), currentUserId(), d.getUpdatedOn(), currentUserId());

			// fire event
			notifyDocumentClosed(d, localEventGen);
		}
		catch(final RuntimeException rx) {
			logger.error("Cannot close document:  " + documentId, rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	void close(final UUID documentUniqueId, final JabberId closedBy)
			throws ParityException {
		logger.info("[LMODEL] [DOCUMENT] [CLOSE BY REQUEST]");
		logger.debug(documentUniqueId);
		logger.debug(closedBy);
		try {
			final Document document = get(documentUniqueId);

			// close the document
			assertStateTransition(document.getState(), ArtifactState.CLOSED);
			documentIO.updateState(document.getId(), ArtifactState.CLOSED);

			// lock the document
			lock(document.getId());

			// update the remote info row
			final InternalArtifactModel iAModel = getInternalArtifactModel();
			iAModel.updateRemoteInfo(document.getId(), closedBy, currentDateTime());

			// audit the closure
			final Document d = get(document.getId());
			auditor.close(d.getId(), closedBy, d.getUpdatedOn(), currentUserId());

			// fire event
			notifyDocumentClosed(document, remoteEventGen);
		}
		catch(final RuntimeException rx) {
			logger.error("Cannot close document:  " + documentUniqueId, rx);
			throw ParityErrorTranslator.translate(rx);
		}
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
    void confirmSend(final Long documentId, final JabberId confirmedBy)
            throws ParityException {
        // audit the receipt
        getInternalArtifactModel().auditConfirmationReceipt(
                documentId, currentUserId(), currentDateTime(), confirmedBy);
        // fire event
        notifyConfirmationReceived(get(documentId), remoteEventGen);
    }

	/**
	 * Import a document. This will take a name, description and location of a
	 * document and copy the document into an internal store, then returns the
	 * newly created document.
	 * 
	 * @param name
	 *            Name of the document you wish to import.
	 * @param description
	 *            Description of the document you wish to import.
	 * @param file
	 *            File content of the document
	 * @return The newly created document.
	 * @throws ParityException
	 */
	Document create(final String name, final String description, final File file)
			throws ParityException {
		logger.info("create(Project,String,String,File)");
		logger.debug(name);
		logger.debug(description);
		logger.debug(file);
		assertCanCreateArtifacts();
		assertIsSessionValid();
		Assert.assertTrue(
				// TODO Centralize business rules about document creation.
				"File \"" + file.getAbsolutePath() + "\" does not exist.",
				file.exists());
		Assert.assertTrue(
				"create(Project,String,String,File)",
				(file.length() <= IParityModelConstants.FILE_SIZE_UPPER_BOUNDS));
		try {
			final Calendar now = currentDateTime();
			final Document document = new Document(preferences.getUsername(),
					now, description, Collections.<ArtifactFlag>emptyList(),
					UUIDGenerator.nextUUID(), name, preferences.getUsername(),
					now);
			document.setState(ArtifactState.ACTIVE);
			final byte[] contentBytes = FileUtil.readBytes(file);
			final DocumentContent content = new DocumentContent();
			content.setContent(contentBytes);
			content.setChecksum(MD5Util.md5Hex(contentBytes));
			content.setDocumentId(document.getId());

			// send a creation packet
			final InternalSessionModel iSessionModel =
				SessionModel.getInternalModel(getContext());
			iSessionModel.sendCreate(document);

			// create the document
			documentIO.create(document, content);

			// create the local file
			final LocalFile localFile = getLocalFile(document);
			localFile.write(contentBytes);

            // flag the document with the key
            final InternalArtifactModel iAModel = getInternalArtifactModel();
            iAModel.applyFlagKey(document.getId());

            // create the remote info row
			getInternalArtifactModel().createRemoteInfo(
                document.getId(), currentUserId(), now);

			// audit the creation
			auditor.create(document.getId(), currentUserId(), document.getCreatedOn());

			// index the creation
			indexor.create(document.getId(), document.getName());

            // fire event
			notifyDocumentCreated(get(document.getId()), localEventGen);
			return get(document.getId());
		}
		catch(final IOException iox) {
			logger.error("[LMODEL] [DOCUMENT] [CREATE] [IO ERROR]", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(final RuntimeException rx) {
			logger.error("[LMODEL] [DOCUMENT] [CREATE] [UNEXPECTED ERROR]", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Create a new document version based upon an existing document. This will
	 * check the cache for updates to the document, write the updates to the
	 * document, then create a new version based upon that document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param action
	 *            The action causing the version creation.
	 * @param actionData
	 *            The data associated with the version creation action.
	 * @return The newly created version.
	 * 
	 * TODO If the user has ownership of the document; the local copy should
	 * *NEVER* be overwritten.
	 * 
	 * TODO Have the ability to send individual versions to a contact.
	 * 
	 * @throws ParityException
	 * @throws NotTrueAssertion
	 *             If the logged in user is not the key holder.
	 */
	DocumentVersion createVersion(final Long documentId) throws ParityException {
		logger.info("createVersion(Long)");
		logger.debug(documentId);
		assertLoggedInUserIsKeyHolder(documentId);
		try {
			final Document document = get(documentId);
			final DocumentContent content = getContent(documentId);

			// read the document local file
			final LocalFile localFile = getLocalFile(document);
			localFile.read();
			content.setChecksum(localFile.getFileChecksum());
			content.setContent(localFile.getFileBytes());

			// create a new version\version content
			final DocumentVersion version = new DocumentVersion();
			version.setArtifactId(documentId);
			version.setArtifactType(document.getType());
			version.setArtifactUniqueId(document.getUniqueId());
			version.setCreatedBy(document.getCreatedBy());
			version.setCreatedOn(document.getCreatedOn());
			version.setName(document.getName());
			version.setUpdatedBy(document.getUpdatedBy());
			version.setUpdatedOn(document.getUpdatedOn());

			final DocumentVersionContent versionContent = new DocumentVersionContent();
			versionContent.setDocumentContent(content);
			versionContent.setDocumentId(documentId);
			versionContent.setVersionId(version.getVersionId());
			documentIO.createVersion(version, versionContent);

			// create the version local file
			final LocalFile versionLocalFile = getLocalFile(document, version);
			versionLocalFile.write(content.getContent());
			versionLocalFile.lock();

			// update the document updated by\on
			document.setUpdatedBy(preferences.getUsername());
			document.setUpdatedOn(currentDateTime());
			documentIO.update(document);

			// update the content bytes\checksum
			content.setContent(localFile.getFileBytes());
			content.setChecksum(localFile.getFileChecksum());
			documentIO.updateContent(content);

			return version;
		}
		catch(final IOException iox) {
			logger.error("[LMODEL] [DOCUMENT] [CREATE VERSION] [IO ERROR]", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(final RuntimeException rx) {
			logger.error("[LMODEL] [DOCUMENT] [CREATE VERSION] [UNEXPECTED ERROR]", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Accept the key request.
	 * 
	 * @param keyRequestId
	 *            The key request id.
	 */
	void declineKeyRequest(final Long keyRequestId) throws ParityException {
		getInternalArtifactModel().declineKeyRequest(keyRequestId);
	}

	/**
	 * Delete a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	void delete(final Long documentId) throws ParityException {
		logger.info("delete(Long)");
		logger.debug(documentId);
		try {
			final Document document = get(documentId);
			assertStateTransition(document.getState(), ArtifactState.DELETED);

			// if the document is not closed ensure the user is not the
			// key holder
			if(ArtifactState.CLOSED != document.getState())
				assertLoggedInUserIsNotKeyHolder(documentId);
			
			// delete the document remotely
			final InternalSessionModel iSModel = getInternalSessionModel();
			iSModel.sendDelete(documentId);

			// delete the audit info
			final InternalAuditModel iAModel = getInternalAuditModel();
			iAModel.delete(documentId);

			// delete the versions locally
			final Collection<DocumentVersion> versions = listVersions(documentId);
			for(final DocumentVersion version : versions) {
				getLocalFile(document, version).delete();
				documentIO.deleteVersion(documentId, version.getVersionId());
			}

			// delete the document locally
			final LocalFile localFile = getLocalFile(document);
			localFile.delete();
			localFile.deleteParent();
			documentIO.delete(documentId);

			// delete the index
			indexor.delete(documentId);

			// fire event
			notifyDocumentDeleted(document, localEventGen);
		}
		catch(final RuntimeException rx) {
			logger.error("[LMODEL] [DOCUMENT] [DELETE] [UNEXPECTED ERROR]", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a document with a specified id.
	 * 
	 * @param id
	 *            The id of the document.
	 * @return The document
	 * @throws ParityException
	 */
	Document get(final Long id) throws ParityException {
		logger.info("get(Long)");
		logger.debug(id);
		try { return documentIO.get(id); }
		catch(RuntimeException rx) {
			logger.error("get(Long)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a document with the specified unique id.
	 * 
	 * @param documentUniqueId
	 *            The document unique id.
	 * @return The document.
	 */
	Document get(final UUID documentUniqueId) throws ParityException {
		logger.info("get(UUID)");
		logger.debug(documentUniqueId);
		try { return documentIO.get(documentUniqueId); }
		catch(final RuntimeException rx) {
			logger.error("get(UUID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain the document content for a given document.
	 * 
	 * @param documentId
	 *            The document id.
	 * @return The document's content.
	 * @throws ParityException
	 */
	DocumentContent getContent(final Long documentId) throws ParityException {
		logger.info("getContent(UUID)");
		logger.debug(documentId);
		try { return documentIO.getContent(documentId); }
		catch(RuntimeException rx) {
			logger.error("getContent(UUID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
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
	DocumentVersion getVersion(final Long documentId, final Long versionId)
			throws ParityException {
		logger.info("getVersion(Long,Long)");
		logger.debug(documentId);
		logger.debug(versionId);
		try { return documentIO.getVersion(documentId, versionId); }
		catch(RuntimeException rx) {
			logger.error("getVersion(UUID,String)", rx);
			throw ParityErrorTranslator.translate(rx);
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
			final Long versionId) throws ParityException {
		logger.info("getVersionContent(Long,Long)");
		logger.debug(documentId);
		logger.debug(versionId);
		try { return documentIO.getVersionContent(documentId, versionId); }
		catch(RuntimeException rx) {
			logger.error("getVersionContent(DocumentVersion)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Determine whether or not the working version of the document is different
	 * from the last version.
	 * 
	 * @return True if the working version is different from the last version.
	 * @throws ParityException
	 */
	Boolean isWorkingVersionEqual(final Long documentId)
			throws ParityException {
		logger.info("isWorkingVersionDifferent(Long)");
		logger.debug(documentId);
		try {
			final Document document = get(documentId);

			final LocalFile localFile = getLocalFile(document);
			localFile.read();
			final String workingVersionChecksum = localFile.getFileChecksum();

			final DocumentVersion version = readLatestVersion(documentId);
			// we might have no recorded versions (initial point)
			if(null == version) { return Boolean.FALSE; }
			else {
				final DocumentVersionContent versionContent =
					getVersionContent(documentId, version.getVersionId());
	
				return versionContent.getDocumentContent().getChecksum()
					.equals(workingVersionChecksum);
			}
		}
		catch(final IOException iox) {
			logger.error("Could not determine working version delta:  " + documentId, iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(final RuntimeException rx) {
			logger.error("Could not determine working version delta:  " + documentId, rx);
			throw ParityErrorTranslator.translate(rx);
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
		ModelFilterManager.filter(documents, filter);
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
	Collection<DocumentVersion> listVersions(final Long documentId)
			throws ParityException {
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
	Collection<DocumentVersion> listVersions(final Long documentId,
			final Comparator<ArtifactVersion> comparator)
			throws ParityException {
		logger.info("listVersions(Document)");
		logger.debug(documentId);
		logger.debug(comparator);
		try {
			final List<DocumentVersion> versions =
				documentIO.listVersions(documentId);
			ModelSorter.sortDocumentVersions(versions, comparator);
			return versions;
		}
		catch(RuntimeException rx) {
			logger.error("listVersions(Document)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
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
			localFile.write(getContent(documentId).getContent());
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
	void open(final Long documentId) throws ParityException {
		logger.info("[LMODEL] [DOCUMENT] [OPEN]");
		logger.debug(documentId);
		try {
			final Document document = get(documentId);

			// open the local file
			final LocalFile localFile = getLocalFile(document);
			localFile.open();
		}
		catch(IOException iox) {
			logger.error("open(UUID)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("open(UUID)", rx);
			throw ParityErrorTranslator.translate(rx);
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

	List<HistoryItem> readHistory(final Long documentId)
			throws ParityException {
		logger.info("readHistory(Long)");
		logger.debug(documentId);
		return readHistory(documentId, getDefaultHistoryItemComparator());
	}

	List<HistoryItem> readHistory(final Long documentId,
			final Comparator<HistoryItem> comparator) throws ParityException {
		logger.info("readHistory(Long,Comparator<HistoryItem>)");
		logger.debug(documentId);
		logger.debug(comparator);
		try {
			final InternalAuditModel iAModel = getInternalAuditModel();

			final HistoryItemBuilder hib =
				new HistoryItemBuilder(l18n, get(documentId));

			final List<HistoryItem> history =
				hib.build(iAModel.read(documentId), currentUserId());
			ModelSorter.sortHistoryItems(history, comparator);

			return history;
		}
		catch(final RuntimeException rx) {
			logger.error("Could not obtain history list.", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain the latest document version.
	 * 
	 * @param documentId
	 *            The document id.
	 * @return The latest version.
	 */
	DocumentVersion readLatestVersion(final Long documentId)
			throws ParityException {
		logger.info("getLatestVersion(Long)");
		logger.debug(documentId);
		try { return documentIO.getLatestVersion(documentId); }
		catch(RuntimeException rx) {
			logger.error("Could not obtain latest version.", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Receive an xmpp document. If no local document exists; create it; then
	 * insert the xmpp document as a version of the local document.
	 * 
	 * @param xmppDocument
	 *            The xmpp document.
	 * @throws ParityException
	 */
	void receive(final JabberId receivedFrom, final UUID uniqueId,
            final Long versionId, final String name, final byte[] content)
            throws ParityException, SmackException {
		logger.info("[LMODEL] [DOCUMENT] [RECEIVE]");
		try {
			Document document = get(uniqueId);
			if(null == document) {
				document = receiveCreate(receivedFrom, uniqueId, versionId,
                        name, content);

				// if key holder:  apply flag key
				final InternalSessionModel iSModel = getInternalSessionModel();
				if(iSModel.isLoggedInUserKeyHolder(document.getId())) {
					final InternalArtifactModel iAModel = getInternalArtifactModel();
					iAModel.applyFlagKey(document.getId());
				}

				// audit the receiving
				final Document d = get(document.getId());
                final Calendar currentDateTime = currentDateTime();
				auditor.receive(d.getId(), currentDateTime, currentUserId(),
                        versionId, receivedFrom, currentUserId(),
                        currentDateTime);

                // confirm the document receipt
                final InternalArtifactModel iAModel = getInternalArtifactModel();
                iAModel.confirmReceipt(receivedFrom, document.getId());

                // fire event
				notifyDocumentCreated(document, remoteEventGen);
			}
			else {
				final DocumentVersion version =
                    getVersion(document.getId(), versionId);
				// i have this version.  wtf biotch
				if(null == version) {
					document.setUpdatedOn(currentDateTime());
                    receiveUpdate(receivedFrom, uniqueId, document.getId(), versionId, name, content);

					// if key holder:  apply flag key
					final InternalSessionModel iSModel = getInternalSessionModel();
					if(iSModel.isLoggedInUserKeyHolder(document.getId())) {
						final InternalArtifactModel iAModel = getInternalArtifactModel();
						iAModel.applyFlagKey(document.getId());
					}
				}

				// audit the receiving
				final Document d = get(document.getId());
                final Calendar currentDateTime = currentDateTime();
				auditor.receive(d.getId(), currentDateTime, currentUserId(),
                        versionId, receivedFrom, currentUserId(),
                        currentDateTime);

                // confirm the document receipt
                final InternalArtifactModel iAModel = getInternalArtifactModel();
                iAModel.confirmReceipt(receivedFrom, document.getId());

				// fire event
				if(null == version) { notifyDocumentUpdated(document, remoteEventGen); }
			}
		}
		catch(IOException iox) {
			logger.error("receiveDocument(XMPPDocument)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("receiveDocument(XMPPDocument)", rx);
			throw ParityErrorTranslator.translate(rx);
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
        notifyKeyRequested(getInternalSessionModel().readUser(requestedBy),
                d, remoteEventGen);
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
			localFile.write(getContent(documentId).getContent());
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

    void updateIndex(final Long documentId) throws ParityException {
		logger.info("[LMODEL] [DOCUMENT] [UPDATE INDEX]");
		logger.debug(documentId);
		indexor.delete(documentId);
		indexor.create(documentId, get(documentId).getName());
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
        assertLoggedInUserIsKeyHolder(documentId);
        final LocalFile localFile = getLocalFile(get(documentId));
        InputStream is = null;
        try {
            is = createInputStream(updateFile);
            localFile.write(createInputStream(updateFile));
        }
        catch(final FileNotFoundException fnfx) {
            logger.error(LOG_UWV_ERROR_FNFX, fnfx);
            throw ParityErrorTranslator.translate(fnfx);
        }
        catch(final IOException iox) {
            logger.error(LOG_UWV_ERROR_IOX, iox);
            throw ParityErrorTranslator.translate(iox);
        }
        finally {
            if(null != is) {
                try { is.close(); }
                catch(final IOException iox) {
                    logger.error(LOG_UWV_ERROR_IOX_2, iox);
                    throw ParityErrorTranslator.translate(iox);
                }
            }
        }
    }

    /**
	 * Assert that the archive output directory has been set.
	 * 
	 */
	private void assertValidOutputDirectory() {
		Assert.assertTrue(
				"Archive output directory has not been set.",
				preferences.isSetArchiveOutputDirectory());
		final File aod = preferences.getArchiveOutputDirectory();
		if(!aod.exists()) {
			Assert.assertTrue(
					format("Cannot create archive output directory [{0}]", aod),
					aod.mkdir());
		}
		Assert.assertTrue(
				format("Archive output directory [{0}] is not a directory.", aod), aod.isDirectory());
		Assert.assertTrue(
				format("Cannot read archive output directory [{0}]", aod), aod.canRead());
		Assert.assertTrue(
				format("Cannot write archive output directory [{0}]", aod), aod.canWrite());
	}

	/**
     * Create an input stream from the input file.
     * 
     * @param inputFile
     *            The input file.
     * @return An input stream.
     * @throws FileNotFoundException
     */
    private InputStream createInputStream(final File inputFile)
            throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(inputFile));
    }

	private String format(final String pattern, final File file) {
		return format(pattern, new Object[] {file.getAbsolutePath()});
	}

	private String format(final String pattern, final Object[] arguments) {
		return MessageFormat.format(
				pattern,
				arguments);
	}

	/**
	 * Obtain the default history item comparator.
	 * 
	 * @return A sort by date descending history item comparator.
	 */
	private Comparator<HistoryItem> getDefaultHistoryItemComparator() {
		if(null == defaultHistoryItemComparator) {
			defaultHistoryItemComparator = new ComparatorBuilder().createDateDescending();
		}
		return defaultHistoryItemComparator;
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
	 * Insert a version for a document.
	 * @param documentId The document.
	 * @param version The version to insert.
	 */
	private void insertVersion(final Long documentId,
			final DocumentVersion version,
			final DocumentVersionContent versionContent) throws ParityException {
		logger.info("[LMODEL] [DOCUMENT] [INSERT VERSION");
		logger.debug(documentId);
		logger.debug(version);
		logger.debug(versionContent);
		try {
			final Document document = get(documentId);

			// insert version info into db
			documentIO.createVersion(version.getVersionId(), version, versionContent);

			// create version local file
			final LocalFile versionFile = getLocalFile(document, version);
			versionFile.write(versionContent.getDocumentContent().getContent());
			versionFile.lock();
		}
		catch(final IOException iox) {
			logger.error("[LMODEL] [DOCUMENT] [INSERT VERSION] [IO ERROR]", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(final RuntimeException rx) {
			logger.error("[LMODEL] [DOCUMENT] [INSERT VERSION] [UNEXPECTED ERROR]", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Check and see if this version is the latest version.
	 * 
	 * @param version
	 *            A document version.
	 * @return True if this is the latest local version of the document.
	 */
	private Boolean isLatestLocalVersion(final DocumentVersion version) {
		final DocumentVersion latestLocalVersion =
			documentIO.getLatestVersion(version.getArtifactId());
		return latestLocalVersion.getVersionId().equals(version.getVersionId());
	}

    /**
     * Fire confirmation received.
     *
     * @param document
     *      A document
     * @param eventGen
     *      The event generator.
     */
    private void notifyConfirmationReceived(final Document document, final DocumentModelEventGenerator eventGen) {
        synchronized(DocumentModelImpl.LISTENERS) {
            for(final DocumentListener l : DocumentModelImpl.LISTENERS) {
                l.confirmationReceived(eventGen.generate(document));
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
	 * Fire document closed.
	 * 
	 * @param document
     *      A document.
     * @param eventGen
     *      The event generator.
	 */
	private void notifyDocumentClosed(final Document document, final DocumentModelEventGenerator eventGen) {
		synchronized(DocumentModelImpl.LISTENERS) {
			for(final DocumentListener l : DocumentModelImpl.LISTENERS) {
				l.documentClosed(eventGen.generate(document));
			}
		}
	}

	/**
	 * Fire document deleted event.
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
	 * Fire document updated.
	 * 
	 * @param document
     *      A document.
	 * @param eventGen
     *      The event generator.
	 */
	private void notifyDocumentUpdated(final Document document, final DocumentModelEventGenerator eventGen) {
		synchronized(DocumentModelImpl.LISTENERS) {
			for(final DocumentListener l : DocumentModelImpl.LISTENERS) {
				l.documentUpdated(eventGen.generate(document));
			}
		}
	}

	/**
	 * This is the first time this particular document has been recieved. We
	 * need to create the document; send a subscription request; then receive
	 * update.
	 * 
	 * @param xmppDocument
	 *            The xmpp document.
	 * @return The new document.
	 * @throws ParityException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Document receiveCreate(final JabberId receivedFrom,
            final UUID uniqueId, final Long versionId, final String name,
            final byte[] bytes) throws ParityException, FileNotFoundException,
            IOException {
		final Calendar currentDateTime = currentDateTime();
		// create the document
		final Document document = new Document();
		document.setCreatedBy(receivedFrom.getUsername());
		document.setCreatedOn(currentDateTime);
		document.setName(name);
		document.setState(ArtifactState.ACTIVE);
		document.setUniqueId(uniqueId);
		document.setUpdatedBy(receivedFrom.getUsername());
		document.setUpdatedOn(currentDateTime);

		final DocumentContent content = new DocumentContent();
		content.setChecksum(MD5Util.md5Hex(bytes));
		content.setContent(bytes);
		documentIO.create(document, content);

		// create the remote info row
		final InternalArtifactModel iAModel = getInternalArtifactModel();
		iAModel.createRemoteInfo(document.getId(), receivedFrom, currentDateTime);

		// create the document file
		final LocalFile file = getLocalFile(document);
		file.write(content.getContent());

		// send a subscription request
		getInternalSessionModel().sendSubscribe(document);

		// index the creation
		indexor.create(document.getId(), document.getName());

		// update the document
		receiveUpdate(receivedFrom, uniqueId, document.getId(), versionId, name, bytes);
		return document;
	}

    /**
	 * Insert the corresponding version for the xmpp document received. Check to
	 * see if this is the latest version locally; and if it is; update the
	 * document\document content. Notify that a version has been received.
	 * 
	 * @param xmppDocument
	 *            The xmpp document received.
	 * @param document
	 *            The existing local document.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParityException
	 */
	private void receiveUpdate(final JabberId receivedFrom,
            final UUID uniqueId, final Long documentId, final Long versionId,
            final String name, final byte[] bytes)
            throws FileNotFoundException, IOException, ParityException {
	    final Calendar currentDateTime = currentDateTime();

        final DocumentVersion version = new DocumentVersion();
		version.setArtifactId(documentId);
		version.setArtifactType(ArtifactType.DOCUMENT);
		version.setArtifactUniqueId(uniqueId);
		version.setCreatedBy(receivedFrom.getUsername());
		version.setCreatedOn(currentDateTime);
		version.setName(name);
		version.setUpdatedBy(currentUserId().getUsername());
		version.setUpdatedOn(currentDateTime);
		version.setVersionId(versionId);

		final DocumentContent content = new DocumentContent();
		content.setChecksum(MD5Util.md5Hex(bytes));
		content.setContent(bytes);
		content.setDocumentId(documentId);

		final DocumentVersionContent versionContent = new DocumentVersionContent();
		versionContent.setDocumentContent(content);
		versionContent.setDocumentId(documentId);
		versionContent.setVersionId(versionId);

		insertVersion(version.getArtifactId(), version, versionContent);

		if(isLatestLocalVersion(version)) {
            final Document d = get(documentId);
			d.setUpdatedBy(currentUserId().getUsername());

			// update the db
			documentIO.update(d);
			documentIO.updateContent(content);

			// update the local file
			final LocalFile localFile = getLocalFile(d);
			localFile.delete();
			localFile.write(content.getContent());
		}

		// if not key holder:  lock
		final InternalSessionModel iSModel = getInternalSessionModel();
		if(!iSModel.isLoggedInUserKeyHolder(documentId)) {
			lock(documentId);
		}

		// remove flag seen
		final InternalArtifactModel iAModel = getInternalArtifactModel();
		iAModel.removeFlagSeen(documentId);

		// update remote info
		iAModel.updateRemoteInfo(documentId, receivedFrom, currentDateTime);
	}
}
