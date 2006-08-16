/*
 * Created On: Aug 14, 2006 10:49:04 AM
 */
package com.thinkparity.model.parity.model.contact;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class OutgoingInvitation extends ContactInvitation {

    /** An e-mail address. */
    private String email;

    /** Create OutgoingInvitation. */
    public OutgoingInvitation() {
        super();
    }

    /**
     * Obtain the email
     *
     * @return The String.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set email.
     *
     * @param email The String.
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * @see com.thinkparity.model.parity.model.contact.ContactInvitation#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(id)
                .toString();
    }
}
