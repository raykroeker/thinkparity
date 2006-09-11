/*
 * Created On: Sun May 07 2006 10:47 PDT
 */
package com.thinkparity.codebase.model.migrator;

import java.util.Calendar;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
public class Library {

    /** The library artifact id. */
    private String artifactId;

    /** The creation date. */
    private Calendar createdOn;

    /** The library group id. */
    private String groupId;

    /** The library id. */
    private Long id;

    /** The relative path. */
    private String path;

    /** The library type. */
    private Library.Type type;

    /** The library version. */
    private String version;

    /** Create Library. */
    public Library() { super(); }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof Library) {
            return ((Library) obj).id.equals(id);
        }
        return false;
    }

    /**
     * Obtain the artifactId
     *
     * @return The String.
     */
    public String getArtifactId() { return artifactId; }

    /**
     * Obtain the creation date.
     *
     * @return The creation date.
     */
    public Calendar getCreatedOn() { return createdOn; }

    /**
     * Obtain the groupId
     *
     * @return The String.
     */
    public String getGroupId() { return groupId; }

    /**
     * Obtain the id
     *
     * @return The Long.
     */
    public Long getId() { return id; }

    /**
     * Obtain the path
     *
     * @return A relative path.
     */
    public String getPath() { return path; }

    /**
     * Obtain the type
     *
     * @return The Library.Type.
     */
    public Library.Type getType() { return type; }

    /**
     * Obtain the version
     *
     * @return The String.
     */
    public String getVersion() { return version; }

    /** @see java.lang.Object#hashCode() */
    public int hashCode() { return id.hashCode(); }

    /**
     * Determine whether or not the library is core.
     * 
     * @return True if the library is core; false otherwise.
     */
    public Boolean isCore() { return getGroupId().equals("com.thinkparity.parity"); }

    /**
     * Set artifactId.
     *
     * @param artifactId The String.
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
     * Set groupId.
     *
     * @param groupId The String.
     */
    public void setGroupId(final String groupId) { this.groupId = groupId; }

    /**
     * Set id.
     *
     * @param id The Long.
     */
    public void setId(final Long id) { this.id = id; }

    /**
     * Set path.
     * 
     * @param path
     *            A relative path.
     */
    public void setPath(final String path) { this.path = path; }

    /**
     * Set type.
     *
     * @param type The Library.Type.
     */
    public void setType(final Library.Type type) { this.type = type; }

    /**
     * Set version.
     *
     * @param version The String.
     */
    public void setVersion(final String version) { this.version = version; }

    /** @see java.lang.Object#toString() */
    public String toString() {
        return new StringBuffer(groupId)
            .append(":").append(artifactId)
            .append(":").append(version)
            .append(":").append(type.toString())
            .toString();
    }

    /** The definition of a library type. */
    public enum Type {
        JAVA(1), NATIVE(0);

        /**
         * Resolve the type via its id.
         * 
         * @param id
         *            The library type id.
         * @return The library type.
         */
        public static Library.Type fromId(final Integer id) {
            switch(id) {
                case 0: return NATIVE;
                case 1: return JAVA;
                default: throw Assert.createUnreachable("[CMIGRATOR] [LIBRARY TYPE FROM ID]");
            }
        }

        /** The type id. */
        private final Integer id;

        /**
         * Create Type.
         * 
         * @param id
         *            The type id.
         */
        private Type(final Integer id) { this.id = id; }

        /**
         * Obtain the type id.
         * 
         * @return The type id.
         */
        public Integer getId() { return id; }
    }
}
