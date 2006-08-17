/*
 * Feb 28, 2006
 */
package com.thinkparity.server.handler.contact;

import java.util.Calendar;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 * TODO Rename to extend invitation.
 */
public class InviteContact extends AbstractController {

    /** Create InviteContact. */
	public InviteContact() { super("contact:invite"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        invite(readString(Xml.Contact.EMAIL), readCalendar(Xml.All.EXECUTED_ON));
    }

    /**
     * Invite a contact.
     * 
     * @param email
     *            An e-mail address.
     */
    private void invite(final String email, final Calendar invitedOn) {
        getContactModel().invite(email, invitedOn);
    }
}
