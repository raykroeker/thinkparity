/*
 * Created On:  2006-11-11 10:09
 */
package com.thinkparity.codebase.model.util.xmpp.event.container;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityBackupEvent;
import com.thinkparity.codebase.model.annotation.ThinkParityFilterEvent;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b>thinkParity CommonModel Container Published Notification Event<br>
 * <b>Description:</b>The event fired to everyone indicating a version has been
 * published. Is used to keep the is latest flag up to date, as well the team
 * definition and draft information.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityBackupEvent
@ThinkParityFilterEvent
public final class PublishedNotificationEvent extends XMPPEvent {

    /** The published by <code>User</code>. */
    private JabberId publishedBy;

    /** The published on <code>Calendar</code>. */
    private Calendar publishedOn;

    /** The <code>ArtifactVersion</code>. */
    private ArtifactVersion version;

    /**
     * Create ArtifactPublishedEvent.
     * 
     */
    public PublishedNotificationEvent() {
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
     * Obtain version.
     *
     * @return A ArtifactVersion.
     */
    public ArtifactVersion getVersion() {
        return version;
    }

    /**
     * Set publishedBy.
     *
     * @param publishedBy
     *		A JabberId.
     */
    public void setPublishedBy(final JabberId publishedBy) {
        this.publishedBy = publishedBy;
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

    /**
     * Set version.
     *
     * @param version
     *		A ArtifactVersion.
     */
    public void setVersion(final ArtifactVersion version) {
        this.version = version;
    }
}