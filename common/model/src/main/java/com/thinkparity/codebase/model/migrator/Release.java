/*
 * Created On: May 9, 2006
 * $Id$
 */
package com.thinkparity.codebase.model.migrator;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class Release {

    /** The artifact id. */
    private String artifactId;

    /** The creation date. */
    private Calendar createdOn;

    /** The group id. */
    private String groupId;

    /** The id. */
    private Long id;

    /** The version. */
    private String version;

    /** Create Release. */
    public Release() { super(); }

    /** @see java.lang.Object#equals(java.lang.Object) */
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof Release) {
            return ((Release) obj).getId().equals(id);
        }
        return false;
    }

    /**
     * Obtain the artifactId
     *
     * @return The artifact id.
     */
    public String getArtifactId() { return artifactId; }

    /**
     * Obtain the creation date.
     *
     * @return The creation date.
     */
    public Calendar getCreatedOn() { return createdOn; }

    /**
     * Obtain the group id
     * 
     * @return The group id.
     */
    public String getGroupId() { return groupId; }

    /**
     * Obtain the id
     *
     * @return The Long.
     */
    public Long getId() { return id; }

    /**
     * Obtain the version
     *
     * @return The version string.
     */
    public String getVersion() { return version; }

    /** @see java.lang.Object#hashCode() */
    public int hashCode() { return id.hashCode(); }

    /**
     * Determine if the release version is a snapshot.
     *
     * @return True if the release version is a snapshot.
     */
    public Boolean isSnapshot() {
        return getVersion().endsWith("-SNAPSHOT");
    }

    /**
     * Set the artifact id.
     * 
     * @param artifactId
     *            The artifact id.
     */
    public void setArtifactId(final String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * Set the creation date.
     *
     * @param createdOn
     *      The creation date.
     */
    public void setCreatedOn(final Calendar createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Set the group id.
     * 
     * @param groupId
     *            The group id.
     */
    public void setGroupId(final String groupId) { this.groupId = groupId; }

    /**
     * Set the release id.
     * 
     * @param id
     *            The release id..
     */
    public void setId(final Long id) { this.id = id; }

    /**
     * Set version.
     * 
     * @param version
     *            A version string.
     */
    public void setVersion(final String version) { this.version = version; }

    /** @see java.lang.Object#toString() */
    public String toString() {
        final StringBuffer buffer = new StringBuffer(groupId)
            .append(":").append(artifactId)
            .append(":").append(version);
        if(null != createdOn) {
            buffer.append(":")
                    .append(new SimpleDateFormat("yyyy MM dd HH:mm:ss")
                    .format(getCreatedOn().getTime()));
        }
        return buffer.toString();
    }
}
