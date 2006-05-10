/*
 * Created On: Sun May 07 2006 10:47 PDT
 * $Id$
 */
package com.thinkparity.migrator;

import com.thinkparity.codebase.assertion.Assert;

public class Library {

    /** The library artifact id. */
    private String artifactId;

    /** The library group id. */
    private String groupId;

    /** The library id. */
    private Long id;

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
     * Set artifactId.
     *
     * @param artifactId The String.
     */
    public void setArtifactId(final String artifactId) {
        this.artifactId = artifactId;
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
