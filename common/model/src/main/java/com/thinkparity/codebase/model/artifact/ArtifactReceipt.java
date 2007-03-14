/*
 * Created On: Oct 1, 2006 11:04:09 AM
 */
package com.thinkparity.codebase.model.artifact;

import java.util.Calendar;

import com.thinkparity.codebase.model.user.User;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArtifactReceipt {

    private Long artifactId;

    private Calendar publishedOn;

    private Calendar receivedOn;

    private User user;

    /**
     * Create ArtifactReceipt.
     * 
     */
    public ArtifactReceipt() {
        super();
    }

    /**
     * Obtain the artifactId
     *
     * @return The Long.
     */
    public Long getArtifactId() {
        return artifactId;
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
     * Obtain the receivedOn
     *
     * @return The Calendar.
     */
    public Calendar getReceivedOn() {
        return receivedOn;
    }

    /**
     * Obtain the userId
     *
     * @return The JabberId.
     */
    public User getUser() {
        return user;
    }

    public Boolean isSetReceivedOn() {
        return null != receivedOn;
    }

    /**
     * Set artifactId.
     *
     * @param artifactId The Long.
     */
    public void setArtifactId(final Long artifactId) {
        this.artifactId = artifactId;
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
     * Set receivedOn.
     *
     * @param receivedOn The Calendar.
     */
    public void setReceivedOn(final Calendar receivedOn) {
        this.receivedOn = receivedOn;
    }

    /**
     * Set userId.
     *
     * @param userId The JabberId.
     */
    public void setUser(final User user) {
        this.user = user;
    }
}
