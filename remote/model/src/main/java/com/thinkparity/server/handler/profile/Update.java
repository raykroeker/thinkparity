/*
 * Created On: Aug 30, 2006 1:31:58 PM
 */
package com.thinkparity.server.handler.profile;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.profile.ProfileModel;

import com.thinkparity.server.handler.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Update extends AbstractController {

    /** Create Update. */
    public Update() { super("profile:update"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
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
