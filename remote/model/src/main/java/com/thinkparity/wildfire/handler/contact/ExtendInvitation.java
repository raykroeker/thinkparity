/*
 * Feb 28, 2006
 */
package com.thinkparity.wildfire.handler.contact;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.wildfire.handler.AbstractHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 * TODO Rename to extend invitation.
 */
public class ExtendInvitation extends AbstractHandler {

    /** Create ExtendInvitation. */
	public ExtendInvitation() { super("contact:extendinvitation"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        extendInvitation(readJabberId("userId"), readEMail("extendedTo"),
                readCalendar("extendedOn"));
    }

    /**
     * Extend an invitation. If the email is registered within the thinkParity
     * community an invitation will be sent via thinkParity otherwise an
     * invitation will be sent via and email.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param extendedTo
     *            A <code>EMail</code> to invite.
     * @param extendedOn
     *            The date <code>Calendar</code> of the invitation.
     */
    private void extendInvitation(final JabberId userId,
            final EMail extendedTo, final Calendar extendedOn) {
        getContactModel().extendInvitation(userId, extendedTo, extendedOn);
    }
}
