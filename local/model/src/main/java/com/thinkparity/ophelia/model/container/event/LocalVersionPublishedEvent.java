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

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedEvent;

import com.thinkparity.ophelia.model.LocalContentEvent;

/**
 * <b>Title:</b>thinkParity Ophelia Model Local Version Published Event<br>
 * <b>Description:</b>A wrapper event that includes the local content for the
 * event data, in this case downloaded files.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class LocalVersionPublishedEvent implements
        LocalContentEvent<DocumentVersion, File> {

    /** A version published event. */
    private VersionPublishedEvent event;

    /** The document version files. */
    private final Map<DocumentVersion, File> localContent;

    /**
     * Create LocalVersionPublishedEvent.
     *
     */
    public LocalVersionPublishedEvent() {
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
     * Obtain the document version files.
     * 
     * @return A <code>Map<DocumentVersion, File></code>.
     */
    public Map<DocumentVersion, File> getDocumentVersionFiles() {
        return Collections.unmodifiableMap(localContent);
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
     * @return A VersionPublishedEvent.
     */
    public VersionPublishedEvent getEvent() {
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
     * Obtain the published by user id.
     * 
     * @return A user id <code>JabberId</code>.
     */
    public JabberId getPublishedBy() {
        return event.getPublishedBy();
    }

    /**
     * Obtain the published on date/time.
     * 
     * @return A <code>Calendar</code>.
     */
    public Calendar getPublishedOn() {
        return event.getPublishedOn();
    }

    /**
     * Obtain the event received by list.
     * 
     * @return A <code>List<ArtifactReceipt></code>.
     */
    public List<ArtifactReceipt> getReceivedBy() {
        return event.getReceivedBy();
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
     * Determine if the event represents the latest version.
     * 
     * @return True if the event represents the latest version.
     */
    public Boolean isLatestVersion() {
        return event.isLatestVersion();
    }

    /**
     * Set event.
     *
     * @param event
     *		A VersionPublishedEvent.
     */
    public void setEvent(final VersionPublishedEvent event) {
        this.event = event;
    }

    /**
     * @see com.thinkparity.ophelia.model.LocalContentEvent#setLocalContent(com.thinkparity.codebase.model.artifact.ArtifactVersion, java.lang.Object)
     *
     */
    public void setLocalContent(final DocumentVersion version,
            final File content) {
        localContent.put(version, content);
    }
}
