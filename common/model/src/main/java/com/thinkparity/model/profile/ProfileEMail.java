/*
 * Created On: Aug 25, 2006 9:00:50 AM
 */
package com.thinkparity.model.profile;

import com.thinkparity.codebase.email.EMail;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ProfileEMail {

    /** The <code>EMail</code>. */
    private EMail email;

    /** The email id <code>Long</code>. */
    private Long emailId;

    /** The profile id <code>Long</code>. */
    private Long profileId;

    /** The verification flag <code>Boolean</code>. */
    private Boolean verified;

    /** Create ProfileEMail. */
    public ProfileEMail() {
        super();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof ProfileEMail) {
            return ((ProfileEMail) obj).profileId.equals(profileId) &&
                    ((ProfileEMail) obj).emailId.equals(emailId);
        }
        return false;
    }

    /**
     * Obtain the email
     *
     * @return The EMail.
     */
    public EMail getEmail() {
        return email;
    }

    /**
     * Obtain the emailId
     *
     * @return The Long.
     */
    public Long getEmailId() {
        return emailId;
    }

    /**
     * Obtain the profileId
     *
     * @return The Long.
     */
    public Long getProfileId() {
        return profileId;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Obtain the verified
     *
     * @return The Boolean.
     */
    public Boolean isVerified() {
        return verified;
    }

    /**
     * Set email.
     *
     * @param email The EMail.
     */
    public void setEmail(final EMail email) {
        this.email = email;
    }

    /**
     * Set emailId.
     *
     * @param emailId The Long.
     */
    public void setEmailId(final Long emailId) {
        this.emailId = emailId;
    }

    /**
     * Set profileId.
     *
     * @param profileId The Long.
     */
    public void setProfileId(final Long profileId) {
        this.profileId = profileId;
    }

    /**
     * Set verified.
     *
     * @param verified The Boolean.
     */
    public void setVerified(final Boolean verified) {
        this.verified = verified;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
            .append(profileId).append("/")
            .append(emailId)
            .toString();
    }
}
