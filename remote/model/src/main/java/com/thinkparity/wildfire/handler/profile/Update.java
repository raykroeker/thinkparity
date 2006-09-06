/*
 * Created On: Aug 30, 2006 1:31:58 PM
 */
package com.thinkparity.wildfire.handler.profile;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.profile.ProfileModel;

import com.thinkparity.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Update extends AbstractHandler {

    /** Create Update. */
    public Update() { super("profile:update"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        update(readJabberId("userId"), readString("name"),
                readString("organization"), readString("title"));
    }

    /**
     * @see ProfileModel#update(JabberId, String, String, String)
     * 
     */
    private void update(final JabberId userId, final String name,
            final String organization, final String title) {
        getProfileModel().update(userId, name, organization, title);
    }
}
