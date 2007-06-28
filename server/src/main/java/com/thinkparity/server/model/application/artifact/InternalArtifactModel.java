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
     * @param artifactId
     *            An artifact id <code>Long</code>.
     */
    public void addTeamMember(final Long artifactId);

    /**
     * Delete a draft.
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
     * Determine whether or not a draft exists for the artifact.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @return True if the draft exists.
     */
    public Boolean doesExistDraft(final Artifact artifact);

    /**
     * Determine if the model user is a team member.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @return True if the user is a team member.
     */
    public Boolean isTeamMember(final Artifact artifact);

    public JabberId readDraftOwner(final UUID uniqueId);

    public List<TeamMember> readTeam(final Long artifactId);

    public List<UUID> readTeamArtifactIds(final User user);

    /**
     * Remove the model user from the team.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     */
    public void removeTeamMember(final Artifact artifact);

    // TODO-javadoc InternalArtifactModel#addTeamMember()
    public void removeTeamMember(final JabberId userId, final Long artifactId,
            final Long teamMemberId);
}
