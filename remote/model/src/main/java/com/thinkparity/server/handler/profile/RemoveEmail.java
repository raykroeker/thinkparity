/*
 * Created On: Aug 28, 2006 9:23:57 AM
 */
package com.thinkparity.server.handler.profile;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.handler.AbstractController;

/** 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RemoveEmail extends AbstractController {

    /** Create RemoveEmail. */
    public RemoveEmail() { super("profile:removeemail"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        removeEmail(readJabberId("userId"), readEMail("email"));
    }

    /**
     * Remove an email from the user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    private void removeEmail(final JabberId userId, final EMail email) {
        getProfileModel().removeEmail(userId, email);
    }
}
