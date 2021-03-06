/*
 * Mar 2, 2006
 */
package com.thinkparity.ophelia.model.artifact;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityConcurrency;
import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.concurrent.Lock;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;

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
     * Add a local team member.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @param user
     *            A <code>User</code>.
     * @return A <code>TeamMember</code>.
     */
    public TeamMember addTeamMember(final Artifact artifact, final User user);

    /**
     * Apply the archived flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public void applyFlagBookmark(final Long artifactId);

    /**
     * Apply the latest flag.
     * 
     * @param artifactId
     *            The artifact id.
     */
    public void applyFlagLatest(final Long artifactId);

    /**
     * Apply the seen flag to an artifact version.
     * 
     * @param version
     *            An <code>ArtifactVersion</code>.
     */
    public void applyFlagSeen(final ArtifactVersion version);

    /**
     * Apply the seen flag.
     * 
     * @param artifactId
     *            The artifact id.
     */
    public void applyFlagSeen(final Long artifactId);

    /**
     * Create the team. This will add the current user to the team.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The new team.
     */
    public List<TeamMember> createTeam(final Artifact artifact);

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
    @ThinkParityConcurrency(Lock.LOCAL_READ)
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
     * Determine if the artifact version exists.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @param versionId
     *            An artifact version id.
     * @return True if the artifact version exists.
     */
    public Boolean doesVersionExist(final UUID uniqueId, final Long versionId);

    /**
     * Handle the remote event generated when a draft is created.
     * 
     * @param event
     *            An <code>ArtifactDraftCreatedEvent</code> remote event.
     */
    public void handleDraftCreated(final ArtifactDraftCreatedEvent event);

    /**
     * Handle the remote event generated when a draft is deleted.
     * 
     * @param event
     *            An <code>ArtifactDraftDeleted</code> remote event.
     */
    public void handleDraftDeleted(final ArtifactDraftDeletedEvent event);

    /**
     * Handle an artifact received remote event.
     * 
     * @param event
     *            An <code>ArtifactReceivedEvent</code>.
     */
	public void handleReceived(final ArtifactReceivedEvent event);

    /**
     * Determine whether or not the seen flag is applied to any version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if any version is flagged as seen.
     */
    public Boolean isVersionFlagSeenApplied(final Artifact artifact);

    /**
     * Determine whether or not the seen flag is applied to all versions.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if all versions are flagged as seen.
     */
    public Boolean isVersionFlagSeenAppliedAll(final Artifact artifact);

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
    @ThinkParityConcurrency(Lock.LOCAL_READ)
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
     * Remove the bookmark flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public void removeFlagBookmark(final Long artifactId);

    /**
     * Remove the latest flag.
     * 
     * @param artifactId
     *            The artifact id.
     */
    public void removeFlagLatest(final Long artifactId);

    /**
     * Remove the seen flag from an artifact version.
     * 
     * @param version
     *            An <code>ArtifactVersion</code>.
     */
    public void removeFlagSeen(final ArtifactVersion version);

    /**
     * Remove the seen flag.
     * 
     * @param artifactId
     *            The artifact id.
     */
    public void removeFlagSeen(final Long artifactId);

    /**
     * Remove the team member. Removes the user from the local team data.
     * 
     * @param teamMember
     *            The team member.
     */
    public void removeTeamMember(final Long artifactId, final JabberId userId);

    /**
     * Handle the team member added remote event.
     * 
     * @param event
     *            An <code>ArtifactTeamMemberAddedEvent</code>.
     */
    void handleEvent(final ArtifactTeamMemberAddedEvent event);

    /**
     * Handle the team member removed remote event.
     * 
     * @param event
     *            An <code>ArtifactTeamMemberRemovedEvent</code>.
     */
    void handleEvent(final ArtifactTeamMemberRemovedEvent event);

    /**
     * Read the team for an artifact.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @return A <code>List<TeamMember></code>.
     */
    List<TeamMember> readTeam(Artifact artifact);
}
