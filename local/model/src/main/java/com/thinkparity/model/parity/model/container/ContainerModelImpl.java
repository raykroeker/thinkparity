/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.model.parity.model.container;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.Constants.IO;
import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerListener;
import com.thinkparity.model.parity.api.events.ContainerEvent.Source;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
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
import com.thinkparity.model.parity.model.session.InternalSessionModel;
import com.thinkparity.model.parity.model.sort.ComparatorBuilder;
import com.thinkparity.model.parity.model.sort.ModelSorter;
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.UUIDGenerator;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * <b>Title:</b>thinkParity Container Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.3
 */
class ContainerModelImpl extends AbstractModelImpl {

    /** A list of container listeners. */
    private static final List<ContainerListener> LISTENERS = new LinkedList<ContainerListener>();

    /**
     * Obtain an apache api log id.
     * @param api The api.
     * @return The log id.
     */
    private static StringBuffer getApiId(final String api) {
        return getModelId("CONTAINER").append(" ").append(api);
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
     * Read the latest container version.
     * 
     * @param containerId
     *            A container id.
     * @return A container version.
     */
    public ContainerVersion readLatestVersion(final Long containerId) {
        logger.info(getApiId("[READ LATEST VERSION]"));
        logger.debug(containerId);
        return containerIO.readLatestVersion(containerId);
    }

    /**
     * Add a document to a container.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     */
    void addDocument(final Long containerId, final Long documentId) {
        logger.info(getApiId("[ADD DOCUMENT]"));
        logger.debug(containerId);
        logger.debug(documentId);
        assertDraftExists(getApiId("[ADD DOCUMENT] [DRAFT DOES NOT EXIST]"), containerId);
        containerIO.createDraftArtifactRel(containerId, documentId, ContainerDraft.ArtifactState.ADDED);

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
    Container create(final String name) {
        logApiId();
        logVariable("name", name);
        try {
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
    
            // local key
            final InternalArtifactModel artifactModel = getInternalArtifactModel();
            artifactModel.applyFlagKey(container.getId());
    
            // create remote info
            artifactModel.createRemoteInfo(container.getId(), localUserId(), currentDateTime);
    
            // audit
            auditor.create(container.getId(), localUserId(), currentDateTime);
    
            // index
            indexor.create(container.getId(), container.getName());
    
            // create team
            final TeamMember teamMember = createTeam(container.getId());
    
            // create first draft
            createFirstDraft(container.getId(), teamMember);
    
            // fire event
            final Container postCreation = read(container.getId());
            notifyContainerCreated(postCreation, localEventGenerator);
            return postCreation;
        } catch (final Throwable t) {
            throw translateError("CREATE", t);
        }
    }

    /**
     * Create a container draft.
     * 
     * @param containerId
     *            The container id.
     * @return A container draft.
     * @see #createFirstDraft(Container, List)
     */
    ContainerDraft createDraft(final Long containerId) {
        logger.info(getApiId("[CREATE DRAFT]"));
        logger.debug(containerId);
        assertOnline(getApiId("[CREATE DRAFT] [USER NOT ONLINE]"));
        final Container container = read(containerId);
        if(!isDistributed(container.getUniqueId())) {
            createDistributed(container);
        }

        final ContainerVersion latestVersion =
                readLatestVersion(container.getId());
        final List<Document> documents = readDocuments(
                latestVersion.getArtifactId(), latestVersion.getVersionId());

        // create
        final ContainerDraft draft = new ContainerDraft();
        draft.setOwner(localTeamMember(containerId));
        draft.setContainerId(containerId);
        for(final Document document : documents) {
            draft.addDocument(document);
            draft.putState(document, ContainerDraft.ArtifactState.NONE);
        }
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
        logApiId();
        logVariable("containerId", containerId);
        return createVersion(containerId, readNextVersionId(containerId),
                localUserId(), currentDateTime());
    }

    /**
     * Delete a container.
     * 
     * @param containerId
     *            A container id.
     */
    void delete(final Long containerId) {
        logApiId();
        logVariable("containerId", containerId);
        try {
            final Container container = read(containerId);
            if (isDistributed(container.getUniqueId())) {
                final TeamMember localTeamMember = localTeamMember(container.getId());
                deleteLocal(container.getId());
                getInternalSessionModel().removeTeamMember(
                        container.getUniqueId(), localTeamMember.getId());
            } else {
                deleteLocal(container.getId());
            }
            // fire event
            notifyContainerDeleted(null, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError("[DELETE]", t);
        }
    }

    /**
     * Handle the artifact sent event from a remote event.
     * 
     * @param containerId
     *            The container id.
     * @param closedBy
     *            The user who closed the container.
     * @param closedOn
     *            When the container was closed.
     * @throws ParityException
     */
    void handleArtifactPublished(final JabberId publishedBy,
            final Calendar publishedOn, final UUID containerUniqueId,
            final Long containerVersionId, final String containerName,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final String artifactName, final ArtifactType artifactType,
            final String artifactChecksum, final byte[] artifactBytes) {
        logApiId();
        logVariable("publishedBy", publishedBy);
        logVariable("publishedOn", publishedOn);
        logVariable("containerUniqueId", containerUniqueId);
        logVariable("containerVersionId", containerVersionId);
        logVariable("containerName", containerName);
        logVariable("artifactUniqueId", artifactUniqueId);
        logVariable("artifactVersionId", artifactVersionId);
        logVariable("artifactName", artifactName);
        logVariable("artifactType", artifactType);
        logVariable("artifactChecksum", artifactChecksum);
        logVariable("artifactBytes", artifactBytes);
        try {
            // determine the existance of the container and the version.
            final InternalArtifactModel artifactModel = getInternalArtifactModel();
            final Boolean doesExist = artifactModel.doesExist(containerUniqueId);
            final Boolean doesVersionExist;
            final Long containerId;
            final Container container;
            final ContainerVersion version;
            if (doesExist) {
                containerId = artifactModel.readId(containerUniqueId);
                container = read(containerId);
                doesVersionExist = artifactModel.doesVersionExist(containerId, containerVersionId);

                if (doesVersionExist) {
                    version = readVersion(container.getId(), containerVersionId);
                } else {
                    version = createVersion(container.getId(),
                            containerVersionId, publishedBy, publishedOn);
                }
            } else { 
                doesVersionExist = Boolean.FALSE;

                container = new Container();
                container.setCreatedBy(publishedBy.getUsername());
                container.setCreatedOn(publishedOn);
                container.setName(containerName);
                container.setState(ArtifactState.ACTIVE);
                container.setType(ArtifactType.CONTAINER);
                container.setUniqueId(containerUniqueId);
                container.setUpdatedBy(container.getCreatedBy());
                container.setUpdatedOn(container.getCreatedOn());
                // create
                containerIO.create(container);

                // create version
                version = createVersion(container.getId(), containerVersionId,
                        publishedBy, publishedOn);
    
                // create remote info
                artifactModel.createRemoteInfo(container.getId(), publishedBy, container.getCreatedOn());
    
                // index
                indexor.create(container.getId(), container.getName());
            }

            // handle the artifact by specific type
            final ArtifactVersion artifactVersion;
            switch(artifactType) {
            case DOCUMENT:
                artifactVersion = handleDocumentPublished(publishedBy,
                        publishedOn, artifactUniqueId, artifactVersionId,
                        artifactName, artifactChecksum, artifactBytes);
                break;
            case CONTAINER:
            default:
                throw Assert.createUnreachable("UNKNOWN ARTIFACT TYPE");
            }
            containerIO.addVersion(version.getArtifactId(),
                    version.getVersionId(), artifactVersion.getArtifactId(),
                    artifactVersion.getVersionId(),
                    artifactVersion.getArtifactType());

            final Container postPublish = read(container.getId());
            final ContainerVersion postPublishVersion =
                readVersion(version.getArtifactId(), version.getVersionId());
            notifyContainerPublished(postPublish, null, postPublishVersion,
                    remoteEventGenerator);
        }
        catch(final Throwable t) {
            throw translateError(getApiId("[HANDLE ARTIFACT SENT]"), t);
        }
    }

    /**
     * Handle the artifact sent event from a remote event.
     * 
     * @param containerId
     *            The container id.
     * @param closedBy
     *            The user who closed the container.
     * @param closedOn
     *            When the container was closed.
     * @throws ParityException
     */
    void handleArtifactSent(final JabberId sentBy, final Calendar sentOn,
            final UUID containerUniqueId, final Long containerVersionId,
            final String containerName, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes) {
        logApiId();
        logVariable("sentBy", sentBy);
        logVariable("sentOn", sentOn);
        logVariable("containerUniqueId", containerUniqueId);
        logVariable("containerVersionId", containerVersionId);
        logVariable("containerName", containerName);
        logVariable("artifactUniqueId", artifactUniqueId);
        logVariable("artifactVersionId", artifactVersionId);
        logVariable("artifactName", artifactName);
        logVariable("artifactType", artifactType);
        logVariable("artifactChecksum", artifactChecksum);
        logVariable("artifactBytes", artifactBytes);
        try {
            final InternalArtifactModel artifactModel = getInternalArtifactModel();
            final Container container;
            final ContainerVersion version;
            final Boolean doesExist = artifactModel.doesExist(containerUniqueId);
            final Boolean doesVersionExist;
            if(doesExist) {
                container = read(artifactModel.readId(containerUniqueId));
                doesVersionExist =
                    artifactModel.doesVersionExist(container.getId(), containerVersionId);
                if(doesVersionExist) {
                    version = readVersion(container.getId(), containerVersionId);
                }
                else {
                    version = createVersion(container.getId(),
                            containerVersionId, sentBy, sentOn);
                }
            }
            else {
                doesVersionExist = Boolean.FALSE;

                container = new Container();
                container.setCreatedBy(sentBy.getUsername());
                container.setCreatedOn(sentOn);
                container.setName(containerName);
                container.setState(ArtifactState.ACTIVE);
                container.setType(ArtifactType.CONTAINER);
                container.setUniqueId(containerUniqueId);
                container.setUpdatedBy(container.getCreatedBy());
                container.setUpdatedOn(container.getCreatedOn());
                // create
                containerIO.create(container);

                // create version
                version = createVersion(container.getId(), containerVersionId,
                        sentBy, sentOn);
    
                // create remote info
                artifactModel.createRemoteInfo(container.getId(), sentBy, container.getCreatedOn());
    
                // index
                indexor.create(container.getId(), container.getName());
            }
    
            // handle the artifact by specific type
            final ArtifactVersion artifactVersion;
            switch(artifactType) {
            case DOCUMENT:
                artifactVersion = handleDocumentSent(sentBy, sentOn, artifactUniqueId,
                        artifactVersionId, artifactName, artifactChecksum, artifactBytes);
                break;
            case CONTAINER:
            default:
                throw Assert.createUnreachable("[UNSUPPORTED ARTIFACT TYPE]");
            }

            // add to the version
            if(!doesVersionExist) {
                containerIO.addVersion(version.getArtifactId(), version
                        .getVersionId(), artifactVersion.getArtifactId(),
                        artifactVersion.getVersionId(), artifactVersion
                                .getArtifactType());
            }
        }
        catch(final Throwable t) {
            throw translateError(getApiId("[HANDLE ARTIFACT SENT]"), t);
        }
    }

    /**
     * Handle the container published event.
     * 
     * @param uniqueId
     *            The container unique id.
     * @param versionId
     *            The container version id.
     * @param name
     *            The container name.
     * @param artifactCount
     *            The container artifact count.
     * @param publishedBy
     *            The publish date.
     * @param publishedTo
     *            The publish to list.
     * @param publishedOn
     *            The published on date.
     */
    void handlePublished(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final JabberId publishedBy, final List<JabberId> publishedTo,
            final Calendar publishedOn) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("name", name);
        logVariable("artifactCount", artifactCount);
        logVariable("publishedBy", publishedBy);
        logVariable("publishedTo", publishedTo);
        logVariable("publishedOn", publishedOn);
        final InternalArtifactModel artifactModel = getInternalArtifactModel();
        final Long containerId = artifactModel.readId(uniqueId);
        final List<JabberId> remoteTeam =
            getInternalSessionModel().readArtifactTeam(uniqueId);
        for (final JabberId remoteTeamMemberId : remoteTeam) {
            getInternalArtifactModel().addTeamMember(containerId, remoteTeamMemberId);
        }
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
     *            The container id.
     * @param contacts
     *            A list of contacts to publish to.
     * @param teamMembers
     *            A list of team members to publish to.
     */
    void publish(final Long containerId, final List<Contact> contacts,
            final List<TeamMember> teamMembers) {
        logApiId();
        logVariable("containerId", containerId);
        logVariable("contacts", contacts);
        logVariable("teamMembers", teamMembers);
        publish(containerId, null, contacts, teamMembers);
    }

    /**
     * Publish the container. Publishing involves determining if the working
     * version of a document differes from the latest version and if so creating
     * a new version; then sending the latest version to all team members.
     * 
     * @param containerId
     *            The container id.
     * @param contacts
     *            A list of contacts to publish to.
     * @param teamMembers
     *            A list of team members to publish to.
     */
    void publish(final Long containerId, final String comment,
            final List<Contact> contacts, final List<TeamMember> teamMembers) {
        logApiId();
        logVariable("containerId", containerId);
        logVariable("comment", comment);
        logVariable("contacts", contacts);
        logVariable("teamMembers", teamMembers);
        assertOnline("USER NOT ONLINE");
        assertDoesExistLocalDraft("LOCAL DRAFT DOES NOT EXIST", containerId);
        assertDoesNotContain("CANNOT PUBLISH TO SELF", teamMembers, localUser());
        try {
            final Container container = read(containerId);
            final ContainerDraft draft = readDraft(containerId);
            final Calendar currentDateTime = currentDateTime();
            // if the artfiact doesn't exist on the server; create it there
            if (!isDistributed(container.getUniqueId())) {
                createDistributed(container);
            }
            // ensure the user is the key holder
            assertIsKeyHolder("USER NOT KEY HOLDER", containerId);

            // create a version
            final ContainerVersion version = createVersion(container.getId());

            final InternalDocumentModel documentModel = getInternalDocumentModel();
            final List<Document> draftDocuments = draft.getDocuments();
            DocumentVersion draftDocumentLatestVersion;
            for(final Document draftDocument : draftDocuments) {
                if(ContainerDraft.ArtifactState.REMOVED != draft
                        .getState(draftDocument)) {
                    if(documentModel.isDraftModified(draftDocument.getId())) {
                        draftDocumentLatestVersion =
                            documentModel.createVersion(draftDocument.getId());
                    }
                    else {
                        draftDocumentLatestVersion =
                            documentModel.readLatestVersion(draftDocument.getId());
                    }
                    containerIO.addVersion(
                            version.getArtifactId(), version.getVersionId(),
                            draftDocumentLatestVersion.getArtifactId(),
                            draftDocumentLatestVersion.getVersionId(),
                            draftDocumentLatestVersion.getArtifactType());
                }
            }

            // delete draft
            for(final Artifact artifact : draft.getArtifacts()) {
                containerIO.deleteDraftArtifactRel(
                        container.getId(), artifact.getId());
            }
            containerIO.deleteDraft(container.getId());

            // update local team
            final InternalArtifactModel artifactModel = getInternalArtifactModel();
            for (final Contact contact : contacts)
                artifactModel.addTeamMember(container.getId(), contact.getId());

            // call remote add team member - note that this is done before the
            // actual publish so that the new team members don't get team member
            // added notifications - their local team data is built within
            // handle publish
            final InternalSessionModel sessionModel = getInternalSessionModel();
            final List<JabberId> remoteTeam = sessionModel.readArtifactTeam(container.getUniqueId());
            final List<TeamMember> localTeam = getInternalArtifactModel().readTeam2(container.getId());
            Boolean didFindTeamMember;
            for (final TeamMember localTeamMember : localTeam) {
                didFindTeamMember = Boolean.FALSE;
                for (final JabberId remoteTeamMemberId : remoteTeam) {
                    if (remoteTeamMemberId.equals(localTeamMember.getId())) {
                        didFindTeamMember = Boolean.TRUE;
                        break;
                    }
                }
                if (!didFindTeamMember) {
                    getInternalSessionModel().addTeamMember(
                            container.getUniqueId(), localTeamMember.getId());
                }
            }

            // build the publish to list then publish
            final List<JabberId> publishTo = new ArrayList<JabberId>();
            for (final Contact contact : contacts)
                publishTo.add(contact.getId());
            for (final TeamMember teamMember : teamMembers)
                publishTo.add(teamMember.getId());
            publish(version, publishTo, localUserId(), currentDateTime);

            // fire event
            final Container postPublish = read(container.getId());
            final ContainerVersion postPublishVersion = readVersion(
                    version.getArtifactId(), version.getVersionId());
            notifyContainerPublished(postPublish, draft, postPublishVersion,
                    localEventGenerator);
        }
        catch(final Throwable t) { throw translateError(getApiId("[PUBLISH]"), t); }
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
        final List<Container> containers = containerIO.read(localUser());
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
        return containerIO.read(containerId, localUser());
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
        return versions;
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
        logApiId();
        logVariable("containerId", containerId);
        logVariable("documentId", documentId);
        assertDraftExists("DRAFT DOES NOT EXIST", containerId);
        assertDraftArtifactStateTransition("INVALID DRAFT DOCUMENT STATE",
                containerId, documentId, ContainerDraft.ArtifactState.REMOVED);
        final ContainerDraft draft = readDraft(containerId);
        final Document document = draft.getDocument(documentId);
        containerIO.deleteDraftArtifactRel(containerId, document.getId());
        switch (draft.getState(document.getId())) {
        case ADDED:     // delete the document
            getInternalDocumentModel().delete(document.getId());
            break;
        case NONE:
            containerIO.createDraftArtifactRel(
                    containerId, document.getId(), ContainerDraft.ArtifactState.REMOVED);
            break;
        case REMOVED:   // fall through
        default:
            Assert.assertUnreachable("UNKNOWN ARTIFACT STATE");
        }

        final Container postAdditionContainer = read(containerId);        
        final ContainerDraft postAdditionDraft = readDraft(containerId);
        notifyDocumentRemoved(postAdditionContainer, postAdditionDraft, document, localEventGenerator);
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
     * Revert a document to it's pre-draft state.
     * 
     * @param documentId
     *            A document id.
     */
    void revertDocument(final Long containerId, final Long documentId) {
        logApiId();
        logVariable("containerId", containerId);
        logVariable("documentId", documentId);
        assertDraftExists("DRAFT DOES NOT EXIST", containerId);
        assertDraftArtifactStateTransition("INVALID DRAFT DOCUMENT STATE",
                containerId, documentId, ContainerDraft.ArtifactState.NONE);
        final ContainerDraft draft = readDraft(containerId);
        final Document document = draft.getDocument(documentId);
        containerIO.deleteDraftArtifactRel(containerId, document.getId());
        containerIO.createDraftArtifactRel(containerId, document.getId(),
                ContainerDraft.ArtifactState.NONE);
        getInternalDocumentModel().revertDraft(documentId);
    }

    /**
     * Assert that a local draft exists.
     * 
     * @param assertion
     *            An assertion.
     * @param containerId
     *            A container id.
     */
    private void assertDoesExistLocalDraft(final Object assertion,
            final Long containerId) {
        Assert.assertTrue(assertion, doesExistLocalDraft(containerId));
    }

    /**
     * Assert the state transition for a draft artifact is valid.
     * 
     * @param assertion
     *            An assertion.
     * @param containerId
     *            A container id.
     * @param artifactId
     *            An artifact id.
     * @param targetState
     *            The target state.
     */
    private void assertDraftArtifactStateTransition(final Object assertion,
            final Long containerId, final Long artifactId,
            final ContainerDraft.ArtifactState targetState) {
        final ContainerDraft draft = readDraft(containerId);
        final Artifact artifact = draft.getArtifact(artifactId);
        switch (draft.getState(artifact)) {
        case ADDED:
            switch (targetState) {
            case ADDED:
                Assert.assertUnreachable(assertion);
                break;
            case REMOVED:   // fall through
            case NONE:      // valid state
                break;
            default:
                Assert.assertUnreachable("UNKNOWN STATE");
            }
            break;
        case REMOVED:
            switch (targetState) {
            case ADDED:     // valid state
                break;
            case REMOVED:
                Assert.assertUnreachable(assertion);
                break;
            case NONE:      // valid state
                break;
            default:
                Assert.assertUnreachable("UNKNOWN STATE");
            }
            break;
        case NONE:
            switch (targetState) {
            case ADDED:
                Assert.assertUnreachable(assertion);
                break;
            case REMOVED:   // valid state
                break;
            case NONE:
                Assert.assertUnreachable(assertion);
                break;
            default:
                Assert.assertUnreachable("UNKNOWN STATE");
            }
            break;
        default:
            Assert.assertUnreachable("UNKNOWN STATE");
        }
    }

    /**
     * Assert a draft exists for the container.
     * 
     * @param assertion
     *            An assertion.
     * @param containerId
     *            A container id.
     * @see #assertContainerDraftExists(Object, Long)
     */
    private void assertDraftExists(final Object assertion,
            final Long containerId) {
        assertContainerDraftExists(assertion, containerId);
    }

    /**
     * Create the container in the distributed network.
     * 
     * @param container
     *            The container.
     */
    private void createDistributed(final Container container) {
        getInternalSessionModel().createArtifact(container.getUniqueId());
    }

    /**
     * Create the first draft for a cotnainer.
     * 
     * @param container
     *            A container.
     * @return The first draft.
     */
    private ContainerDraft createFirstDraft(final Long containerId,
            final TeamMember teamMember) {
        final ContainerDraft draft = new ContainerDraft();
        draft.setContainerId(containerId);
        draft.setOwner(teamMember);
        containerIO.createDraft(draft);

        // fire draft event
        final Container postCreation = read(containerId);
        final ContainerDraft postCreationDraft = readDraft(containerId);
        notifyDraftCreated(postCreation, postCreationDraft, localEventGenerator);
        return postCreationDraft;
    }

    /**
     * Create the team for a container. The team will consist of the local user
     * only.
     * 
     * @param containerId
     * @return
     */
    private TeamMember createTeam(final Long containerId) {
        final List<TeamMember> team =
            getInternalArtifactModel().createTeam(containerId);
        return team.get(0);
    }

    /**
     * Create a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A container version id.
     * @param createdBy
     *            By whom the container was created.
     * @param createdOn
     *            When the container was created.
     * @return The version.
     */
    private ContainerVersion createVersion(final Long containerId,
            final Long versionId, final JabberId createdBy,
            final Calendar createdOn) {
        final Container container = read(containerId);
        
        final ContainerVersion version = new ContainerVersion();
        version.setArtifactId(container.getId());
        version.setArtifactType(container.getType());
        version.setArtifactUniqueId(container.getUniqueId());
        version.setCreatedBy(createdBy.getUsername());
        version.setCreatedOn(createdOn);
        version.setName(container.getName());
        version.setUpdatedBy(container.getCreatedBy());
        version.setUpdatedOn(container.getCreatedOn());
        version.setVersionId(versionId);
        containerIO.createVersion(version);

        return containerIO.readVersion(version.getArtifactId(), version.getVersionId());
        
    }
    
    /**
     * Delete the local info for this container.
     * 
     * @param containerId
     *            The container id.
     */
    private void deleteLocal(final Long containerId) throws ParityException {
        // delete the draft
        final ContainerDraft draft = readDraft(containerId);
        if(null != draft) {
            for(final Artifact artifact : draft.getArtifacts()) {
                containerIO.deleteDraftArtifactRel(containerId, artifact.getId());
            }
            containerIO.deleteDraft(containerId);
        }
        // delete the team
        final InternalArtifactModel artifactModel = getInternalArtifactModel();
        artifactModel.deleteTeam(containerId);
        // delete the remote info
        artifactModel.deleteRemoteInfo(containerId);
        // delete the audit events
        getInternalAuditModel().delete(containerId);
        // delete versions
        final InternalDocumentModel documentModel = getInternalDocumentModel();
        final List<ContainerVersion> versions = readVersions(containerId);
        List<Document> documents;
        for(final ContainerVersion version : versions) {
            // remove the version's artifact versions
            containerIO.removeVersions(containerId, version.getVersionId());

            documents = containerIO.readDocuments(version.getArtifactId(), version.getVersionId());
            for(final Document document : documents) {
                documentModel.delete(document.getId());
            }
            // delete the version
            containerIO.deleteVersion(containerId, version.getVersionId());
        }
        // delete the index
        indexor.delete(containerId);
        // delete the container
        containerIO.delete(containerId);
    }

    /**
     * Determine whether or not a local draft exists.
     * 
     * @param containerId
     *            A container id.
     * @return True if a draft exists; and the draft owner is the current user.
     */
    private Boolean doesExistLocalDraft(final Long containerId) {
        final ContainerDraft draft = readDraft(containerId);
        if(null != draft) {
            return draft.getOwner().getId().equals(localUserId());
        }
        else { return Boolean.FALSE; }
    }

    /**
     * Handle the receipt of a document.
     * 
     * @param sentBy
     *            By whom the document was sent.
     * @param sentOn
     *            When the document was sent.
     * @param container
     *            A container.
     * @param uniqueId
     *            A unique id.
     * @param name
     *            A name.
     * @param bytes
     *            A byte array.
     * @return A document version.
     * @throws IOException
     */
    private DocumentVersion handleDocumentPublished(final JabberId publishedBy,
            final Calendar publishedOn, final UUID uniqueId,
            final Long versionId, final String name, final String checksum,
            final byte[] bytes) throws IOException {
        final InternalDocumentModel documentModel = getInternalDocumentModel();
        final InputStream inputStream =
            new BufferedInputStream(new ByteArrayInputStream(bytes), IO.BUFFER_SIZE);
        try {
            final DocumentVersion version =
                    documentModel.handleDocumentPublished(publishedBy,
                            publishedOn, uniqueId, versionId, name, checksum,
                            inputStream);
            return version;
        }
        finally { inputStream.close(); }
    }

    /**
     * Handle the receipt of a document.
     * 
     * @param sentBy
     *            By whom the document was sent.
     * @param sentOn
     *            When the document was sent.
     * @param uniqueId
     *            A unique id.
     * @param name
     *            A name.
     * @param bytes
     *            A byte array.
     * @return A document version.
     * @throws IOException
     */
    private DocumentVersion handleDocumentSent(final JabberId sentBy,
            final Calendar sentOn, final UUID uniqueId, final Long versionId,
            final String name, final String checksum, final byte[] bytes)
            throws IOException {
        final InternalDocumentModel documentModel = getInternalDocumentModel();
        final InputStream inputStream =
            new BufferedInputStream(new ByteArrayInputStream(bytes), IO.BUFFER_SIZE);
        try {
            final DocumentVersion version = documentModel.handleDocumentSent(
                    sentBy, sentOn, uniqueId, versionId, checksum, name, inputStream);
            return version;
        }
        finally { inputStream.close(); }
    }

    /**
     * Determine if the container has been distributed.
     * 
     * @param container
     *            A container.
     * @return True if the container has been distributed; false otherwise.
     */
    private Boolean isDistributed(final UUID uniqueId) {
        final JabberId keyHolder =
            getInternalSessionModel().readKeyHolder(uniqueId);
        return keyHolder != null;
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
     * Fire a container published event.
     * 
     * @param container
     *            A container.
     * @param version
     *            A container version.
     * @param eventGenerator
     *            A container event generator.
     */
    private void notifyContainerPublished(final Container container,
            final ContainerDraft draft, final ContainerVersion version,
            final ContainerEventGenerator eventGenerator) {
        synchronized(LISTENERS) {
            for(final ContainerListener l : LISTENERS) {
                l.draftPublished(eventGenerator.generate(container, draft, version));
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
    @SuppressWarnings("unused")
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
     * Notify that a team member was added to the container's team.
     * 
     * @param container
     *            A container.
     * @param teamMember
     *            A team member.
     * @param eventGenerator
     *            An event generator.
     */
    @SuppressWarnings("unused")
    private void notifyTeamMemberRemoved(final Container container,
            final TeamMember teamMember,
            final ContainerEventGenerator eventGenerator) {
        synchronized(LISTENERS) {
            for(final ContainerListener l : LISTENERS) {
                l.teamMemberRemoved(eventGenerator.generate(container, teamMember));
            }
        }
    }

    /**
     * Publish a container.
     * 
     * @param container
     *            A container.
     * @param publishTo
     *            A list of ids to publish to.
     * @param publishedBy
     *            The publisher.
     * @param publishedOn
     *            The publish date.
     */
    private void publish(final ContainerVersion container,
            final List<JabberId> publishTo, final JabberId publishedBy,
            final Calendar publishedOn) {
        getInternalSessionModel().publish(
                container,
                readDocumentVersionStreams(container.getArtifactId(), container
                        .getVersionId()), publishTo, publishedBy, publishedOn);
    }

    /**
     * Read the document version streams for a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A container version id.
     * @return A list of document versions and their input streams.
     */
    private Map<DocumentVersion, InputStream> readDocumentVersionStreams(
            final Long containerId, final Long versionId) {
        final InternalDocumentModel documentModel = getInternalDocumentModel();
        final Map<DocumentVersion, InputStream> documentVersionStreams =
            new HashMap<DocumentVersion, InputStream>();
        final List<Document> documents = readDocuments(containerId, versionId);
        DocumentVersion documentVersion;
        for(final Document document : documents) {
            documentVersion = documentModel.readLatestVersion(document.getId());
            documentVersionStreams.put(documentVersion,
                    documentModel.openVersionStream(
                            documentVersion.getArtifactId(),
                            documentVersion.getVersionId()));
        }
        return documentVersionStreams;
    }
}
