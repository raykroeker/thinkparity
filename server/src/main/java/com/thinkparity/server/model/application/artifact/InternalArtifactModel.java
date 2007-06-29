/*
 * Created On:  26-Feb-07 10:10:27 AM
 */
package com.thinkparity.desdemona.model.artifact;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Internal Artifact Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalArtifactModel extends ArtifactModel {

    /**
     * Add the model user to the artifact team.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     */
    public void addTeamMember(final Artifact artifact);

    /**
     * Add the users to the artifact team.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @param users
     *            A <code>List<User></code>.
     */
    public void addTeamMembers(final Artifact artifact, final List<User> users);

    /**
     * Create an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    public Artifact create(final UUID uniqueId, final Calendar createdOn);

    /**
     * Delete a draft.  The draft ownership is reverted back to the system user;
     * and all team members are sent a "draft deleted" event.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @param deletedOn
     *            The deleted on <code>Calendar</code>.
     */
    public void deleteDraft(final Artifact artifact, final Calendar deletedOn);

    /**
     * Delete all drafts for the model user.
     * 
     */
    public void deleteDrafts(final Calendar deletedOn);

    /**
     * Determine whether or not an artifact exists.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return True if the artifact exists.
     */
    public Boolean doesExist(final UUID uniqueId);

    /**
     * Determine whether or not the model user is the draft owner.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @return True if the model user is the draft owner.
     */
    public Boolean isDraftOwner(final Artifact artifact);

    /**
     * Determine if the model user is a team member.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @return True if the user is a team member.
     */
    public Boolean isTeamMember(final Artifact artifact);

    /**
     * Obtain a handle to an artifact for a given artifact unique id.
     * 
     * @param artifactUniqueId
     *            An artifact unique id.
     */
    public Artifact read(final UUID artifactUniqueId);

    public JabberId readDraftOwner(final UUID uniqueId);

    /**
     * Read the latest version id.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     */
    public Long readLatestVersionId(final Artifact artifact);

    public List<TeamMember> readTeam(final Long artifactId);

    public List<UUID> readTeamArtifactIds(final User user);

    /**
     * Remove the model user from the team.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     */
    public void removeTeamMember(final Artifact artifact);

    /**
     * Update the latest version reference.
     * 
     * @param version
     *            An <code>ArtifactVersion</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param updatedOn
     *            A updated on <code>Calendar</code>.
     */
    public void updateLatestVersionId(final Artifact artifact,
            final Long versionId, final Calendar updatedOn);
}
