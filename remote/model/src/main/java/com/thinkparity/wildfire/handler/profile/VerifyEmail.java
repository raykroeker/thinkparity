/*
 * Created On: Aug 27, 2006 1:29:56 PM
 */
package com.thinkparity.wildfire.handler.profile;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.wildfire.handler.AbstractHandler;

/** 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class VerifyEmail extends AbstractHandler {

    /** Create VerifyEmail. */
    public VerifyEmail() { super("profile:verifyemail"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        verifyEmail(readJabberId("userId"), readEMail("email"), readString("key"));
    }

    /**
     * Verify an email in the user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     * @param key
     *            An email verification key <code>String</code>.
     */
    private void verifyEmail(final JabberId userId, final EMail email,
            final String key) {
        getProfileModel().verifyEmail(userId, email, key);
    }
}
