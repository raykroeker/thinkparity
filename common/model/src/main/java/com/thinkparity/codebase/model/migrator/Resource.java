/*
 * Created On:  23-Jan-07 4:19:35 PM
 */
package com.thinkparity.codebase.model.migrator;

import com.thinkparity.codebase.HashCodeUtil;

/**
 * <b>Title:</b>thinkParity Resource<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Resource {

    /** A resource checksum <code>String</code>. */
    private String checksum;

    /** A checksum algorithm <code>String</code>. */
    private String checksumAlgorithm;

    /** A resource id <code>Long</code>. */
    private transient Long id;

    /** A resource path <code>String</code>. */
    private String path;

    /** The release name <code>String</code>. */
    private String releaseName;

    /** A resource size <code>Long</code>. */
    private Long size;

    /**
     * Create Resource.
     *
     */
    public Resource() {
        super();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return ((Resource) obj).releaseName.equals(releaseName)
                && ((Resource) obj).path.equals(path);
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
     * Obtain checksumAlgorithm.
     *
     * @return A String.
     */
    public String getChecksumAlgorithm() {
        return checksumAlgorithm;
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
     * Obtain path.
     *
     * @return A String.
     */
    public String getPath() {
        return path;
    }

    /**
     * Obtain releaseName.
     *
     * @return A String.
     */
    public String getReleaseName() {
        return releaseName;
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
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        return HashCodeUtil.hashCode(releaseName, path);
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
     * Set checksumAlgorithm.
     *
     * @param checksumAlgorithm
     *		A String.
     */
    public void setChecksumAlgorithm(final String checksumAlgorithm) {
        this.checksumAlgorithm = checksumAlgorithm;
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
     * Set path.
     *
     * @param path
     *		A String.
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * Set releaseName.
     *
     * @param releaseName
     *		A String.
     */
    public void setReleaseName(final String releaseName) {
        this.releaseName = releaseName;
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
}
