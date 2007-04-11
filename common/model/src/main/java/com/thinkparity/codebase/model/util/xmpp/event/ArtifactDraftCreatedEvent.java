package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityBackupEvent;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityBackupEvent
public final class ArtifactDraftCreatedEvent extends XMPPEvent {

    /** The creator. */
    private JabberId createdBy;

    /** The creation date. */
    private Calendar createdOn;

    /** The artifact unique id. */
    private UUID uniqueId;

    /**
     * Create ArtifactDraftCreatedEvent.
     *
     */
    public ArtifactDraftCreatedEvent() {
        super();
    }

    /**
     * Obtain createdBy.
     *
     * @return A JabberId.
     */
    public JabberId getCreatedBy() {
        return createdBy;
    }

    /**
     * Obtain createdOn.
     *
     * @return A Calendar.
     */
    public Calendar getCreatedOn() {
        return createdOn;
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
     * Set createdBy.
     *
     * @param createdBy
     *		A JabberId.
     */
    public void setCreatedBy(final JabberId createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Set createdOn.
     *
     * @param createdOn
     *		A Calendar.
     */
    public void setCreatedOn(final Calendar createdOn) {
        this.createdOn = createdOn;
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