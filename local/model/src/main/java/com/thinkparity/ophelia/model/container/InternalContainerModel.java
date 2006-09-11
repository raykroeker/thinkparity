/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.artifact.ArtifactType;


import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.InternalModel;
import com.thinkparity.ophelia.model.audit.event.AuditEvent;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Container Internal Model<br>
 * <b>Description:</b>A model internal interface to the container
 * implementation.<br>
 * 
 * @author CreateModel.groovy
 * @version 1.1.2.12
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
     */
    public void handleArtifactPublished(final UUID uniqueId,
            final Long versionId, final String name,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final String artifactName, final ArtifactType artifactType,
            final String artifactChecksum, final byte[] artifactBytes,
            final JabberId publishedBy, final Calendar publishedOn) {
        synchronized(getImplLock()) {
            getImpl().handleArtifactPublished(uniqueId, versionId,
                    name, artifactUniqueId, artifactVersionId,
                    artifactName, artifactType, artifactChecksum,
                    artifactBytes, publishedBy, publishedOn);
        }
    }

    /**
     * Handle the artifact sent remote event. If the container does not yet
     * exist it will be created; same goes for the version. The artifact will
     * then be passed off to the appropriate model then attached to the version.
     * 
     * @param uniqueId
     *            The container <code>UUID</code>
     * @param versionId
     *            The container version id <code>Long</code>.
     * @param name
     *            The container name <code>String</code>.
     * @param artifactUniqueId
     *            The artifact <code>UUID</code>.
     * @param artifactVersionId
     *            The artifact version id <code>Long</code>.
     * @param artifactName
     *            The artifact name <code>String</code>.
     * @param artifactType
     *            The artifact's <code>ArtifactType</code>.
     * @param artifactChecksum
     *            The artifact checksum <code>String</code>.
     * @param artifactBytes
     *            The artifact's bytes <code>byte[]</code>.
     * @param sentBy
     *            The sender <code>JabberId</code>.
     * @param sentOn
     *            The sent date <code>Calendar</code>.
     */
    public void handleArtifactSent(final UUID uniqueId,
            final Long versionId, final String name,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final String artifactName, final ArtifactType artifactType,
            final String artifactChecksum, final byte[] artifactBytes,
            final JabberId sentBy, final Calendar sentOn) {
        synchronized(getImplLock()) {
            getImpl().handleArtifactSent(uniqueId, versionId, name,
                    artifactUniqueId, artifactVersionId, artifactName,
                    artifactType, artifactChecksum, artifactBytes,
                    sentBy, sentOn);
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
     * Handle the container shared remote event.  All we're doing here is saving
     * the sent to list and firing an event.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param name
     *            A container name <code>String</code>.
     * @param artifactCount
     *            An artifact count <code>Integer</code>.
     * @param sentBy
     *            The sent by user's <code>JabberId</code>.
     * @param sentOn
     *            The sent date <code>Calendar</code>.
     * @param sentTo
     *            The sent to <code>List&lt;JabberId&gt;</code>.
     */
    public void handleSent(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final JabberId sentBy, final Calendar sentOn,
            final List<JabberId> sentTo) {
        synchronized (getImplLock()) {
            getImpl().handleSent(uniqueId, versionId, name, artifactCount,
                    sentBy, sentOn, sentTo);
        }
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
