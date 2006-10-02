/*
 * Created On: Oct 1, 2006 11:04:09 AM
 */
package com.thinkparity.codebase.model.artifact;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArtifactReceipt {

    private Long artifactId;

    private Calendar receivedOn;

    private JabberId userId;

    /** Create ArtifactReceipt. */
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
    public JabberId getUserId() {
        return userId;
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
    public void setUserId(final JabberId userId) {
        this.userId = userId;
    }
}
