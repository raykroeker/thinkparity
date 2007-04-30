/*
 * Created On:  2006-11-11 10:09
 */
package com.thinkparity.codebase.model.util.xmpp.event.container;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityBackupEvent;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.user.User;
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
public final class PublishedNotificationEvent extends XMPPEvent {

    /** The published by <code>User</code>. */
    private JabberId publishedBy;

    /** The published on <code>Calendar</code>. */
    private Calendar publishedOn;

    /** A <code>List</code> of <code>User</code>s participating in the team. */
    private final List<User> team;

    /** The <code>ArtifactVersion</code>. */
    private ArtifactVersion version;

    /**
     * Create ArtifactPublishedEvent.
     * 
     */
    public PublishedNotificationEvent() {
        super();
        this.team = new ArrayList<User>();
    }

    /**
     * Add a team id.
     * 
     * @param teamMember
     *            A team member's <code>User</code>.
     * @return True if the team id list is modified.
     */
    public boolean addTeamMember(final User teamMember) {
        return team.add(teamMember);
    }


    /**
     * Clear all team user ids.
     *
     */
    public void clearTeam() {
        this.team.clear();
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
     * Obtain teamUserIds.
     * 
     * @return A <code>List</code> of user id <code>JabberId</code>s.
     */
    public List<User> getTeam() {
        return Collections.unmodifiableList(team);
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
     * Remove a team id.
     * 
     * @param teamId
     *            A team member's user id <code>JabberId</code>.
     * @return True if the team id list is modified.
     */
    public boolean removeTeamMember(final User teamMember) {
        return team.remove(teamMember);
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
     * Set teamUserIds.
     *
     * @param teamUserIds
     *		A List<JabberId>.
     */
    public void setTeam(final List<User> team) {
        this.team.clear();
        this.team.addAll(team);
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