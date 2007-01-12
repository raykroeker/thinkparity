/*
 * Created On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.ophelia.model.container;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.ZipUtil;
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
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta;
import com.thinkparity.codebase.model.container.ContainerVersionDelta;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerPublishedEvent;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.UploadMonitor;
import com.thinkparity.ophelia.model.archive.InternalArchiveModel;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.audit.HistoryItem;
import com.thinkparity.ophelia.model.audit.event.AuditEvent;
import com.thinkparity.ophelia.model.backup.InternalBackupModel;
import com.thinkparity.ophelia.model.container.export.PDFWriter;
import com.thinkparity.ophelia.model.container.monitor.PublishMonitor;
import com.thinkparity.ophelia.model.container.monitor.PublishStage;
import com.thinkparity.ophelia.model.document.DocumentNameGenerator;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.events.ContainerDraftListener;
import com.thinkparity.ophelia.model.events.ContainerListener;
import com.thinkparity.ophelia.model.events.ContainerEvent.Source;
import com.thinkparity.ophelia.model.index.InternalIndexModel;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler;
import com.thinkparity.ophelia.model.io.handler.ContainerIOHandler;
import com.thinkparity.ophelia.model.io.handler.DocumentIOHandler;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.util.Printer;
import com.thinkparity.ophelia.model.util.UUIDGenerator;
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
        AbstractModelImpl<ContainerListener> implements ContainerModel,
        InternalContainerModel {

    private static final PublishMonitor PUBLISH_MONITOR;

    private static final int STEP_SIZE = 1024;

    static {
        PUBLISH_MONITOR = new PublishMonitor() {
            public void determine(final Integer stages) {}
            public void processBegin() {}
            public void processEnd() {}
            public void stageBegin(final PublishStage stage, final Object data) {}
            public void stageEnd(final PublishStage stage) {}
        };
    }

    /** The artifact io layer. */
    private ArtifactIOHandler artifactIO;

    /** A container audit generator. */
    private ContainerAuditor auditor;

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

    /** A default history item comparator. */
    private final Comparator<? super HistoryItem> defaultHistoryComparator;

    /** A default history item filter. */
    private final Filter<? super HistoryItem> defaultHistoryFilter;

    /** A default user comparator. */
    private final Comparator<User> defaultUserComparator;

    /** A default user filter. */
    private final Filter<? super User> defaultUserFilter;

    /** The default container version comparator. */
    private final Comparator<ArtifactVersion> defaultVersionComparator;

    /** The default container version filter. */
    private final Filter<? super ArtifactVersion> defaultVersionFilter;

    /** The document io layer. */
    private DocumentIOHandler documentIO;

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
        this.defaultHistoryComparator = new ComparatorBuilder().createDateDescending();
        this.defaultHistoryFilter = FilterManager.createDefault();
        this.defaultUserComparator = UserComparatorFactory.createOrganizationAndName(Boolean.TRUE);
        this.defaultUserFilter = FilterManager.createDefault();
        this.defaultVersionComparator = new ComparatorBuilder().createVersionById(Boolean.FALSE);
        this.defaultVersionFilter = FilterManager.createDefault();
        this.localEventGenerator = new ContainerEventGenerator(Source.LOCAL);
        this.remoteEventGenerator = new ContainerEventGenerator(Source.REMOTE);
    }

    /**
     * Apply a bookmark to a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void addBookmark(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            getArtifactModel().applyFlagBookmark(containerId);
            notifyContainerUpdated(read(containerId), localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Add a document to a container.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     */
    public void addDocument(final Long containerId, final Long documentId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("documentId", documentId);
        try {
            assertDraftExists("DRAFT DOES NOT EXIST", containerId);
            containerIO.createDraftArtifactRel(containerId, documentId, ContainerDraft.ArtifactState.ADDED);
            getIndexModel().indexDocument(containerId, documentId);
            final Container postAdditionContainer = read(containerId);        
            final ContainerDraft postAdditionDraft = readDraft(containerId);
            final Document postAdditionDocument = getDocumentModel().read(documentId);
            notifyDocumentAdded(postAdditionContainer, postAdditionDraft,
                    postAdditionDocument, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Add a container listener to the model.
     * 
     * @param listener
     *            A <code>ContainerListener</code>.
     */
    @Override
    public void addListener(final ContainerListener listener) {
        super.addListener(listener);
    }

    /**
     * Archive a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void archive(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        assertIsDistributed("Container has not been distributed.", containerId);
        try {
            final Container container = read(containerId);
            getArchiveModel().archive(container.getId());
            deleteLocal(container.getId());
            notifyContainerArchived(container, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Create a container.
     * 
     * @param name
     *            The container name.
     * @return The new container.
     */
    public Container create(final String name) {
        logger.logApiId();
        logger.logVariable("name", name);
        try {
            final Container container = new Container();
            container.setCreatedBy(localUserId());
            container.setCreatedOn(currentDateTime());
            container.setName(name);
            container.setState(ArtifactState.ACTIVE);
            container.setType(ArtifactType.CONTAINER);
            container.setUniqueId(UUIDGenerator.nextUUID());
            container.setUpdatedBy(container.getCreatedBy());
            container.setUpdatedOn(container.getCreatedOn());
            // local create
            containerIO.create(container);
    
            // local key
            final InternalArtifactModel artifactModel = getArtifactModel();
            artifactModel.applyFlagKey(container.getId());
            artifactModel.applyFlagLatest(container.getId());
    
            // create remote info
            artifactModel.createRemoteInfo(container.getId(),
                    container.getCreatedBy(), container.getCreatedOn());
    
            // index
            getIndexModel().indexContainer(container.getId());
    
            // create team
            final TeamMember teamMember = getArtifactModel().createTeam(
                    container.getId()).get(0);
    
            // create first draft
            createFirstDraft(container.getId(), teamMember);
    
            // audit\fire event
            final Container postCreation = read(container.getId());
            auditContainerCreated(postCreation);
            notifyContainerCreated(postCreation, localEventGenerator);
            return postCreation;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Create a <code>ContainerDraft</code>. If the container does not yet
     * exist as an artifact on the server; it will be created. All of the
     * artifact references that exist in the latest version of the container
     * will be copied into the draft. A remote call to create draft will also be
     * made.
     * 
     * @param containerId
     *            The container id.
     * @return A new <code>ContainerDraft</code>.
     * @see #createDistributed(Container)
     * @see #createFirstDraft(Long, TeamMember)
     * @see #isDistributed(Long)
     * @see #localTeamMember(Long)
     * @see #notifyDraftCreated(Container, ContainerDraft,
     *      ContainerEventGenerator)
     * @see #read(Long)
     * @see #readDocuments(Long, Long)
     * @see #readLatestVersion(Long)
     * @see InternalSessionModel#createDraft(UUID)
     */
    public ContainerDraft createDraft(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            assertContainerDraftDoesNotExist(containerId);
            if (isFirstDraft(containerId)) {
                createFirstDraft(containerId, localTeamMember(containerId));
            } else {
                final Calendar createdOn = getSessionModel().readDateTime();
                final InternalArtifactModel artifactModel = getArtifactModel();

                assertOnline("The user is not online.");
                final Container container = read(containerId);
                if (!isDistributed(container.getId())) {
                    createDistributed(container, createdOn);
                }
                final ContainerVersion latestVersion =
                        readLatestVersion(container.getId());
                final List<Document> documents = readDocuments(
                        latestVersion.getArtifactId(), latestVersion.getVersionId());
                // create
                final ContainerDraft draft = new ContainerDraft();
                draft.setOwner(localTeamMember(containerId));
                draft.setContainerId(containerId);
                final InternalDocumentModel documentModel = getDocumentModel();
                for (final Document document : documents) {
                    draft.addDocument(document);
                    draft.putState(document, ContainerDraft.ArtifactState.NONE);
                    documentModel.createDraft(document.getId());
                    
                }
                containerIO.createDraft(draft);
                artifactModel.applyFlagKey(container.getId());
                // remote create
                final List<JabberId> team = artifactModel.readTeamIds(containerId);
                team.remove(localUserId());
                getSessionModel().createDraft(team, container.getUniqueId());
            }
            // fire event
            final Container postCreation = read(containerId);
            final ContainerDraft postCreationDraft = readDraft(containerId);
            notifyDraftCreated(postCreation, postCreationDraft, localEventGenerator);
            return postCreationDraft;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete a container.
     * 
     * @param containerId
     *            A container id.
     */
    public void delete(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            final Container container = read(containerId);
            if (isDistributed(container.getId())) {
                /* the archive user is not part of the team */
                if (isLocalTeamMember(container.getId())) {
                    final TeamMember localTeamMember = localTeamMember(container.getId());
                    final List<JabberId> team = getArtifactModel().readTeamIds(container.getId());
                    team.remove(localUserId());
                    getSessionModel().removeTeamMember(
                            container.getUniqueId(), team, localTeamMember.getId());
                }
                deleteLocal(container.getId());
            } else {
                deleteLocal(container.getId());
            }
            // fire event
            notifyContainerDeleted(container, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete a draft.
     * 
     * @param containerId
     *            A container id.
     */
    public void deleteDraft(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            assertDoesExistDraft(containerId, "Draft does not exist.");
            final Container container = read(containerId);
            if (doesExistLocalDraft(containerId)) {
                if (!isFirstDraft(container.getId())) {
                    assertOnline("User is not online.");
                    assertIsDistributed("Draft has not been distributed.", containerId);
                    getSessionModel().deleteDraft(container.getUniqueId());
                }
            }
            // delete local data
            final InternalDocumentModel documentModel = getDocumentModel();
            final ContainerDraft draft = readDraft(containerId);
            for (final Artifact artifact : draft.getArtifacts()) {
                documentModel.deleteDraft(artifact.getId());
                containerIO.deleteDraftArtifactRel(containerId, artifact.getId());
            }
            containerIO.deleteDraft(containerId);
            notifyDraftDeleted(container, draft, localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Determine whether or not a draft exists.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if a draft exists.
     */
    public Boolean doesExistDraft(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            return null != readDraft(containerId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Export a container.
     * 
     * @param exportDirectory
     *            The directory <code>File</code> to export to.
     * @param containerId
     *            The container id <code>Long</code>.
     */
    public File export(final File exportDirectory, final Long containerId) {
        logger.logApiId();
        logger.logVariable("exportDirectory", exportDirectory);
        logger.logVariable("containerId", containerId);
        try {
            Assert.assertTrue(exportDirectory.isDirectory(),
                    "Export directory {0} is not a directory.", exportDirectory);
            final Container container = read(containerId);
            final List<ContainerVersion> versions = readVersions(containerId);
            return export(exportDirectory,
                    getNameGenerator().exportFileName(container), container, versions);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Export a container version.
     * 
     * @param exportDirectory
     *            A file output stream representing a zip file.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     */
    public File exportVersion(final File exportDirectory, final Long containerId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("exportDirectory", exportDirectory);
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        try {
            Assert.assertTrue(exportDirectory.isDirectory(),
                    "Export directory {0} is not a directory.", exportDirectory);
            final Container container = read(containerId);
            final List<ContainerVersion> versions = new ArrayList<ContainerVersion>(1);
            final ContainerVersion version = readVersion(containerId, versionId);
            versions.add(version);
            return export(exportDirectory,
                    getNameGenerator().exportFileName(version), container, versions);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Obtain a draft monitor. The monitor will be notified if and when a
     * document's state is changed.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param listener
     *            A <code>ContainerDraftListener</code>.
     * @return A <code>ContainerDraftMonitor</code>.
     */
    public ContainerDraftMonitor getDraftMonitor(final Long containerId,
            final ContainerDraftListener listener) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        assertDraftExists("Cannot monitor a null draft.", containerId);
        try {
            return new ContainerDraftMonitor(modelFactory,
                    readDraft(containerId), localEventGenerator, listener);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Handle the container artifact published remote event. If the container
     * does not yet exist it will be created; same goes for the version. The
     * artifact will then be passed off to the appropriate model then attached
     * to the version.
     * 
     * @param uniqueId
     *            The container <code>UUID</code>.
     * @param versionId
     *            The container version id <code>Long</code>.
     * @param name
     *            The container name <code>String</code>.
     * @param artifactUniqueId
     *            The artifact <code>UUID</code>
     * @param artifactVersionId
     *            The artifact version id <code>Long</code>.
     * @param artifactName
     *            The artifact name <code>String</code>.
     * @param artifactType
     *            The artifact's <code>ArtifactType</code>.
     * @param artifactChecksum
     *            The artifact checksum <code>String</code>.
     * @param artifactBytes
     *            The artifact bytes <code>byte[]</code>.
     * @param publishedBy
     *            The publisher <code>JabberId</code>.
     * @param publishedOn
     *            The publish date <code>Calendar</code>.
     * 
     * @see #createVersion(Long, Long, JabberId, Calendar)
     * @see #handleDocumentPublished(UUID, Long, String, String, byte[],
     *      JabberId, Calendar)
     * @see #notifyContainerPublished(Container, ContainerDraft,
     *      ContainerVersion, ContainerEventGenerator)
     * @see #read(Long)
     * @see #readVersion(Long, Long)
     * @see ContainerIndexor#create(Long, String)
     * @see InternalArtifactModel#createRemoteInfo(Long, JabberId, Calendar)
     * @see InternalArtifactModel#doesExist(UUID)
     * @see InternalArtifactModel#doesVersionExist(Long, Long)
     * @see InternalArtifactModel#readId(UUID)
     */
    public void handleArtifactPublished(final ContainerArtifactPublishedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            // determine the existance of the container and the version.
            final InternalArtifactModel artifactModel = getArtifactModel();
            final boolean doesExist = artifactModel.doesExist(
                    event.getUniqueId()).booleanValue();
            final boolean doesVersionExist;
            final Container container;
            final ContainerVersion version;
            if (doesExist) {
                final Long containerId = artifactModel.readId(event.getUniqueId());
                container = read(containerId);
                doesVersionExist = artifactModel.doesVersionExist(
                        containerId, event.getVersionId()).booleanValue();

                if (doesVersionExist) {
                    version = readVersion(container.getId(), event.getVersionId());
                } else {
                    version = createVersion(container.getId(),
                            event.getVersionId(), event.getPublishedBy(),
                            event.getPublishedOn());
                }
            } else {
                doesVersionExist = false;

                // ensure the published by user exists locally
                getUserModel().readLazyCreate(event.getPublishedBy());

                container = new Container();
                container.setCreatedBy(event.getPublishedBy());
                container.setCreatedOn(event.getPublishedOn());
                container.setName(event.getName());
                container.setState(ArtifactState.ACTIVE);
                container.setType(ArtifactType.CONTAINER);
                container.setUniqueId(event.getUniqueId());
                container.setUpdatedBy(container.getCreatedBy());
                container.setUpdatedOn(container.getCreatedOn());
                // create
                containerIO.create(container);
                // create version
                version = createVersion(container.getId(), event.getVersionId(),
                        event.getPublishedBy(), event.getPublishedOn());
                // create remote info
                artifactModel.createRemoteInfo(container.getId(),
                        event.getPublishedBy(), container.getCreatedOn());
                // index
                getIndexModel().indexContainer(container.getId());
            }

            // handle the artifact by specific type
            final ArtifactVersion artifactVersion;
            switch (event.getArtifactType()) {
            case DOCUMENT:
                artifactVersion = handleDocumentPublished(event);
                break;
            default:
                throw Assert.createUnreachable("Cannot publish a container within a container.");
            }
            final Long artifactId = artifactModel.readId(event.getArtifactUniqueId());
            if (!containerIO.doesExistVersion(container.getId(),
                    event.getVersionId(), artifactId,
                    event.getArtifactVersionId()).booleanValue()) {
                containerIO.addVersion(version.getArtifactId(),
                        version.getVersionId(), artifactVersion.getArtifactId(),
                        artifactVersion.getVersionId(),
                        artifactVersion.getArtifactType());
            }
            
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Handle the remote draft created event. A
     * <code>ContainerDraft</code is created;
     * and the created by <code>JabberId</code> is set as the owner; and a 
     * notification is fired.
     * 
     * @param containerId
     *            The container id <code>Long</code>.
     * @param createdBy
     *            The creation user <code>JabberId</code>.
     * @param createdOn
     *            The creation date <code>Calendar</code>.
     *            
     * @see #notifyDraftCreated(Container, ContainerDraft, ContainerEventGenerator)
     * @see #readTeam(Long)
     */
    public void handleDraftCreated(final Long containerId,
            final JabberId createdBy, final Calendar createdOn) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("createdBy", createdBy);
        logger.logVariable("createdOn", createdOn);
        try {
            final ContainerDraft draft = new ContainerDraft();
            draft.setContainerId(containerId);
            final List<TeamMember> team = readTeam(containerId);
            logger.logVariable("team", team);
            draft.setOwner(team.get(indexOf(team, createdBy)));
            containerIO.createDraft(draft);
            // fire event
            notifyDraftCreated(read(containerId), readDraft(containerId),
                    remoteEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Handle the remote draft deleted event.
     * 
     * @param containerId
     *            A container id.
     * @param deletedBy
     *            Who deleted the draft.
     * @param deletedOn
     *            When the draft was deleted.
     */
    public void handleDraftDeleted(final ArtifactDraftDeletedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
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
            throw translateError(t);
        }
    }

    public void handlePublished(final ArtifactPublishedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final Long containerId = getArtifactModel().readId(event.getUniqueId());
            notifyContainerUpdated(read(containerId), remoteEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
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
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final InternalArtifactModel artifactModel = getArtifactModel();
            final Long containerId = artifactModel.readId(event.getUniqueId());
            // build published to list
            final InternalUserModel userModel = getUserModel();
            final List<User> publishedToUsers = new ArrayList<User>();
            for (final JabberId publishedToId : event.getPublishedTo()) {
                publishedToUsers.add(userModel.readLazyCreate(publishedToId));
            }
            // add only published by user to the team
            final TeamMember publishedBy;
            final List<TeamMember> localTeam = artifactModel.readTeam2(containerId);
            if (!contains(localTeam, event.getPublishedBy())) {
                publishedBy = artifactModel.addTeamMember(containerId, event.getPublishedBy());
            } else {
                publishedBy = localTeam.get(indexOf(localTeam, event.getPublishedBy()));
            }
            // delete draft
            final ContainerDraft draft = readDraft(containerId);
            if (null == draft) {
                logger.logWarning("Draft did not previously exist for {0}.", event.getName());
            } else {
                deleteDraft(containerId);
            }
            // create published to list
            containerIO.createPublishedTo(containerId, event.getVersionId(),
                    publishedToUsers, event.getPublishedOn());
            // calculate differences
            final ContainerVersion version = readVersion(containerId, event.getVersionId());
            final ContainerVersion previous = readPreviousVersion(containerId, event.getVersionId());
            final ContainerVersion next = readNextVersion(containerId, event.getVersionId());
            if (null == previous) {
                logger.logInfo("First version of {0}.", event.getName());
            } else {
                containerIO.deleteDelta(containerId, previous.getVersionId(),
                        version.getVersionId());
                containerIO.createDelta(calculateDelta(read(containerId),
                        version, previous));
            }
            if (null == next) {
                logger.logInfo("Latest version of {0}.", event.getName());
            } else {
                containerIO.deleteDelta(containerId, version.getVersionId(),
                        next.getVersionId());
                containerIO.createDelta(calculateDelta(read(containerId),
                        next, version));
            }
            // apply comment
            if (null != event.getComment() && 0 < event.getComment().trim().length())
                containerIO.updateComment(containerId, version.getVersionId(),
                        event.getComment());
            // index
            getIndexModel().indexContainer(containerId);
            // send confirmation
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar confirmedOn = sessionModel.readDateTime();
            sessionModel.confirmArtifactReceipt(localUserId(),
                    event.getUniqueId(), event.getVersionId(),
                    event.getPublishedBy(), event.getPublishedOn(),
                    event.getPublishedTo(), localUserId(), confirmedOn);
            // audit\fire event
            final Container postPublish = read(containerId);
            final ContainerVersion postPublishVersion = readVersion(containerId, event.getVersionId());
            auditContainerPublished(postPublish, draft,
                    postPublishVersion, event.getPublishedBy(),
                    event.getPublishedTo(), event.getPublishedOn());
            notifyContainerPublished(postPublish, publishedBy,
                    postPublishVersion, remoteEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public void handleReceived(final ArtifactReceivedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final Long containerId = artifactIO.readId(event.getUniqueId());
            final User receivedBy = getUserModel().readLazyCreate(event.getReceivedBy());
            final ArtifactReceipt receipt = containerIO.readPublishedTo(
                    containerId, event.getVersionId(), event.getPublishedOn(),
                    receivedBy);
            if (null == receipt) {
                containerIO.createPublishedTo(containerId, event.getVersionId(),
                        receivedBy, event.getPublishedOn());
            }
            containerIO.updatePublishedTo(containerId, event.getVersionId(),
                    event.getPublishedOn(), event.getReceivedBy(),
                    event.getReceivedOn());
            notifyContainerUpdated(read(containerId), remoteEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
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
            throw translateError(t);
        }
    }

    /**
     * Print a container draft.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param printer
     *            An <code>Printer</code>.
     */
    public void printDraft(final Long containerId, final Printer printer) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("printer", printer);
        try {
            final ContainerDraft draft = readDraft(containerId);
            final InternalDocumentModel documentModel = getDocumentModel();
            for (final Document document : draft.getDocuments()) {
                documentModel.printDraft(document.getId(), printer);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Print a container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param printer
     *            An <code>Printer</code>.
     */
    public void printVersion(final Long containerId, final Long versionId,
            final Printer printer) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("printer", printer);
        try {
            final InternalDocumentModel documentModel = getDocumentModel();
            final List<DocumentVersion> documentVersions =
                containerIO.readDocumentVersions(containerId, versionId);
            for (final DocumentVersion documentVersion : documentVersions) {
                documentModel.printVersion(documentVersion.getArtifactId(),
                        documentVersion.getVersionId(), printer);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
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
    public void publish(final Long containerId, final List<Contact> contacts,
            final List<TeamMember> teamMembers) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("contacts", contacts);
        logger.logVariable("teamMembers", teamMembers);
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
    public void publish(final Long containerId, final String comment,
            final List<Contact> contacts, final List<TeamMember> teamMembers) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("comment", comment);
        logger.logVariable("contacts", contacts);
        logger.logVariable("teamMembers", teamMembers);
        try {
            publish(PUBLISH_MONITOR, containerId, comment, contacts, teamMembers);
        } catch (final Throwable t) {
            throw translateError(t);
        }
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
    public void publish(final PublishMonitor monitor, final Long containerId,
            final String comment, final List<Contact> contacts,
            final List<TeamMember> teamMembers) {
        logger.logApiId();
        logger.logVariable("monitor", monitor);
        logger.logVariable("containerId", containerId);
        logger.logVariable("comment", comment);
        logger.logVariable("contacts", contacts);
        logger.logVariable("teamMembers", teamMembers);
        assertOnline("USER NOT ONLINE");
        assertDoesExistLocalDraft("LOCAL DRAFT DOES NOT EXIST", containerId);
        assertDoesNotContain("CANNOT PUBLISH TO SELF", teamMembers, localUser());
        try {
            final Calendar publishedOn = getSessionModel().readDateTime();
            final Container container = read(containerId);
            final ContainerDraft draft = readDraft(containerId);
            // if the artfiact doesn't exist on the server; create it there
            if (!isDistributed(container.getId())) {
                createDistributed(container, publishedOn);
            }
            // ensure the user is the key holder
            assertIsKeyHolder("USER NOT KEY HOLDER", containerId);
            // previous version
            final ContainerVersion previous = readLatestVersion(containerId);
            // create version
            fireProcessBegin(monitor);
            fireStageBegin(monitor, PublishStage.CreateVersion);
            final ContainerVersion version = createVersion(container.getId(),
                    readNextVersionId(containerId), comment, localUserId(),
                    publishedOn);

            // attach artifacts to the version
            final InternalDocumentModel documentModel = getDocumentModel();
            final List<Document> draftDocuments = draft.getDocuments();
            DocumentVersion draftDocumentLatestVersion;
            for(final Document draftDocument : draftDocuments) {
                if(ContainerDraft.ArtifactState.REMOVED != draft
                        .getState(draftDocument)) {
                    if (documentModel.isDraftModified(draftDocument.getId())) {
                        draftDocumentLatestVersion =
                            documentModel.createVersion(draftDocument.getId());
                    } else {
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
            // first version
            if (null == previous) {
                logger.logInfo("First version of {0}.", container.getName());
            } else {
                // delta previous with version
                containerIO.createDelta(calculateDelta(read(containerId), version, previous));
            }

            fireStageEnd(monitor, PublishStage.CreateVersion);

            doPublishVersion(monitor, containerId, version.getVersionId(),
                    contacts, teamMembers, publishedOn);

            // delete draft
            for (final Artifact artifact : draft.getArtifacts()) {
                documentModel.deleteDraft(artifact.getId());
                containerIO.deleteDraftArtifactRel(containerId, artifact.getId());
            }
            containerIO.deleteDraft(containerId);

            // remove key
            getArtifactModel().removeFlagKey(container.getId());

            // fire event
            final Container postPublish = read(container.getId());
            final ContainerVersion postPublishVersion = readVersion(
                    version.getArtifactId(), version.getVersionId());
            notifyContainerPublished(postPublish, draft, postPublishVersion,
                    localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        } finally {
            fireProcessEnd(monitor);
        }
    }

    /**
     * Publish the container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param contacts
     *            A contact <code>List</code>.
     */
    public void publishVersion(final PublishMonitor monitor,
            final Long containerId, final Long versionId,
            final List<Contact> contacts, final List<TeamMember> teamMembers) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("contacts", contacts);
        try {
            // start monitor
            fireProcessBegin(monitor);
            final Calendar publishedOn = getSessionModel().readDateTime();
            final ContainerVersion version = readVersion(containerId, versionId);
            final List<JabberId> publishTo = new ArrayList<JabberId>();
            final List<User> publishToUsers = new ArrayList<User>();
            // build the team ids and the publish to list
            final List<JabberId> teamMemberIds =
                getArtifactModel().readTeamIds(containerId);
            for (final Contact contact : contacts) {
                publishTo.add(contact.getId());
                publishToUsers.add(contact);
                teamMemberIds.add(contact.getId());
            }
            for (final TeamMember teamMember : teamMembers) {
                if (!contains(publishToUsers, teamMember)) {
                    if (!containsUser(publishTo, teamMember)) {
                        publishTo.add(teamMember.getId());
                        publishToUsers.add(teamMember);
                    } else {
                        Assert.assertUnreachable("Inconsistent publish to state.");
                    }
                }
            }
            // publish
            publish(monitor, version, readTeam(containerId), publishTo, version
                    .getCreatedBy(), publishedOn);
            // update the remote team
            final InternalSessionModel sessionModel = getSessionModel();
            final Container container = read(containerId);
            for (final Contact contact : contacts) {
                sessionModel.addTeamMember(container.getUniqueId(),
                        teamMemberIds, contact.getId());

            }
            // only create a published to reference if one for the user does not
            // already exist
            final Map<User, ArtifactReceipt> publishedTo =
                containerIO.readPublishedTo(containerId, versionId);
            for (final User user : publishToUsers) {
                if (!publishedTo.containsKey(user))
                    containerIO.createPublishedTo(containerId, versionId,
                            user, publishedOn);
            }
            // fire event
            final Container postPublish = read(containerId);
            final ContainerVersion postPublishVersion =
                readVersion(containerId, versionId);
            notifyContainerPublished(postPublish, postPublishVersion,
                    localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        } finally {
            fireProcessEnd(monitor);
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
     * Read the containers.
     * 
     * @param comparator
     *            A sort ordering to user.
     * @return A list of containers.
     */
    public List<Container> read(final Comparator<Artifact> comparator) {
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
    public List<Container> read(final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        logger.logApiId();
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        final List<Container> containers = containerIO.read(localUser());
        FilterManager.filter(containers, filter);
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
    public List<Container> read(final Filter<? super Artifact> filter) {
        logger.logApiId();
        logger.logVariable("filter", filter);
        return read(defaultComparator, filter);
    }

    /**
     * Read a container.
     * 
     * @param containerId
     *            A container id.
     * @return A container.
     */
    public Container read(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        return containerIO.read(containerId, localUser());
    }

    /**
     * Read the list of audit events for a container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of audit events.
     */
    public List<AuditEvent> readAuditEvents(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        return getAuditModel().read(containerId);
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
     * Read the delta for a version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param compareVersionId
     *            A container compare version id <code>Long</code>.
     * @return A <code>ContainerVersionDelta</code>.
     */
    public Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final Long containerId, final Long compareVersionId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("compareVersionId", compareVersionId);
        try {
            assertDoesExistVersion("Compare version does not exist.", containerId, compareVersionId);
            final Map<DocumentVersion, Delta> deltas = new TreeMap<DocumentVersion, Delta>(
                    new ComparatorBuilder().createVersionByName());
            // only one version exists therefore all addeds
            final List<DocumentVersion> versions = readDocumentVersions(containerId, compareVersionId);
            for (final DocumentVersion version : versions) {
                deltas.put(version, Delta.ADDED);
            }
            return deltas;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the delta between two versions.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param compareVersionId
     *            A container compare version id <code>Long</code>.
     * @param compareToVersionId
     *            A container compare to version id <code>Long</code>.
     * @return A <code>ContainerVersionDelta</code>.
     */
    public Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final Long containerId, final Long compareVersionId,
            final Long compareToVersionId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("compareVersionId", compareVersionId);
        logger.logVariable("compareToVersionId", compareToVersionId);
        try {
            assertDoesExistVersion("Compare version does not exist.", containerId, compareVersionId);
            assertDoesExistVersion("Compare to version does not exist.", containerId, compareVersionId);
            final Map<DocumentVersion, Delta> deltas = new TreeMap<DocumentVersion, Delta>(
                    new ComparatorBuilder().createVersionByName());
            // the two versions exist
            final ContainerVersionDelta delta = containerIO.readDelta(
                    containerId, compareVersionId, compareToVersionId);
            for (final ContainerVersionArtifactVersionDelta artifactDelta :
                delta.getDeltas()) {
                deltas.put(documentIO.getVersion(artifactDelta.getArtifactId(),
                        artifactDelta.getArtifactVersionId()),
                        artifactDelta.getDelta());
            }
            return deltas;
        } catch (final Throwable t) {
            throw translateError(t);
        }
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
            throw translateError(t);
        }
    }

    /**
     * Read the document versions for a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @param filter
     *            A document filter.
     * @return A list of document versions.
     */
    public List<DocumentVersion> readDocumentVersions(final Long containerId,
            final Long versionId, final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        return readDocumentVersions(containerId, versionId,
                defaultDocumentVersionComparator, filter);
    }

    /**
     * Read a container draft.
     * 
     * @param containerId
     *            A container id.
     * @return A container draft.
     */
    public ContainerDraft readDraft(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
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
    }

    /**
     * Read the container history.
     * 
     * @param containerId
     *            A container id.
     * @return A list of history items.
     */
    public List<ContainerHistoryItem> readHistory(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        return readHistory(containerId, defaultHistoryComparator, defaultHistoryFilter);
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
     * Read the next container version sequentially.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>ContainerVersion</code>.
     */
    public ContainerVersion readNextVersion(final Long containerId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
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
            throw translateError(t);
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
            throw translateError(t);
        }
    }

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        return readPublishedTo(containerId, versionId, defaultUserComparator,
                defaultUserFilter);
    }

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param comparator
     *            A <code>Comparator&lt;User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId, final Comparator<User> comparator) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        return readPublishedTo(containerId, versionId, comparator,
                defaultUserFilter);
    }

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param comparator
     *            A <code>Comparator&lt;User&gt;</code>.
     * @param filter
     *            A <code>Filter&lt;? super User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId, final Comparator<User> comparator,
            final Filter<? super User> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        final Map<User, ArtifactReceipt> publishedTo =
            containerIO.readPublishedTo(containerId, versionId);
        final List<User> users = new ArrayList<User>(publishedTo.size());
        for (final Entry<User, ArtifactReceipt> entry : publishedTo.entrySet()) {
            users.add(entry.getKey());
        }
        FilterManager.filter(users, filter);
        final Map<User, ArtifactReceipt> filteredPublishedTo = new TreeMap<User, ArtifactReceipt>(comparator);
        for (final User user : users) {
            filteredPublishedTo.put(user, publishedTo.get(user));
        }
        return filteredPublishedTo;
    }

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param filter
     *            A <code>Filter&lt;? super User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId, final Filter<? super User> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        return readPublishedTo(containerId, versionId, defaultUserComparator,
                filter); 
    }

    public List<ArtifactReceipt> readPublishedTo2(final Long containerId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        try {
            return readPublishedTo2(containerId, versionId,
                    defaultUserComparator, defaultUserFilter);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the team for the container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of users.
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
     * Read a list of versions for the container.
     * 
     * @param containerId
     *            A container id.
     * @param filter
     *            A container version filter.
     * 
     * @return A list of versions.
     */
    public List<ContainerVersion> readVersions(final Long containerId,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("filter", filter);
        return readVersions(containerId, defaultVersionComparator, filter);
    }

    /**
     * Remove a bookmark from a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void removeBookmark(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            getArtifactModel().removeFlagBookmark(containerId);
            notifyContainerUpdated(read(containerId), localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Remove a document from a container.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     */
    public void removeDocument(final Long containerId, final Long documentId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("documentId", documentId);
        assertDraftExists("DRAFT DOES NOT EXIST", containerId);
        assertDraftArtifactStateTransition("INVALID DRAFT DOCUMENT STATE",
                containerId, documentId, ContainerDraft.ArtifactState.REMOVED);
        final ContainerDraft draft = readDraft(containerId);
        final Document document = draft.getDocument(documentId);
        containerIO.deleteDraftArtifactRel(containerId, document.getId());
        switch (draft.getState(document.getId())) {
        case ADDED:     // delete the document
            getDocumentModel().delete(document.getId());
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
     *            A <code>ContainerListener</code>.
     */
    @Override
    public void removeListener(final ContainerListener listener) {
        super.removeListener(listener);
    }

    /**
     * Rename the container.
     * 
     * @param containerId
     *            A container id.
     * @param name
     *            The new container name.
     */
    public void rename(final Long containerId, final String name) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("name", name);
        assertIsNotDistributed("CONTAINER HAS BEEN DISTRIBUTED", containerId);
        try {
            containerIO.updateName(containerId, name);
            // fire event
            notifyContainerUpdated(read(containerId), localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Restore a container from an archive.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    public void restore(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        assertArtifactDoesNotExist(uniqueId,
                "Cannot restore an artifact that still exists.");
        try {
            final InternalArchiveModel archiveModel = getArchiveModel();
            // restore container info
            final Container container = archiveModel.readContainer(uniqueId);
            Assert.assertNotNull(container, "Container \"{0}\" has not been archived.", uniqueId);
            restore(container, new RestoreModel() {
                public InputStream openDocumentVersion(final UUID uniqueId,
                        final Long versionId) {
                    return archiveModel.openDocumentVersion(uniqueId, versionId);
                }
                public List<ContainerVersion> readContainerVersions(
                        final UUID uniqueId) {
                    return archiveModel.readContainerVersions(uniqueId);
                }
                public List<Document> readDocuments(final UUID uniqueId,
                        final Long versionId) {
                    return archiveModel.readDocuments(uniqueId, versionId);
                }
                public List<DocumentVersion> readDocumentVersions(
                        final UUID uniqueId, final Long versionId) {
                    return archiveModel.readDocumentVersions(uniqueId,
                            versionId);
                }
                public Map<User, ArtifactReceipt> readPublishedTo(
                        final UUID uniqueId, final Long versionId) {
                    return archiveModel.readPublishedTo(uniqueId, versionId);
                }
                public List<JabberId> readTeamIds(final UUID uniqueId) {
                    return archiveModel.readTeamIds(uniqueId);
                }
            });
            archiveModel.restore(uniqueId);
            notifyContainerRestored(read(container.getId()), localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Restore all containers from the backup.
     *
     */
    public void restoreBackup() {
        logger.logApiId();
        try {
            final List<Container> containers = read();
            if (0 < containers.size()) {
                logger.logWarning("{0} containers will be deleted.", containers.size());
                for (final Container container : containers) {
                    deleteLocal(container.getId());
                }
            }

            final InternalBackupModel backupModel = getBackupModel();
            final List<Container> backupContainers = backupModel.readContainers();
            logger.logVariable("backupContainers.size()", backupContainers.size());
            for (final Container backupContainer : backupContainers) {
                restore(backupContainer, new RestoreModel() {
                    public InputStream openDocumentVersion(final UUID uniqueId,
                            final Long versionId) {
                        return backupModel.openDocumentVersion(uniqueId,
                                versionId);
                    }
                    public List<ContainerVersion> readContainerVersions(
                            final UUID uniqueId) {
                        return backupModel.readContainerVersions(uniqueId);
                    }
                    public List<Document> readDocuments(final UUID uniqueId,
                            final Long versionId) {
                        return backupModel.readDocuments(uniqueId, versionId);
                    }
                    public List<DocumentVersion> readDocumentVersions(
                            final UUID uniqueId, final Long versionId) {
                        return backupModel.readDocumentVersions(uniqueId,
                                versionId);
                    }
                    public Map<User, ArtifactReceipt> readPublishedTo(
                            final UUID uniqueId, final Long versionId) {
                        return backupModel.readPublishedTo(uniqueId, versionId);
                    }
                    public List<JabberId> readTeamIds(final UUID uniqueId) {
                        return backupModel.readTeamIds(uniqueId);
                    }
                });
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Revert a document to it's pre-draft state.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void revertDocument(final Long containerId, final Long documentId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("documentId", documentId);
        assertDraftExists("DRAFT DOES NOT EXIST", containerId);
        assertDraftArtifactStateTransition("INVALID DRAFT DOCUMENT STATE",
                containerId, documentId, ContainerDraft.ArtifactState.NONE);
        assertDoesExistLatestVersion("LATEST VERSION DOES NOT EXIST", containerId);
        final ContainerDraft draft = readDraft(containerId);
        final Document document = draft.getDocument(documentId);
        containerIO.deleteDraftArtifactRel(containerId, document.getId());
        containerIO.createDraftArtifactRel(containerId, document.getId(),
                ContainerDraft.ArtifactState.NONE);
        getDocumentModel().revertDraft(documentId);

        final Container postRevertContainer = read(containerId);        
        final ContainerDraft postRevertDraft = readDraft(containerId);
        notifyDocumentReverted(postRevertContainer, postRevertDraft, document,
                localEventGenerator);

    }

    /**
     * Search for containers.
     * 
     * @param expression
     *            A search expression <code>String</code>.
     * @return A <code>List&lt;Long&gt;</code>.
     */
    public List<Long> search(final String expression) {
        logger.logApiId();
        logger.logVariable("expression", expression);
        try {
            final InternalIndexModel indexModel = getIndexModel();
            final List<Long> containerIds = indexModel.searchContainers(expression);
            containerIds.addAll(indexModel.searchDocuments(expression));
            return containerIds;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.AbstractModelImpl#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.artifactIO = IOFactory.getDefault(workspace).createArtifactHandler();
        this.auditor = new ContainerAuditor(modelFactory);
        this.containerIO = IOFactory.getDefault(workspace).createContainerHandler();
        this.documentIO = IOFactory.getDefault(workspace).createDocumentHandler();
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
            case MODIFIED:
                Assert.assertUnreachable(assertion);
                break;
            case REMOVED:   // valid state
            case NONE:      // valid state
                break;
            default:
                Assert.assertUnreachable("UNKNOWN STATE");
            }
            break;
        case MODIFIED:
            switch (targetState) {
            case ADDED:
                Assert.assertUnreachable(assertion);
                break;
            case MODIFIED:
                Assert.assertUnreachable(assertion);
                break;
            case REMOVED:   // valid state
                break;
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
            case MODIFIED:
                Assert.assertUnreachable(assertion);
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
            case MODIFIED:  // valid state
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
     * Assert that the container has been distributed.
     * 
     * @param assertion
     *            An assertion.
     * @param containerId
     *            A container id.
     */
    private void assertIsDistributed(final Object assertion,
            final Long containerId) {
        Assert.assertTrue(assertion, isDistributed(containerId));
    }

    /**
     * Assert that the container has been distributed.
     * 
     * @param assertion
     *            An assertion.
     * @param containerId
     *            A container id.
     */
    private void assertIsNotDistributed(final Object assertion,
            final Long containerId) {
        Assert.assertNotTrue(assertion, isDistributed(containerId));
    }

    /**
     * Audit a container created event.
     * 
     * @param container
     *            An container.
     */
    private void auditContainerCreated(final Container container) {
        auditor.create(container);
    }

    /**
     * Audit a container created event.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param publishedBy
     *            The publish user.
     * @param publishedTo
     *            The publish to users.
     * @param publishedOn
     *            The publish date.
     */
    private void auditContainerPublished(final Container container,
            final ContainerDraft draft, final ContainerVersion version,
            final JabberId publishedBy, final List<JabberId> publishedTo,
            final Calendar publishedOn) {
        auditor.publish(container, draft, version, publishedBy, publishedTo,
                publishedOn);
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
     * Create the container in the distributed network.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    private void createDistributed(final Container container,
            final Calendar createdOn) {
        final InternalSessionModel sessionModel = getSessionModel();
        sessionModel.createArtifact(localUserId(), container.getUniqueId(),
                createdOn);
        // TODO update the container's created on date
        // TODO update all documents' created on dates
    }

    /**
     * Create the first draft for a cotnainer.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param owner
     *            The draft owner <code>TeamMember</code>.
     * @return A new <code>ContainerDraft</code>.
     * 
     * @see #createDraft(Long)
     * @see #read(Long)
     * @see #notifyDraftCreated(Container, ContainerDraft,
     *      ContainerEventGenerator)
     */
    private ContainerDraft createFirstDraft(final Long containerId,
            final TeamMember owner) {
        final ContainerDraft draft = new ContainerDraft();
        draft.setContainerId(containerId);
        draft.setOwner(owner);
        containerIO.createDraft(draft);
        // fire draft event
        final Container postCreation = read(containerId);
        final ContainerDraft postCreationDraft = readDraft(containerId);
        notifyDraftCreated(postCreation, postCreationDraft, localEventGenerator);
        return postCreationDraft;
    }

    /**
     * Create a new container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param createdBy
     *            The created by user id <code>JabberId</code>.
     * @param createdOn
     *            The created on <code>Calendar</code>.
     * @return The new <code>ContainerVersion</code>.
     * 
     * @deprecated Use
     *             {@link ContainerModelImpl#createVersion(Long, Long, String, JabberId, Calendar)}
     *             instead.
     */
    @Deprecated
    private ContainerVersion createVersion(final Long containerId,
            final Long versionId, final JabberId createdBy,
            final Calendar createdOn) {
        return createVersion(containerId, versionId, null, createdBy, createdOn);
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

        return containerIO.readVersion(
                version.getArtifactId(), version.getVersionId());
    }

    /**
     * Delete the local info for this container.
     * 
     * @param containerId
     *            The container id.
     */
    private void deleteLocal(final Long containerId) {
        // delete the draft
        final List<Document> allDocuments = new ArrayList<Document>();
        final ContainerDraft draft = readDraft(containerId);
        if (null != draft) {
            for(final Document document : draft.getDocuments()) {
                containerIO.deleteDraftArtifactRel(containerId, document.getId());
                if (!allDocuments.contains(document))
                    allDocuments.add(document);
            }
            containerIO.deleteDraft(containerId);
        }
        // delete the team
        final InternalArtifactModel artifactModel = getArtifactModel();
        artifactModel.deleteTeam(containerId);
        // delete the remote info
        artifactModel.deleteRemoteInfo(containerId);
        // delete the audit events
        getAuditModel().delete(containerId);
        // delete versions
        final InternalDocumentModel documentModel = getDocumentModel();
        final List<ContainerVersion> versions = readVersions(containerId);
        List<Document> documents;
        for (final ContainerVersion version : versions) {
            documents =
                containerIO.readDocuments(version.getArtifactId(), version.getVersionId());
            for (final Document document : documents) {
                if (!allDocuments.contains(document))
                    allDocuments.add(document);
            }
            // remove the version's artifact versions
            containerIO.removeVersions(containerId, version.getVersionId());
            // delete the version's deltas
            containerIO.deleteDeltas(containerId, version.getVersionId());
            // delete the version
            containerIO.deleteVersion(containerId, version.getVersionId());
        }
        // delete documents
        for(final Document document : allDocuments) {
            documentModel.delete(document.getId());
        }
        // delete the index
        getIndexModel().deleteContainer(containerId);
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
        if (null != draft) {
            return draft.getOwner().getId().equals(localUserId());
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Publish a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     */
    private void doPublishVersion(final PublishMonitor monitor,
            final Long containerId, final Long versionId,
            final List<Contact> contacts, final List<TeamMember> teamMembers,
            final Calendar publishedOn) throws IOException {
        final Container container = read(containerId);
        final ContainerVersion version = readVersion(containerId, versionId);
        // build the team ids
        final List<JabberId> teamMemberIds =
            getArtifactModel().readTeamIds(containerId);
        // build the publish to list
        final List<JabberId> publishTo = new ArrayList<JabberId>();
        final List<User> publishToUsers = new ArrayList<User>();
        for (final Contact contact : contacts) {
            publishTo.add(contact.getId());
            publishToUsers.add(contact);
            teamMemberIds.add(contact.getId());
        }
        for (final TeamMember teamMember : teamMembers) {
            publishTo.add(teamMember.getId());
            publishToUsers.add(teamMember);
            if (!containsUser(teamMemberIds, teamMember))
                teamMemberIds.add(teamMember.getId());
        }
        // publish container contents
        publish(monitor, version, readTeam(containerId), publishTo,
                localUserId(), publishedOn);
        // update remote team
        for (final Contact contact : contacts)
            getSessionModel().addTeamMember(container.getUniqueId(),
                    teamMemberIds, contact.getId());
        // create published to list
        containerIO.createPublishedTo(containerId,
                version.getVersionId(), publishToUsers, publishedOn);
    }

    /**
     * Export a container and a list of versions.
     * 
     * @param exportDirectory
     *            An export directory <code>File</code>.
     * @param container
     *            A <code>Container</code>.
     * @param versions
     *            A <code>List&lt;ContainerVersion&gt;</code>.
     * @throws IOException
     * @throws TransformerException
     */
    private File export(final File exportDirectory,
            final String exportFileName, final Container container,
            final List<ContainerVersion> versions) throws IOException,
            TransformerException {
        final ContainerNameGenerator nameGenerator = getNameGenerator();
        final FileSystem exportFileSystem = new FileSystem(
                workspace.createTempDirectory(
                        nameGenerator.exportDirectoryName(container)));

        try {
            final InternalDocumentModel documentModel = getDocumentModel();
            final DocumentNameGenerator documentNameGenerator = documentModel.getNameGenerator();
            final Map<ContainerVersion, User> versionsPublishedBy =
                new HashMap<ContainerVersion, User>(versions.size(), 1.0F);
            final Map<ContainerVersion, List<DocumentVersion>> documents =
                new HashMap<ContainerVersion, List<DocumentVersion>>(versions.size(), 1.0F);
            final Map<DocumentVersion, Long> documentsSize = new HashMap<DocumentVersion, Long>();
            final Map<ContainerVersion, Map<User, ArtifactReceipt>> publishedTo =
                new HashMap<ContainerVersion, Map<User, ArtifactReceipt>>(versions.size(), 1.0F);
            InputStream stream;
            File directory, file;
            for (final ContainerVersion version : versions) {
                versionsPublishedBy.put(version, readUser(version.getUpdatedBy()));
                publishedTo.put(version, readPublishedTo(
                        version.getArtifactId(), version.getVersionId()));
    
                documents.put(version, readDocumentVersions(
                        version.getArtifactId(), version.getVersionId()));
                directory = exportFileSystem.createDirectory(
                        nameGenerator.exportDirectoryName(version));
                for (final DocumentVersion documentVersion : documents.get(version)) {
                    documentsSize.put(documentVersion, readDocumentVersionSize(
                            documentVersion.getArtifactId(), documentVersion.getVersionId()));
    
                    file = new File(directory,
                            documentNameGenerator.exportFileName(documentVersion));
                    Assert.assertTrue(file.createNewFile(),
                            "Cannot create file {0}.", file);
                    stream = documentModel.openVersionStream(
                            documentVersion.getArtifactId(),
                            documentVersion.getVersionId());
                    try {
                        FileUtil.write(stream, file);
                    } finally {
                        stream.close();
                    }
                }
            }
    
            final PDFWriter pdfWriter = new PDFWriter(exportFileSystem);
            pdfWriter.write(nameGenerator.pdfFileName(container), container,
                    readUser(container.getCreatedBy()), versions,
                    versionsPublishedBy, documents, documentsSize, publishedTo);
    
            final File zipFile = new File(exportFileSystem.getRoot(), exportFileName);
            ZipUtil.createZipFile(zipFile, exportFileSystem.getRoot());
            final File exportFile = new File(exportDirectory, zipFile.getName());
            FileUtil.copy(zipFile, exportFile, Boolean.TRUE,
                    getString("container.export.niceFilePattern"));
            return exportFile;
        } finally {
            exportFileSystem.deleteTree();
        }
    }

    /**
     * Initialize a publish monitor.
     * 
     * @param monitor
     *            A <code>PublishMonitor</code>.
     * @param stages
     *            An <code>Integer</code>. number of stages.
     */
    private void fireDetermine(final PublishMonitor monitor,
            final Iterator<DocumentVersion> iDocuments) {
        int steps = 1;
        while (iDocuments.hasNext()) {
            // each 1K is a step
            steps += (iDocuments.next().getSize() / STEP_SIZE);
        }
        monitor.determine(steps);
    }

    /**
     * Fire the process being for the publish monitor.
     * 
     * @param monitor
     *            A <code>PublishMonitor</code>.
     */
    private void fireProcessBegin(final PublishMonitor monitor) {
        monitor.processBegin();
    }

    /**
     * Notify the monitor of the end of the process.
     * 
     * @param monitor
     *            A <code>PublishMonitor</code>.
     */
    private void fireProcessEnd(final PublishMonitor monitor) {
        monitor.processEnd();
    }

    private void fireStageBegin(final PublishMonitor monitor,
            final PublishStage stage) {
        fireStageBegin(monitor, stage, null);
    }

    private void fireStageBegin(final PublishMonitor monitor,
            final PublishStage stage, final Object data) {
        monitor.stageBegin(stage, data);
    }


    private void fireStageEnd(final PublishMonitor monitor,
            final PublishStage stage) {
        monitor.stageEnd(stage);
    }

    /**
     * Obtain a container name generator.
     * 
     * @return A <code>ContainerNameGenerator</code>.
     */
    private ContainerNameGenerator getNameGenerator() {
        logger.logApiId();
        try {
            return new ContainerNameGenerator(l18n);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Handle a document published remote event.
     * 
     * @param uniqueId
     *            The document unique id <code>UUID</code>.
     * @param versionId
     *            The document version id <code>Long</code>.
     * @param name
     *            The document name <code>String</code>
     * @param checksum
     *            The document checksum <code>String</code>.
     * @param streamId
     *            The document stream id <code>String</code>.
     * @param publishedBy
     *            The publish user <code>JabberId</code>.
     * @param publishedOn
     *            The publish date <code>Calendar</code>.
     * @return A new <code>DocumentVersion</code>.
     * 
     * @throws IOException
     * 
     * @see InternalDocumentModel#handleDocumentPublished(JabberId, Calendar, UUID, Long, String, String, InputStream)
     */
    private DocumentVersion handleDocumentPublished(
            final ContainerArtifactPublishedEvent event) throws IOException {
        return getDocumentModel().handleDocumentPublished(event);
    }

    /**
     * Determine whether or not the draft is the first draft.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if the draft is the first draft.
     */
    private Boolean isFirstDraft(final Long containerId) {
        return 0 == readVersions(containerId).size();
    }

    /**
     * Determine if the local user ia a team member. The only scenaio where this
     * will not be the case is for archive users.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if the local user is a team member.
     */
    private boolean isLocalTeamMember(final Long containerId) {
        return contains(getArtifactModel().readTeam2(containerId), localUser());
    }

    /**
     * Fire a container archived notification.
     * 
     * @param container
     *            A container.
     * @param eventGenerator
     *            A container event generator.
     */
    private void notifyContainerArchived(final Container container,
            final ContainerEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.containerArchived(eventGenerator.generate(container));
            }
        });
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
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.containerCreated(eventGenerator.generate(container));
            }
        });
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
            final ContainerDraft draft, final ContainerVersion version,
            final ContainerEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.draftPublished(eventGenerator.generate(container,
                        draft, version));
            }
        });
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
            final ContainerVersion version,
            final ContainerEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.draftPublished(eventGenerator.generate(container, version));
            }
        });
    }

    /**
     * Fire a container published event.
     * 
     * @param container
     *            A container.
     * @param draft
     *            A container draft.
     * @param version
     *            A container version.
     * @param eventGenerator
     *            A container event generator.
     */
    private void notifyContainerPublished(final Container container,
            final TeamMember teamMember, final ContainerVersion version,
            final ContainerEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.draftPublished(eventGenerator.generate(container,
                        teamMember, version));
            }
        });
    }

    /**
     * Fire a container restored notification.
     * 
     * @param container
     *            A container.
     * @param eventGenerator
     *            A container event generator.
     */
    private void notifyContainerRestored(final Container container,
            final ContainerEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.containerRestored(eventGenerator.generate(container));
            }
        });
    }

    /**
     * Notify that a container has been updated.
     * 
     * @param container
     *            A container.
     * @param eventGenerator
     *            A container event generator.
     */
    private void notifyContainerUpdated(final Container container,
            final ContainerEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.containerUpdated(eventGenerator.generate(container));
            }
        });
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
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.documentAdded(eventGenerator.generate(container,
                        draft, document));
            }
        });
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
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.documentRemoved(eventGenerator.generate(container,
                        draft, document));
            }
        });
    }

    /**
     * Fire a document reverted notification.
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
    private void notifyDocumentReverted(final Container container,
            final ContainerDraft draft, final Document document,
            final ContainerEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.documentReverted(eventGenerator.generate(container,
                        draft, document));
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
    private void publish(final PublishMonitor monitor,
            final ContainerVersion version, final List<TeamMember> team,
            final List<JabberId> publishTo, final JabberId publishedBy,
            final Calendar publishedOn) throws IOException {
        final Map<DocumentVersion, InputStream> documentVersionStreams =
            readDocumentVersionStreams(version.getArtifactId(),
                    version.getVersionId());
        final Map<DocumentVersion, String> documentVersionStreamIds =
            new HashMap<DocumentVersion, String>(documentVersionStreams.size(), 1.0F);
        final StreamSession session = getSessionModel().createStreamSession();
        fireDetermine(monitor, documentVersionStreams.keySet().iterator());
        for (final Entry<DocumentVersion, InputStream> entry :
                documentVersionStreams.entrySet()) {
            fireStageBegin(monitor, PublishStage.UploadStream,
                    entry.getKey().getName());
            try {
                documentVersionStreamIds.put(entry.getKey(), uploadStream(
                        new UploadMonitor() {
                            private long totalChunks = 0;
                            public void chunkUploaded(final int chunkSize) {
                                totalChunks += chunkSize;
                                if (totalChunks >= STEP_SIZE) {
                                    totalChunks -= STEP_SIZE;
                                    fireStageEnd(monitor, PublishStage.UploadStream);
                                }
                            }
                        }, session, entry.getValue(), entry.getKey().getSize()));
                fireStageEnd(monitor, PublishStage.UploadStream);
            } finally {
                entry.getValue().close();
            }
        }
        getSessionModel().deleteStreamSession(session);
        fireStageBegin(monitor, PublishStage.PublishContainer);
        getSessionModel().publish(version, documentVersionStreamIds, team,
                publishTo, publishedBy, publishedOn);
        fireStageEnd(monitor, PublishStage.PublishContainer);
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

    private Long readDocumentVersionSize(final Long documentId,
            final Long versionId) {
        return getDocumentModel().readVersionSize(documentId, versionId);
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
        final InternalDocumentModel documentModel = getDocumentModel();
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
    private List<ContainerHistoryItem> readHistory(final Long containerId,
            final Comparator<? super HistoryItem> comparator,
            final Filter<? super HistoryItem> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        final ContainerHistoryBuilder historyBuilder =
            new ContainerHistoryBuilder(getContainerModel(), l18n);
        final List<ContainerHistoryItem> history = historyBuilder.createHistory(containerId);
        FilterManager.filter(history, filter);
        ModelSorter.sortHistory(history, defaultHistoryComparator);
        return history;
    }

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param comparator
     *            A <code>User</code> <code>Comparator</code>.
     * @param filter
     *            A <code>User</code> <code>Filter</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    private List<ArtifactReceipt> readPublishedTo2(final Long containerId,
            final Long versionId, final Comparator<User> comparator,
            final Filter<? super User> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        try {
            final Map<User, ArtifactReceipt> publishedTo =
                containerIO.readPublishedTo(containerId, versionId);
            final List<User> users = new ArrayList<User>(publishedTo.size());
            for (final Entry<User, ArtifactReceipt> entry : publishedTo.entrySet()) {
                users.add(entry.getKey());
            }
            FilterManager.filter(users, filter);
            Collections.sort(users, comparator);
            final List<ArtifactReceipt> filteredPublishedTo = new ArrayList<ArtifactReceipt>();
            for (final User user : users) {
                filteredPublishedTo.add(publishedTo.get(user));
            }
            return filteredPublishedTo;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    private User readUser(final JabberId userId) {
        return getUserModel().read(userId);
    }

    /**
     * Restore a container to the local database.
     * 
     * @param container
     *            A container.
     * @param archiveReader
     *            An archive reader.
     * @throws IOException
     */
    private void restore(final Container container, final RestoreModel restoreModel)
            throws IOException {
        final InternalUserModel userModel = getUserModel();
        userModel.readLazyCreate(container.getCreatedBy());
        userModel.readLazyCreate(container.getUpdatedBy());
        containerIO.create(container);
        artifactIO.createRemoteInfo(container.getId(),
                container.getUpdatedBy(), container.getUpdatedOn());
        // restore team info
        final List<JabberId> teamIds = restoreModel.readTeamIds(container.getUniqueId());
        for (final JabberId teamId : teamIds) {
            artifactIO.createTeamRel(
                    container.getId(),
                    userModel.readLazyCreate(teamId).getLocalId());
        }
        // restore version info
        final List<ContainerVersion> versions =
            restoreModel.readContainerVersions(container.getUniqueId());
        // we want to restore in from first to last chronologically
        ModelSorter.sortContainerVersions(versions, new ComparatorBuilder().createVersionById(Boolean.TRUE));
        List<Document> documents;
        List<DocumentVersion> documentVersions;
        InputStream documentVersionStream;
        ContainerVersion previous;
        Map<User, ArtifactReceipt> publishedTo;
        for (final ContainerVersion version : versions) {
            logger.logTrace("Restoring container \"{0}\" version \"{1}.\"",
                    version.getName(), version.getVersionId());
            userModel.readLazyCreate(version.getCreatedBy());
            userModel.readLazyCreate(version.getUpdatedBy());
            version.setArtifactId(container.getId());
            containerIO.createVersion(version);
            artifactIO.updateFlags(container.getId(), container.getFlags());
            publishedTo = restoreModel.readPublishedTo(
                    version.getArtifactUniqueId(), version.getVersionId());
            for (final Entry<User, ArtifactReceipt> entry : publishedTo.entrySet()) {
                containerIO.createPublishedTo(container.getId(),
                        version.getVersionId(),
                        userModel.read(entry.getValue().getUserId()),
                        entry.getValue().getPublishedOn());
                if (entry.getValue().isSetReceivedOn()) {
                    containerIO.updatePublishedTo(container.getId(),
                            version.getVersionId(),
                            entry.getValue().getPublishedOn(),
                            entry.getValue().getUserId(),
                            entry.getValue().getReceivedOn());
                }
            }
            // restore version links
            documents = restoreModel.readDocuments(container.getUniqueId(), version.getVersionId());
            documentVersions = restoreModel.readDocumentVersions(container.getUniqueId(), version.getVersionId());
            for (final Document document : documents) {
                logger.logTrace("Restoring container \"{0}\" version \"{1}\" document \"{2}.\"",
                        version.getName(), version.getVersionId(),
                        document.getName());
                userModel.readLazyCreate(document.getCreatedBy());
                userModel.readLazyCreate(document.getUpdatedBy());
                if (artifactIO.doesExist(document.getUniqueId())) {
                    document.setId(artifactIO.readId(document.getUniqueId()));
                    artifactIO.updateRemoteInfo(document.getId(), document
                            .getUpdatedBy(), document.getUpdatedOn());
                } else {
                    documentIO.create(document);
                    artifactIO.createRemoteInfo(document.getId(),
                            document.getUpdatedBy(), document.getUpdatedOn());
                }
                for (final DocumentVersion documentVersion : documentVersions) {
                    if (documentVersion.getArtifactUniqueId().equals(document.getUniqueId())) {
                        if (!artifactIO.doesVersionExist(document.getId(),
                                documentVersion.getVersionId())) {
                            logger.logTrace("Restoring container \"{0}\" version \"{1}\" document \"{2}\" version \"{3}.\"",
                                    version.getName(), version.getVersionId(),
                                    documentVersion.getName(), documentVersion.getVersionId());
                            userModel.readLazyCreate(documentVersion.getCreatedBy());
                            userModel.readLazyCreate(documentVersion.getUpdatedBy());
                            documentVersion.setArtifactId(document.getId());
                            documentVersionStream =
                                restoreModel.openDocumentVersion(
                                        document.getUniqueId(), documentVersion.getVersionId());
                            try {
                                documentIO.createVersion(documentVersion, documentVersionStream);
                            } finally {
                                documentVersionStream.close();
                            }
                            containerIO.addVersion(container.getId(),
                                    version.getVersionId(), document.getId(),
                                    documentVersion.getVersionId(),
                                    document.getType());
                            getIndexModel().indexDocument(container.getId(), document.getId());
                            logger.logTrace("Document version has been restored.");
                            break;
                        }
                    }
                }
                logger.logTrace("Document has been restored.");
                previous = readPreviousVersion(version.getArtifactId(), version.getVersionId());
                if (null != previous) {
                    containerIO.deleteDelta(version.getArtifactId(),
                            version.getVersionId(), previous.getVersionId());
                    containerIO.createDelta(calculateDelta(read(version.getArtifactId()),
                            version, previous));
                }
            }
            logger.logTrace("Container version has been restored.");
        }
        getIndexModel().indexContainer(container.getId());
    }

    /**
     * A private interface used by the restore api such that it can be shared
     * by both restore and restoreBackup.
     */
    private interface RestoreModel {

        /**
         * Open a document version stream.
         * 
         * @param uniqueId
         *            A document unique id <code>UUID</code>.
         * @param versionId
         *            A document version id <code>Long</code>.
         * @return An <code>InputStream</code>.
         */
        public InputStream openDocumentVersion(final UUID uniqueId,
                final Long versionId);

        /**
         * Read a list of versions for a container.
         * 
         * @param uniqueId
         *            A container unique id <code>UUID</code>.
         * @return A <code>List&lt;ContainerVersion&gt;</code>.
         */
        public List<ContainerVersion> readContainerVersions(final UUID uniqueId);

        /**
         * Read a list of documents for a container version.
         * 
         * @param uniqueId
         *            A container unique id <code>UUID</code>.
         * @param versionId
         *            A container version id <code>Long</code>.
         * @return A <code>List&lt;Document&gt;</code>.
         */
        public List<Document> readDocuments(final UUID uniqueId,
                final Long versionId);

        /**
         * Read a list of document versions for a container version's document.
         * 
         * @param uniqueId
         *            A container unique id <code>UUID</code>.
         * @param versionId
         *            A container version id <code>Long</code>.
         * @param documentUniqueId
         *            A document unique id <code>UUID</code>.
         * @return A <code>List&lt;DocumentVersion&gt;</code>.
         */
        public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
                final Long versionId);

        /**
         * Read a list of users the version has been published to and the
         * receipt information.
         * 
         * @param uniqueId
         *            A unique id <code>UUID</code>.
         * @param versionId
         *            A version id <code>Long</code>.
         * @return A list of <code>User</code>s and the corresponding
         *         <code>ArtifactReceipt</code>.
         */
        public Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId,
                final Long versionId);

        /**
         * Read the team for an artifact.
         * 
         * @param uniqueId
         *            An artifact unique id <code>UUID</code>.
         * @return A <code>List&lt;JabberId&gt;</code>.
         */
        public List<JabberId> readTeamIds(final UUID uniqueId);
    }
}
