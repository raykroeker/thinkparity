/*
 * Created On: Aug 26, 2006 10:32:39 AM
 */
package com.thinkparity.server.handler.profile;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.handler.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class AddEmail extends AbstractController {

    /** Create AddEmail. */
    public AddEmail() { super("profile:addemail"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        addEmail(readJabberId("userId"), readEMail("email"));
    }

    /**
     * Add an email to the user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    private void addEmail(final JabberId userId, final EMail email) {
        getProfileModel().addEmail(userId, email);
    }
}
