/*
 * Created On: Jul 19, 2006 2:31:39 PM
 */
package com.thinkparity.server.handler.profile;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.profile.ProfileEMail;

import com.thinkparity.server.handler.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class ReadEmails extends AbstractController {

    /** Create Read. */
    public ReadEmails() { super("profile:reademails"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        writeProfileEMails("emails", readEMails(readJabberId("jabberId")));
    }

    /**
     * Read the profile.
     * 
     * @param jabberId
     *            A jabber id.
     * @return A profile.
     */
    private List<ProfileEMail> readEMails(final JabberId jabberId) {
        return getProfileModel().readEMails(jabberId);
    }
}
