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
import com.thinkparity.model.parity.api.events.CreationEvent;
import com.thinkparity.model.parity.api.events.CreationListener;
import com.thinkparity.model.parity.api.events.DeleteEvent;
import com.thinkparity.model.parity.api.events.UpdateEvent;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.api.events.VersionCreationEvent;
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
import com.thinkparity.model.xmpp.JabberId;

/**
 * Implementation of the document model interface.
 * 
 * @author raykroeker@gmail.com
 * @version 1.5.2.43
 */
class DocumentModelImpl extends AbstractModelImpl {

    /** An assertion statement. */
	private static final String ASSERT_UWV_0 =
        "[LMODEL] [DOCUMENT] [UPDATE WORKING VERSION] [WORKING VERSION EQUAL TO LATEST VERSION]";

    /**
	 * List of listeners to notify when a document is created or received.
	 * 
	 * @see DocumentModelImpl#creationListenersLock
	 */
	private static final Collection<CreationListener> creationListeners;

    /**
	 * Synchronization lock for the creation listeners.
	 * 
	 * @see DocumentModelImpl#creationListeners
	 */
	private static final Object creationListenersLock;

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

	/**
	 * List of listeners to notify when a document has been updated.
	 * 
	 * @see DocumentModelImpl#updateListenersLock
	 */
	private static final Collection<UpdateListener> updateListeners;

	/**
	 * Synchronization lock for the update listeners.
	 * 
	 * @see DocumentModelImpl#updateListeners
	 */
	private static final Object updateListenersLock;

	static {
		// initialize the creation event listeners
		creationListeners = new Vector<CreationListener>(7);
		creationListenersLock = new Object();
		// initialize the update event listeners
		updateListeners = new Vector<UpdateListener>(7);
		updateListenersLock = new Object();
	}

	/**
	 * The document model auditor.
	 * 
	 */
	private final DocumentModelAuditor auditor;

	/**
	 * Default sort comparator for documents.
	 * 
	 */
	private final Comparator<Artifact> defaultComparator;

	/**
	 * Default history item comparator.
	 * 
	 */
	private Comparator<HistoryItem> defaultHistoryItemComparator;

	/**
	 * Default sort comparator for document versions.
	 * 
	 * @see #getDefaultHistoryItemComparator()
	 */
	private final Comparator<ArtifactVersion> defaultVersionComparator;

	/**
	 * Document history i\o.
	 * 
	 */
	private final DocumentHistoryIOHandler documentHistoryIO;

	/**
	 * Document xml input\output.
	 */
	private final DocumentIOHandler documentIO;

	/**
	 * Responsible for indexing documents.
	 * 
	 */
	private final DocumentIndexor indexor;

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
	 * @see DocumentModel#addListener(CreationListener)
	 * @param listener
	 *            The listener to add.
	 */
	void addListener(final CreationListener listener) {
		logger.info("addCreationListener(CreationListener)");
		logger.debug(listener);
		Assert.assertNotNull("addCreationListener(CreationListener)", listener);
		synchronized (DocumentModelImpl.creationListenersLock) {
			Assert.assertNotTrue(
					"addCreationListener(CreationListener)",
					DocumentModelImpl.creationListeners.contains(listener));
			DocumentModelImpl.creationListeners.add(listener);
		}
	}

	/**
	 * Add a document update event listener.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	void addListener(final UpdateListener listener) {
		logger.info("addUpdateListener(UpdateListener)");
		logger.debug(listener);
		Assert.assertNotNull("addUpdateListener(UpdateListener)", listener);
		synchronized (DocumentModelImpl.updateListenersLock) {
			Assert.assertNotTrue("Update listener already registered.",
					DocumentModelImpl.updateListeners.contains(listener));
			DocumentModelImpl.updateListeners.add(listener);
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
		return documentHistoryIO.archive(documentId, readHistory(documentId));
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
			final Calendar createdOn, final JabberId receivedFrom) {
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
			notifyUpdate_objectClosed(d);
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
			notifyUpdate_objectClosed(d);
		}
		catch(final RuntimeException rx) {
			logger.error("Cannot close document:  " + documentUniqueId, rx);
			throw ParityErrorTranslator.translate(rx);
		}
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

			// flag the document as having been seen.
			final InternalArtifactModel iAModel = getInternalArtifactModel();
			iAModel.applyFlagKey(document.getId());

			// create the remote info row
			iAModel.createRemoteInfo(document.getId(), currentUserId(), now);

			// fire a creation event
			final Document d = get(document.getId());
			notifyCreation_objectCreated(d);

			// audit the creation
			auditor.create(d.getId(), currentUserId(), d.getCreatedOn());

			// index the creation
			indexor.create(d.getId(), d.getName());
			return d;
		}
		catch(IOException iox) {
			logger.error("createDocument(Document)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("createDocument(Document)", rx);
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

			// fire the object version event notification
			notifyCreation_objectVersionCreated(version);
			return version;
		}
		catch(IOException iox) {
			logger.error("createVersion(Document,DocumentAction,DocumentActionData)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("createVersion(Document,DocumentAction,DocumentActionData)", rx);
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

			// notify
			notifyUpdate_objectDeleted(document);
		}
		catch(RuntimeException rx) {
			logger.error("delete(UUID)", rx);
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
				hib.create(iAModel.read(documentId), getInternalSessionModel());
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
            throws ParityException {
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
				auditor.recieve(d.getId(), versionId, receivedFrom,
                        currentUserId(), currentDateTime());

				// notify
				notifyUpdate_objectReceived(d);
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
				auditor.recieve(d.getId(), versionId, receivedFrom,
                        currentUserId(), currentDateTime());

				// notify if this is a new version
				if(null == version) { notifyUpdate_objectReceived(d); }
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
	 * Remove a document creation event listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	void removeListener(final CreationListener listener) {
		logger.info("removeCreationListener(CreationListener)");
		logger.debug(listener);
		Assert.assertNotNull(
				"removeCreationListener(CreationListener)", listener);
		synchronized(DocumentModelImpl.creationListenersLock) {
			Assert.assertTrue(
					"removeCreationListener(CreationListener)",
					DocumentModelImpl.creationListeners.contains(listener));
			DocumentModelImpl.creationListeners.remove(listener);
		}
	}

	/**
	 * Remove a document update event listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	void removeListener(final UpdateListener listener) {
		logger.info("removeUpdateListener(UpdateListener)");
		logger.debug(listener);
		Assert.assertNotNull("removeUpdateListener(UpdateListener)", listener);
		synchronized (DocumentModelImpl.updateListenersLock) {
			Assert.assertTrue(
					"removeUpdateListener(UpdateListener)",
					DocumentModelImpl.updateListeners.contains(listener));
			DocumentModelImpl.updateListeners.remove(listener);
		}
	}

	void requestKey(final Long documentId, final JabberId requestedBy)
			throws ParityException {
		logger.info("[LMODEL] [DOCUMENT] [REQUEST KEY]");
		logger.debug(documentId);

		// remove seen flag
		final InternalArtifactModel iAModel = getInternalArtifactModel();
		iAModel.removeFlagSeen(documentId);

		// create system message
		final InternalSystemMessageModel iSMModel = getInternalSystemMessageModel();
		iSMModel.createKeyRequest(documentId, requestedBy);

		// update the document's last update date
		final Document d = get(documentId);
		d.setUpdatedOn(currentDateTime());
		documentIO.update(d);
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
        Assert.assertNotTrue(
                ASSERT_UWV_0,
                isWorkingVersionEqual(documentId));
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
		logger.info("insertVersion(Long,DocumentVersion,DocumentVersionContent)");
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

			// fire the object version event notification
			notifyCreation_objectVersionCreated(version);
		}
		catch(IOException iox) {
			logger.error("createVersion(Document,DocumentAction,DocumentActionData)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("createVersion(Document,DocumentAction,DocumentActionData)", rx);
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
	 * Fire the objectCreated event for all of the creation listeners.
	 * 
	 * @param document
	 *            The document to use as the event source.
	 * @see CreationListener#objectCreated(CreationEvent)
	 */
	private void notifyCreation_objectCreated(final Document document) {
		synchronized(DocumentModelImpl.creationListenersLock) {
			for(CreationListener listener : DocumentModelImpl.creationListeners) {
				listener.objectCreated(new CreationEvent(document));
			}
		}
	}

	/**
	 * Fire the objectVersionCreated event for all of the creation listeners.
	 * 
	 * @param documentVersion
	 *            The document version to use as the event source.
	 * @see CreationListener#objectVersionCreated(VersionCreationEvent)
	 */
	private void notifyCreation_objectVersionCreated(
			final DocumentVersion documentVersion) {
		synchronized (DocumentModelImpl.creationListenersLock) {
			for (CreationListener listener : DocumentModelImpl.creationListeners) {
				listener.objectVersionCreated(new VersionCreationEvent(
						documentVersion));
			}
		}
	}

	/**
	 * Fire the object closed event.
	 * 
	 * @param document
	 *            The document that was closed.
	 */
	private void notifyUpdate_objectClosed(final Document document) {
		synchronized(updateListenersLock) {
			for(final UpdateListener l : updateListeners) {
				l.objectClosed(new com.thinkparity.model.parity.api.events.CloseEvent(document));
			}
		}
	}

	/**
	 * Fire the object deleted event for all of the update listeners.
	 * 
	 * @param document
	 *            The document that was deleted.
	 */
	private void notifyUpdate_objectDeleted(final Document document) {
		synchronized(DocumentModelImpl.updateListeners) {
			for(UpdateListener listener : DocumentModelImpl.updateListeners) {
				listener.objectDeleted(new DeleteEvent(document));
			}
		}
	}

	/**
	 * Fire the objectReceived event for all update listeners.
	 * 
	 * @param document
	 *            The document to use as the event source.
	 * @see UpdateListener#objectReceived(UpdateEvent)
	 */
	private void notifyUpdate_objectReceived(final Document document) {
		synchronized(DocumentModelImpl.updateListenersLock) {
			for(UpdateListener listener : DocumentModelImpl.updateListeners) {
				listener.objectReceived(new UpdateEvent(document));
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
