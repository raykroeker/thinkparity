/*
 * Created On:  26-Feb-07 10:10:27 AM
 */
package com.thinkparity.desdemona.model.artifact;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Internal Artifact Model<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public class InternalArtifactModel extends ArtifactModel {

    /**
     * Create InternalArtifactModel.
     *
     */
    InternalArtifactModel(final Context context) {
        super();
    }

    /**
     * Create InternalArtifactModel.
     *
     */
    InternalArtifactModel(final Context context, final Session session) {
        super(session);
    }

    // TODO-javadoc InternalArtifactModel#addTeamMember()
    public void addTeamMember(final JabberId userId, final Long artifactId,
            final Long teamMemberId) {
        synchronized (getImplLock()) {
            getImpl().addTeamMember(userId, artifactId, teamMemberId);
        }
    }

    /**
     * Delete all drafts for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    public void deleteDrafts(final JabberId userId, final Calendar deletedOn) {
        synchronized (getImplLock()) {
            getImpl().deleteDrafts(userId, deletedOn);
        }
    }

    public JabberId readDraftOwner(final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readDraftOwner(uniqueId);
        }
    }

    // TODO-javadoc InternalArtifactModel#readTeam()
    public List<TeamMember> readTeam(final JabberId userId,
            final Long artifactId) {
        synchronized (getImplLock()) {
            return getImpl().readTeam(userId, artifactId);
        }
    }

    public List<UUID> readTeamArtifactIds(final User user) {
        synchronized (getImplLock()) {
            return getImpl().readTeamArtifactIds(user);
        }
    }

    // TODO-javadoc InternalArtifactModel#addTeamMember()
    public void removeTeamMember(final JabberId userId, final Long artifactId,
            final Long teamMemberId) {
        synchronized (getImplLock()) {
            getImpl().removeTeamMember(userId, artifactId, teamMemberId);
        }
    }
}
