/*
 * Created On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.ophelia.model.container;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import com.thinkparity.codebase.Pair;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta;
import com.thinkparity.codebase.model.container.ContainerVersionDelta;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerPublishedEvent;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.document.DocumentFileLock;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.events.ContainerListener;
import com.thinkparity.ophelia.model.events.ContainerEvent.Source;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler;
import com.thinkparity.ophelia.model.io.handler.ContainerIOHandler;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;
import com.thinkparity.ophelia.model.util.sort.user.UserComparatorFactory;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Container Model Implementation</br>
 * <b>Description:</b>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.85
 */
public final class ContainerModelImpl extends
        Model<ContainerListener> implements ContainerModel,
        InternalContainerModel {

    /** The artifact io layer. */
    private ArtifactIOHandler artifactIO;

    /** The container io layer. */
    private ContainerIOHandler containerIO;

    /** A default container comparator. */
    private final Comparator<Artifact> defaultComparator;

    /** The default document comparator. */
    private final Comparator<Artifact> defaultDocumentComparator;

    /** The default document filter. */
    private final Filter<? super Artifact> defaultDocumentFilter;

    /** A default document version comparator. */
    private final Comparator<ArtifactVersion> defaultDocumentVersionComparator;

    /** A default container filter. */
    private final Filter<? super Artifact> defaultFilter;

    /** A default artifact receipt comparator. */
    private final Comparator<ArtifactReceipt> defaultReceiptComparator;

    /** A default artifact receipt filter. */
    private final Filter<? super ArtifactReceipt> defaultReceiptFilter;

    /** The default container version comparator. */
    private final Comparator<ArtifactVersion> defaultVersionComparator;

    /** The default container version filter. */
    private final Filter<? super ArtifactVersion> defaultVersionFilter;

    /** A local event generator. */
    private final ContainerEventGenerator localEventGenerator;

    /** A remote event generator. */
    private final ContainerEventGenerator remoteEventGenerator;

    /**
     * Create ContainerModelImpl.
     *
     * @param workspace
     *      The thinkParity workspace.
     */
    public ContainerModelImpl() {
        super();
        this.defaultComparator = new ComparatorBuilder().createByName(Boolean.TRUE);
        this.defaultDocumentComparator = new ComparatorBuilder().createByName(Boolean.TRUE);
        this.defaultDocumentFilter = FilterManager.createDefault();
        this.defaultDocumentVersionComparator = new ComparatorBuilder().createVersionByName();
        this.defaultFilter = FilterManager.createDefault();
        this.defaultReceiptComparator = new ComparatorBuilder().createArtifactReceiptByReceivedOnAscending();
        this.defaultReceiptFilter = FilterManager.createDefault();
        this.defaultVersionComparator = new ComparatorBuilder().createVersionById(Boolean.FALSE);
        this.defaultVersionFilter = FilterManager.createDefault();
        this.localEventGenerator = new ContainerEventGenerator(Source.LOCAL);
        this.remoteEventGenerator = new ContainerEventGenerator(Source.REMOTE);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerModel#delete(java.lang.Long)
     * 
     */
    public void delete(final Long containerId) throws CannotLockException {
        try {
            final Container container = read(containerId);
            final List<Document> allDocuments = readAllDocuments(containerId);
            final Map<Document, DocumentFileLock> allDocumentsLocks = lockDocuments(allDocuments);
            final Map<DocumentVersion, DocumentFileLock> allDocumentVersionsLocks = lockDocumentVersions(allDocuments);
            deleteLocal(container.getId(), allDocuments, allDocumentsLocks, allDocumentVersionsLocks);
            // fire event
            notifyContainerDeleted(container, localEventGenerator);
        } catch (final CannotLockException clx) {
            throw clx;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Delete a draft.
     * 
     * @param containerId
     *            A container id.
     */
    public void deleteDraft(final Long containerId) throws CannotLockException {
        try {
            assertDoesExistDraft(containerId, "Draft does not exist.");
            final InternalDocumentModel documentModel = getDocumentModel();
            final Container container = read(containerId);
            final ContainerDraft draft = readDraft(containerId);
            final List<Document> draftDocuments = draft.getDocuments();
            final Map<Document, DocumentFileLock> locks = lockDocuments(draftDocuments);
            try {
                // delete local data
                for (final Document draftDocument : draftDocuments) {
                    documentModel.deleteDraft(locks.get(draftDocument), draftDocument.getId());
                    containerIO.deleteDraftArtifactRel(containerId, draftDocument.getId());
                }
                containerIO.deleteDraftDocuments(containerId);
                containerIO.deleteDraft(containerId);
                notifyDraftDeleted(container, draft, localEventGenerator);
            } finally {
                releaseLocks(locks.values());
            }
        } catch (final CannotLockException clx) {
            throw clx;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.InternalContainerModel#doesExistDraft(java.lang.Long)
     * 
     */
    public Boolean doesExistDraft(final Long containerId) {
        try {
            return containerIO.doesExistDraft(containerId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerModel#doesExistLocalDraft(java.lang.Long)
     *
     */
    public Boolean doesExistLocalDraft(Long containerId) {
        try {
            final User localUser = localUser();
            return containerIO.doesExistLocalDraft(containerId,
                    localUser.getLocalId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.InternalContainerModel#handleDraftCreated(java.lang.Long,
     *      com.thinkparity.codebase.jabber.JabberId, java.util.Calendar)
     * 
     */
    public void handleDraftCreated(final Long containerId,
            final JabberId createdBy, final Calendar createdOn) {
        try {
            final ContainerDraft draft = new ContainerDraft();
            draft.setContainerId(containerId);
            draft.setLocal(Boolean.FALSE);
            final List<TeamMember> team = readTeam(containerId);
            draft.setOwner(team.get(indexOf(team, createdBy)));
            containerIO.createDraft(draft);
            // fire event
            notifyDraftCreated(read(containerId), readDraft(containerId),
                    remoteEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.InternalContainerModel#handleDraftDeleted(com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent)
     * 
     */
    public void handleDraftDeleted(final ArtifactDraftDeletedEvent event) {
        try {
            if (localUserId().equals(event.getDeletedBy())) {
                logger.logInfo("Receiving {0} event for local user:  {1}.",
                        event.getClass().getSimpleName(), event);
            } else {
                final Long containerId = getArtifactModel().readId(event.getUniqueId());
                final ContainerDraft draft = readDraft(containerId);
                for (final Artifact artifact : draft.getArtifacts()) {
                    containerIO.deleteDraftArtifactRel(containerId, artifact.getId());
                }
                containerIO.deleteDraft(containerId);
                // fire event
                notifyDraftDeleted(read(containerId), draft, remoteEventGenerator);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.InternalContainerModel#handlePublished(com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent)
     *
     */
    public void handlePublished(final ArtifactPublishedEvent event) {
        try {
            final InternalArtifactModel artifactModel = getArtifactModel();
            final Long containerId = artifactModel.readId(event.getUniqueId());
            // if a draft exists, delete it
            if (doesExistDraft(containerId)) {
                Assert.assertNotTrue(doesExistLocalDraft(containerId),
                        "Invalid state.  Local draft for {0} should not exist.",
                        containerId);
                deleteDraft(containerId);
            }

            /* HACK we know the latest flag was either applied or remoted by
             * the artifact model */
            final Container container = read(containerId);
            notifyContainerFlagged(container, remoteEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Handle the container published event. The local team definition is built
     * from the publishedTo list. The published to list is also saved.
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
    public void handlePublished(final ContainerPublishedEvent event) {
        try {
            final InternalArtifactModel artifactModel = getArtifactModel();
            final boolean didExist = artifactModel.doesVersionExist(
                    event.getVersion().getArtifactUniqueId(),
                    event.getVersion().getVersionId()).booleanValue();
            if (didExist) {
                logger.logWarning("Container version for event {0} already exists locally.",
                        event);
            } else {
                final Container container = handleResolution(event);
                final ContainerVersion version = handleVersionResolution(event);
                // handle the documents
                for (final Entry<DocumentVersion, String> entry :
                        event.getDocumentVersions().entrySet()) {
                    final ArtifactVersion artifactVersion =
                        getDocumentModel().handleDocumentPublished(container.getId(),
                                entry.getKey(), entry.getValue(),
                                event.getPublishedBy(), event.getPublishedOn());
                    final Long artifactId = getArtifactModel().readId(
                            entry.getKey().getArtifactUniqueId());
                    logger.logVariable("artifactId", artifactId);
                    if (!containerIO.doesExistVersion(container.getId(),
                            version.getVersionId(), artifactId,
                            entry.getKey().getVersionId()).booleanValue()) {
                        containerIO.addVersion(version.getArtifactId(),
                                version.getVersionId(),
                                artifactVersion.getArtifactId(),
                                artifactVersion.getVersionId(),
                                artifactVersion.getArtifactType());
                    }
                }
                // build published to list
                final InternalUserModel userModel = getUserModel();
                final List<JabberId> publishedToIds = new ArrayList<JabberId>(event.getPublishedTo().size());
                final List<User> publishedToUsers = new ArrayList<User>(event.getPublishedTo().size());
                for (final User publishedToUser : event.getPublishedTo()) {
                    publishedToIds.add(publishedToUser.getId());
                    publishedToUsers.add(userModel.readLazyCreate(publishedToUser.getId()));
                }
                // add only published by user to the team
                final TeamMember publishedBy;
                final List<TeamMember> localTeam = artifactModel.readTeam2(container.getId());
                if (!contains(localTeam, event.getPublishedBy())) {
                    publishedBy = artifactModel.addTeamMember(container.getId(), event.getPublishedBy());
                } else {
                    publishedBy = localTeam.get(indexOf(localTeam, event.getPublishedBy()));
                }
                // delete draft
                final ContainerDraft draft = readDraft(container.getId());
                if (null == draft) {
                    logger.logInfo("Draft did not previously exist for {0}.",
                            container.getName());
                } else {
                    deleteDraft(container.getId());
                }
                // create published to list
                containerIO.createPublishedTo(container.getId(),
                        version.getVersionId(), publishedToUsers,
                        event.getPublishedOn());
                // update the local published to list for the received by list
                ArtifactReceipt localReceipt;
                User receivedBy;
                for (final ArtifactReceipt receipt : event.getReceivedBy()) {
                    receivedBy = userModel.readLazyCreate(receipt.getUser().getId());
                    localReceipt = containerIO.readPublishedToReceipt(
                        container.getId(), version.getVersionId(),
                        event.getPublishedOn(), receivedBy);
                    if (null == localReceipt) {
                        containerIO.createPublishedTo(container.getId(),
                                version.getVersionId(), receivedBy,
                                receipt.getPublishedOn());
                    }
                    containerIO.updatePublishedTo(container.getId(),
                            version.getVersionId(), receipt.getPublishedOn(),
                            receivedBy.getId(), receipt.getReceivedOn());
                }
                // calculate differences
                final ContainerVersion previous = readPreviousVersion(container.getId(), version.getVersionId());
                final ContainerVersion next = readNextVersion(container.getId(), version.getVersionId());
                if (null == previous) {
                    logger.logInfo("First version of {0}.", container.getName());
                } else {
                    containerIO.deleteDelta(container.getId(), previous.getVersionId(),
                            version.getVersionId());
                    containerIO.createDelta(calculateDelta(container,
                            version, previous));
                }
                if (null == next) {
                    logger.logInfo("Latest version of {0}.", container.getName());
                } else {
                    containerIO.deleteDelta(container.getId(),
                            version.getVersionId(), next.getVersionId());
                    containerIO.createDelta(calculateDelta(container, next,
                            version));
                }
                // index
                getIndexModel().indexContainer(container.getId());
                // audit\fire event
                final Container postPublish = read(container.getId());
                final ContainerVersion postPublishVersion = readVersion(
                        container.getId(), version.getVersionId());
                notifyContainerPublished(postPublish, draft, previous,
                        postPublishVersion, next, publishedBy, remoteEventGenerator);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    public void handleReceived(final ArtifactReceivedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final Long containerId = artifactIO.readId(event.getUniqueId());
            final User receivedBy = getUserModel().readLazyCreate(event.getReceivedBy());
            final ArtifactReceipt receipt = containerIO.readPublishedToReceipt(
                    containerId, event.getVersionId(), event.getPublishedOn(),
                    receivedBy);
            if (null == receipt) {
                containerIO.createPublishedTo(containerId, event.getVersionId(),
                        receivedBy, event.getPublishedOn());
            }
            containerIO.updatePublishedTo(containerId, event.getVersionId(),
                    event.getPublishedOn(), event.getReceivedBy(),
                    event.getReceivedOn());
            notifyContainerReceived(read(containerId), remoteEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Determine if the container has been distributed. If a container is
     * distributed it will have 1 or more versions. If it has not been
     * distributed it will have no versions.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if the container has been distributed; false otherwise.
     */
    public Boolean isDistributed(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            return getArtifactModel().doesVersionExist(containerId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read the containers.
     * 
     * @return A list of containers.
     */
    public List<Container> read() {
        logger.logApiId();
        return read(defaultComparator);
    }

    /**
     * Read a container.
     * 
     * @param containerId
     *            A container id.
     * @return A container.
     */
    public Container read(final Long containerId) {
        try {
            return containerIO.read(containerId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @return A list of documents.
     */
    public List<Document> readDocuments(final Long containerId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        return readDocuments(containerId, versionId, defaultDocumentComparator,
                defaultDocumentFilter);
    }

    /**
     * Read the document versions for a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @return A list of documents.
     */
    public List<DocumentVersion> readDocumentVersions(final Long containerId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        return readDocumentVersions(containerId, versionId,
                defaultDocumentVersionComparator);
    }

    /**
     * Read the document versions for a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @param comparator
     *            A document comparator.
     * @return A list of documents.
     */
    public List<DocumentVersion> readDocumentVersions(final Long containerId,
            final Long versionId, final Comparator<ArtifactVersion> comparator) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        return readDocumentVersions(containerId, versionId, comparator,
                FilterManager.createDefault());
    }

    /**
     * Read the document versions for a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @param comparator
     *            A document comparator.
     * @param filter
     *            A document filter.
     * @return A list of documents.
     */
    public List<DocumentVersion> readDocumentVersions(final Long containerId,
            final Long versionId, final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        try {
            final List<DocumentVersion> documentVersions =
                containerIO.readDocumentVersions(containerId, versionId);
            FilterManager.filter(documentVersions, filter);
            ModelSorter.sortDocumentVersions(documentVersions, comparator);
            return Collections.unmodifiableList(documentVersions);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerModel#readDraft(java.lang.Long)
     * 
     */
    public ContainerDraft readDraft(final Long containerId) {
        try {
            final InternalDocumentModel documentModel = getDocumentModel();
            final ContainerDraft draft = containerIO.readDraft(containerId);
    
            // NOTE-Begin:this should be a parameterized comparator
            if (null != draft) {
                final List<Document> documents = new ArrayList<Document>();
                documents.addAll(draft.getDocuments());
                ModelSorter.sortDocuments(documents, defaultComparator);
                draft.setDocuments(documents);
            }
            // NOTE-End:this should be a parameterized comparator
    
            if (null != draft) {
                for (final Document document : draft.getDocuments()) {
                    if (ContainerDraft.ArtifactState.NONE == draft.getState(document)) {
                        if (documentModel.isDraftModified(document.getId())) {
                            draft.putState(document, ContainerDraft.ArtifactState.MODIFIED);
                        }
                    }
                }
            }
            return draft;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.InternalContainerModel#readForDocument(java.lang.Long)
     *
     */
    public List<Container> readForDocument(final Long documentId) {
        try {
            final List<ContainerVersion> versions = containerIO.readVersionsForDocument(documentId);
            final List<Container> containers = new ArrayList<Container>(versions.size());
            // add unique containers only
            for (final ContainerVersion version : versions)
                if (!ARTIFACT_UTIL.contains(containers, version))
                    containers.add(read(version.getArtifactId()));
            return containers;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.InternalContainerModel#readForTeamMember(java.lang.Long)
     *
     */
    public List<Container> readForTeamMember(final Long teamMemberId) {
        try {
            return containerIO.readForTeamMember(teamMemberId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read the latest container version.
     * 
     * @param containerId
     *            A container id.
     * @return A container version.
     */
    public ContainerVersion readLatestVersion(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        if (doesExistLatestVersion(containerId)) {
            return containerIO.readLatestVersion(containerId);
        } else {
            return null;
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerModel#readNextVersion(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public ContainerVersion readNextVersion(final Long containerId,
            final Long versionId) {
        try {
            final Long nextVersionId =
                artifactIO.readNextVersionId(containerId, versionId);
            if (null != nextVersionId
                    && doesExistVersion(containerId, nextVersionId)) {
                return containerIO.readVersion(containerId, nextVersionId);
            } else {
                return null;
            }     
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read the next container version sequentially.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>ContainerVersion</code>.
     */
    public ContainerVersion readPreviousVersion(final Long containerId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        try {
            final Long previousVersionId =
                artifactIO.readPreviousVersionId(containerId, versionId);
            if (null != previousVersionId
                    && doesExistVersion(containerId, previousVersionId)) {
                return containerIO.readVersion(containerId, previousVersionId);
            } else {
                return null;
            }     
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerModel#readPublishedTo(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public List<ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId) {
        try {
            return readPublishedTo(containerId, versionId,
                    defaultReceiptComparator, defaultReceiptFilter);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerModel#readPublishedTo(java.lang.Long,
     *      java.lang.Long, java.util.Comparator,
     *      com.thinkparity.codebase.filter.Filter)
     * 
     */
    public List<ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId, final Comparator<ArtifactReceipt> comparator,
            final Filter<? super ArtifactReceipt> filter) {
        try {
            final List<ArtifactReceipt> publishedTo =
                containerIO.readPublishedToReceipts(containerId, versionId);
            // sort by user
            ModelSorter.sortReceipts(publishedTo, new Comparator<ArtifactReceipt>() {
                public int compare(final ArtifactReceipt o1, final ArtifactReceipt o2) {
                    return o1.getUser().getLocalId().compareTo(o2.getUser().getLocalId());
                }
            });
            // remote duplicate users
            ArtifactReceipt previousReceipt = null, receipt = null;
            for (final Iterator<ArtifactReceipt> iPublishedTo =
                publishedTo.iterator(); iPublishedTo.hasNext();) {
                receipt = iPublishedTo.next();
                if (null != previousReceipt
                        && previousReceipt.getUser().equals(receipt.getUser())) {
                    iPublishedTo.remove();
                }
                previousReceipt = receipt;
            }
            FilterManager.filter(publishedTo, filter);
            ModelSorter.sortReceipts(publishedTo, comparator);
            return publishedTo;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerModel#readTeam(java.lang.Long)
     * 
     */
    public List<TeamMember> readTeam(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        return getArtifactModel().readTeam(containerId,
                UserComparatorFactory.createName(Boolean.TRUE),
                FilterManager.createDefault());
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
    public ContainerVersion readVersion(final Long containerId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        return containerIO.readVersion(containerId, versionId);
    }

    /**
     * Read the container versions.
     * 
     * @param containerId
     *            The container id.
     * @return A list of container versions.
     */
    public List<ContainerVersion> readVersions(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
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
    public List<ContainerVersion> readVersions(final Long containerId,
            final Comparator<ArtifactVersion> comparator) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("comparator", comparator);
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
    public List<ContainerVersion> readVersions(final Long containerId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        final List<ContainerVersion> versions = containerIO.readVersions(containerId);
        FilterManager.filter(versions, filter);
        ModelSorter.sortContainerVersions(versions, comparator);
        return versions;
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.artifactIO = IOFactory.getDefault(workspace).createArtifactHandler();
        this.containerIO = IOFactory.getDefault(workspace).createContainerHandler();
    }

    /**
     * Assert that a draft exists.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param assertMessage
     *            An assertion message <code>String</code>.
     * @param assertArguments
     *            Optional assertion message argument <code>Object[]</code>.
     */
    private void assertDoesExistDraft(final Long containerId,
            final String assertMessage, final Object... assertArguments) {
        Assert.assertTrue(doesExistDraft(containerId), assertMessage,
                assertArguments);
    }

    /**
     * Calculate a delta between a version and its previous version.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param compare
     *            A <code>ContainerVersion</code>.
     * @param compareTo
     *            Another <code>ContainerVersion</code>.
     * @return A <code>ContainerVersionDelta</code>.
     */
    private ContainerVersionDelta calculateDelta(final Container container,
            final ContainerVersion compare, final ContainerVersion compareTo) {
        final ContainerVersionDelta versionDelta = new ContainerVersionDelta();
        versionDelta.setCompareVersionId(compare.getVersionId());
        versionDelta.setCompareToVersionId(compareTo.getVersionId());
        versionDelta.setContainerId(container.getId());

        final Delta didNotHit, notContains;
        if (compare.getVersionId() > compareTo.getVersionId()) {
            didNotHit = Delta.REMOVED;
            notContains = Delta.ADDED;
        } else if (compare.getVersionId() < compareTo.getVersionId()) {
            didNotHit = Delta.ADDED;
            notContains = Delta.REMOVED;
        } else {
            throw Assert.createUnreachable("Unexpected equality.");
        }

        final List<DocumentVersion> compareDocuments =
            containerIO.readDocumentVersions(
                    versionDelta.getContainerId(),
                    versionDelta.getCompareVersionId());
        final List<DocumentVersion> compareToDocuments =
            containerIO.readDocumentVersions(
                    versionDelta.getContainerId(),
                    versionDelta.getCompareToVersionId());
        ContainerVersionArtifactVersionDelta artifactVersionDelta;
        boolean didHit;

        // walk through compare to version's documents
        for (final ArtifactVersion compareToDocument : compareToDocuments) {
            artifactVersionDelta = new ContainerVersionArtifactVersionDelta();
            artifactVersionDelta.setArtifactId(compareToDocument.getArtifactId());
            // walk through compare version's documents
            didHit = false;
            for (final ArtifactVersion versionDocument : compareDocuments) {
                // the artifact exists in this version; and in the compare version
                // therefore it must be the same or modified
                if (versionDocument.getArtifactId().equals(compareToDocument.getArtifactId())) {
                    artifactVersionDelta.setArtifactVersionId(versionDocument.getVersionId());
                    // the version numbers are the same; no change
                    if (versionDocument.getVersionId().equals(compareToDocument.getVersionId())) {
                        artifactVersionDelta.setDelta(Delta.NONE);
                    } else {
                        artifactVersionDelta.setDelta(Delta.MODIFIED);
                    }
                    didHit = true;
                    break;
                }
            }
            // the document is not in compare's version
            if (!didHit) {
                artifactVersionDelta.setArtifactVersionId(compareToDocument.getVersionId());
                artifactVersionDelta.setDelta(didNotHit);
            }
            versionDelta.addDelta(artifactVersionDelta);
        }
        // walk through next version's documents
        for (final ArtifactVersion compareDocument : compareDocuments) {
            if (!versionDelta.containsDelta(compareDocument)) {
                artifactVersionDelta = new ContainerVersionArtifactVersionDelta();
                artifactVersionDelta.setArtifactId(compareDocument.getArtifactId());
                artifactVersionDelta.setArtifactVersionId(compareDocument.getVersionId());
                artifactVersionDelta.setDelta(notContains);
                versionDelta.addDelta(artifactVersionDelta);
            }
        }
        return versionDelta;
    }

    /**
     * Create a new container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param comment
     *            A comment <code>String</code>.
     * @param createdBy
     *            The created by user id <code>JabberId</code>.
     * @param createdOn
     *            The created on <code>Calendar</code>.
     * @return The new <code>ContainerVersion</code>.
     */
    private ContainerVersion createVersion(final Long containerId,
            final Long versionId, final String comment,
            final JabberId createdBy, final Calendar createdOn) {
        final Container container = read(containerId);

        final ContainerVersion version = new ContainerVersion();
        version.setArtifactId(container.getId());
        version.setArtifactType(container.getType());
        version.setArtifactUniqueId(container.getUniqueId());
        version.setComment(comment);
        version.setCreatedBy(createdBy);
        version.setCreatedOn(createdOn);
        version.setName(container.getName());
        version.setUpdatedBy(version.getCreatedBy());
        version.setUpdatedOn(version.getCreatedOn());
        version.setVersionId(versionId);
        containerIO.createVersion(version);
        getIndexModel().indexContainerVersion(new Pair<Long, Long>(
                containerId, versionId));
        return containerIO.readVersion(
                version.getArtifactId(), version.getVersionId());
    }

    /**
     * Delete the local info for this container.
     * 
     * @param containerId
     *            The container id.
     */
    private void deleteLocal(
            final Long containerId,
            final List<Document> documents,
            final Map<Document, DocumentFileLock> documentLocks,
            final Map<DocumentVersion, DocumentFileLock> documentVersionLocks) {
        // delete the draft
        final ContainerDraft draft = readDraft(containerId);
        if (null != draft) {
            for(final Document document : draft.getDocuments()) {
                containerIO.deleteDraftArtifactRel(containerId, document.getId());
            }
            containerIO.deleteDraftDocuments(containerId);
            containerIO.deleteDraft(containerId);
        }
        // delete the team
        final InternalArtifactModel artifactModel = getArtifactModel();
        artifactModel.deleteTeam(containerId);
        // delete versions
        final InternalDocumentModel documentModel = getDocumentModel();
        final List<ContainerVersion> versions = readVersions(containerId);
        for (final ContainerVersion version : versions) {
            // remove the version's artifact versions
            containerIO.removeVersions(containerId, version.getVersionId());
            // delete the version's deltas
            containerIO.deleteDeltas(containerId, version.getVersionId());
            // delete the version
            containerIO.deleteVersion(containerId, version.getVersionId());
        }
        // delete documents
        for (final Document document : documents) {
            documentModel.delete(documentLocks.get(document),
                    documentVersionLocks, document.getId());
        }
        // delete the index
        getIndexModel().deleteContainer(containerId);
        // delete the container
        containerIO.delete(containerId);
    }

    /**
     * Handle the resolution of a container. If the container does not exist it
     * will be created.
     * 
     * @param event
     *            A <code>ContainerPublishedEvent</code>.
     * @return A <code>Container</code>.
     */
    private Container handleResolution(final ContainerPublishedEvent event) {
        // determine the existance of the container and the version.
        final InternalArtifactModel artifactModel = getArtifactModel();
        final UUID uniqueId = event.getVersion().getArtifactUniqueId();
        final JabberId publishedBy = event.getPublishedBy();
        final Calendar publishedOn = event.getPublishedOn();
        final String name = event.getVersion().getName();
        final boolean doesExist = artifactModel.doesExist(uniqueId).booleanValue();
        final Container container;
        if (doesExist) {
            container = read(artifactModel.readId(uniqueId));
        } else {
            // ensure the published by user exists locally
            getUserModel().readLazyCreate(publishedBy);

            container = new Container();
            container.setCreatedBy(publishedBy);
            container.setCreatedOn(publishedOn);
            container.setName(name);
            container.setState(ArtifactState.ACTIVE);
            container.setType(ArtifactType.CONTAINER);
            container.setUniqueId(uniqueId);
            container.setUpdatedBy(container.getCreatedBy());
            container.setUpdatedOn(container.getCreatedOn());
            // create
            containerIO.create(container);

            // index
            getIndexModel().indexContainer(container.getId());
        }
        return container;
    }

    /**
     * Handle the resolution of a container version. If the version does not
     * exist it will be created.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param publishedBy
     *            A container published by user id <code>JabberId</code>.
     * @param publishedOn
     *            A conatiner published on <code>Calendar</code>.
     * @return A <code>ContainerVersion</code>.
     */
    private ContainerVersion handleVersionResolution(
            final ContainerPublishedEvent event) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        final UUID uniqueId = event.getVersion().getArtifactUniqueId();
        final Long containerId = artifactModel.readId(uniqueId);
        final Long versionId = event.getVersion().getVersionId();
        final JabberId publishedBy = event.getPublishedBy();
        final String comment = event.getVersion().getComment();
        final ContainerVersion version;
        if (artifactModel.doesVersionExist(containerId, versionId).booleanValue()) {
            version = readVersion(containerId, versionId);
        } else {
            version = createVersion(containerId, versionId, comment,
                    publishedBy, event.getVersion().getCreatedOn());
        }
        return version;
    }

    /**
     * Lock a document.
     * 
     * @param document
     *            A <code>Document</code>.
     * @return A <code>DocumentLock</code>.
     */
    private DocumentFileLock lockDocument(final Document document)
            throws CannotLockException {
        return getDocumentModel().lock(document);
    }

    /**
     * Lock a list of documents.
     * 
     * @param documents
     *            A <code>List</code> of <code>Document</code>s.
     * @return A <code>Map</code> of <code>Document</code>s to their
     *         <code>DocumentLock</code>s.
     */
    private Map<Document, DocumentFileLock> lockDocuments(
            final List<Document> documents) throws CannotLockException {
        final Map<Document, DocumentFileLock> locks = new HashMap<Document, DocumentFileLock>();
        for (final Document document : documents) {
            try {
            	locks.put(document, lockDocument(document));
            } catch (final CannotLockException clx) {
            	releaseLocks(locks.values());
            	throw clx;
            }
        }
        return locks;
    }

    /**
     * Lock a list of documents' versions.
     * 
     * @param documents
     *            A <code>List</code> of <code>Document</code>s.
     * @return A <code>Map</code> of <code>Document</code>s to their
     *         <code>DocumentLock</code>s.
     */
    private Map<DocumentVersion, DocumentFileLock> lockDocumentVersions(
            final List<Document> documents) throws CannotLockException {
        final Map<DocumentVersion, DocumentFileLock> locks = new HashMap<DocumentVersion, DocumentFileLock>();
        final List<DocumentVersion> versions = new ArrayList<DocumentVersion>();
        for (final Document document : documents) {
            versions.clear();
            versions.addAll(getDocumentModel().readVersions(document.getId()));
            for (final DocumentVersion version : versions) {
                locks.put(version, getDocumentModel().lockVersion(version));
            }
        }
        return locks;
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
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.containerDeleted(eventGenerator.generate(container));
            }
        });
    }

    /**
     * Notify that a container has been flagged.
     * 
     * @param container
     *            A container.
     * @param eventGenerator
     *            A container event generator.
     */
    private void notifyContainerFlagged(final Container container,
            final ContainerEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.containerFlagged(eventGenerator.generate(container));
            }
        });
    }

    /**
     * Fire a container published event.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param eventGenerator
     *            A <code>ContainerEventGenerator</code>.
     */
    private void notifyContainerPublished(final Container container,
            final ContainerDraft draft, final ContainerVersion previousVersion,
            final ContainerVersion version, final ContainerVersion nextVersion,
            final TeamMember teamMember,
            final ContainerEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.containerPublished(eventGenerator.generate(container,
                        draft, previousVersion, version, nextVersion, teamMember));
            }
        });
    }

    /**
     * Notify that the container has been received.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param eventGenerator
     *            A <code>ContainerEventGenerator</code>.
     */
    private void notifyContainerReceived(final Container container,
            final ContainerEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.containerReceived(eventGenerator.generate(container));
            }
        });
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
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener
                        .draftCreated(eventGenerator.generate(container, draft));
            }
        });
    }

    /**
     * Notify that a container draft was deleted.
     * 
     * @param container
     *            The container.
     * @param draft
     *            The draft.
     * @param eventGenerator
     *            The container event generator.
     */
    private void notifyDraftDeleted(final Container container,
            final ContainerDraft draft,
            final ContainerEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.draftDeleted(eventGenerator.generate(container, draft));
            }
        });
    }

    /**
     * Read the containers.
     * 
     * @param comparator
     *            A sort ordering to user.
     * @return A list of containers.
     */
    private List<Container> read(final Comparator<Artifact> comparator) {
        logger.logApiId();
        logger.logVariable("comparator", comparator);
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
    private List<Container> read(final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        try {
            final List<Container> containers = containerIO.read();
            FilterManager.filter(containers, filter);
            ModelSorter.sortContainers(containers, comparator);
            return containers;        
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read a flat list of all documents for a container. This will
     * look in the draft as well as all versions.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A <code>List</code> of <code>Document</code>s.
     */
    private List<Document> readAllDocuments(final Long containerId) {
        final List<Document> allDocuments = new ArrayList<Document>();
        final ContainerDraft draft = readDraft(containerId);
        if (null != draft) {
            for(final Document document : draft.getDocuments()) {
                if (!allDocuments.contains(document))
                    allDocuments.add(document);
            }
        }
        final List<ContainerVersion> versions = readVersions(containerId);
        for (final ContainerVersion version : versions)
            for (final Document document :
                readDocuments(version.getArtifactId(), version.getVersionId()))
                if (!allDocuments.contains(document))
                    allDocuments.add(document);
        return allDocuments;
    }

    /**
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param comparator
     *            A document comparator <code>Comparator&lt;Artifact&gt;</code>.
     * @param filter
     *            A document filter <code>Filter&lt;? super Artifact&gt;</code>.
     * @return A <code>List&gt;Document&gt;</code>.
     * 
     * @see FilterManager#filter(List, Filter)
     * @see ModelSorter#sortDocuments(List, Comparator)
     */
    private List<Document> readDocuments(final Long containerId, final Long versionId,
            final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        final List<Document> documents =
                containerIO.readDocuments(containerId, versionId);
        FilterManager.filter(documents, filter);
        ModelSorter.sortDocuments(documents, comparator);
        return documents;
    }

    /**
     * Release the exclusive lock.
     * 
     * @param lock
     *            A <code>DocumentLock</code>.
     */
    private void releaseLock(final DocumentFileLock lock) {
        try {
            lock.release();
        } catch (final IOException iox) {
            logger.logError("Could not release lock.", iox);
        }
    }

    /**
     * Release the exclusive lock.
     * 
     * @param lock
     *            A <code>DocumentLock</code>.
     */
    private void releaseLocks(final Iterable<DocumentFileLock> locks) {
        for (final DocumentFileLock lock : locks) {
            releaseLock(lock);
        }
    }
}
