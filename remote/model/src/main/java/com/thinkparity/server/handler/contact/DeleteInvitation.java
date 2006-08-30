/*
 * Created On: Aug 29, 2006 3:00:18 PM
 */
package com.thinkparity.server.handler.contact;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.handler.AbstractController;
import com.thinkparity.server.model.contact.ContactModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DeleteInvitation extends AbstractController {

    /** Create DeleteInvitation. */
    public DeleteInvitation() { super("contact:deleteinvitation"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
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
