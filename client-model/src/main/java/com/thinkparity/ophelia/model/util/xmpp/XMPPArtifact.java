/*
 * Mar 1, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.model.Constants.Xml;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.smack.SmackException;
import com.thinkparity.ophelia.model.util.xmpp.event.ArtifactListener;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
final class XMPPArtifact extends AbstractXMPP<ArtifactListener> {

    /**
	 * Create a XMPPArtifact.
	 * 
	 * @param xmppCore
	 *            The xmpp core functionality.
	 */
	XMPPArtifact(final XMPPCore xmppCore) {
		super(xmppCore);
	}

    /**
     * Add a team member. This will create the team member relationship in the
     * distributed network with a pending state.
     * 
     * @param artifactId
     *            An artifact id.
     * @param jabberId
     *            A jabber id.
     * @throws SmackException
     */
    void addTeamMember(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final JabberId teamMemberId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("team", team);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("teamMemberId", teamMemberId);
        final XMPPMethod addTeamMember = new XMPPMethod("artifact:addteammember");
        addTeamMember.setParameter("userId", userId);
        addTeamMember.setParameter("team", "teamMember", team);
        addTeamMember.setParameter("uniqueId", uniqueId);
        addTeamMember.setParameter("teamMemberId", teamMemberId);
        execute(addTeamMember);
    }

	/**
     * Confirm artifact receipt.
     * 
     * @param receivedFrom
     *            From whom the artifact was received.
     * @param uniqueId
     *            The artifact unique id.
     * @param versionId
     *            The artifact version id.
     */
	void confirmReceipt(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final Long versionId,
            final JabberId receivedBy, final Calendar receivedOn) {
	    logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("team", team);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("receivedBy", receivedBy);
        logger.logVariable("receivedOn", receivedOn);
        final XMPPMethod confirmReceipt = new XMPPMethod("artifact:confirmreceipt");
        confirmReceipt.setParameter("userId", userId);
        confirmReceipt.setParameter("team", "teamMember", team);
        confirmReceipt.setParameter("uniqueId", uniqueId);
        confirmReceipt.setParameter("versionId", versionId);
        confirmReceipt.setParameter("receivedBy", receivedBy);
        confirmReceipt.setParameter("receivedOn", receivedOn);
        execute(confirmReceipt);
	}

    /**
     * Create an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
	void create(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod create = new XMPPMethod("artifact:create");
        create.setParameter("userId", userId);
        create.setParameter("uniqueId", uniqueId);
        execute(create);
    }

	/**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    void createDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("team", team);
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod createDraft = new XMPPMethod("artifact:createdraft");
        createDraft.setParameter("userId", userId);
        createDraft.setParameter("team", "teamMember", team);
        createDraft.setParameter("uniqueId", uniqueId);
        execute(createDraft);
    }

    /**
     * Delete an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    void delete(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod delete = new XMPPMethod("artifact:delete");
        delete.setParameter(Xml.Artifact.UNIQUE_ID, uniqueId);
        execute(delete);
    }

    /**
     * Delete an artifact draft.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    void deleteDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("team", team);
        logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod deleteDraft = new XMPPMethod("artifact:deletedraft");
        deleteDraft.setParameter("userId", userId);
        deleteDraft.setParameter("team", "teamMember", team);
        deleteDraft.setParameter("uniqueId", uniqueId);
        execute(deleteDraft);
    }

	/**
     * Read the artifact key holder.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return A jabber id.
     */
    JabberId readKeyHolder(final JabberId userId, final UUID uniqueId) {
        final XMPPMethod method = new XMPPMethod("artifact:readkeyholder");
        method.setParameter("userId", userId);
        method.setParameter("uniqueId", uniqueId);
        return execute(method, Boolean.TRUE).readResultJabberId("keyHolder");
    }

    /**
     * Read the artifact team member ids.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
	List<JabberId> readTeamIds(final UUID uniqueId) {
		logger.logApiId();
		logger.logVariable("uniqueId", uniqueId);
        final XMPPMethod readTeam = new XMPPMethod("artifact:readteamids");
        readTeam.setParameter("uniqueId", uniqueId);
        final XMPPMethodResponse response = execute(readTeam, Boolean.TRUE);
        return response.readResultJabberIds("teamIds");
	}

	/**
     * Remove a team member from the artifact team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    void removeTeamMember(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final JabberId teamMemberId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("team", team);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("teamMemberId", teamMemberId);
        final XMPPMethod removeTeamMember = new XMPPMethod("artifact:removeteammember");
        removeTeamMember.setParameter("userId", userId);
        removeTeamMember.setParameter("team", "teamMember", team);
        removeTeamMember.setParameter("uniqueId", uniqueId);
        removeTeamMember.setParameter("teamMemberId", teamMemberId);
        execute(removeTeamMember);
    }
}
