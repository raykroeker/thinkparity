/*
 * Created On:  10-Nov-06 2:51:36 PM
 */
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
public final class ArtifactReceivedEvent extends XMPPEvent {

    /** When the artifact was published. */
    private Calendar publishedOn;

    /** Who is confirming receipt. */
    private JabberId receivedBy;

    /** The received on date <code>Calendar</code>. */
    private Calendar receivedOn;

    /** The artifact unique id. */
    private UUID uniqueId;

    /** The artifact version id. */
    private Long versionId;

    /**
     * Create ArtifactReceivedEvent.
     *
     */
    public ArtifactReceivedEvent() {
        super();
    }

    /**
     * Obtain receivedBy.
     *
     * @return A JabberId.
     */
    public JabberId getReceivedBy() {
        return receivedBy;
    }

    /**
     * Obtain receivedOn.
     *
     * @return A Calendar.
     */
    public Calendar getReceivedOn() {
        return receivedOn;
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
     * Obtain versionId.
     *
     * @return A Long.
     */
    public Long getVersionId() {
        return versionId;
    }

    /**
     * Set receivedBy.
     *
     * @param receivedBy
     *		A JabberId.
     */
    public void setReceivedBy(final JabberId receivedBy) {
        this.receivedBy = receivedBy;
    }

    /**
     * Set receivedOn.
     *
     * @param receivedOn
     *		A Calendar.
     */
    public void setReceivedOn(final Calendar receivedOn) {
        this.receivedOn = receivedOn;
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

    /**
     * Set versionId.
     *
     * @param versionId
     *		A Long.
     */
    public void setVersionId(final Long versionId) {
        this.versionId = versionId;
    }

    /**
     * Obtain publishedOn.
     *
     * @return A Calendar.
     */
    public Calendar getPublishedOn() {
        return publishedOn;
    }

    /**
     * Set publishedOn.
     *
     * @param publishedOn
     *		A Calendar.
     */
    public void setPublishedOn(final Calendar publishedOn) {
        this.publishedOn = publishedOn;
    }
}