/*
 * Created On: Jul 19, 2006 2:31:39 PM
 */
package com.thinkparity.wildfire.handler.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public class ReadEmails extends AbstractHandler {

    /** Create Read. */
    public ReadEmails() { super("profile:reademails"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        writeEMails("emails", "emails", readEmails(readJabberId("userId")));
    }

    /**
     * Read the profile's email addresses.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;EMail&gt;</code>.
     */
    private List<EMail> readEmails(final JabberId userId) {
        return getProfileModel().readEmails(userId);
    }
}
