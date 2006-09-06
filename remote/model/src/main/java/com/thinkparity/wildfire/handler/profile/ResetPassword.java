/*
 * Created On: Aug 29, 2006 9:11:55 AM
 */
package com.thinkparity.wildfire.handler.profile;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ResetPassword extends AbstractHandler {

    /** Create ResetPassword. */
    public ResetPassword() { super("profile:resetpassword"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        writeString("password", resetCredentials(readJabberId("userId"), readString("securityAnswer")));
    }

    /**
     * Reset a user's credentials.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param securityAnswer
     *            A security question answer <code>String</code>.
     */
    private String resetCredentials(final JabberId userId,
            final String securityAnswer) {
        return getProfileModel().resetPassword(userId, securityAnswer);
    }
}
