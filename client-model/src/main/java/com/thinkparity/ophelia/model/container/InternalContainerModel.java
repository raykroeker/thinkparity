/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerPublishedEvent;

import com.thinkparity.ophelia.model.audit.event.AuditEvent;

/**
 * <b>Title:</b>thinkParity Container Internal Model<br>
 * <b>Description:</b>A model internal interface to the container
 * implementation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.19
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalContainerModel extends ContainerModel {

    /**
     * Determine whether or not a draft exists.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if a draft exists.
     */
    public Boolean doesExistDraft(final Long containerId);

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
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void handleArtifactPublished(final ContainerArtifactPublishedEvent event);

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
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void handleDraftCreated(final Long containerId,
            final JabberId createdBy, final Calendar createdOn);

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
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void handleDraftDeleted(final ArtifactDraftDeletedEvent event);

    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void handlePublished(final ArtifactPublishedEvent event);

    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void handlePublished(final ContainerPublishedEvent event);

    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void handleReceived(final ArtifactReceivedEvent event);

    /**
     * Read the list of audit events for a container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of audit events.
     */
    public List<AuditEvent> readAuditEvents(final Long containerId);

    /**
     * Read a list of of documents for a container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>List</code> of <code>Document</code>s.
     */
    public List<Document> readDocuments(final Long containerId, final Long versionId);

    /**
     * Read the container draft.
     * 
     * @param containerId
     *            A container id.
     * @return A container draft.
     */
    public ContainerDraft readDraft(final Long containerId);

    /**
     * Read the list of published to artifact receipts.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List</code> of <code>ArtifactReceipt</code>s.
     */
    public List<ArtifactReceipt> readPublishedTo2(final Long containerId,
            final Long versionId);

    /**
     * Restore all containers from the backup.
     *
     */
    public void restoreBackup();
}
