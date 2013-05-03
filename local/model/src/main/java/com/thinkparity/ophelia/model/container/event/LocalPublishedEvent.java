/*
 * Created On:  13-Jul-07 4:39:35 PM
 */
package com.thinkparity.ophelia.model.container.event;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedEvent;

import com.thinkparity.ophelia.model.LocalContentEvent;

/**
 * <b>Title:</b>thinkParity Ophelia Model Container Local Published Event<br>
 * <b>Description:</b>A local version of the container published event. The
 * local event includes the original remote event as well as pre-downloaded
 * document version files.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class LocalPublishedEvent implements
        LocalContentEvent<DocumentVersion, File> {

    /** The container published event. */
    private PublishedEvent event;

    /** The document version content. */
    private final Map<DocumentVersion, File> localContent;

    /**
     * Obtain the document version files.
     * 
     * @return A <code>Map<DocumentVersion, File></code>.
     */
    public Map<DocumentVersion, File> getDocumentVersionFiles() {
        return Collections.unmodifiableMap(localContent);
    }

    /**
     * Create PublishedEvent.
     *
     */
    public LocalPublishedEvent() {
        super();
        this.localContent = new HashMap<DocumentVersion, File>();
    }

    /**
     * @see com.thinkparity.ophelia.model.LocalContentEvent#getAllLocalContent()
     *
     */
    public List<File> getAllLocalContent() {
        final List<File> allLocalContent = new ArrayList<File>(localContent.size());
        allLocalContent.addAll(localContent.values());
        return Collections.unmodifiableList(allLocalContent);
    }

    /**
     * Obtain the event document versions.
     * 
     * @return A <code>List<DocumentVersion></code>.
     */
    public List<DocumentVersion> getDocumentVersions() {
        return event.getDocumentVersions();
    }

    /**
     * Obtain event.
     *
     * @return A PublishedEvent.
     */
    public PublishedEvent getEvent() {
        return event;
    }

    /**
     * @see com.thinkparity.ophelia.model.LocalContentEvent#getLocalContent(com.thinkparity.codebase.model.artifact.ArtifactVersion)
     *
     */
    public File getLocalContent(final DocumentVersion version) {
        return localContent.get(version);
    }

    /**
     * Obtain the event published by user id.
     * 
     * @return A published by <code>JabberId</code>.
     */
    public JabberId getPublishedBy() {
        return event.getPublishedBy();
    }

    /**
     * Obtain the event published on date.
     * 
     * @return A <code>Calendar</code>.
     */
    public Calendar getPublishedOn() {
        return event.getPublishedOn();
    }

    /**
     * Obtain the event published to user list.
     * 
     * @return A <code>List<User></code>.
     */
    public List<User> getPublishedTo() {
        return event.getPublishedTo();
    }

    /**
     * Obtain the event team.
     * 
     * @return A <code>List<TeamMember></code>.
     */
    public List<TeamMember> getTeam() {
        return event.getTeam();
    }

    /**
     * Obtain the event container version.
     * 
     * @return A <code>ContainerVersion</code>.
     */
    public ContainerVersion getVersion() {
        return event.getVersion();
    }

    /**
     * @see com.thinkparity.ophelia.model.LocalContentEvent#getVersions()
     *
     */
    public List<DocumentVersion> getVersions() {
        return getDocumentVersions();
    }

    /**
     * Set event.
     *
     * @param event
     *		A PublishedEvent.
     */
    public void setEvent(final PublishedEvent event) {
        this.event = event;
    }

    /**
     * @see com.thinkparity.ophelia.model.LocalContentEvent#setLocalContent(com.thinkparity.codebase.model.artifact.ArtifactVersion, java.lang.Object)
     *
     */
    public void setLocalContent(final DocumentVersion version, final File content) {
        this.localContent.put(version, content);
    }
}
