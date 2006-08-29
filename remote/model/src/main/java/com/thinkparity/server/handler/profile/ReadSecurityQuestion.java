/*
 * Created On: Aug 29, 2006 11:08:50 AM
 */
package com.thinkparity.server.handler.profile;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.handler.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadSecurityQuestion extends AbstractController {

    /** Create ReadSecurityQuestion. */
    public ReadSecurityQuestion() { super("profile:readsecurityquestion"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        writeString("securityQuestion", readSecurityQuestion(readJabberId("userId")));
    }

    /**
     * Read the security question for the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A security question <code>String</code>.
     */
    private String readSecurityQuestion(final JabberId userId) {
        return getProfileModel().readSecurityQuestion(userId);
    }
}
