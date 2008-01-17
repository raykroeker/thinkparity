/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.DraftExistsException;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedNotificationEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedNotificationEvent;

import com.thinkparity.ophelia.model.container.event.LocalPublishedEvent;
import com.thinkparity.ophelia.model.container.event.LocalVersionPublishedEvent;
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
     * Delete the published to reference for the invitation.
     * 
     * @param publishedTo
     *            A published to <code>EMail</code> e-mail address.
     */
    public void deletePublishedTo(final EMail publishedTo);

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
     *
     * @throws DraftExistsException if a local draft exists
     */
    public void handleDraftCreated(final Long containerId,
            final JabberId createdBy, final Calendar createdOn)
            throws DraftExistsException;

    /**
     * Handle the remote draft deleted event.
     * 
     * @param event
     *            An <code>ArtifactDraftDeletedEvent</code>.
     */
    public void handleDraftDeleted(final ArtifactDraftDeletedEvent event);

    /**
     * Handle the remote container published event.
     * 
     * @param event
     *            A <code>LocalPublishedEvent</code>.
     */
    public void handleEvent(final LocalPublishedEvent event);

    /**
     * Handle the remote container version published event.
     * 
     * @param event
     *            A <code>LocalVersionPublishedEvent</code>.
     */
    public void handleEvent(final LocalVersionPublishedEvent event);

    /**
     * Handle the remote container published notification event.
     * 
     * @param event
     *            A <code>PublishedNotificationEvent</code>.
     */
    public void handleEvent(final PublishedNotificationEvent event);

    /**
     * Handle the remote container version published notification event.
     * 
     * @param event
     *            A <code>VersionPublishedNotificationEvent</code>.
     */
    public void handleEvent(final VersionPublishedNotificationEvent event);

    /**
     * Handle the remove artifact received event.
     * 
     * @param event
     *            An <code>ArtifactReceivedEvent</code>.
     */
    public void handleReceived(final ArtifactReceivedEvent event);

    /**
     * Notify the container listenter a team member has been added.
     * 
     * @param teamMember
     *            A <code>TeamMember</code>.
     * @deprecated the event should really be tied to the container
     */
    @Deprecated
    @ThinkParityTransaction(TransactionType.SUPPORTED)
    public void notifyTeamMemberAdded(final TeamMember teamMember);

    /**
     * Notify the container listenter a team member has been removed.
     * 
     * @param teamMember
     *            A <code>TeamMember</code>.
     * @deprecated the event should really be tied to the container
     */
    @Deprecated
    @ThinkParityTransaction(TransactionType.SUPPORTED)
    public void notifyTeamMemberRemoved(final TeamMember teamMember);

    /**
     * Publish a welcome container.
     * 
     */
    public void publishWelcome();
    
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
     * Read the containers for a document.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @return A <code>List</code> of <code>Container</code>s.
     */
    public List<Container> readForDocument(final Long documentId);

    /**
     * Read a list of containers for a team member.
     * 
     * @param teamMemberId
     *            A team member id <code>Long</code>.
     * @return A <code>List</code> of <code>Container</code>s.
     */
    public List<Container> readForTeamMember(final Long teamMemberId);

    /**
     * Read the published to list of users.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List</code> of <code>User</code>s.
     */
    public List<User> readPublishedToUsers(final Long containerId,
            final Long versionId);

    /**
     * Delete local container data.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    void deleteLocal(final ProcessMonitor monitor);

    /**
     * Restore local container data from the backup.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    void restoreLocal(final ProcessMonitor monitor);
}
