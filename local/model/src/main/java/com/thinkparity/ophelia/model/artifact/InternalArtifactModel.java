/*
 * Mar 2, 2006
 */
package com.thinkparity.ophelia.model.artifact;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;

import com.thinkparity.ophelia.model.ParityException;

/**
 * <b>Title:</b>thinkParity Internal Artifact Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.17
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalArtifactModel extends ArtifactModel {

	/**
     * Add the team member. Add the user to the local team data in a pending
     * state; and call the server's add team member api.
     * 
     * @param artifactId
     *            The artifact id.
     * @param userId
     *            The user id.
     * @throws ParityException
     */
    public TeamMember addTeamMember(final Long artifactId, final JabberId userId);

    /**
     * Apply the archived flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public void applyFlagArchived(final Long artifactId);

    /**
     * Apply the archived flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public void applyFlagBookmark(final Long artifactId);

    /**
     * Apply the key flag.
     * 
     * @param artifactId
     *            The artifact id.
     */
    public void applyFlagKey(final Long artifactId);

    /**
     * Apply the key flag.
     * 
     * @param artifactId
     *            The artifact id.
     */
    public void applyFlagLatest(final Long artifactId);

    /**
     * Create the artifact's remote info.
     * 
     * @param artifactId
     *            The artifact id.
     * @param updatedBy
     *            The remote user to update the artifact.
     * @param updatedOn
     *            The last time the artifact was updated.
     */
	public void createRemoteInfo(final Long artifactId,
			final JabberId updatedBy, final Calendar updatedOn);

    /**
     * Create the team. This will add the current user to the team.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The new team.
     */
    public List<TeamMember> createTeam(final Long artifactId);

	/**
     * Delete the artifact's remote info.
     * 
     * @param artifactId
     *            The artifact id.
     */
	public void deleteRemoteInfo(final Long artifactId);

	/**
     * Delete the team in its entirety.
     *
     * @param artifactId
     *      An artifact id.
     * @see InternalArtifactModel#addTeamMember(java.lang.Long,com.thinkparity.codebase.jabber.JabberId)
     * @see InternalArtifactModel#removeTeamMember(java.lang.Long,com.thinkparity.codebase.jabber.JabberId)
     */
    public void deleteTeam(final Long artifactId);

    /**
     * Determine if an artifact exists.
     * 
     * @param artifactId
     *            The artifact id.
     * @return True if the artifact exists; false otherwise.
     */
    public Boolean doesExist(final Long artifactId);

    /**
     * Determine if an artifact exists.
     * 
     * @param uniqueId
     *            The unique id.
     * @return True if the artifact exists; false otherwise.
     */
    public Boolean doesExist(final UUID uniqueId);

    /**
     * Determine if an artifact version exists.
     * 
     * @param artifactId
     *            An artifact id.
     * @return True if the artifact version exists.
     */
    public Boolean doesVersionExist(final Long artifactId);

    /**
     * Determine if the artifact version exists.
     * 
     * @param artifactId
     *            An artifact id.
     * @param versionId
     *            An artifact version id.
     * @return True if the artifact version exists.
     */
    public Boolean doesVersionExist(final Long artifactId, final Long versionId);

    /**
     * Handle the remote event generated when a draft is created.
     * 
     * @param event
     *            An <code>ArtifactDraftCreatedEvent</code> remote event.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void handleDraftCreated(final ArtifactDraftCreatedEvent event);

    /**
     * Handle the remote event generated when a draft is deleted.
     * 
     * @param event
     *            An <code>ArtifactDraftDeleted</code> remote event.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void handleDraftDeleted(final ArtifactDraftDeletedEvent event);

    /**
     * Handle an artifact published remote event.
     * 
     * @param event
     *            An <code>ArtifactPublishedEvent</code>.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void handlePublished(final ArtifactPublishedEvent event);

    /**
     * Handle an artifact received remote event.
     * 
     * @param event
     *            An <code>ArtifactReceivedEvent</code>.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
	public void handleReceived(final ArtifactReceivedEvent event);

    /**
     * Handle the remote event generated when a team member is added. This will
     * download the user's info if required and create the team data locally.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @param jabberId
     *            The user's jabber id.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void handleTeamMemberAdded(final ArtifactTeamMemberAddedEvent event);
    
    /**
     * Handle the team member removed remote event.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void handleTeamMemberRemoved(
            final ArtifactTeamMemberRemovedEvent event);

    /**
     * Read the earliest version id for an artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @return A version id.
     */
    public Long readEarliestVersionId(final Long artifactId);

    /**
     * Read the artifact id.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return The artifact id.
     */
    public Long readId(final UUID uniqueId);

    /**
     * Read the latest version id for an artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @return A version id.
     */
    public Long readLatestVersionId(final Long artifactId);

    /**
     * Read the team for an artifact.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param comparator
     *            An artifact sort <code>Comparator</code>.
     * @param filter
     *            An artifact <code>Filter</code>.
     * @return A <code>TeamMember</code> <code>List</code>.
     */
    public List<TeamMember> readTeam(final Long artifactId,
            final Comparator<? super User> comparator,
            final Filter<? super User> filter);

    /**
     * Read the artifact team.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public List<TeamMember> readTeam2(final Long artifactId);

    /**
     * Read the artifact team.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public List<JabberId> readTeamIds(final Long artifactId);

    /**
     * Read the artifact unique id.
     * 
     * @param artifactId
     *            An artifact id.
     * @return An artifact unique id.
     */
    public UUID readUniqueId(final Long artifactId);

    /**
     * Remove the archived flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public void removeFlagArchived(final Long artifactId);

    /**
     * Remove the bookmark flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public void removeFlagBookmark(final Long artifactId);

    /**
     * Remove the key flag.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     */
    public void removeFlagKey(final Long artifactId);
    
    /**
     * Remove the team member. Removes the user from the local team data.
     * 
     * @param teamMember
     *            The team member.
     */
    public void removeTeamMember(final Long artifactId, final JabberId userId);

    /**
     * Update the artifact's remote info.
     * 
     * @param artifactId
     *            The artifact id.
     * @param updatedBy
     *            The last user to update the artifact.
     * @param updatedOn
     *            The last time the artifact was updated.
     */
	public void updateRemoteInfo(final Long artifactId,
			final JabberId updatedBy, final Calendar updatedOn);

    /**
     * Update an artifact's state.
     * 
     * @param artifactId
     *            An artifact id.
     * @param state
     *            The artifact state.
     */
	public void updateState(final Long artifactId, final ArtifactState state);
}
