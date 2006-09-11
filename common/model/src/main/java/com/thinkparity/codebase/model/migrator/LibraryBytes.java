/*
 * Created On: May 28, 2006 6:30:45 PM
 * $Id$
 */
package com.thinkparity.codebase.model.migrator;


/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class LibraryBytes {

    /** The library bytes. */
    private byte[] bytes;

    /** The library bytes checksum. */
    private String checksum;

    /** The library id. */
    private Long libraryId;

    /** Create LibraryBytes. */
    public LibraryBytes() { super(); }

    /** @see java.lang.Object#equals(java.lang.Object) */
    public boolean equals(Object obj) {
        if(null != obj && obj instanceof LibraryBytes) {
            return libraryId.equals(((LibraryBytes) obj).libraryId);
        }
        else {  return false; }
    }

    /**
     * Obtain the bytes
     *
     * @return The byte[].
     */
    public byte[] getBytes() { return bytes; }

    /**
     * Obtain the checksum
     *
     * @return The String.
     */
    public String getChecksum() { return checksum; }

    /**
     * Obtain the libraryId
     *
     * @return The Long.
     */
    public Long getLibraryId() { return libraryId; }

    /** @see java.lang.Object#hashCode() */
    public int hashCode() { return libraryId.hashCode(); }

    /**
     * Set bytes.
     *
     * @param bytes The byte[].
     */
    public void setBytes(final byte[] bytes) { this.bytes = bytes; }

    /**
     * Set checksum.
     *
     * @param checksum The String.
     */
    public void setChecksum(final String checksum) { this.checksum = checksum; }

    /**
     * Set libraryId.
     *
     * @param libraryId The Long.
     */
    public void setLibraryId(final Long libraryId) { this.libraryId = libraryId; }

    /** @see java.lang.Object#toString() */
    public String toString() {
        return new StringBuffer()
            .append(libraryId)
            .append(":").append(bytes.length)
            .append(":").append(checksum)
            .toString();

    }
}
