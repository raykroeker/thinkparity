/*
 * 
 */
package com.thinkparity.codebase.model.util.xmpp.event.container;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityBackupEvent;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b>thinkParity CommonModel Container Version Published Event<br>
 * <b>Description:</b>The event that is fired when an existing container
 * version is published to users.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityBackupEvent
public final class VersionPublishedEvent extends XMPPEvent {

    /** The <code>DocumentVersion</code>s. */
    private final List<DocumentVersion> documentVersions;

    /** A latest version flag. */
    private Boolean latestVersion;

    /** Who published the container. */
    private JabberId publishedBy;

    /** When the container was published. */
    private Calendar publishedOn;

    /** Who the container was published to. */
    private final List<User> publishedTo;

    /** Who has already received the version. */
    private final List<ArtifactReceipt> receivedBy;

    /** The <code>ContainerVersion</code>. */
    private ContainerVersion version;

    /**
     * Create ContainerVersionPublishedEvent.
     *
     */
    public VersionPublishedEvent() {
        super();
        this.documentVersions = new ArrayList<DocumentVersion>();
        this.publishedTo = new ArrayList<User>();
        this.receivedBy = new ArrayList<ArtifactReceipt>();
    }

    public void clearDocumentVersions() {
        documentVersions.clear();
    }

    public void clearPublishedTo() {
        this.publishedTo.clear();
    }

    public void clearReceivedBy() {
        receivedBy.clear();
    }

    public List<DocumentVersion> getDocumentVersions() {
        return Collections.unmodifiableList(documentVersions);
    }

    /**
     * @see VersionPublishedEvent#isLatestVersion()
     * 
     */
    public Boolean getLatestVersion() {
        return isLatestVersion();
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

    public List<ArtifactReceipt> getReceivedBy() {
        return Collections.unmodifiableList(receivedBy);
    }

    /**
     * Obtain version.
     *
     * @return A ContainerVersion.
     */
    public ContainerVersion getVersion() {
        return version;
    }

    /**
     * Determine if the publish event is for the latest version.
     * 
     * @return True if the event is for the latest version.
     */
    public Boolean isLatestVersion() {
        return latestVersion;
    }

    public void setDocumentVersions(final List<DocumentVersion> documentVersions) {
        this.documentVersions.addAll(documentVersions);
    }

    /**
     * Set latest version.
     * 
     * @param latestVersion
     *            A latest version <code>Boolean</code>.
     */
    public void setLatestVersion(final Boolean latestVersion) {
        this.latestVersion = latestVersion;
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

    public void setReceivedBy(final List<ArtifactReceipt> receivedBy) {
        this.receivedBy.addAll(receivedBy);
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