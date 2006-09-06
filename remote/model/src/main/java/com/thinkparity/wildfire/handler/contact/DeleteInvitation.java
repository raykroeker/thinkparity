/*
 * Created On: Aug 29, 2006 3:00:18 PM
 */
package com.thinkparity.wildfire.handler.contact;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.contact.ContactModel;

import com.thinkparity.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DeleteInvitation extends AbstractHandler {

    /** Create DeleteInvitation. */
    public DeleteInvitation() { super("contact:deleteinvitation"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        deleteInvitation(readJabberId("userId"), readEMail("invitedAs"),
                readCalendar("deletedOn"));
    }

    /**
     * @see ContactModel#deleteInvitation(JabberId, EMail, Calendar)
     */
    private void deleteInvitation(final JabberId userId, final EMail invitedAs,
            final Calendar deletedOn) {
        getContactModel().deleteInvitation(userId, invitedAs, deletedOn);
    }
}
