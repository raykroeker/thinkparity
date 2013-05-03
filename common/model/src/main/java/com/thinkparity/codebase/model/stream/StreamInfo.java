/*
 * Created On:  25-Jul-07 10:31:02 AM
 */
package com.thinkparity.codebase.model.stream;

/**
 * <b>Title:</b>thinkParity Stream Info<br>
 * <b>Description:</b>Encapsulates the information required to create an
 * upstream/downstream session.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class StreamInfo {

    /** The stream md5 checksum. */
    private String md5;

    /** The stream size. */
    private Long size;

    /**
     * Create StreamInfo.
     *
     */
    public StreamInfo() {
        super();
    }

    /**
     * Obtain the md5 checksum.
     * 
     * @return A <code>String</code>.
     */
    public String getMD5() {
        return md5;
    }

    /**
     * Obtain the size.
     *
     * @return A <code>Long</code>.
     */
    public Long getSize() {
        return size;
    }

    /**
     * Set the md5 checksum.
     * 
     * @param md5
     *            A <code>String</code>.
     */
    public void setMD5(final String md5) {
        this.md5 = md5;
    }

    /**
     * Set the size.
     * 
     * @param size
     *            A <code>Long</code>.
     */
    public void setSize(final Long size) {
        this.size = size;
    }
}
