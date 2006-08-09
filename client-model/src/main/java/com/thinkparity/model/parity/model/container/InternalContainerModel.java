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
            final Long containerId, final Long containerVersionId,
            final UUID uniqueId, final Long id, final Long versionId,
            final String name, final ArtifactType type, final byte[] bytes) {
        synchronized(getImplLock()) {
            getImpl().handleArtifactPublished(publishedBy, publishedOn,
                    containerUniqueId, containerVersionId, uniqueId, id,
                    versionId, name, type, bytes);
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
            final Long containerId, final Long containerVersionId,
            final String containerName, final UUID uniqueId, final Long id,
            final Long versionId, final String name, final ArtifactType type,
            final byte[] bytes) {
        synchronized(getImplLock()) {
            getImpl().handleArtifactSent(sentBy, sentOn, containerUniqueId,
                    containerVersionId, containerName, uniqueId, id, versionId,
                    name, type, bytes);
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
