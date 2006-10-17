/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.profile;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.user.Token;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadToken extends AbstractHandler {

    /** Create ReadToken. */
    public ReadToken() {
        super("profile:readtoken");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final Token token = readToken(readJabberId("userId"));
        writeToken("token", token);
    }

    /**
     * Read the user's token.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>Token</code>.
     */
    private Token readToken(final JabberId userId) {
        return getProfileModel().readToken(userId);
    }
}
