/*
 * Created On:  23-Jan-07 4:19:35 PM
 */
package com.thinkparity.codebase.model.migrator;

import com.thinkparity.codebase.OS;


/**
 * <b>Title:</b>thinkParity Resource<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Resource {

    /** A resource checksum <code>String</code>. */
    private String checksum;

    /** A resource id <code>Long</code>. */
    private Long id;

    /** A resource name <code>String</code>. */
    private String name;

    /** An <code>OS</code>. */
    private OS os;

    /** A resource path <code>String</code>. */
    private String path;

    /** A resource size <code>Long</code>. */
    private Long size;

    /** A resource version <code>String</code>. */
    private String version;

    /**
     * Create Resource.
     *
     */
    public Resource() {
        super();
    }

    /**
     * Obtain checksum.
     *
     * @return A String.
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * Obtain id.
     *
     * @return A Long.
     */
    public Long getId() {
        return id;
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
     * Obtain os.
     *
     * @return A OS.
     */
    public OS getOs() {
        return os;
    }

    /**
     * Obtain path.
     *
     * @return A String.
     */
    public String getPath() {
        return path;
    }

    /**
     * Obtain size.
     *
     * @return A Long.
     */
    public Long getSize() {
        return size;
    }

    /**
     * Obtain version.
     *
     * @return A String.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set checksum.
     *
     * @param checksum
     *		A String.
     */
    public void setChecksum(final String checksum) {
        this.checksum = checksum;
    }

    /**
     * Set id.
     *
     * @param id
     *		A Long.
     */
    public void setId(final Long id) {
        this.id = id;
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
     * Set os.
     *
     * @param os
     *		A OS.
     */
    public void setOs(final OS os) {
        this.os = os;
    }

    /**
     * Set path.
     *
     * @param path
     *		A String.
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * Set size.
     *
     * @param size
     *		A Long.
     */
    public void setSize(final Long size) {
        this.size = size;
    }

    /**
     * Set version.
     *
     * @param version
     *		A String.
     */
    public void setVersion(final String version) {
        this.version = version;
    }
}
