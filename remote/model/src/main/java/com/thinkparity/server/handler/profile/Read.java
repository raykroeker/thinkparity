/*
 * Created On: Jul 19, 2006 2:31:39 PM
 */
package com.thinkparity.server.handler.profile;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.profile.Profile;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class Read extends AbstractController {

    /** Create Read. */
    public Read() { super("profile:read"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        final Profile profile = read(readJabberId("jabberId"));

        if(null != profile) {
            writeJabberId(Xml.Profile.JABBER_ID, profile.getId());
            writeString(Xml.Profile.NAME, profile.getName());
            if (profile.isSetOrganization())
                writeString(Xml.Profile.ORGANIZATION, profile.getOrganization());
            if (profile.isSetTitle())
                writeString(Xml.Profile.TITLE, profile.getTitle());
        }
    }

    /**
     * Read the profile.
     * 
     * @param jabberId
     *            A jabber id.
     * @return A profile.
     */
    private Profile read(final JabberId jabberId) {
        return getProfileModel().read(jabberId);
    }
}
