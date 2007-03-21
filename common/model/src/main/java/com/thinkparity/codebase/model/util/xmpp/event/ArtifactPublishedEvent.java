/*
 * Created On:  2006-11-11 10:09
 */
package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArtifactPublishedEvent extends XMPPEvent {

    /** The latest version flag <code>Boolean</code>. */
    private Boolean latestVersion;

    /** The published by user id <code>JabberId</code>. */
    private JabberId publishedBy;

    /** The published on <code>Calendar</code>. */
    private Calendar publishedOn;

    /** A list of team member user id <code>JabberId</code>s. */
    private final List<JabberId> teamUserIds;

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
        this.teamUserIds = new ArrayList<JabberId>();
    }

    /**
     * Add a team id.
     * 
     * @param teamId
     *            A team member's user id <code>JabberId</code>.
     * @return True if the team id list is modified.
     */
    public boolean addTeamUserId(final JabberId teamId) {
        return teamUserIds.add(teamId);
    }


    /**
     * Clear all team user ids.
     *
     */
    public void clearTeamUserIds() {
        this.teamUserIds.clear();
    }


    /**
     * Obtain latestVersion.
     *
     * @return A Boolean.
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
     * Obtain teamUserIds.
     * 
     * @return A <code>List</code> of user id <code>JabberId</code>s.
     */
    public List<JabberId> getTeamUserIds() {
        return Collections.unmodifiableList(teamUserIds);
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
     * Obtain latestVersionId.
     *
     * @return A Long.
     */
    public Boolean isLatestVersion() {
        return latestVersion;
    }

    /**
     * Remove a team id.
     * 
     * @param teamId
     *            A team member's user id <code>JabberId</code>.
     * @return True if the team id list is modified.
     */
    public boolean removeTeamUserId(final JabberId teamId) {
        return teamUserIds.remove(teamId);
    }

    /**
     * Set latestVersionId.
     *
     * @param latestVersionId
     *		A Long.
     */
    public void setLatestVersion(final Boolean latestVersion) {
        this.latestVersion = latestVersion;
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
     * Set teamUserIds.
     *
     * @param teamUserIds
     *		A List<JabberId>.
     */
    public void setTeamUserIds(final List<JabberId> teamUserIds) {
        this.teamUserIds.clear();
        this.teamUserIds.addAll(teamUserIds);
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