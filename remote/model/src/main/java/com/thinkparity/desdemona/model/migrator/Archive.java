/*
 * Created On:  9-Oct-07 4:05:47 PM
 */
package com.thinkparity.desdemona.model.migrator;

import java.util.UUID;


/**
 * <b>Title:</b>thinkParity Desdemona Model Migrator Archive<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Archive {

    private String md5;

    private Long size;

    private UUID uniqueId;

    /**
     * Create Archive.
     *
     */
    public Archive() {
        super();
    }

    /**
     * Obtain the md5.
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
     * Obtain the uniqueId.
     *
     * @return A <code>UUID</code>.
     */
    public UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * Set the md5.
     *
     * @param md5
     *		A <code>String</code>.
     */
    public void setMD5(final String md5) {
        this.md5 = md5;
    }

    /**
     * Set the size.
     *
     * @param size
     *		A <code>Long</code>.
     */
    public void setSize(final Long size) {
        this.size = size;
    }

    /**
     * Set the uniqueId.
     *
     * @param uniqueId
     *		A <code>UUID</code>.
     */
    public void setUniqueId(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
}
