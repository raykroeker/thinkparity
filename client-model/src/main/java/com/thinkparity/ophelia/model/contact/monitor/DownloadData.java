/*
 * Created On:  27-Aug-07 5:44:19 PM
 */
package com.thinkparity.ophelia.model.contact.monitor;

import com.thinkparity.codebase.model.contact.Contact;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DownloadData {

    /** A contact. */
    private Contact contact;

    /** The download size. */
    private Integer size;

    /**
     * Create DownloadData.
     *
     */
    public DownloadData() {
        super();
    }

    /**
     * Obtain the contact.
     *
     * @return A <code>Contact</code>.
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Obtain the size.
     *
     * @return A <code>Integer</code>.
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Set the contact.
     *
     * @param contact
     *		A <code>Contact</code>.
     */
    public void setContact(final Contact contact) {
        this.contact = contact;
    }

    /**
     * Set the size.
     *
     * @param size
     *		A <code>Integer</code>.
     */
    public void setSize(final Integer size) {
        this.size = size;
    }
}
