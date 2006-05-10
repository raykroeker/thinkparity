/*
 * May 9, 2006
 */
package com.thinkparity.migrator;

import java.util.LinkedList;
import java.util.List;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class Release {

    private String artifactId;

    private String groupId;

    private Long id;

    private final List<Library> libraries;

    private String name;

    private String version;

    /** Create Release. */
    public Release() {
        super();
        this.libraries = new LinkedList<Library>();
    }

    public boolean addLibrary(final Library library) {
        return this.libraries.add(library);
    }

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
     * Obtain the libraries
     * 
     * @return The libraries.
     */
    public List<Library> getLibraries() { return libraries; }

    /**
     * Obtain the name
     *
     * @return The String.
     */
    public String getName() { return name; }

    /**
     * Obtain the version
     *
     * @return The version string.
     */
    public String getVersion() { return version; }

    /** @see java.lang.Object#hashCode() */
    public int hashCode() { return id.hashCode(); }

    public boolean removeLibrary(final Library library) {
        return this.libraries.remove(library);
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
     * Set name.
     *
     * @param name The String.
     */
    public void setName(final String name) { this.name = name; }

    /**
     * Set version.
     * 
     * @param version
     *            A version string.
     */
    public void setVersion(final String version) { this.version = version; }

}
