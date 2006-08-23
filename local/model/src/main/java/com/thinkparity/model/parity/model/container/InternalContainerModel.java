/*
 * Generated On: Jun 27 06 12:13:12 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.audit.event.AuditEvent;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;

/**
 * <b>Title:</b>thinkParity Container Internal Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version $Revision$
 */
public class InternalContainerModel extends ContainerModel implements InternalModel {

    /**
     * Create InternalContainerModel
     *
     * @param workspace
     *		A thinkParity workspace.
     * @param context
     *		A thinkParity internal context.
     */
    InternalContainerModel(final Workspace workspace, final Context context) {
        super(workspace);
        context.assertContextIsValid();
    }

    /**
     * Create a new version for a container.
     * 
     * @param containerId
     *            A container id.
     * @return A new container version.
     */
    public ContainerVersion createVersion(final Long containerId) {
        synchronized(getImplLock()) { return getImpl().createVersion(containerId); }
    }

    /**
     * Handle the artifact sent event for the container.
     * 
     * @param sentBy
     *            Who sent the artifact.
     * @param sentOn
     *            When the artifact was sent.
     * @param containerUniqueId
     *            The container unique id.
     * @param containerVersionId
     *            The container version id.
     * @param count
     *            The artifact count.
     * @param index
     *            The artifact index.
     * @param uniqueId
     *            The artifact unique id.
     * @param versionId
     *            The artifact version id.
     * @param type
     *            The artifact type.
     * @param bytes
     *            The artifact bytes.
     */
    public void handleArtifactPublished(final JabberId publishedBy,
            final Calendar publishedOn, final UUID containerUniqueId,
            final Long containerVersionId, final String containerName,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final String artifactName, final ArtifactType artifactType,
            final String artifactChecksum, final byte[] artifactBytes) {
        synchronized(getImplLock()) {
            getImpl().handleArtifactPublished(publishedBy, publishedOn,
                    containerUniqueId, containerVersionId, containerName,
                    artifactUniqueId, artifactVersionId, artifactName,
                    artifactType, artifactChecksum, artifactBytes);
        }
    }

    /**
     * Handle the artifact sent event for the container.
     * 
     * @param sentBy
     *            Who sent the artifact.
     * @param sentOn
     *            When the artifact was sent.
     * @param containerUniqueId
     *            The container unique id.
     * @param containerVersionId
     *            The container version id.
     * @param count
     *            The artifact count.
     * @param index
     *            The artifact index.
     * @param uniqueId
     *            The artifact unique id.
     * @param versionId
     *            The artifact version id.
     * @param type
     *            The artifact type.
     * @param bytes
     *            The artifact bytes.
     */
    public void handleArtifactSent(final JabberId sentBy,
            final Calendar sentOn, final UUID containerUniqueId,
            final Long containerVersionId, final String containerName,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final String artifactName, final ArtifactType artifactType,
            final String artifactChecksum, final byte[] artifactBytes) {
        synchronized(getImplLock()) {
            getImpl().handleArtifactSent(sentBy, sentOn, containerUniqueId,
                    containerVersionId, containerName, artifactUniqueId,
                    artifactVersionId, artifactName, artifactType,
                    artifactChecksum, artifactBytes);
        }
    }

    /**
     * Handle the remote draft created event.
     * 
     * @param containerId
     *            A container id.
     * @param deletedBy
     *            Who created the draft.
     * @param deletedOn
     *            When the draft was created.
     */
    public void handleDraftCreated(final Long containerId,
            final JabberId createdBy, final Calendar createdOn) {
        synchronized (getImplLock()) {
            getImpl().handleDraftCreated(containerId, createdBy, createdOn);
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
    public void handleDraftDeleted(final Long containerId,
            final JabberId deletedBy, final Calendar deletedOn) {
        synchronized (getImplLock()) {
            getImpl().handleDraftDeleted(containerId, deletedBy, deletedOn);
        }
    }

    public void handlePublished(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final JabberId publishedBy, final List<JabberId> publishedTo,
            final Calendar publishedOn) {
        synchronized (getImplLock()) {
            getImpl().handlePublished(uniqueId, versionId, name, artifactCount,
                    publishedBy, publishedTo, publishedOn);
        }
    }

    /**
     * Determine if the container has been locally modified.
     * 
     * @param containerId
     * @return True if the container has been locally modified.
     */
    public Boolean isLocallyModified(final Long containerId)
            throws ParityException {
        synchronized(getImplLock()) {
            return getImpl().isLocallyModified(containerId);
        }
    }

    /**
     * Lock the container.
     * 
     * @param containerId
     *            The container id.
     */
    public void lock(final Long containerId) throws ParityException {
        synchronized(getImplLock()) { getImpl().lock(containerId); }
    }

    /**
     * Read the list of audit events for a container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of audit events.
     */
    public List<AuditEvent> readAuditEvents(final Long containerId) {
        synchronized(getImplLock()) {
            return getImpl().readAuditEvents(containerId);
        }
    }

    /**
     * Read the container draft.
     * 
     * @param containerId
     *            A container id.
     * @return A container draft.
     */
    public ContainerDraft readDraft(final Long containerId) {
        synchronized(getImplLock()) { return getImpl().readDraft(containerId); }
    }
}
