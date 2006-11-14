/*
 * 
 */
package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerPublishedEvent extends XMPPEvent {

    /** How many artifacts in the container. */
    private Integer artifactCount;

    /** A version comment. */
    private String comment;

    /** The name of the container. */
    private String name;

    /** Who published the container. */
    private JabberId publishedBy;

    /** When the container was published. */
    private Calendar publishedOn;

    /** Who the container was published to. */
    private List<JabberId> publishedTo;

    /** The artifact unique id. */
    private UUID uniqueId;

    /** Which version was published. */
    private Long versionId;

    /**
     * Create ContainerPublishedEvent.
     *
     */
    public ContainerPublishedEvent() {
        super();
    }

    /**
     * Obtain artifactCount.
     *
     * @return A Integer.
     */
    public Integer getArtifactCount() {
        return artifactCount;
    }

    /**
     * Obtain comment.
     *
     * @return A String.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Obtain name.
     *
     * @return A String.
     */
    public String getName() {
        return name;
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
    public List<JabberId> getPublishedTo() {
        return publishedTo;
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
     * Set artifactCount.
     *
     * @param artifactCount
     *		A Integer.
     */
    public void setArtifactCount(final Integer artifactCount) {
        this.artifactCount = artifactCount;
    }

    /**
     * Set comment.
     *
     * @param comment
     *		A String.
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * Set name.
     *
     * @param name
     *		A String.
     */
    public void setName(final String name) {
        this.name = name;
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
     * Set publishedTo.
     *
     * @param publishedTo
     *		A List<JabberId>.
     */
    public void setPublishedTo(final List<JabberId> publishedTo) {
        this.publishedTo = publishedTo;
    }

    /**
     * Set uniqueId.
     *
     * @param uniqueId
     *		A UUID.
     */
    public void setUniqueId(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Set versionId.
     *
     * @param versionId
     *		A Long.
     */
    public void setVersionId(final Long versionId) {
        this.versionId = versionId;
    }
}