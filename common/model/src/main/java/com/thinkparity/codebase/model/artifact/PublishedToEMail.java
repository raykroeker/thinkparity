/*
 * Created On:  15-May-07 9:24:10 PM
 */
package com.thinkparity.codebase.model.artifact;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PublishedToEMail {

    private Long artifactId;

    private EMail email;

    private Calendar publishedOn;

    /**
     * Create ArtifactPublishedToEMail.
     *
     */
    public PublishedToEMail() {
        super();
    }

    /**
     * Obtain artifactId.
     *
     * @return A Long.
     */
    public Long getArtifactId() {
        return artifactId;
    }

    /**
     * Obtain email.
     *
     * @return A EMail.
     */
    public EMail getEMail() {
        return email;
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
     * Set artifactId.
     *
     * @param artifactId
     *		A Long.
     */
    public void setArtifactId(final Long artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * Set email.
     *
     * @param email
     *		A EMail.
     */
    public void setEMail(final EMail email) {
        this.email = email;
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
}
