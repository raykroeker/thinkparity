/*
 * Created On: Aug 14, 2006 10:48:55 AM
 */
package com.thinkparity.model.parity.model.contact;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class IncomingInvitation extends ContactInvitation {

    /** A user id. */
    private JabberId userId;

    /** Create IncomingInvitation. */
    public IncomingInvitation() {
        super();
    }

    /**
     * Obtain the userId
     *
     * @return The Long.
     */
    public JabberId getUserId() {
        return userId;
    }

    /**
     * Set userId.
     *
     * @param userId The Long.
     */
    public void setUserId(final JabberId userId) {
        this.userId = userId;
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
