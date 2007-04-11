package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityBackupEvent;

/**
 * <b>Title:</b>thinkparity XMPP Artifact Handle Team Member Added Query<br>
 * <b>Description:</b>Provides a wrapper for the team member added remote
 * event data.<br>
 */
@ThinkParityBackupEvent
public final class ArtifactTeamMemberAddedEvent extends XMPPEvent {

    /** The team member jabber id. */
    private JabberId jabberId;

    /** The artifact unique id. */
    private UUID uniqueId;

    /**
     * Create ArtifactTeamMemberAddedEvent.
     * 
     */
    public ArtifactTeamMemberAddedEvent() {
        super();
    }

    /**
     * Obtain jabberId.
     *
     * @return A JabberId.
     */
    public JabberId getJabberId() {
        return jabberId;
    }

    /**
     * Obtain uniqueId.
     *
     * @return A UUID.
     */
    public UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * Set jabberId.
     *
     * @param jabberId
     *		A JabberId.
     */
    public void setJabberId(final JabberId jabberId) {
        this.jabberId = jabberId;
    }

    /**
     * Set uniqueId.
     *
     * @param uniqueId
     *		A UUID.
     */
    public void setUniqueId(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
}