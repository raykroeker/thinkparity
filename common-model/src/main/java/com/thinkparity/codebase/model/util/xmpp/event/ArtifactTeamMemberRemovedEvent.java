package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityBackupEvent;
import com.thinkparity.codebase.model.annotation.ThinkParityFilterEvent;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityBackupEvent
@ThinkParityFilterEvent
public final class ArtifactTeamMemberRemovedEvent extends XMPPEvent {

    /** The team member jabber id. */
    private JabberId jabberId;

    /** The artifact unique id. */
    private UUID uniqueId;

    /**
     * Create ArtifactTeamMemberRemovedEvent.
     *
     */
    public ArtifactTeamMemberRemovedEvent() {
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