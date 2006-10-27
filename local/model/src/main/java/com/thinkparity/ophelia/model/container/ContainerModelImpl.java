/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.ophelia.model.container;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.Constants.Versioning;
import com.thinkparity.ophelia.model.archive.InternalArchiveModel;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.audit.HistoryItem;
import com.thinkparity.ophelia.model.audit.event.AuditEvent;
import com.thinkparity.ophelia.model.backup.InternalBackupModel;
import com.thinkparity.ophelia.model.container.export.PDFWriter;
import com.thinkparity.ophelia.model.document.DocumentNameGenerator;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.events.ContainerListener;
import com.thinkparity.ophelia.model.events.ContainerEvent.Source;
import com.thinkparity.ophelia.model.index.InternalIndexModel;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler;
import com.thinkparity.ophelia.model.io.handler.ContainerIOHandler;
import com.thinkparity.ophelia.model.io.handler.DocumentIOHandler;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.user.TeamMember;
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
 * @author CreateModel.groovy
 * @version 1.1.2.3
 */
final class ContainerModelImpl extends AbstractModelImpl<ContainerListener> {

    /** The artifact io layer. */
    private final ArtifactIOHandler artifactIO;

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
    private final DocumentIOHandler documentIO;

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
    ContainerModelImpl(final Environment environment, final Workspace workspace) {
        super(environment, workspace);
        this.artifactIO = IOFactory.getDefault(workspace).createArtifactHandler();
        this.auditor = new ContainerAuditor(internalModelFactory);
        this.containerIO = IOFactory.getDefault(workspace).createContainerHandler();
        this.documentIO = IOFactory.getDefault(workspace).createDocumentHandler();
        this.defaultComparator = new ComparatorBuilder().createByName(Boolean.TRUE);
        this.defaultDocumentComparator = new ComparatorBuilder().createByName(Boolean.TRUE);
        this.defaultDocumentFilter = FilterManager.createDefault();
        this.defaultDocumentVersionComparator = new ComparatorBuilder().createVersionById(Boolean.FALSE);
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
     * @see com.thinkparity.ophelia.model.AbstractModelImpl#addListener(com.thinkparity.ophelia.model.util.EventListener)
     */
    @Override
    protected boolean addListener(final ContainerListener listener) {
        return super.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.AbstractModelImpl#removeListener(com.thinkparity.ophelia.model.util.EventListener)
     */
    @Override
    protected boolean removeListener(final ContainerListener listener) {
        return super.removeListener(listener);
    }

    /**
     * Apply a bookmark to a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    void addBookmark(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            getInternalArtifactModel().applyFlagBookmark(containerId);
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
    void addDocument(final Long containerId, final Long documentId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("documentId", documentId);
        assertDraftExists("DRAFT DOES NOT EXIST", containerId);
        containerIO.createDraftArtifactRel(containerId, documentId, ContainerDraft.ArtifactState.ADDED);
        getIndexModel().indexDocument(containerId, documentId);
        final Container postAdditionContainer = read(containerId);        
        final ContainerDraft postAdditionDraft = readDraft(containerId);
        final Document postAdditionDocument = getInternalDocumentModel().read(documentId);
        notifyDocumentAdded(postAdditionContainer, postAdditionDraft,
                postAdditionDocument, localEventGenerator);
    }

    /**
     * Archive a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    void archive(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
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
    Container create(final String name) {
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
            final InternalArtifactModel artifactModel = getInternalArtifactModel();
            artifactModel.applyFlagKey(container.getId());
    
            // create remote info
            artifactModel.createRemoteInfo(container.getId(),
                    container.getCreatedBy(), container.getCreatedOn());
    
            // index
            getIndexModel().indexContainer(container.getId());
    
            // create team
            final TeamMember teamMember = createTeam(container.getId());
    
            // create first draft
            createFirstDraft(container.getId(), teamMember);
    
            // audit\fire event
            final Container postCreation = read(container.getId());
            auditContainerCreated(postCreation);
            notifyContainerCreated(postCreation, localEventGenerator);
            return postCreation;
        } catch (final Throwable t) {
            throw translateError(t);
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
    ContainerDraft createDraft(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        assertContainerDraftDoesNotExist("Draft already exist.", containerId);
        if (isFirstDraft(containerId)) {
            createFirstDraft(containerId, localTeamMember(containerId));
        } else {
            assertOnline("The user is not online.");
            final Container container = read(containerId);
            if (!isDistributed(container.getId())) {
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
            for (final Document document : documents) {
                draft.addDocument(document);
                draft.putState(document, ContainerDraft.ArtifactState.NONE);
            }
            containerIO.createDraft(draft);
            getInternalArtifactModel().applyFlagKey(container.getId());
            // remote create
            getSessionModel().createDraft(container.getUniqueId());
        }
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
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            return createVersion(containerId, readNextVersionId(containerId),
                    localUserId(), currentDateTime());
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
    void delete(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            final Container container = read(containerId);
            if (isDistributed(container.getId())) {
                final TeamMember localTeamMember = localTeamMember(container.getId());
                deleteLocal(container.getId());
                getSessionModel().removeTeamMember(
                        container.getUniqueId(), localTeamMember.getId());
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
    void deleteDraft(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        assertDoesExistLocalDraft("Draft does not exist.", containerId);
        final Container container = read(containerId);
        if (!isFirstDraft(container.getId())) {
            assertOnline("User is not online.");
            assertIsDistributed("Draft has not been distributed.", containerId);
            getSessionModel().deleteDraft(container.getUniqueId());
        }
        // delete local data
        final ContainerDraft draft = readDraft(containerId);
        for (final Artifact artifact : draft.getArtifacts()) {
            containerIO.deleteDraftArtifactRel(containerId, artifact.getId());
        }
        containerIO.deleteDraft(containerId);
        notifyDraftDeleted(container, draft, localEventGenerator);
    }

    /**
     * Export a container.
     * 
     * @param exportDirectory
     *            The directory <code>File</code> to export to.
     * @param containerId
     *            The container id <code>Long</code>.
     */
    void export(final File exportDirectory, final Long containerId) {
        logger.logApiId();
        logger.logVariable("exportDirectory", exportDirectory);
        logger.logVariable("containerId", containerId);
        try {
            Assert.assertTrue(exportDirectory.isDirectory(),
                    "Export directory {0} is not a directory.", exportDirectory);
            final Container container = read(containerId);
            final List<ContainerVersion> versions = readVersions(containerId);
            export(exportDirectory, container, versions);
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
    void exportVersion(final File exportDirectory, final Long containerId,
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
            versions.add(readVersion(containerId, versionId));
            export(exportDirectory, container, versions);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Obtain a container name generator.
     * 
     * @return A <code>ContainerNameGenerator</code>.
     */
    ContainerNameGenerator getNameGenerator() {
        logger.logApiId();
        try {
            return new ContainerNameGenerator(l18n);
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
    void handleArtifactPublished(final UUID uniqueId, final Long versionId,
            final String name, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final String artifactStreamId, final JabberId publishedBy,
            final Calendar publishedOn) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("name", name);
        logger.logVariable("artifactUniqueId", artifactUniqueId);
        logger.logVariable("artifactVersionId", artifactVersionId);
        logger.logVariable("artifactName", artifactName);
        logger.logVariable("artifactType", artifactType);
        logger.logVariable("artifactChecksum", artifactChecksum);
        logger.logVariable("artifactStreamId", artifactStreamId);
        logger.logVariable("publishedBy", publishedBy);
        logger.logVariable("publishedOn", publishedOn);
        assertIsNotLocalUserId(publishedBy);
        try {
            // determine the existance of the container and the version.
            final InternalArtifactModel artifactModel = getInternalArtifactModel();
            final Boolean doesExist = artifactModel.doesExist(uniqueId);
            final Boolean doesVersionExist;
            final Long containerId;
            final Container container;
            final ContainerVersion version;
            if (doesExist) {
                containerId = artifactModel.readId(uniqueId);
                container = read(containerId);
                doesVersionExist = artifactModel.doesVersionExist(containerId, versionId);

                if (doesVersionExist) {
                    version = readVersion(container.getId(), versionId);
                } else {
                    version = createVersion(container.getId(),
                            versionId, publishedBy, publishedOn);
                }
            } else { 
                doesVersionExist = Boolean.FALSE;

                // ensure the published by user exists locally
                getInternalUserModel().readLazyCreate(publishedBy);

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

                // create version
                version = createVersion(container.getId(), versionId,
                        publishedBy, publishedOn);
    
                // create remote info
                artifactModel.createRemoteInfo(container.getId(), publishedBy, container.getCreatedOn());
    
                // index
                getIndexModel().indexContainer(container.getId());
            }

            // handle the artifact by specific type
            final ArtifactVersion artifactVersion;
            switch(artifactType) {
            case DOCUMENT:
                artifactVersion = handleDocumentPublished(artifactUniqueId,
                        artifactVersionId, artifactName, artifactChecksum,
                        artifactStreamId, publishedBy, publishedOn);
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
    void handleDraftCreated(final Long containerId,
            final JabberId createdBy, final Calendar createdOn) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("createdBy", createdBy);
        logger.logVariable("createdOn", createdOn);
        final ContainerDraft draft = new ContainerDraft();
        draft.setContainerId(containerId);
        final List<TeamMember> team = readTeam(containerId);
        logger.logVariable("team", team);
        draft.setOwner(team.get(indexOf(team, createdBy)));
        containerIO.createDraft(draft);
        // fire event
        notifyDraftCreated(read(containerId), readDraft(containerId),
                remoteEventGenerator);
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
    void handleDraftDeleted(final Long containerId,
            final JabberId deletedBy, final Calendar deletedOn) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("createdBy", deletedBy);
        logger.logVariable("createdOn", deletedOn);
        final ContainerDraft draft = readDraft(containerId);
        for (final Artifact artifact : draft.getArtifacts()) {
            containerIO.deleteDraftArtifactRel(containerId, artifact.getId());
        }
        containerIO.deleteDraft(containerId);
        // fire event
        notifyDraftDeleted(read(containerId), draft, remoteEventGenerator);
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
    void handlePublished(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final JabberId publishedBy, final List<JabberId> publishedTo,
            final Calendar publishedOn) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("name", name);
        logger.logVariable("artifactCount", artifactCount);
        logger.logVariable("publishedBy", publishedBy);
        logger.logVariable("publishedTo", publishedTo);
        logger.logVariable("publishedOn", publishedOn);
        final InternalArtifactModel artifactModel = getInternalArtifactModel();
        final Long containerId = artifactModel.readId(uniqueId);
        // add to local team
        final InternalUserModel userModel = getInternalUserModel();
        final List<TeamMember> localTeam = artifactModel.readTeam2(containerId);
        final List<User> publishedToUsers = new ArrayList<User>();
        for (final JabberId publishedToId : publishedTo) {
            if (!contains(localTeam, publishedToId)) {
                artifactModel.addTeamMember(containerId, publishedToId);
            }
            publishedToUsers.add(userModel.read(publishedToId));
        }
        // add the sender as well
        if (!contains(localTeam, publishedBy)) {
            artifactModel.addTeamMember(containerId, publishedBy);
        }
        // delete draft
        final ContainerDraft draft = readDraft(containerId);
        if (null == draft) {
            logger.logWarning("Draft did not previously exist for {0}.", name);
        } else {
            for (final Artifact artifact : draft.getArtifacts()) {
                containerIO.deleteDraftArtifactRel(containerId, artifact.getId());
            }
            containerIO.deleteDraft(containerId);
        }
        // create published to list
        containerIO.createPublishedTo(containerId, versionId, publishedToUsers);
        // send confirmation
        getSessionModel().confirmArtifactReceipt(localUserId(),
                uniqueId, versionId, localUserId(), currentDateTime());
        // audit\fire event
        final Container postPublish = read(containerId);
        final ContainerDraft postPublishDraft = readDraft(containerId);
        final ContainerVersion postPublishVersion = readVersion(containerId, versionId);
        auditContainerPublished(postPublish, postPublishDraft,
                postPublishVersion, publishedBy, publishedTo, publishedOn);
        notifyContainerPublished(postPublish, postPublishDraft,
                postPublishVersion, remoteEventGenerator);
    }

    void handleReceived(final Long containerId, final Long versionId,
            final JabberId receivedBy, final Calendar receivedOn) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("receivedBy", receivedBy);
        logger.logVariable("receivedOn", receivedOn);
        containerIO.updatePublishedTo(containerId, versionId, receivedBy, receivedOn);
        containerIO.updateSharedWith(containerId, versionId, receivedBy, receivedOn);
    }

    /**
     * Print a container draft.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param printer
     *            An <code>Printer</code>.
     */
    void printDraft(final Long containerId, final Printer printer) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("printer", printer);
        try {
            final ContainerDraft draft = readDraft(containerId);
            final InternalDocumentModel documentModel = getInternalDocumentModel();
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
    void printVersion(final Long containerId, final Long versionId,
            final Printer printer) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("printer", printer);
        try {
            final InternalDocumentModel documentModel = getInternalDocumentModel();
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
    void publish(final Long containerId, final List<Contact> contacts,
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
    void publish(final Long containerId, final String comment,
            final List<Contact> contacts, final List<TeamMember> teamMembers) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("comment", comment);
        logger.logVariable("contacts", contacts);
        logger.logVariable("teamMembers", teamMembers);
        assertOnline("USER NOT ONLINE");
        assertDoesExistLocalDraft("LOCAL DRAFT DOES NOT EXIST", containerId);
        assertDoesNotContain("CANNOT PUBLISH TO SELF", teamMembers, localUser());
        try {
            final Container container = read(containerId);
            final ContainerDraft draft = readDraft(containerId);
            // if the artfiact doesn't exist on the server; create it there
            if (!isDistributed(container.getId())) {
                createDistributed(container);
            }
            // ensure the user is the key holder
            assertIsKeyHolder("USER NOT KEY HOLDER", containerId);

            // create version
            final ContainerVersion version = createVersion(container.getId());

            // attach artifacts to the version
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
            doPublishVersion(containerId, version.getVersionId(), contacts,
                    teamMembers);

            // delete draft
            for(final Artifact artifact : draft.getArtifacts()) {
                containerIO.deleteDraftArtifactRel(
                        container.getId(), artifact.getId());
            }
            containerIO.deleteDraft(container.getId());

            // fire event
            final Container postPublish = read(container.getId());
            final ContainerVersion postPublishVersion = readVersion(
                    version.getArtifactId(), version.getVersionId());
            notifyContainerPublished(postPublish, draft, postPublishVersion,
                    localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
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
    void publishVersion(final Long containerId, final Long versionId,
            final List<Contact> contacts) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("contacts", contacts);
        try {
            // remove local key
            getInternalArtifactModel().removeFlagKey(containerId);

            final List<TeamMember> teamMembers = Collections.emptyList();
            doPublishVersion(containerId, versionId, contacts, teamMembers);

            // fire event
            final Container postPublish = read(containerId);
            final ContainerVersion postPublishVersion =
                readVersion(containerId, versionId);
            notifyContainerPublished(postPublish, postPublishVersion,
                    localEventGenerator);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the containers.
     * 
     * @return A list of containers.
     */
    List<Container> read() {
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
    List<Container> read(final Comparator<Artifact> comparator) {
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
    List<Container> read(final Comparator<Artifact> comparator,
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
    List<Container> read(final Filter<? super Artifact> filter) {
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
    Container read(final Long containerId) {
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
    List<AuditEvent> readAuditEvents(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
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
     */
    List<Document> readDocuments(final Long containerId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
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
     */
    List<Document> readDocuments(final Long containerId, final Long versionId,
            final Comparator<Artifact> comparator) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        return readDocuments(containerId, versionId, comparator, defaultDocumentFilter);
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
    List<Document> readDocuments(final Long containerId, final Long versionId,
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
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @param filter
     *            A document filter.
     * @return A list of documents.
     */
    List<Document> readDocuments(final Long containerId, final Long versionId,
            final Filter<? super Artifact> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
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
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        final ContainerVersion latestVersion = readLatestVersion(containerId);
        return readDocumentVersions(containerId, latestVersion.getVersionId());
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
    List<DocumentVersion> readDocumentVersions(final Long containerId,
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
    List<DocumentVersion> readDocumentVersions(final Long containerId,
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
    List<DocumentVersion> readDocumentVersions(final Long containerId,
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
            return documentVersions;
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
    List<DocumentVersion> readDocumentVersions(final Long containerId,
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
    ContainerDraft readDraft(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        final InternalDocumentModel documentModel = getInternalDocumentModel();
        final ContainerDraft draft = containerIO.readDraft(containerId);
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
    List<ContainerHistoryItem> readHistory(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
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
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("comparator", comparator);
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
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("filter", filter);
        return readHistory(containerId, defaultHistoryComparator, filter);
    }

    /**
     * Read the latest container version.
     * 
     * @param containerId
     *            A container id.
     * @return A container version.
     */
    ContainerVersion readLatestVersion(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        if (doesExistLatestVersion(containerId)) {
            return containerIO.readLatestVersion(containerId);
        } else {
            return null;
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
    Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
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
    Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
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
    Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId, final Comparator<User> comparator,
            final Filter<? super User> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        final List<User> users =
            containerIO.readPublishedTo(containerId, versionId);
        FilterManager.filter(users, filter);
        ModelSorter.sortUsers(users, comparator);
        final Map<User, ArtifactReceipt> publishedTo = new HashMap<User, ArtifactReceipt>(users.size(), 1.0F);
        for (final User user : users) {
            final ArtifactReceipt receipt = new ArtifactReceipt();
            receipt.setArtifactId(containerId);
            receipt.setReceivedOn(currentDateTime());
            receipt.setUserId(user.getId());
            publishedTo.put(user, receipt);
        }
        return publishedTo;
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
    Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId, final Filter<? super User> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        return readPublishedTo(containerId, versionId, defaultUserComparator,
                filter); 
    }

    /**
     * Read a list of users the container version was shared with.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    Map<User, ArtifactReceipt> readSharedWith(final Long containerId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        return readSharedWith(containerId, versionId, defaultUserComparator,
                defaultUserFilter);
    }

    /**
     * Read a list of users the container version was shared with.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.\
     * @param comparator
     *            A <code>Comparator&lt;User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    Map<User, ArtifactReceipt> readSharedWith(final Long containerId,
            final Long versionId, final Comparator<User> comparator) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        return readSharedWith(containerId, versionId, comparator,
                defaultUserFilter);
    }

    /**
     * Read a list of users the container version was shared with.
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
    Map<User, ArtifactReceipt> readSharedWith(final Long containerId,
            final Long versionId, final Comparator<User> comparator,
            final Filter<? super User> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        final List<User> users = containerIO.readSharedWith(containerId, versionId);
        FilterManager.filter(users, filter);
        ModelSorter.sortUsers(users, comparator);
        final Map<User, ArtifactReceipt> sharedWith = new HashMap<User, ArtifactReceipt>(users.size(), 1.0F);
        for (final User user : users) {
            sharedWith.put(user, new ArtifactReceipt());
        }
        return sharedWith;
    }

    /**
     * Read a list of users the container version was shared with.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param filter
     *            A <code>Filter&lt;? super User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    Map<User, ArtifactReceipt> readSharedWith(final Long containerId,
            final Long versionId, final Filter<? super User> filter) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        return readSharedWith(containerId, versionId, defaultUserComparator,
                filter);
    }

    /**
     * Read the team for the container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of users.
     */
    List<TeamMember> readTeam(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
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
    List<ContainerVersion> readVersions(final Long containerId) {
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
    List<ContainerVersion> readVersions(final Long containerId,
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
    List<ContainerVersion> readVersions(final Long containerId,
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
    List<ContainerVersion> readVersions(final Long containerId,
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
    void removeBookmark(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            getInternalArtifactModel().removeFlagBookmark(containerId);
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
    void removeDocument(final Long containerId, final Long documentId) {
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
     * Rename the container.
     * 
     * @param containerId
     *            A container id.
     * @param name
     *            The new container name.
     */
    void rename(final Long containerId, final String name) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        logger.logVariable("name", name);
        assertIsNotDistributed("CONTAINER HAS BEEN DISTRIBUTED", containerId);
        containerIO.updateName(containerId, name);
        // fire event
        notifyContainerUpdated(read(containerId), localEventGenerator);
    }

    /**
     * Restore a container from an archive.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    void restore(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        try {
            final InternalArchiveModel archiveModel = getArchiveModel();
            // restore container info
            final Container container = archiveModel.readContainer(uniqueId);
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
                        final UUID uniqueId, final Long versionId,
                        final UUID documentUniqueId) {
                    return archiveModel.readDocumentVersions(uniqueId,
                            versionId, documentUniqueId);
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
    void restoreBackup() {
        logger.logApiId();
        try {
            final List<Container> containers = read();
            if (0 < containers.size()) {
                logger.logWarning("{0} containers will be deleted.", containers.size());
                for (final Container container : containers) {
                    delete(container.getId());
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
                            final UUID uniqueId, final Long versionId,
                            final UUID documentUniqueId) {
                        return backupModel.readDocumentVersions(uniqueId,
                                versionId, documentUniqueId);
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
     *            A document id.
     */
    void revertDocument(final Long containerId, final Long documentId) {
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
        getInternalDocumentModel().revertDraft(documentId);
    }

    /**
     * Search for containers.
     * 
     * @param expression
     *            A search expression <code>String</code>.
     * @return A <code>List&lt;Long&gt;</code>.
     */
    List<Long> search(final String expression) {
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
     * Subscribe to the container's team.
     * 
     * @param containerId
     *            A container id.
     */
    void subscribe(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        assertNotTeamMember("USER A TEAM MEMBER", containerId, localUserId());
        final InternalArtifactModel artifactModel = getInternalArtifactModel();
        final UUID containerUniqueId = artifactModel.readUniqueId(containerId);
        artifactModel.removeTeamMember(containerId, localUserId());
        getSessionModel().removeTeamMember(containerUniqueId, localUserId());
    }

    /**
     * Unsubscribe from the container's team.
     * 
     * @param containerId
     *            A container id.
     */
    void unsubscribe(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        assertTeamMember("USER NOT A TEAM MEMBER", containerId, localUserId());
        final InternalArtifactModel artifactModel = getInternalArtifactModel();
        final UUID containerUniqueId = artifactModel.readUniqueId(containerId);
        artifactModel.addTeamMember(containerId, localUserId());
        getSessionModel().addTeamMember(containerUniqueId, localUserId());
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
     * Create the container in the distributed network.
     * 
     * @param container
     *            The container.
     */
    private void createDistributed(final Container container) {
        final InternalSessionModel sessionModel = getSessionModel();
        sessionModel.createArtifact(localUserId(), container.getUniqueId());
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
     * Create the team for a container. The team will consist of the local user
     * only.
     * 
     * @param containerId
     *            A container id.
     * @return The created <code>TeamMember</code>.
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
        version.setCreatedBy(createdBy);
        version.setCreatedOn(createdOn);
        version.setName(container.getName());
        version.setUpdatedBy(version.getCreatedBy());
        version.setUpdatedOn(version.getCreatedOn());
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
    private void deleteLocal(final Long containerId) {
        // delete the draft
        final ContainerDraft draft = readDraft(containerId);
        if (null != draft) {
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
        for (final ContainerVersion version : versions) {
            documents = containerIO.readDocuments(version.getArtifactId(), version.getVersionId());

            // remove the version's artifact versions
            containerIO.removeVersions(containerId, version.getVersionId());

            for(final Document document : documents) {
                documentModel.delete(document.getId());
            }
            // delete the version
            containerIO.deleteVersion(containerId, version.getVersionId());

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
        if(null != draft) {
            return draft.getOwner().getId().equals(localUserId());
        }
        else { return Boolean.FALSE; }
    }

    /**
     * Publish a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     */
    private void doPublishVersion(final Long containerId, final Long versionId,
            final List<Contact> contacts, final List<TeamMember> teamMembers)
            throws IOException {
        final Container container = read(containerId);
        final ContainerVersion version = readVersion(containerId, versionId);

        // update local team
        final InternalArtifactModel artifactModel = getInternalArtifactModel();
        for (final Contact contact : contacts)
            artifactModel.addTeamMember(container.getId(), contact.getId());

        // build the publish to list then publish
        final List<JabberId> publishTo = new ArrayList<JabberId>();
        final List<User> publishToUsers = new ArrayList<User>();
        for (final Contact contact : contacts) {
            publishTo.add(contact.getId());
            publishToUsers.add(contact);
        }
        for (final TeamMember teamMember : teamMembers) {
            publishTo.add(teamMember.getId());
            publishToUsers.add(teamMember);
        }
        final Calendar currentDateTime = currentDateTime();
        publish(version, publishTo, localUserId(), currentDateTime);

        // update remote team
        final InternalSessionModel sessionModel = getSessionModel();
        for (final Contact contact : contacts)
            sessionModel.addTeamMember(container.getUniqueId(), contact.getId());

        // create published to list
        containerIO.createPublishedTo(containerId,
                version.getVersionId(), publishToUsers);
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
    private void export(final File exportDirectory, final Container container,
            final List<ContainerVersion> versions) throws IOException,
            TransformerException {
        final ContainerNameGenerator nameGenerator = getNameGenerator();
        final FileSystem exportFileSystem = new FileSystem(
                workspace.createTempDirectory(
                        nameGenerator.exportDirectoryName(container)));

        final InternalDocumentModel documentModel = getInternalDocumentModel();
        final DocumentNameGenerator documentNameGenerator = documentModel.getNameGenerator();
        final Map<ContainerVersion, User> versionsPublishedBy =
            new HashMap<ContainerVersion, User>(versions.size(), 1.0F);
        final Map<ContainerVersion, List<DocumentVersion>> documents =
            new HashMap<ContainerVersion, List<DocumentVersion>>(versions.size(), 1.0F);
        final Map<DocumentVersion, Long> documentsSize = new HashMap<DocumentVersion, Long>();
        final Map<ContainerVersion, Map<User, ArtifactReceipt>> publishedTo =
            new HashMap<ContainerVersion, Map<User, ArtifactReceipt>>(versions.size(), 1.0F);
        final Map<ContainerVersion, Map<User, ArtifactReceipt>> sharedWith =
            new HashMap<ContainerVersion, Map<User, ArtifactReceipt>>(versions.size(), 1.0F);
        InputStream stream;
        File directory, file;
        for (final ContainerVersion version : versions) {
            versionsPublishedBy.put(version, readUser(version.getUpdatedBy()));
            publishedTo.put(version, readPublishedTo(
                    version.getArtifactId(), version.getVersionId()));
            sharedWith.put(version, readSharedWith(
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
                versionsPublishedBy, documents, documentsSize, publishedTo,
                sharedWith);

        final File zipFile = new File(exportFileSystem.getRoot(), MessageFormat.format(
                "{0}.zip", container.getName()));
        ZipUtil.createZipFile(zipFile, exportFileSystem.getRoot());
        FileUtil.copy(zipFile, new File(exportDirectory, zipFile.getName()));
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
    private DocumentVersion handleDocumentPublished(final UUID uniqueId,
            final Long versionId, final String name, final String checksum,
            final String streamId, final JabberId publishedBy,
            final Calendar publishedOn) throws IOException {
        return getInternalDocumentModel().handleDocumentPublished(publishedBy,
                publishedOn, uniqueId, versionId, name, checksum, streamId);
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
    private Boolean isDistributed(final Long containerId) {
        return getInternalArtifactModel().doesVersionExist(containerId, Versioning.START);
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
     *            A container.
     * @param draft
     *            A container draft.
     * @param version
     *            A container version.
     * @param eventGenerator
     *            A container event generator.
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
    private void publish(final ContainerVersion version,
            final List<JabberId> publishTo, final JabberId publishedBy,
            final Calendar publishedOn) throws IOException {
        final Map<DocumentVersion, InputStream> documentVersionStreams =
            readDocumentVersionStreams(version.getArtifactId(),
                    version.getVersionId());
        final Map<DocumentVersion, String> documentVersionStreamIds =
            new HashMap<DocumentVersion, String>(documentVersionStreams.size(), 1.0F);
        final StreamSession session = getSessionModel().createStreamSession();
        for (final Entry<DocumentVersion, InputStream> entry :
                documentVersionStreams.entrySet()) {
            documentVersionStreamIds.put(entry.getKey(),
                    uploadStream(session, entry.getValue()));
        }
        getSessionModel().deleteStreamSession(session);

        getSessionModel().publish(version, documentVersionStreamIds,
                publishTo, publishedBy, publishedOn);
    }

    private Long readDocumentVersionSize(final Long documentId,
            final Long versionId) {
        return getInternalDocumentModel().readVersionSize(documentId, versionId);
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

    /**
     * Read a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    private User readUser(final JabberId userId) {
        return getInternalUserModel().read(userId);
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
        final InternalUserModel userModel = getInternalUserModel();

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
        List<Document> documents;
        List<DocumentVersion> documentVersions;
        InputStream documentVersionStream;
        for (final ContainerVersion version : versions) {
            userModel.readLazyCreate(version.getCreatedBy());
            userModel.readLazyCreate(version.getUpdatedBy());

            version.setArtifactId(container.getId());
            containerIO.createVersion(version);
            // restore version links
            documents = restoreModel.readDocuments(container.getUniqueId(), version.getVersionId());
            for (final Document document : documents) {
                userModel.readLazyCreate(document.getCreatedBy());
                userModel.readLazyCreate(document.getUpdatedBy());

                documentIO.create(document);
                artifactIO.createRemoteInfo(document.getId(),
                        document.getUpdatedBy(), document.getUpdatedOn());
                documentVersions = restoreModel.readDocumentVersions(container.getUniqueId(), version.getVersionId(), document.getUniqueId());
                for (final DocumentVersion documentVersion : documentVersions) {
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
                    getIndexModel().indexDocument(container.getId(), document.getId());

                    containerIO.addVersion(container.getId(),
                            version.getVersionId(), document.getId(),
                            documentVersion.getVersionId(),
                            document.getType());
                }
            }
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
                final Long versionId, final UUID documentUniqueId);

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
