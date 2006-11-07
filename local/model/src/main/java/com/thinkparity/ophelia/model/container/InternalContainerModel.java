/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.session.Environment;

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
public class InternalContainerModel extends ContainerModel implements
        InternalModel {

    /**
     * Create InternalContainerModel
     *
     * @param workspace
     *		A thinkParity workspace.
     * @param context
     *		A thinkParity internal context.
     */
    InternalContainerModel(final Context context,
            final Environment environment, final Workspace workspace) {
        super(environment, workspace);
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
     * @param artifactStreamId
     *            The stream id <code>String</code>.
     * @param publishedBy
     *            The publisher <code>JabberId</code>.
     * @param publishedOn
     *            The publish date <code>Calendar</code>.
     */
    public void handleArtifactPublished(final UUID uniqueId,
            final Long versionId, final String name,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final String artifactName, final ArtifactType artifactType,
            final String artifactChecksum, final String artifactStreamId,
            final JabberId publishedBy, final Calendar publishedOn) {
        synchronized(getImplLock()) {
            getImpl().handleArtifactPublished(uniqueId, versionId,
                    name, artifactUniqueId, artifactVersionId,
                    artifactName, artifactType, artifactChecksum,
                    artifactStreamId, publishedBy, publishedOn);
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

    public void handleReceived(final Long artifactId, final Long versionId,
            final JabberId receivedBy, final Calendar receivedOn) {
        synchronized (getImplLock()) {
            getImpl().handleReceived(artifactId, versionId, receivedBy, receivedOn);
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
     * Read a list of o
     * @param containerId
     * @param versionId
     * @return
     */
    public List<Document> readDocuments(final Long containerId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readDocuments(containerId, versionId);
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

    /**
     * Restore all containers from the backup.
     *
     */
    public void restoreBackup() {
        synchronized (getImplLock()) {
            getImpl().restoreBackup();
        }
    }
}
