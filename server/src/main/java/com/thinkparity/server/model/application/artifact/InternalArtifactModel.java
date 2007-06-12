/*
 * Created On:  26-Feb-07 10:10:27 AM
 */
package com.thinkparity.desdemona.model.artifact;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

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
     * Delete all drafts for the model user.
     * 
     */
    public void deleteDrafts(final Calendar deletedOn);

    public JabberId readDraftOwner(final UUID uniqueId);

    public List<TeamMember> readTeam(final Long artifactId);

    public List<UUID> readTeamArtifactIds(final User user);

    // TODO-javadoc InternalArtifactModel#addTeamMember()
    public void removeTeamMember(final JabberId userId, final Long artifactId,
            final Long teamMemberId);
}
