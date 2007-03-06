/*
 * Created On: Aug 3, 2006 11:13:04 AM
 */
package com.thinkparity.codebase.model.user;


/**
 * <b>Title:</b>thinkParity Team Member<br>
 * <b>Description:</b>A thinkParity team member is a user; with some additional
 * state and artifact information.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @see User
 */
public class TeamMember extends User {

    /** The artifact id. */
    private Long artifactId;

    /**
     * Create TeamMember.
     *
     */
    public TeamMember() {
        super();
    }

    /**
     * @see com.thinkparity.codebase.model.user.User#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof TeamMember) {
            return ((TeamMember) obj).getId().equals(getId()) &&
                    ((TeamMember) obj).artifactId.equals(artifactId);
        }
        return false;
    }

    /**
     * Obtain the artifactId
     *
     * @return The Long.
     */
    public Long getArtifactId() { return artifactId; }

    /**
     * @see com.thinkparity.codebase.model.user.User#hashCode()
     */
    @Override
    public int hashCode() { return toString().hashCode(); }

    /**
     * Set artifactId.
     *
     * @param artifactId The Long.
     */
    public void setArtifactId(final Long artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * @see com.thinkparity.codebase.model.user.User#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(getId())
                .append("/").append(artifactId)
                .toString();
    }
}
