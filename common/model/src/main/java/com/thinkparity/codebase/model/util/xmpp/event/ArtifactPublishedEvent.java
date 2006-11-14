/*
 * Created On:  2006-11-11 10:09
 */
package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArtifactPublishedEvent extends XMPPEvent {

    /** The published by user id <code>JabberId</code>. */
    private JabberId publishedBy;

    /** The published on <code>Calendar</code>. */
    private Calendar publishedOn;

    /** The artifact id <code>UUID</code>. */
    private UUID uniqueId;

    /** The version id. <code>Long</code>. */
    private Long versionId;

    /**
     * Create ArtifactPublishedEvent.
     * 
     */
    public ArtifactPublishedEvent() {
        super();
    }

    /**
     * Obtain publishedBy.
     *
     * @return A JabberId.
     */
    public JabberId getPublishedBy() {
        return publishedBy;
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
     * Set publishedBy.
     *
     * @param publishedBy
     *		A JabberId.
     */
    public void setPublishedBy(JabberId publishedBy) {
        this.publishedBy = publishedBy;
    }

    /**
     * Set publishedOn.
     *
     * @param publishedOn
     *		A Calendar.
     */
    public void setPublishedOn(Calendar publishedOn) {
        this.publishedOn = publishedOn;
    }

    /**
     * Set uniqueId.
     *
     * @param uniqueId
     *		A UUID.
     */
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Set versionId.
     *
     * @param versionId
     *		A Long.
     */
    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }
}