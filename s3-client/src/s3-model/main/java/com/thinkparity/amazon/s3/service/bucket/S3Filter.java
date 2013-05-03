/*
 * Created On:  14-Oct-07 12:14:45 AM
 */
package com.thinkparity.amazon.s3.service.bucket;

/**
 * <b>Title:</b>thinkParity Amazon S3 Service Bucket Filter<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3Filter {

    /** A filter delimtier. */
    private String delimiter;

    /** A filter prefix. */
    private String prefix;

    /**
     * Create S3Filter.
     *
     */
    public S3Filter() {
        super();
    }

    /**
     * Obtain the delimiter.
     *
     * @return A <code>String</code>.
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Obtain the prefix.
     *
     * @return A <code>String</code>.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Determine if the delimiter is set.
     * 
     * @return True if it is set.
     */
    public Boolean isSetDelimiter() {
        return null != delimiter;
    }

    /**
     * Set the delimiter.
     *
     * @param delimiter
     *		A <code>String</code>.
     */
    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Set the prefix.
     *
     * @param prefix
     *		A <code>String</code>.
     */
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
}
