/*
 * 
 */
package com.thinkparity.codebase.model.util.xmpp.event.container;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityBackupEvent;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b>thinkParity CommonModel Container Published Event<br>
 * <b>Description:</b>The event that is fired when a new container version is
 * published to users.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityBackupEvent
public final class PublishedEvent extends XMPPEvent {

    /**
     * The <code>DocumentVersion</code>s and their stream id
     * <code>String</code>s.
     */
    private final Map<DocumentVersion, String> documentVersions;

    /** Who published the container. */
    private JabberId publishedBy;

    /** When the container was published. */
    private Calendar publishedOn;

    /** Who the container was published to. */
    private final List<User> publishedTo;

    /** The <code>ContainerVersion</code>. */
    private ContainerVersion version;

    /**
     * Create ContainerPublishedEvent.
     *
     */
    public PublishedEvent() {
        super();
        this.documentVersions = new HashMap<DocumentVersion, String>();
        this.publishedTo = new ArrayList<User>();
    }

    public void clearDocumentVersions() {
        documentVersions.clear();
    }

    public void clearPublishedTo() {
        this.publishedTo.clear();
    }

    public Map<DocumentVersion, String> getDocumentVersions() {
        return Collections.unmodifiableMap(documentVersions);
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
     * Obtain publishedTo.
     *
     * @return A List<JabberId>.
     */
    public List<User> getPublishedTo() {
        return Collections.unmodifiableList(publishedTo);
    }

    /**
     * Obtain version.
     *
     * @return A ContainerVersion.
     */
    public ContainerVersion getVersion() {
        return version;
    }

    public void setDocumentVersions(final Map<DocumentVersion, String> documentVersions) {
        this.documentVersions.putAll(documentVersions);
    }

    /**
     * Set publishedBy.
     *
     * @param publishedBy
     *      A JabberId.
     */
    public void setPublishedBy(final JabberId publishedBy) {
        this.publishedBy = publishedBy;
    }

    /**
     * Set publishedOn.
     *
     * @param publishedOn
     *      A Calendar.
     */
    public void setPublishedOn(final Calendar publishedOn) {
        this.publishedOn = publishedOn;
    }

    /**
     * Set publishedTo.
     *
     * @param publishedTo
     *      A List<JabberId>.
     */
    public void setPublishedTo(final List<User> publishedTo) {
        this.publishedTo.addAll(publishedTo);
    }

    /**
     * Set version.
     *
     * @param version
     *      A ContainerVersion.
     */
    public void setVersion(final ContainerVersion version) {
        this.version = version;
    }
}