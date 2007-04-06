/*
 * 
 */
package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerPublishedEvent extends XMPPEvent {

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

    /** Who has already received the version. */
    private final List<ArtifactReceipt> receivedBy;

    /** The <code>ContainerVersion</code>. */
    private ContainerVersion version;

    /**
     * Create ContainerPublishedEvent.
     *
     */
    public ContainerPublishedEvent() {
        super();
        this.documentVersions = new HashMap<DocumentVersion, String>();
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