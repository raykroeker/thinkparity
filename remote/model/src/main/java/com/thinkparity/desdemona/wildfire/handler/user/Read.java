/*
 * Created On: Sep 8, 2006 4:43:34 PM
 */
package com.thinkparity.desdemona.wildfire.handler.user;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Read extends AbstractHandler {

    /** Create ReadUser. */
    public Read() { super("user:read"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final User user = read(readJabberId("userId"));
        logger.logVariable("user", user);
        if (null != user) {
            writeJabberId("id", user.getId());
            writeString("name", user.getName());
            if (user.isSetOrganization())
                writeString("organization", user.getOrganization());
            if (user.isSetTitle())
                writeString("title", user.getTitle());
        }
    }

    /**
     * Read a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    private User read(final JabberId userId) {
        return getUserModel().read(userId);
    }
}
