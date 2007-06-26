/*
 * Created On:  27-Apr-07 10:04:03 AM
 */
package com.thinkparity.ophelia.model.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionDelta;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;

import com.thinkparity.ophelia.model.DefaultDelegate;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler;
import com.thinkparity.ophelia.model.io.handler.ContainerIOHandler;

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

    /** An instance of a artifact persistence interface. */ 
    protected ArtifactIOHandler artifactIO;

    /** An instance of a container persistence interface. */ 
    protected ContainerIOHandler containerIO;

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
     * @see ContainerModelImpl#handleDocumentVersionsResolution(ContainerVersion,
     *      List, JabberId, Calendar)
     * 
     */
    protected final void handleDocumentVersionsResolution(
            final ContainerVersion containerVersion,
            final List<DocumentVersion> documentVersions,
            final JabberId publishedBy, final Calendar publishedOn) {
        modelImplementation.handleDocumentVersionsResolution(containerVersion,
                documentVersions, publishedBy, publishedOn);
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
     * @see ContainerModelImpl#read(Long)
     * 
     */
    protected final Container read(final Long containerId) {
        return modelImplementation.read(containerId);
    }

    /**
     * @see ContainerModelImpl#readDraft(Long)
     * 
     */
    protected final ContainerDraft readDraft(final Long containerId) {
        return modelImplementation.readDraft(containerId);
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
}
