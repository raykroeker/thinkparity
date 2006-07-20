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

    /**
     * Obtain an api log id.
     * 
     * @param api
     *            An api.
     * @return An api log id.
     */
    private static StringBuffer getApiId(final String api) {
        return getControllerId("[READ]").append(" ").append(api);
    }

    /** Create Read. */
    public Read() { super("profile:read"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logger.info(getApiId("[SERVICE]"));
        final Profile profile = read(readJabberId("jabberId"));

        if(null != profile) {
            writeStrings(Xml.Profile.EMAILS, Xml.Profile.EMAIL, profile.getEmails());
            writeJabberId(Xml.Profile.JABBER_ID, profile.getId());
            writeString(Xml.Profile.NAME, profile.getName());
            writeString(Xml.Profile.ORGANIZATION, profile.getOrganization());
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
