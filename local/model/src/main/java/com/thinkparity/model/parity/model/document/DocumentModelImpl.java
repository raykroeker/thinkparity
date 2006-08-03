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
import com.thinkparity.codebase.assertion.NotTrueAssertion;

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
import com.thinkparity.model.parity.model.artifact.ArtifactType;
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
import com.thinkparity.model.parity.model.session.InternalSessionModel;
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
 * @version $Revision$
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

	private static StringBuffer getApiId(final String api) {
        return getModelId("DOCUMENT").append(" ").append(api);
    }

	private static String getErrorId(final String api, final String error) {
        return getApiId(api).append(" ").append(error).toString();
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
     * @see com.thinkparity.model.parity.model.AbstractModelImpl#isDistributed(java.lang.Long)
     * 
     */
    protected Boolean isDistributed(Long artifactId) {
        return super.isDistributed(artifactId);
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
     * Create a document and attach it to a container. This will take a name,
     * and input stream of a file and create a document.
     * 
     * @param name
     *            The document name.
     * @param inputStream
     *            The document content's input stream.
     * @return The document.
     * @throws ParityException
     */
    Document create(final String name, final InputStream inputStream)
            throws ParityException {
        logger.info(getApiId("[CREATE]"));
        logger.debug(name);
        logger.debug(inputStream);
        assertIsSetCredentials();
        try {
            final Calendar currentDateTime = currentDateTime();

            // create the document
            final Document document = new Document(readCredentials().getUsername(),
                    currentDateTime, null, Collections.<ArtifactFlag>emptyList(),
                    UUIDGenerator.nextUUID(), name, readCredentials().getUsername(),
                    currentDateTime);
            document.setState(ArtifactState.ACTIVE);
            documentIO.create(document);

            // create the local file
            final LocalFile localFile = getLocalFile(document);
            final byte[] content = StreamUtil.read(inputStream);
            localFile.write(content);

            // create a version
            createVersion(document.getId(), content);

            // create remote info
            final InternalArtifactModel aModel = getInternalArtifactModel();
            aModel.createRemoteInfo(document.getId(), currentUserId(), currentDateTime);

            // audit the creation
            auditor.create(document.getId(), currentUserId(), document.getCreatedOn());

            // index the document
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
     * Create a duplicate document version.
     * 
     * @param documentId
     *            A document id.
     * @return The new version.
     * @throws ParityException
     */
    DocumentVersion createVersion(final Long documentId)
            throws ParityException {
        final DocumentVersionContent latestVersionContent =
            readLatestVersionContent(documentId);
        return createVersion(documentId, latestVersionContent.getContent());
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
		logger.info("[LMODEL] [DOCUMENT MODEL] [DELETE]");
		logger.debug(documentId);
        assertOnline("[LMODEL] [DOCUMENT MODEL] [DELETE] [USER IS NOT ONLINE]");

        final Document document = get(documentId);
        if(isClosed(document)) { deleteLocal(document); }
        else {
            if(isKeyHolder(documentId)) {
                if(!isDistributed(documentId)) { deleteLocal(document); }
                else {
                    throw Assert.createUnreachable(
                            "[LMODEL] [DOCUMENT] [DELETE] [CAN ONLY DELETE CLOSED DOCUMENTS IF YOU ARE THE KEY HOLDER]");
                }
            }
            else {
                getInternalSessionModel().sendDelete(document.getId());

                deleteLocal(document);
            }
        }

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
		try { return documentIO.readVersionContent(documentId, versionId); }
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
		logger.info("[LMODEL] [DOCUMENT] [IS WORKING VERSION EQUAL]");
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
				return version.getChecksum().equals(workingVersionChecksum);
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
     * Open an input stream to read a document version. Note: It is a good idea
     * to buffer the input stream.
     * 
     * @param documentId
     *            A document id.
     * @param versionId
     *            A version id.
     * @return An input stream.
     */
    InputStream openStream(final Long documentId, final Long versionId) {
        logger.info(getApiId("[OPEN STREAM]"));
        logger.debug(documentId);
        logger.debug(versionId);
        return documentIO.openStream(documentId, versionId);
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
     * Publish a document.  Publishing a document involves the following
     * process:<ol>
     *  <li>Check if the working version differs from the latest version. If<ul>
     *      <li>True:  Create a new version.
     *      <li>False:  Do nothing.
     *  <li>Send the latest version to all team members.
     * </ol>
     *
     * @param documentId
     *      The document id.
     */
    void publish(final Long documentId) throws ParityException {
        logger.info("[LMODEL] [DOCUMENT] [SEND]");
        logger.debug(documentId);
        assertOnline("[LMODEL] [DOCUMENT] [SEND] [USER IS NOT ONLINE]");
        assertIsKeyHolder("[LMODEL] [DOCUMENT] [SEND] [USER IS NOT KEYHOLDER]", documentId);
        // we assume here that we are publishing a new version
        Assert.assertNotTrue(
                "[LMODEL] [DOCUMENT] [SEND] [WORKING VERSION EQUALS LATEST VERSION]",
                isWorkingVersionEqual(documentId));

        final DocumentVersion version = createVersion(documentId);

        // read the team
        final Set<User> teamUsers = getInternalArtifactModel().readTeam(documentId);
        teamUsers.remove(currentUser());

        // audit
        auditor.publish(documentId, version.getVersionId(),
                currentDateTime(), currentUserId());

        // send to the team
        getInternalSessionModel().send(teamUsers, documentId, version.getVersionId());

        // fire event
        notifyDocumentPublished(get(documentId),
                getVersion(documentId, version.getVersionId()), localEventGen);
    }

    /**
     * Reactivate a document.
     * 
     * @param documentId
     *            The document id.
     * @throws ParityException
     */
    void reactivate(final Long documentId) throws ParityException {
        logger.info(getApiId("[REACTIVATE]"));
        assertOnline(getErrorId("[REACTIVATE]", "[CANNOT REACTIVATE WHILE OFFLINE]"));
        assertIsClosed(getErrorId("[REACTIVATE]", "[CANNOT REACTIVATE A CLOSED DOCUMENT]"), get(documentId));

        // update the local state
        final Document document = get(documentId);
        assertStateTransition(document.getState(), ArtifactState.ACTIVE);
        document.setState(ArtifactState.ACTIVE);
        documentIO.update(document);

        // set key locally
        final InternalArtifactModel iAModel = getInternalArtifactModel();
        iAModel.applyFlagKey(documentId);

        // reactivate remotely
        final DocumentVersion version = readLatestVersion(documentId);
        final DocumentVersionContent versionContent = getVersionContent(documentId, version.getVersionId());
        final Set<User> users = iAModel.readTeam(documentId);
        final List<JabberId> team = new ArrayList<JabberId>();
        for(final User user : users) { team.add(user.getId()); }
        getInternalSessionModel().sendDocumentReactivate(team,
                document.getUniqueId(), version.getVersionId(),
                document.getName(), versionContent.getContent());

        // audit
        final Calendar currentDateTime = currentDateTime();
        auditor.reactivate(documentId, currentDateTime, currentUserId(),
                version.getVersionId(), currentUserId(), currentDateTime);

        // fire event
        notifyDocumentReactivated(readUser(currentUserId()), document, version, localEventGen);
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
        try { return get(documentId); }
        catch(final ParityException px) {
            logger.error(getErrorId("[READ]", "[CANNOT READ DOCUMENT]"), px);
            throw ParityErrorTranslator.translateUnchecked(px);
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
	DocumentVersion readLatestVersion(final Long documentId)
			throws ParityException {
		logger.info("getLatestVersion(Long)");
		logger.debug(documentId);
		try { return documentIO.readLatestVersion(documentId); }
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

				final Document d = get(document.getId());
                final Calendar currentDateTime = currentDateTime();
				// audit create
				auditor.createRemote(
                        d.getId(), currentUserId(), currentDateTime,
                        receivedFrom);
                // audit receive
                auditor.receive(d.getId(), currentDateTime, currentUserId(),
                        versionId, receivedFrom, currentUserId(),
                        currentDateTime);

                // confirm the document receipt
                final InternalArtifactModel iAModel = getInternalArtifactModel();
                iAModel.confirmReceipt(receivedFrom, document.getId(), versionId);

                // fire event
                {
                    final User receivedFromUser =
                        getInternalUserModel().read(receivedFrom);
                    notifyDocumentCreated(receivedFromUser, document, remoteEventGen);
                }
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
                iAModel.confirmReceipt(receivedFrom, document.getId(), versionId);

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

	/**
     * Rename a document.
     *
     * @param documentId
     *      A document id.
     * @param documentName
     *      A document name.
     */
    void rename(final Long documentId, final String documentName)
            throws ParityException {
        logger.info("[LMODEL] [DOCUMENT] [RENAME]");
        logger.debug(documentId);
        logger.debug(documentName);
        assertIsNotDistributed("[RENAME]", documentId);
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

        try {
            dFile.write(versionContent.getContent());
            dVersionFile.write(versionContent.getContent());
        }
        catch(final IOException iox) {
            logger.error("[LMODEL] [DOCUMENT] [RENAME] [IO ERROR]", iox);
            throw ParityErrorTranslator.translate(iox);
        }

        // audit the rename
        auditor.rename(documentId, currentDateTime(), currentUserId(),
                originalName,documentName);
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
     * Assert that a document has not yet been sent to anyone.
     *
     * @param assertion
     *      Assertion text.
     * @param documentId
     *      A document id.
     */
    private void assertIsNotDistributed(final String assertion, final Long documentId)
            throws ParityException {
        final StringBuffer buffer = new StringBuffer("[LMODEL] [DOCUMENT] ")
                .append(assertion)
                .append(" [DOCUMENT HAS ALREADY BEEN DISTRIBUTED]");
        Assert.assertNotTrue(buffer.toString(), isDistributed(documentId));
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
	 * @throws ParityException
	 * @throws NotTrueAssertion
	 *             If the logged in user is not the key holder.
	 */
	private DocumentVersion createVersion(final Long documentId,
            final byte[] content) throws ParityException {
		logger.info(getApiId("[CREATE VERSION]"));
		logger.debug(documentId);
        logger.debug(content);
        assertOnline("[LMODEL] [DOCUMENT] [CREATE VERSION] [USER IS NOT ONLINE]");
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
			version.setChecksum(MD5Util.md5Hex(content));
			version.setCompression(Compression.NONE);
			version.setCreatedBy(document.getCreatedBy());
			version.setCreatedOn(document.getCreatedOn());
			version.setEncoding(Encoding.BASE_64);
			version.setName(document.getName());
			version.setUpdatedBy(document.getUpdatedBy());
			version.setUpdatedOn(document.getUpdatedOn());
			final DocumentVersionContent versionContent = new DocumentVersionContent();
			versionContent.setContent(content);
			versionContent.setVersion(version);
            documentIO.createVersion(version, versionContent);

			// create the version local file
			final LocalFile versionLocalFile = getLocalFile(document, version);
			versionLocalFile.write(content);
			versionLocalFile.lock();

			// update the document updated by\on
			document.setUpdatedBy(readCredentials().getUsername());
			document.setUpdatedOn(currentDateTime());
			documentIO.update(document);

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

	/**
     * Determine if an artifact exists.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @return True if the artifact exists; false otherwise.
     */
    private Boolean doesExist(final UUID uniqueId) {
        return getInternalArtifactModel().doesExist(uniqueId);
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
			versionFile.write(versionContent.getContent());
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
			documentIO.readLatestVersion(version.getArtifactId());
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
     * Fire document created.
     * 
     * @param user
     *            A user.
     * @param document
     *            A document.
     * @param eventGen
     *            An event generator.
     */
    private void notifyDocumentCreated(final User user,
            final Document document, final DocumentModelEventGenerator eventGen) {
        synchronized(DocumentModelImpl.LISTENERS) {
            for(final DocumentListener l : DocumentModelImpl.LISTENERS) {
                l.documentCreated(eventGen.generate(user, document));
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
     * Fire document published.
     *
     * @param document
     *      The document.
     * @param eventGen
     *      The event generator.
     */
    private void notifyDocumentPublished(final Document document,
            final DocumentVersion version,
            final DocumentModelEventGenerator eventGen) {
        synchronized(DocumentModelImpl.LISTENERS) {
            for(final DocumentListener l : DocumentModelImpl.LISTENERS) {
                l.documentPublished(eventGen.generate(document, version));
            }
        }
    }

    /**
     * Fire document reactivated.
     * 
     * @param document
     *            A document.
     * @param version
     *            A document version.
     * @param eventGen
     *            An event generator.
     */
    private void notifyDocumentReactivated(final User user,
            final Document document, final DocumentVersion version,
            final DocumentModelEventGenerator eventGen) {
        synchronized(LISTENERS) {
            for(final DocumentListener l : LISTENERS) {
                l.documentReactivated(eventGen.generate(user, document, version));
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

		documentIO.create(document);

		// create the remote info row
		final InternalArtifactModel iAModel = getInternalArtifactModel();
		iAModel.createRemoteInfo(document.getId(), receivedFrom, currentDateTime);

		// create the document file
		final LocalFile file = getLocalFile(document);
		file.write(bytes);

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
		versionContent.setContent(bytes);
        versionContent.setVersion(version);

		insertVersion(version.getArtifactId(), version, versionContent);

		if(isLatestLocalVersion(version)) {
            final Document d = get(documentId);
			d.setUpdatedBy(currentUserId().getUsername());

			// update the db
			documentIO.update(d);

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

    /**
     * Update the index info for the document.
     * 
     * @param documentId
     *            The document id.
     * @throws ParityException
     */
	private void updateIndex(final Long documentId) throws ParityException {
		logger.info("[LMODEL] [DOCUMENT] [UPDATE INDEX]");
		logger.debug(documentId);
		indexor.delete(documentId);
		indexor.create(documentId, get(documentId).getName());
	}
}
