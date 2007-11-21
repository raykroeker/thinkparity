/*
 * Created On:  Nov 19, 2007 3:44:12 PM
 */
package com.thinkparity.ophelia.support.util.process;

/**
 * <b>Title:</b>thinkParity Ophelia Support Process Info<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProcessInfo {

    /** A process file. */
    private String file;

    /** A process id. */
    private String id;

    /** A process name. */
    private String name;

    /** A parent process id. */
    private String parentId;

    /** A process path. */
    private String path;

    /**
     * Create ProcessInfo.
     *
     */
    public ProcessInfo() {
        super();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj) {
            return false;
        } else {
            if (this == obj) {
                return true;
            } else {
                if (obj.getClass() == getClass()) {
                    return ((ProcessInfo) obj).id.equals(id);
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * Obtain file.
     *
     * @return A String.
     */
    public String getFile() {
        return file;
    }

    /**
     * Obtain id.
     *
     * @return A String.
     */
    public String getId() {
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
     * Obtain parentId.
     *
     * @return A String.
     */
    public String getParentId() {
        return parentId;
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
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Set file.
     *
     * @param file
     *		A String.
     */
    public void setFile(final String file) {
        this.file = file;
    }

    /**
     * Set id.
     *
     * @param id
     *		A String.
     */
    public void setId(final String id) {
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
     * Set parentId.
     *
     * @param parentId
     *		A String.
     */
    public void setParentId(final String parentId) {
        this.parentId = parentId;
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
}
