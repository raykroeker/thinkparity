package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.Calendar;
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
public final class ArtifactDraftDeletedEvent extends XMPPEvent {

    /** The creator. */
    private JabberId deletedBy;

    /** The creation date. */
    private Calendar deletedOn;

    /** The artifact unique id. */
    private UUID uniqueId;

    /**
     * Create ArtifactDraftDeletedEvent.
     * 
     */
    public ArtifactDraftDeletedEvent() {
        super();
    }

    /**
     * Obtain deletedBy.
     *
     * @return A JabberId.
     */
    public JabberId getDeletedBy() {
        return deletedBy;
    }

    /**
     * Obtain deletedOn.
     *
     * @return A Calendar.
     */
    public Calendar getDeletedOn() {
        return deletedOn;
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
     * Set deletedBy.
     *
     * @param deletedBy
     *		A JabberId.
     */
    public void setDeletedBy(final JabberId deletedBy) {
        this.deletedBy = deletedBy;
    }

    /**
     * Set deletedOn.
     *
     * @param deletedOn
     *		A Calendar.
     */
    public void setDeletedOn(final Calendar deletedOn) {
        this.deletedOn = deletedOn;
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