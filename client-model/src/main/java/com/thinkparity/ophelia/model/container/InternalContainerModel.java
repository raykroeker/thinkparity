/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerPublishedEvent;

import com.thinkparity.ophelia.model.audit.event.AuditEvent;
import com.thinkparity.ophelia.model.events.ContainerEvent.Source;
import com.thinkparity.ophelia.model.util.ProcessMonitor;

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
            final JabberId createdBy, final Calendar createdOn);

    /**
     * Handle the remote draft deleted event.
     * 
     * @param event
     *            An <code>ArtifactDraftDeletedEvent</code>.
     */
    public void handleDraftDeleted(final ArtifactDraftDeletedEvent event);

    /**
     * Handle the container published remote event.
     * 
     * @param event
     *            A <code>ContainerPublishedEvent</code>.
     */
    public void handlePublished(final ContainerPublishedEvent event);

    /**
     * Handle the remove artifact received event.
     * 
     * @param event
     *            An <code>ArtifactReceivedEvent</code>.
     */
    public void handleReceived(final ArtifactReceivedEvent event);

    /**
     * Notify that a container has been flagged.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param source
     *            A container event <code>Source</code>.
     */
    public void notifyContainerFlagged(final Long containerId,
            final Source source);

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
     * Read a list of containers for a team member.
     * 
     * @param teamMemberId
     *            A team member id <code>Long</code>.
     * @return A <code>List</code> of <code>Container</code>s.
     */
    public List<Container> readForTeamMember(final Long teamMemberId);

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
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    public void restoreBackup(final ProcessMonitor monitor);
}
