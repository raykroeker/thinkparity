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
import com.thinkparity.codebase.model.annotation.ThinkParityFilterEvent;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
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
@ThinkParityFilterEvent
public final class PublishedEvent extends XMPPEvent {

    /**The <code>DocumentVersion</code>s. */
    private final List<DocumentVersion> documentVersions;

    /** Who published the container. */
    private JabberId publishedBy;

    /** When the container was published. */
    private Calendar publishedOn;

    /** Who the container was published to. */
    private final List<User> publishedTo;

    /** The existing <code>List<TeamMember></code>. */
    private final List<TeamMember> team;

    /** The <code>ContainerVersion</code>. */
    private ContainerVersion version;

    /**
     * Create ContainerPublishedEvent.
     *
     */
    public PublishedEvent() {
        super();
        this.documentVersions = new ArrayList<DocumentVersion>();
        this.publishedTo = new ArrayList<User>();
        this.team = new ArrayList<TeamMember>();
    }

    public void clearDocumentVersions() {
        documentVersions.clear();
    }

    public void clearPublishedTo() {
        this.publishedTo.clear();
    }

    public List<DocumentVersion> getDocumentVersions() {
        return Collections.unmodifiableList(documentVersions);
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

    public List<TeamMember> getTeam() {
        return Collections.unmodifiableList(team);
    }

    /**
     * Obtain version.
     *
     * @return A ContainerVersion.
     */
    public ContainerVersion getVersion() {
        return version;
    }

    public void setDocumentVersions(final List<DocumentVersion> documentVersions) {
        this.documentVersions.addAll(documentVersions);
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

    public void setTeam(final List<TeamMember> team) {
        this.team.clear();
        this.team.addAll(team);
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