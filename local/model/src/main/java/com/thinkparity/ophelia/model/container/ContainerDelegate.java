/*
 * Created On:  27-Apr-07 10:04:03 AM
 */
package com.thinkparity.ophelia.model.container;

import java.io.File;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerConstraints;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionDelta;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;

import com.thinkparity.ophelia.model.DefaultDelegate;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.document.DocumentFileLock;
import com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler;
import com.thinkparity.ophelia.model.io.handler.ContainerIOHandler;
import com.thinkparity.ophelia.model.io.handler.DocumentIOHandler;
import com.thinkparity.ophelia.model.util.ProcessMonitor;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.ContainerService;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Delegate<br>
 * <b>Description:</b>The abstraction of all container delegate implemenations.
 * It uses the package scope implementation of the container model to share
 * implementation. It also contains the persistence io instances for the
 * delegates.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ContainerDelegate extends
        DefaultDelegate<ContainerModelImpl> {

    /** An instance of an artifact persistence interface. */ 
    protected ArtifactIOHandler artifactIO;

    /** An instance of a container persistence interface. */ 
    protected ContainerIOHandler containerIO;

    /** An instance of a container web-service interface. */
    protected ContainerService containerService;

    /** An instance of a document persistence interface. */ 
    protected DocumentIOHandler documentIO;

    /**
     * Create ContainerDelegate.
     * 
     */
    protected ContainerDelegate() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.model.DefaultDelegate#initialize(com.thinkparity.ophelia.model.Model)
     * 
     */
    @Override
    public void initialize(final ContainerModelImpl modelImplementation) {
        super.initialize(modelImplementation);
        this.artifactIO = modelImplementation.getArtifactIO();
        this.containerIO = modelImplementation.getContainerIO();
        this.documentIO = modelImplementation.getDocumentIO();

        this.containerService = modelImplementation.getContainerService();
    }

    /**
     * @see ContainerModelImpl#calculateDelta(Container, ContainerVersion,
     *      ContainerVersion)
     * 
     */
    protected final ContainerVersionDelta calculateDelta(
            final Container container, final ContainerVersion compare,
            final ContainerVersion compareTo) {
        return modelImplementation.calculateDelta(container, compare, compareTo);
    }
    /**
     * @see ContainerModelImpl#createVersion(Long, Long, String, String,
     *      JabberId, Calendar)
     * 
     */
    protected final ContainerVersion createVersion(final Long containerId,
            final Long versionId, final String name, final String comment,
            final JabberId createdBy, final Calendar createdOn) {
        return modelImplementation.createVersion(containerId, versionId,
                name, comment, createdBy, createdOn);
    }

    /**
     * @see ContainerModelImpl#deleteDraft(Long)
     * 
     */
    protected final void deleteDraft(final Long containerId)
            throws CannotLockException {
        modelImplementation.deleteDraft(containerId);
    }

    /**
     * @see ContainerModelImpl#deleteLocal(Long, List, Map, Map)
     * 
     */
    protected final void deleteLocal(final Long containerId,
            final List<Document> documents,
            final Map<Document, DocumentFileLock> documentLocks,
            final Map<DocumentVersion, DocumentFileLock> documentVersionLocks) {
        modelImplementation.deleteLocal(containerId, documents, documentLocks,
                documentVersionLocks);
    }

    /**
     * @see ContainerModelImpl#doesExistDraft(Long)
     * 
     */
    protected final Boolean doesExistDraft(final Long containerId) {
        return modelImplementation.doesExistDraft(containerId);
    }

    /**
     * @see ContainerModelImpl#doesExistLocalDraft(Long)
     * 
     */
    protected final Boolean doesExistLocalDraft(final Long containerId) {
        return modelImplementation.doesExistLocalDraft(containerId);
    }

    /**
     * Obtain the web-service authentication token.
     * 
     * @return An <code>AuthToken</code>.
     */
    protected final AuthToken getAuthToken() {
        return getSessionModel().getAuthToken();
    }

    /**
     * @see ContainerModelImpl#getContainerConstraints()
     * 
     */
    protected final ContainerConstraints getContainerConstraints() {
        return modelImplementation.getContainerConstraints();
    }

    /**
     * @see ContainerModelImpl#handleDocumentVersionsResolution(ContainerVersion,
     *      List, Map, JabberId, Calendar)
     * 
     */
    protected final void handleDocumentVersionsResolution(
            final ContainerVersion containerVersion,
            final List<DocumentVersion> versions,
            final Map<DocumentVersion, File> versionFiles,
            final JabberId publishedBy, final Calendar publishedOn) {
        modelImplementation.handleDocumentVersionsResolution(containerVersion,
                versions, versionFiles, publishedBy, publishedOn);
    }

    /**
     * @see ContainerModelImpl#handleResolution(UUID, JabberId, Calendar,
     *      String)
     * 
     */
    protected final Container handleResolution(final UUID uniqueId,
            final JabberId publishedBy, final Calendar publishedOn,
            final String name) {
        return modelImplementation.handleResolution(uniqueId, publishedBy,
                publishedOn, name);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerModelImpl#handleTeamResolution(Container,
     *      List)
     * 
     */
    protected final List<TeamMember> handleTeamResolution(
            final Container container, final List<TeamMember> team) {
        return modelImplementation.handleTeamResolution(container, team);
    }

    /**
     * @see ContainerModelImpl#isDistributed(Long)
     * 
     */
    protected final Boolean isDistributed(final Long containerId) {
        return modelImplementation.isDistributed(containerId);
    }

    /**
     * @see ContainerModelImpl#isLocalDraftModified(Long)
     * 
     */
    protected final Boolean isLocalDraftModified(final Long containerId) {
        return modelImplementation.isLocalDraftModified(containerId);
    }

    /**
     * @see ContainerModelImpl#isLocalDraftSaved(Long)
     * 
     */
    protected final Boolean isLocalDraftSaved(final Long containerId) {
        return modelImplementation.isLocalDraftSaved(containerId);
    }

    /**
     * @see ContainerModelImpl#lockDocument(Document)
     * 
     */
    protected final DocumentFileLock lockDocument(final Document document)
            throws CannotLockException {
        return modelImplementation.lockDocument(document);
    }

    /**
     * @see ContainerModelImpl#lockDocuments(List)
     * 
     */
    protected final Map<Document, DocumentFileLock> lockDocuments(
            final List<Document> documents) throws CannotLockException {
        return modelImplementation.lockDocuments(documents);
    }

    /**
     * @see ContainerModelImpl#lockDocumentVersions(Document)
     * 
     */
    protected final Map<DocumentVersion, DocumentFileLock> lockDocumentVersions(
            final Document document) throws CannotLockException {
        return modelImplementation.lockDocumentVersions(document);
    }

    /**
     * @see ContainerModelImpl#lockDocumentVersions(List)
     * 
     */
    protected final Map<DocumentVersion, DocumentFileLock> lockDocumentVersions(
            final List<Document> documents) throws CannotLockException {
        return modelImplementation.lockDocumentVersions(documents);
    }

    /**
     * @see ContainerModelImpl#read()
     * 
     */
    protected final List<Container> read() {
        return modelImplementation.read();
    }

    /**
     * @see ContainerModelImpl#read(Long)
     * 
     */
    protected final Container read(final Long containerId) {
        return modelImplementation.read(containerId);
    }

    /**
     * @see ContainerModelImpl#readAllDocuments(Long)
     * 
     */
    protected final List<Document> readAllDocuments(final Long containerId) {
        return modelImplementation.readAllDocuments(containerId);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerModelImpl#readDocumentVersionDeltas(Long,
     *      Long)
     * 
     */
    protected final Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final Long containerId, final Long compareVersionId) {
        return modelImplementation.readDocumentVersionDeltas(containerId,
                compareVersionId);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerModelImpl#readDocumentVersionDeltas(Long,
     *      Long, Long)
     * 
     */
    protected final Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final Long containerId, final Long compareVersionId,
            final Long compareToVersionId) {
        return modelImplementation.readDocumentVersionDeltas(containerId,
                compareVersionId, compareToVersionId);
    }

    /**
     * @see ContainerModelImpl#readDocumentVersions(Long, Long, Comparator)
     * 
     */
    protected final List<DocumentVersion> readDocumentVersions(
            final Long containerId, final Long versionId,
            final Comparator<ArtifactVersion> comparator) {
        return modelImplementation.readDocumentVersions(containerId, versionId,
                comparator);
    }

    /**
     * @see ContainerModelImpl#readDraft(Long)
     * 
     */
    protected final ContainerDraft readDraft(final Long containerId) {
        return modelImplementation.readDraft(containerId);
    }

    /**
     * @see ContainerModelImpl#readLatestVersion(Long, Long)
     * 
     */
    protected final ContainerVersion readLatestVersion(final Long containerId) {
        return modelImplementation.readLatestVersion(containerId);
    }

    /**
     * @see ContainerModelImpl#readNextVersion(Long, Long)
     * 
     */
    protected final ContainerVersion readNextVersion(
            final Long containerId, final Long versionId) {
        return modelImplementation.readNextVersion(containerId, versionId);
    }

    /**
     * @see ContainerModelImpl#readPreviousVersion(Long, Long)
     * 
     */
    protected final ContainerVersion readPreviousVersion(
            final Long containerId, final Long versionId) {
        return modelImplementation.readPreviousVersion(containerId, versionId);
    }

    /**
     * @see ContainerModelImpl#readPublishedTo(Long, Long)
     * 
     */
    protected final List<ArtifactReceipt> readPublishedTo(final ContainerVersion version) {
        return modelImplementation.readPublishedTo(version.getArtifactId(),
                version.getVersionId());
    }

    /**
     * @see ContainerModelImpl#readTeam(Long)
     * 
     */
    protected final List<TeamMember> readTeam(final Long containerId) {
        return modelImplementation.readTeam(containerId);
    }

    /**
     * @see ContainerModelImpl#readVersion(Long, Long)
     * 
     */
    protected final ContainerVersion readVersion(final Long containerId,
            final Long versionId) {
        return modelImplementation.readVersion(containerId, versionId);
    }

    /**
     * @see ContainerModelImpl#readVersions(Long)
     * 
     */
    protected final List<ContainerVersion> readVersions(
            final Container container) {
        return modelImplementation.readVersions(container.getId());
    }

    /**
     * @see ContainerModelImpl#releaseLock(DocumentFileLock)
     * 
     */
    protected final void releaseLock(final DocumentFileLock lock) {
        modelImplementation.releaseLock(lock);
    }

    /**
     * @see ContainerModelImpl#releaseLocks(Iterable)
     * 
     */
    protected final void releaseLocks(final Iterable<DocumentFileLock> locks) {
        modelImplementation.releaseLocks(locks);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerModelImpl#uploadDocumentVersions(ProcessMonitor,
     *      List, Map)
     * 
     */
    protected final void uploadDocumentVersions(final ProcessMonitor monitor,
            final List<DocumentVersion> versions,
            final Map<DocumentVersion, Delta> deltas) {
        modelImplementation.uploadDocumentVersions(monitor, versions, deltas);
    }
}
