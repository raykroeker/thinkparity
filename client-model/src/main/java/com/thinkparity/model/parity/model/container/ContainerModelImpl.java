/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.model.parity.model.container;

import java.io.InputStream;
import java.util.*;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerListener;
import com.thinkparity.model.parity.api.events.ContainerEvent.Source;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.artifact.InternalArtifactModel;
import com.thinkparity.model.parity.model.artifact.KeyRequest;
import com.thinkparity.model.parity.model.audit.HistoryItem;
import com.thinkparity.model.parity.model.audit.event.AuditEvent;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.InternalDocumentModel;
import com.thinkparity.model.parity.model.filter.ArtifactFilterManager;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.artifact.DefaultFilter;
import com.thinkparity.model.parity.model.filter.history.HistoryFilterManager;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ContainerIOHandler;
import com.thinkparity.model.parity.model.session.Credentials;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.parity.model.sort.ComparatorBuilder;
import com.thinkparity.model.parity.model.sort.ModelSorter;
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.UUIDGenerator;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * <b>Title:</b>thinkParity Container Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.3
 */
public class ContainerModelImpl extends AbstractModelImpl {

    /** A list of container listeners. */
    private static final List<ContainerListener> LISTENERS = new LinkedList<ContainerListener>();

    /**
     * Obtain an apache api log id.
     * @param api The api.
     * @return The log id.
     */
    private static StringBuffer getApiId(final String api) {
        return getModelId("[CONTAINER]").append(" ").append(api);
    }

    /** A container audit generator. */
    private final ContainerAuditor auditor;

    /** The container io layer. */
    private final ContainerIOHandler containerIO;

    /** A default container comparator. */
    private final Comparator<Artifact> defaultComparator;

    /** The default document comparator. */
    private final Comparator<Artifact> defaultDocumentComparator;

    /** The default document filter. */
    private final Filter<? super Artifact> defaultDocumentFilter;

    /** A default container filter. */
    private final Filter<? super Artifact> defaultFilter;

    /** A default history item comparator. */
    private final Comparator<? super HistoryItem> defaultHistoryComparator;

    /** A default history item filter. */
    private final Filter<? super HistoryItem> defaultHistoryFilter;

    /** A default key request comparator. */
    private final Comparator<KeyRequest> defaultKeyRequestComparator;

    /** A default key request filter. */
    private final Filter<? super KeyRequest> defaultKeyRequestFilter;

    /** The default container version comparator. */
    private final Comparator<ArtifactVersion> defaultVersionComparator;

    /** The default container version filter. */
    private final Filter<? super ArtifactVersion> defaultVersionFilter;

    /** A container index writer. */
    private final ContainerIndexor indexor;

    /** A local event generator. */
    private final ContainerEventGenerator localEventGenerator;

    /** A remote event generator. */
    private final ContainerEventGenerator remoteEventGenerator;

    /**
     * Create ContainerModelImpl.
     *
     * @param workspace
     *		The thinkParity workspace.
     */
    ContainerModelImpl(final Workspace workspace) {
        super(workspace);
        this.auditor = new ContainerAuditor(getContext());
        this.containerIO = IOFactory.getDefault().createContainerHandler();
        this.defaultComparator = new ComparatorBuilder().createByName(Boolean.TRUE);
        this.defaultDocumentComparator = new ComparatorBuilder().createByName(Boolean.TRUE);
        this.defaultDocumentFilter = new DefaultFilter();
        this.defaultFilter = new DefaultFilter();
        this.defaultHistoryComparator = new ComparatorBuilder().createDateDescending();
        this.defaultHistoryFilter = new com.thinkparity.model.parity.model.filter.history.DefaultFilter();
        this.defaultKeyRequestComparator = null;
        this.defaultKeyRequestFilter = null;
        this.defaultVersionComparator = new ComparatorBuilder().createVersionById(Boolean.TRUE);
        this.defaultVersionFilter = new com.thinkparity.model.parity.model.filter.container.DefaultVersionFilter();
        this.indexor = new ContainerIndexor(getContext());
        this.localEventGenerator = new ContainerEventGenerator(Source.LOCAL);
        this.remoteEventGenerator = new ContainerEventGenerator(Source.REMOTE);
    }

    /**
     * Accept a key request made by a user.
     * 
     * @param keyRequestId
     *            The key request id.
     */
    void acceptKeyRequest(final Long keyRequestId) throws ParityException {
        logger.info(getApiId("[ACCEPT KEY REQUEST]"));
        logger.debug(keyRequestId);
        final KeyRequest keyRequest = readKeyRequest(keyRequestId);
        acceptKeyRequest(keyRequest.getArtifactId(), keyRequest.getRequestedBy());
    }

    /**
     * Add a document to a container.
     * 
     * @param containerId
     *            A container id.
     * @param inputStream
     *            An input stream.
     */
    void addDocument(final Long containerId, final Long documentId) {
        logger.info(getApiId("[ADD DOCUMENT]"));
        logger.debug(containerId);
        logger.debug(documentId);
        assertContainerDraftExists(getApiId("[ADD DOCUMENT] [DRAFT DOES NOT EXIST]"), containerId);
        containerIO.createDraftArtifactRel(
                containerId, documentId,
                ContainerDraftArtifactState.ADDED);

        final Container postAdditionContainer = read(containerId);        
        final ContainerDraft postAdditionDraft = readDraft(containerId);
        final Document postAdditionDocument = getInternalDocumentModel().read(documentId);
        notifyDocumentAdded(postAdditionContainer, postAdditionDraft,
                postAdditionDocument, localEventGenerator);
    }

    /**
     * Add a container listener.
     * 
     * @param listener
     *            A container listener.
     */
    void addListener(final ContainerListener listener) {
        logger.info(getApiId("[ADD LISTENER]"));
        logger.debug(listener);
        Assert.assertNotNull(getApiId("[ADD LISTENER] [LISTENER IS NULL]"),
                listener);
        synchronized(LISTENERS) {
            if(LISTENERS.contains(listener)) { return; }
            LISTENERS.add(listener);
        }
    }

    /**
     * Close a container.
     * 
     * @param containerId
     *            A container id.
     */
    void close(final Long containerId) throws ParityException {
        throw Assert.createNotYetImplemented("ContainerModelImpl#close(Long)");
    }

    /**
     * Create a container.
     * 
     * @param name
     *            The container name.
     * @return The new container.
     */
    Container create(final String name) throws ParityException {
        logger.info(getApiId("[CREATE]"));
        final Credentials credentials = readCredentials();
        final Calendar currentDateTime = currentDateTime();

        final Container container = new Container();
        container.setCreatedBy(credentials.getUsername());
        container.setCreatedOn(currentDateTime);
        container.setName(name);
        container.setState(ArtifactState.ACTIVE);
        container.setType(ArtifactType.CONTAINER);
        container.setUniqueId(UUIDGenerator.nextUUID());
        container.setUpdatedBy(credentials.getUsername());
        container.setUpdatedOn(currentDateTime);

        // local create
        containerIO.create(container);

        // create remote info
        final InternalArtifactModel artifactModel = getInternalArtifactModel();
        artifactModel.createRemoteInfo(container.getId(), currentUserId(), currentDateTime);

        // audit
        auditor.create(container.getId(), currentUserId(), currentDateTime);

        // index
        indexor.create(container.getId(), container.getName());

        // fire event
        final Container postCreation = read(container.getId());
        notifyContainerCreated(postCreation, localEventGenerator);

        // create team
        final TeamMember teamMember = artifactModel.createTeam(container.getId());

        // create first draft
        final ContainerDraft draft = new ContainerDraft();
        draft.setContainerId(container.getId());
        draft.setOwner(teamMember);
        containerIO.createDraft(draft);

        // fire draft event
        final ContainerDraft postCreationDraft = readDraft(container.getId());
        notifyDraftCreated(postCreation, postCreationDraft, localEventGenerator);

        return postCreation;
    }

    /**
     * Create a container draft.
     * 
     * @param containerId
     *            The container id.
     * @return A container draft.
     */
    ContainerDraft createDraft(final Long containerId) {
        logger.info(getApiId("[CREATE DRAFT]"));
        logger.debug(containerId);
        assertOnline(getApiId("[CREATE DRAFT] [USER NOT ONLINE]"));
        final Container container = read(containerId);
        if(!isDistributed(containerId)) { createDistributed(container); }

        final ContainerVersion version = createVersion(containerId);
        final List<Document> documents = readDocuments(version.getArtifactId(), version.getVersionId());

        // create
        final ContainerDraft draft = new ContainerDraft();
        draft.setContainerId(containerId);
        for(final Document document : documents)
            draft.addDocument(document, ContainerDraftArtifactState.NONE);
        containerIO.createDraft(draft);

        // remote create
        getInternalSessionModel().createDraft(container.getUniqueId());

        // fire event
        final Container postCreation= read(containerId);
        final ContainerDraft postCreationDraft = readDraft(containerId);
        notifyDraftCreated(postCreation, postCreationDraft, localEventGenerator);

        return postCreationDraft;
    }

    /**
     * Create a container version.
     * 
     * @param containerId
     *            A container id.
     * @return A container version.
     */
    ContainerVersion createVersion(final Long containerId) {
        logger.info(getApiId("[CREATE VERSION]"));
        logger.debug(containerId);
        return createVersion(read(containerId));
    }

    /**
     * Decline a key request made by a user.
     * 
     * @param keyRequestId
     *            The key request id.
     */
    void declineKeyRequest(final Long keyRequestId) throws ParityException {
        logger.info(getApiId("[DECLINE KEY REQUEST]"));
        logger.debug(keyRequestId);
        assertOnline(getApiId("[DECLINE KEY REQUEST] [USER NOT ONLINE]"));
        final KeyRequest keyRequest = readKeyRequest(keyRequestId);
        final Container container = read(keyRequest.getArtifactId());
        assertIsKeyHolder(getApiId("[DECLINE KEY REQUEST] [USER NOT KEY HOLDER]"), container.getId());
        // remote deline
        remoteDecline(container.getId(), keyRequest.getRequestedBy());
        // delete request
        deleteKeyRequest(keyRequestId);
    }

    /**
     * Delete a container.
     * 
     * @param containerId
     *            A container id.
     */
    void delete(final Long containerId) throws ParityException {
        logger.info(getApiId("[DELETE]"));
        logger.debug(containerId);
        assertOnline(getApiId("[DELETE] [USER NOT ONLINE]"));
        final Container container = read(containerId);
        if(isClosed(container)) { deleteLocal(containerId); }
        else {
            if(isKeyHolder(containerId)) {
                if(!isDistributed(containerId)) { deleteLocal(containerId); }
                else {
                    throw Assert.createUnreachable(getApiId(""));
                }
            }
            else {
                deleteRemote(containerId);
                deleteLocal(containerId);
            }
        }
        // fire event
        notifyContainerDeleted(null, localEventGenerator);
    }

    /**
     * Handle the close event from a remote event.
     * 
     * @param containerId
     *            The container id.
     * @param closedBy
     *            The user who closed the container.
     * @param closedOn
     *            When the container was closed.
     * @throws ParityException
     */
    void handleClose(final Long containerId, final JabberId closedBy,
            final Calendar closedOn) throws ParityException {
        throw Assert.createNotYetImplemented("ContainerModelImpl#handleClose(Long,JabberId)");
    }

    /**
     * Determine if the container has been locally modified.
     * 
     * @param containerId
     * @return True if the container has been locally modified.
     */
    Boolean isLocallyModified(final Long containerId) throws ParityException {
        throw Assert.createNotYetImplemented("ContainerModelImpl#isLocallyModified(Long)");
    }

    /**
     * Lock the container.
     * 
     * @param containerId
     *            The container id.
     */
    void lock(final Long containerId) throws ParityException {
        throw Assert.createNotYetImplemented("ContainerModelImpl#lock(Long)");
    }

    /**
     * Publish the container. Publishing involves determining if the working
     * version of a document differes from the latest version and if so creating
     * a new version; then sending the latest version to all team members.
     * 
     * @param containerId
     *            The conainter id.
     * @throws ParityException
     */
    void publish(final Long containerId) throws ParityException {
        logger.info(getApiId("[PUBLISH]"));
        logger.debug(containerId);
        assertOnline(getApiId("[PUBLISH] [USER NOT ONLINE]"));
        assertIsKeyHolder(getApiId("[PUBLISH] [USER NOT KEY HOLDER]"), containerId);
        final JabberId currentUserId = currentUserId();
        final Calendar currentDateTime = currentDateTime();

        // create neccessary versions
        Boolean didCreate = Boolean.FALSE;
        final InternalDocumentModel dModel = getInternalDocumentModel();
        final ContainerVersion latestVersion = readLatestVersion(containerId);
        final List<Document> documents = containerIO.readDocuments(containerId, latestVersion.getVersionId());
        DocumentVersion version;
        for(final Document document : documents) {
            if(!dModel.isWorkingVersionEqual(document.getId())) {
                didCreate = Boolean.TRUE;
                version = dModel.createVersion(document.getId());
                auditor.publish(document.getId(), version.getVersionId(),
                        currentUserId, currentDateTime);
            }
        }
        Assert.assertTrue(getApiId("[CREATE] [NO DOCUMENTS DIFFER]"), didCreate);

        // create container
        final ContainerVersion newVersion = createVersion(containerId);

        // audit
        auditor.publish(containerId, newVersion.getVersionId(), currentUserId,
                currentDateTime);

        // send
        final List<User> team =
            getInternalSessionModel().readArtifactTeamList(containerId);
        team.remove(currentUserId());
        send(newVersion, team);
    }

    /**
     * Read the containers.
     * 
     * @return A list of containers.
     */
    List<Container> read() {
        logger.info(getApiId("[READ]"));
        return read(defaultComparator);
    }

    /**
     * Read the containers.
     * 
     * @param comparator
     *            A sort ordering to user.
     * @return A list of containers.
     */
    List<Container> read(final Comparator<Artifact> comparator) {
        logger.info(getApiId("[READ]"));
        logger.debug(comparator);
        logger.warn(getApiId("[READ] [NOT YET IMPLEMENTED]"));
        return read(comparator, defaultFilter);
    }

    /**
     * Read the containers.
     * 
     * @param comparator
     *            A sort ordering to user.
     * @param filter
     *            A filter to apply.
     * @return A list of containers.
     */
    List<Container> read(final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        logger.info(getApiId("[READ]"));
        logger.debug(comparator);
        logger.debug(filter);
        final List<Container> containers = containerIO.read();
        ArtifactFilterManager.filter(containers, filter);
        ModelSorter.sortContainers(containers, comparator);
        return containers;        
    }

    /**
     * Read the containers.
     * 
     * @param filter
     *            A filter to apply.
     * @return A list of containers.
     */
    List<Container> read(final Filter<? super Artifact> filter) {
        logger.info(getApiId("[READ]"));
        logger.debug(filter);
        return read(defaultComparator, filter);
    }

    /**
     * Read a container.
     * 
     * @param containerId
     *            A container id.
     * @return A container.
     */
    Container read(final Long containerId) {
        logger.info(getApiId("[READ]"));
        logger.debug(containerId);
        return containerIO.read(containerId);
    }

    /**
     * Read the list of audit events for a container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of audit events.
     */
    List<AuditEvent> readAuditEvents(final Long containerId) {
        logger.info(getApiId("[READ AUDIT EVENTS]"));
        logger.debug(containerId);
        return getInternalAuditModel().read(containerId);
    }

    /**
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @return A list of documents.
     * @throws ParityException
     */
    List<Document> readDocuments(final Long containerId, final Long versionId) {
        logger.info(getApiId("[READ DOCUMENTS]"));
        logger.debug(containerId);
        return readDocuments(containerId, versionId, defaultDocumentComparator, defaultDocumentFilter);
    }

    /**
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @param comparator
     *            A document comparator.
     * @return A list of documents.
     * @throws ParityException
     */
    List<Document> readDocuments(final Long containerId, final Long versionId,
            final Comparator<Artifact> comparator) {
        logger.info(getApiId("[READ DOCUMENTS]"));
        return readDocuments(containerId, versionId, comparator, defaultDocumentFilter);
    }

    /**
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id.
     * @versionId A version id.
     * @param comparator
     *            A document comparator.
     * @param filter
     *            A document filter.
     * @return A list of documents.
     * @throws ParityException
     */
    List<Document> readDocuments(final Long containerId, final Long versionId,
            final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        logger.info(getApiId("[READ DOCUMENTS]"));
        logger.debug(containerId);
        logger.debug(versionId);
        logger.debug(comparator);
        logger.debug(filter);
        final List<Document> documents = containerIO.readDocuments(
                containerId, versionId);
        ArtifactFilterManager.filter(documents, filter);
        ModelSorter.sortDocuments(documents, comparator);
        return documents;
    }

    /**
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @param filter
     *            A document filter.
     * @return A list of documents.
     * @throws ParityException
     */
    List<Document> readDocuments(final Long containerId, final Long versionId,
            final Filter<? super Artifact> filter) {
        logger.info(getApiId("[READ DOCUMENTS]"));
        logger.debug(containerId);
        logger.debug(versionId);
        logger.debug(filter);
        return readDocuments(containerId, versionId, defaultDocumentComparator, filter);
    }

    /**
     * Read a list of document versions for the latest container version.
     * 
     * @param containerId
     *            The container id.
     * @return A list of document versions.
     */
    List<DocumentVersion> readDocumentVersions(final Long containerId) {
        logger.info(getApiId("[READ DOCUMENT VERSIONS]"));
        logger.debug(containerId);
        final ContainerVersion latestVersion = readLatestVersion(containerId);
        return containerIO.readDocumentVersions(containerId, latestVersion.getVersionId());
    }

    /**
     * Read a container draft.
     * 
     * @param containerId
     *            A container id.
     * @return A container draft.
     */
    ContainerDraft readDraft(final Long containerId) {
        logger.info(getApiId("[READ DRAFT]"));
        logger.debug(containerId);
        return containerIO.readDraft(containerId);
    }

    /**
     * Read the container history.
     * 
     * @param containerId
     *            A container id.
     * @return A list of history items.
     */
    List<ContainerHistoryItem> readHistory(final Long containerId) {
        logger.info(getApiId("[READ HISTORY]"));
        logger.debug(containerId);
        return readHistory(containerId, defaultHistoryComparator, defaultHistoryFilter);
    }

    /**
     * Read the container history.
     * 
     * @param containerId
     *            A container id.
     * @param comparator
     *            A history item comparator
     * @return A list of history items.
     */
    List<ContainerHistoryItem> readHistory(final Long containerId,
            final Comparator<? super HistoryItem> comparator) {
        logger.info(getApiId("[READ HISTORY]"));
        logger.debug(containerId);
        logger.debug(comparator);
        return readHistory(containerId, comparator, defaultHistoryFilter);
    }

    /**
     * Read the container history.
     * 
     * @param containerId
     *            A container id.
     * @param comparator
     *            A history item comparator
     * @param filter
     *            A history item filter.
     * @return A list of history items.
     */
    List<ContainerHistoryItem> readHistory(final Long containerId,
            final Comparator<? super HistoryItem> comparator,
            final Filter<? super HistoryItem> filter) {
        logger.info(getApiId("[READ HISTORY]"));
        logger.debug(containerId);
        logger.debug(comparator);
        logger.debug(filter);
        final ContainerHistoryBuilder historyBuilder =
            new ContainerHistoryBuilder(getInternalContainerModel(), l18n);
        final List<ContainerHistoryItem> history = historyBuilder.createHistory(containerId);
        HistoryFilterManager.filter(history, filter);
        ModelSorter.sortHistory(history, defaultHistoryComparator);
        return history;
    }

    /**
     * Read the container history.
     * 
     * @param containerId
     *            A container id.
     * @param filter
     *            A history item filter.
     * @return A list of history items.
     */
    List<ContainerHistoryItem> readHistory(final Long containerId,
            final Filter<? super HistoryItem> filter) {
        logger.info(getApiId("[READ HISTORY]"));
        logger.debug(containerId);
        logger.debug(filter);
        return readHistory(containerId, defaultHistoryComparator, filter);
    }

    /**
     * Read a list of key requests for the container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of key requests.
     */
    List<KeyRequest> readKeyRequests(final Long containerId) {
        logger.info(getApiId("[READ KEY REQUESTS]"));
        logger.debug(containerId);
        return readKeyRequests(containerId, defaultKeyRequestComparator);
    }

    /**
     * Read a list of key requests for the container.
     * 
     * @param containerId
     *            A container id.
     * @param comparator
     *            A key request comparator.
     * @return A list of key requests.
     */
    List<KeyRequest> readKeyRequests(final Long containerId,
            final Comparator<KeyRequest> comparator) {
        logger.info(getApiId("[READ KEY REQUESTS]"));
        logger.debug(containerId);
        logger.debug(comparator);
        return readKeyRequests(containerId, comparator, defaultKeyRequestFilter);
    }

    /**
     * Read a list of key requests for the container.
     * 
     * @param containerId
     *            A container id.
     * @param comparator
     *            A key request comparator.
     * @param filter
     *            A key request filter.
     * @return A list of key requests.
     */
    List<KeyRequest> readKeyRequests(final Long containerId,
            final Comparator<KeyRequest> comparator,
            final Filter<? super KeyRequest> filter) {
        logger.info(getApiId("[READ KEY REQUESTS]"));
        logger.debug(containerId);
        logger.debug(comparator);
        logger.debug(filter);
        logger.warn(getApiId("[READ KEY REQUESTS] [COMPARATOR NOT USED]"));
        logger.warn(getApiId("[READ KEY REQUESTS] [FILTER NOT USED]"));
        return getInternalArtifactModel().readKeyRequests(containerId);
    }

    /**
     * Read a list of key requests for the container.
     * 
     * @param containerId
     *            A container id.
     * @param filter
     *            A key request filter.
     * @return A list of key requests.
     */
    List<KeyRequest> readKeyRequests(final Long containerId,
            final Filter<? super KeyRequest> filter) {
        logger.info(getApiId("[READ KEY REQUESTS]"));
        logger.debug(containerId);
        logger.debug(filter);
        return readKeyRequests(containerId, defaultKeyRequestComparator, filter);
    }

    /**
     * Read the latest container version.
     * 
     * @param containerId
     *            A container id.
     * @return A container version.
     */
    ContainerVersion readLatestVersion(final Long containerId) {
        logger.info(getApiId("[READ LATEST VERSION]"));
        logger.debug(containerId);
        return containerIO.readLatestVersion(containerId);
    }

    /**
     * Read the team for the container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of users.
     */
    List<TeamMember> readTeam(final Long containerId) {
        logger.info(getApiId("[READ TEAM]"));
        logger.debug(containerId);
        return getInternalArtifactModel().readTeam2(containerId);
    }

    /**
     * Read a container version.
     * 
     * @param containerId
     *            The container id.
     * @param versionId
     *            The version id.
     * @return A container version.
     */
    ContainerVersion readVersion(final Long containerId, final Long versionId) {
        logger.info(getApiId("[READ VERSION]"));
        logger.debug(containerId);
        logger.debug(versionId);
        return containerIO.readVersion(containerId, versionId);
    }


    /**
     * Read the container versions.
     * 
     * @param containerId
     *            The container id.
     * @return A list of container versions.
     */
    List<ContainerVersion> readVersions(final Long containerId) {
        logger.info(getApiId("[READ VERSIONS]"));
        logger.debug(containerId);
        return readVersions(containerId, defaultVersionComparator);
    }

    /**
     * Read a list of versions for the container.
     * 
     * @param containerId
     *            A container id.
     * @param comparator
     *            A container version comparator.
     * @return A list of versions.
     */
    List<ContainerVersion> readVersions(final Long containerId,
            final Comparator<ArtifactVersion> comparator) {
        logger.info(getApiId("[READ VERSIONS]"));
        logger.debug(containerId);
        logger.debug(comparator);
        return readVersions(containerId, comparator, defaultVersionFilter);
    }

    /**
     * Read a list of versions for the container.
     * 
     * @param containerId
     *            A container id.
     * @param comparator
     *            A container version comparator.
     * @param filter
     *            A container version filter.
     * @return A list of versions.
     */
    List<ContainerVersion> readVersions(final Long containerId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        logger.info(getApiId("[READ VERSIONS]"));
        logger.debug(containerId);
        logger.debug(comparator);
        logger.debug(filter);
        final List<ContainerVersion> versions = containerIO.readVersions(containerId);
        ArtifactFilterManager.filterVersions(versions, filter);
        ModelSorter.sortContainerVersions(versions, comparator);
        return containerIO.readVersions(containerId);
    }
   
    /**
     * Read a list of versions for the container.
     * 
     * @param containerId
     *            A container id.
     * @param filter
     *            A container version filter.
     * 
     * @return A list of versions.
     */
    List<ContainerVersion> readVersions(final Long containerId,
            final Filter<? super ArtifactVersion> filter) {
        logger.info(getApiId("[READ VERSIONS]"));
        logger.debug(containerId);
        logger.debug(filter);
        return readVersions(containerId, defaultVersionComparator, filter);
    }

    /**
     * Remove a document from a container.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     */
    void removeDocument(final Long containerId, final Long documentId) {
        logger.info(getApiId("[ADD DOCUMENT]"));
        logger.debug(containerId);
        logger.debug(documentId);
        assertContainerDraftExists(getApiId("[ADD DOCUMENT] [DRAFT DOES NOT EXIST]"), containerId);
        containerIO.deleteDraftArtifactRel(containerId, documentId);
        containerIO.createDraftArtifactRel(containerId, documentId,
                ContainerDraftArtifactState.REMOVED);

        final Container postAdditionContainer = read(containerId);        
        final ContainerDraft postAdditionDraft = readDraft(containerId);
        final Document postAdditionDocument = getInternalDocumentModel().read(documentId);
        notifyDocumentRemoved(postAdditionContainer, postAdditionDraft, postAdditionDocument, localEventGenerator);
    }

    /**
     * Remove a container listener.
     * 
     * @param listener
     *            A container listener.
     */
    void removeListener(final ContainerListener listener) {
        logger.info(getApiId("[REMOVE LISTENER]"));
        logger.debug(listener);
        Assert.assertNotNull(getApiId("[REMOVE LISTENER] [LISTENER IS NULL]"),
                listener);
        synchronized(LISTENERS) {
            if(!LISTENERS.contains(listener)) { return; }
            LISTENERS.remove(listener);
        }
    }

    /**
     * Send the container's key to a user. All pending key requests not made by
     * the recipient of the key will be declined.
     * 
     * @param containerId
     *            The container id.
     * @param jabberId
     *            The jabber id.
     */
    void sendKey(final Long containerId, final JabberId jabberId)
            throws ParityException {
        logger.info(getApiId("[SEND KEY]"));
        logger.debug(containerId);
        logger.debug(jabberId);
        acceptKeyRequest(containerId, jabberId);
    }

    /**
     * Update the team for the container.
     * 
     * @param containerId
     *            A container id.
     * @param team
     *            A list of users.
     */
    void updateTeam(final Long containerId, final List<User> users) {
        logger.info(getApiId("[UPDATE TEAM]"));
        logger.debug(containerId);
        logger.debug(users);
        assertOnline(getApiId("[UPDATE TEAM] [USER NOT ONLINE]"));
        if(!isDistributed(containerId)) { createDistributed(read(containerId)); }
        try {
            final List<TeamMember> originalTeam = readTeam(containerId);
    
            final List<User> usersToAdd = new ArrayList<User>();
            for(final User user : users)
                if(!contains(originalTeam, user))
                    usersToAdd.add(user);
            addTeamMembers(containerId, usersToAdd);
    
            final List<TeamMember> membersToRemove = new ArrayList<TeamMember>();
            for(final TeamMember teamMember : originalTeam)
                if(!contains(users, teamMember))
                    membersToRemove.add(teamMember);
            removeTeamMembers(containerId, membersToRemove);
        }
        catch(final ParityException px) {
            logger.error(getApiId("[UPDATE TEAM]"), px);
            throw ParityErrorTranslator.translateUnchecked(px);
        }
    }

    /**
     * Accept a key request made by a user.
     * 
     * @param containerId
     *            The container id.
     * @param jabberId
     *            The user.
     * @throws ParityException
     */
    private void acceptKeyRequest(final Long containerId,
            final JabberId jabberId) throws ParityException {
        assertOnline(getApiId("[ACCEPT KEY REQUEST] [USER NOT ONLINE]"));
        assertIsKeyHolder(getApiId("[ACCEPT KEY REQUEST] [USER NOT KEY HOLDER]"), containerId);

        // decline\delete all other requests
        final List<KeyRequest> keyRequests = readKeyRequests(containerId);
        for(final KeyRequest keyRequest : keyRequests) {
            if(!keyRequest.getRequestedBy().equals(jabberId)) {
                declineKeyRequest(keyRequest.getId());
            }
            else { deleteKeyRequest(keyRequest.getId()); }
        }

        // if the container has been changed publish it
        if(isLocallyModified(containerId))
            publish(containerId);

        // send key
        remoteAccept(containerId, jabberId);

        // remove key
        removeFlagKey(containerId);

        // lock 
        lock(containerId);

        // audit
        final JabberId currentUserId = currentUserId();
        final Calendar currentDateTime = currentDateTime();
        final ContainerVersion latestVersion = readLatestVersion(containerId);
        auditor.sendKey(containerId, currentUserId, currentDateTime,
                latestVersion.getVersionId(), currentUserId, currentDateTime,
                jabberId);
    }

    /**
     * Add team members to the container. The team members are added to the
     * local db; then sent the latest version of the container.
     * 
     * @param containerId
     *            The container id.
     * @param teamMembers
     *            The team members.
     * @throws ParityException
     */
    private void addTeamMembers(final Long containerId, final List<User> users)
            throws ParityException {
        // add team member data
        getInternalArtifactModel().addTeamMembers(containerId, users);
        // send package
        send(readLatestVersion(containerId), users);
        // index
        updateIndex(containerId);

        final Container postAdditionContainer = read(containerId);
        final List<TeamMember> postAdditionTeam = readTeam(containerId);
        for(final User user : users) {
            for(final TeamMember postAdditionTeamMember : postAdditionTeam) {
                if(user.getId().equals(postAdditionTeamMember.getId()))
                    notifyTeamMemberAdded(postAdditionContainer, postAdditionTeamMember, localEventGenerator);
            }
        }
    }

    /**
     * Assert that the list of documents contains the document id.
     * 
     * @param assertion
     *            An assertion message.
     * @param documents
     *            A list of documents.
     * @param documentId
     *            A document id.
     */
    private void assertContains(final Object assertion,
            final List<Document> documents, final Long documentId) {
        Boolean didContain = Boolean.FALSE;
        for(final Document document : documents) {
            if(document.getId().equals(documentId)) {
                didContain = Boolean.TRUE;
                break;
            }
        }
        Assert.assertTrue(assertion, didContain);
    }

    /**
     * Assert that the list of documents does not contain the document.
     * 
     * @param assertion
     *            The assertion.
     * @param documents
     *            A list of documents.
     * @param documentId
     *            A document id.
     */
    private void assertDoesNotContain(final Object assertion,
            final List<Document> documents, final Long documentId) {
        for(final Document document : documents)
            Assert.assertNotTrue(assertion, document.getId().equals(documentId));
    }

    /**
     * Determine if the list of team members contains the user.
     * 
     * @param team
     *            A list of team members.
     * @param user
     *            A user.
     * @return True if the id of the user matches one of the team members.
     */
    private Boolean contains(final List<TeamMember> team, final User user) {
        for(final TeamMember teamMember : team) {
            if(teamMember.getId().equals(user.getId())) { return Boolean.TRUE; }
        }
        return Boolean.FALSE;
    }

    /**
     * Determine if the list of users contains the team member.
     * 
     * @param users
     *            A list of users.
     * @param teamMember
     *            A team member.
     * @return True if the id of the team member matches one of the users.
     */
    private Boolean contains(final List<User> users, final TeamMember teamMember) {
        for(final User user: users) {
            if(user.getId().equals(teamMember.getId())) { return Boolean.TRUE; }
        }
        return Boolean.FALSE;
    }

    /**
     * Create the container in the distributed network.
     * 
     * @param container
     *            The container.
     */
    private void createDistributed(final Container container) {
        try { getInternalSessionModel().sendCreate(container); }
        catch(final ParityException px) {
            throw ParityErrorTranslator.translateUnchecked(px);
        }
    }
    
    /**
     * Create a container version.
     * 
     * @param containerId
     *            A container id.
     * @return The container version.
     */
    private ContainerVersion createVersion(final Container container) {
        final ContainerVersion version = new ContainerVersion();
        version.setArtifactId(container.getId());
        version.setArtifactType(container.getType());
        version.setArtifactUniqueId(container.getUniqueId());
        version.setCreatedBy(container.getCreatedBy());
        version.setCreatedOn(container.getCreatedOn());
        version.setName(container.getName());
        version.setUpdatedBy(container.getUpdatedBy());
        version.setUpdatedOn(container.getUpdatedOn());
        containerIO.createVersion(version);
        return containerIO.readVersion(version.getArtifactId(), version.getVersionId());
    }

    /**
     * Delete a key request.
     * 
     * @param keyRequestId
     *            A key request id.
     */
    private void deleteKeyRequest(final Long keyRequestId) {
        getInternalMessageModel().delete(keyRequestId);
    }

    /**
     * Delete the local info for this container.
     * 
     * @param containerId
     *            The container id.
     */
    private void deleteLocal(final Long containerId) throws ParityException {
        final InternalArtifactModel aModel = getInternalArtifactModel();
        // delete the team
        aModel.deleteTeam(containerId);
        // delete the remote info
        aModel.deleteRemoteInfo(containerId);
        // delete the audit events
        getInternalAuditModel().delete(containerId);
        // delete versions
        final List<ContainerVersion> versions = readVersions(containerId);
        for(final ContainerVersion version : versions) {
            // remove the version's artifact versions
            containerIO.removeVersions(containerId, version.getVersionId());
            // delete the version
            containerIO.deleteVersion(containerId, version.getVersionId());
        }
        // delete the index
        indexor.delete(containerId);
        // delete the container
        containerIO.delete(containerId);
    }

    /**
     * Delete the remote info for this container.
     * 
     * @param containerId
     *            The container id.
     */
    private void deleteRemote(final Long containerId) throws ParityException {
        getInternalSessionModel().sendDelete(containerId);
    }

    /**
     * Fire a container created notification.
     * 
     * @param container
     *            A container.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyContainerCreated(final Container container,
            final ContainerEventGenerator eventGenerator) {
        synchronized(LISTENERS) {
            for(final ContainerListener l : LISTENERS) {
                l.containerCreated(eventGenerator.generate(container));
            }
        }
    }

    /**
     * Fire a container deleted notification.
     * 
     * @param container
     *            A container.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyContainerDeleted(final Container container,
            final ContainerEventGenerator eventGenerator) {
        synchronized(LISTENERS) {
            for(final ContainerListener l : LISTENERS) {
                l.containerDeleted(eventGenerator.generate(container));
            }
        }
    }

    /**
     * Fire a document added notification.
     * 
     * @param container
     *            A container.
     * @param draft
     *            A draft.
     * @param document
     *            A document.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyDocumentAdded(final Container container,
            final ContainerDraft draft, final Document document,
            final ContainerEventGenerator eventGenerator) {
        synchronized(LISTENERS) {
            for(final ContainerListener l : LISTENERS) {
                l.documentAdded(eventGenerator.generate(container, draft, document));
            }
        }
    }

    /**
     * Fire a document removed notification.
     * 
     * @param container
     *            A container.
     * @param draft
     *            A draft.
     * @param document
     *            A document.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyDocumentRemoved(final Container container,
            final ContainerDraft draft, final Document document,
            final ContainerEventGenerator eventGenerator) {
        synchronized(LISTENERS) {
            for(final ContainerListener l : LISTENERS) {
                l.documentRemoved(eventGenerator.generate(container, draft, document));
            }
        }
        
    }

    /**
     * Notify that a container draft was created.
     * 
     * @param draft
     *            The draft.
     * @param eventGenerator
     *            The container event generator.
     */
    private void notifyDraftCreated(final Container container,
            final ContainerDraft draft,
            final ContainerEventGenerator eventGenerator) {
        synchronized(LISTENERS) {
            for(final ContainerListener l : LISTENERS) {
                l.draftCreated(eventGenerator.generate(container, draft));
            }
        }
    }

    /**
     * Notify that a team member was added to the container's team.
     * 
     * @param container
     *            A container.
     * @param teamMember
     *            A team member.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyTeamMemberAdded(final Container container,
            final TeamMember teamMember,
            final ContainerEventGenerator eventGenerator) {
        synchronized(LISTENERS) {
            for(final ContainerListener l : LISTENERS) {
                l.teamMemberAdded(eventGenerator.generate(container, teamMember));
            }
        }
    }

    /**
     * Read a key request.
     * 
     * @param keyRequestId
     *            A key request id.
     * @return A key request.
     */
    private KeyRequest readKeyRequest(final Long keyRequestId) {
        return getInternalArtifactModel().readKeyRequest(keyRequestId);
    }

    /**
     * Call the remote accept api.
     * 
     * @param containerId
     *            The container id.
     * @param jabberId
     *            The jabber id.
     * @throws ParityException
     */
    private void remoteAccept(final Long containerId, final JabberId jabberId)
            throws ParityException {
        getInternalSessionModel().sendKeyResponse(KeyResponse.ACCEPT, containerId, jabberId);
    }

    /**
     * Call the remote decline api.
     * 
     * @param containerId
     *            The container id.
     * @param jabberId
     *            The jabber id.
     * @throws ParityException
     */
    private void remoteDecline(final Long containerId, final JabberId jabberId)
            throws ParityException {
        getInternalSessionModel().sendKeyResponse(KeyResponse.DENY, containerId, jabberId);
    }

    /**
     * Remove the key locally.
     * 
     * @param containerId
     *            The container id.
     */
    private void removeFlagKey(final Long containerId) {
        getInternalArtifactModel().removeFlagKey(containerId);
    }

    /**
     * Add team members to the container. The team members are added to the
     * local db; then sent the latest version of the container.
     * 
     * @param containerId
     *            The container id.
     * @param teamMembers
     *            The team members.
     * @throws ParityException
     */
    private void removeTeamMembers(final Long containerId,
            final List<TeamMember> teamMembers) throws ParityException {
        // remove team member data
        getInternalArtifactModel().removeTeamMembers(teamMembers);
        // update index
        updateIndex(containerId);
    }

    /**
     * Send the version to a user.
     * 
     * @param version
     *            The container version.
     * @param user
     *            A list of users.
     * @throws ParityException
     */
    private void send(final ContainerVersion version, final List<User> users)
            throws ParityException {
        final InternalDocumentModel documentModel = getInternalDocumentModel();
        final Map<DocumentVersion, InputStream> documentVersions =
            new HashMap<DocumentVersion, InputStream>();
        final List<Document> documents =
                readDocuments(version.getArtifactId(), version.getVersionId());
        DocumentVersion documentVersion;
        for(final Document document : documents) {
            documentVersion = documentModel.readLatestVersion(document.getId());
            documentVersions.put(documentVersion,
                    documentModel.openStream(
                            documentVersion.getArtifactId(),
                            documentVersion.getVersionId()));
        }
        getInternalSessionModel().send(version, documentVersions, users);
    }

    /**
     * Update the container's index entry.
     * 
     * @param containerId
     *            The container id.
     * @throws ParityException
     */
    private void updateIndex(final Long containerId) throws ParityException {
        logger.info("[LMODEL] [DOCUMENT] [UPDATE INDEX]");
        logger.debug(containerId);
        indexor.delete(containerId);
        indexor.create(containerId, read(containerId).getName());
    }
}
