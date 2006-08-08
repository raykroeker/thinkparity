/*
 * Created On: Jul 19, 2006 2:31:39 PM
 */
package com.thinkparity.server.handler.profile;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.profile.Profile;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;
import com.thinkparity.server.model.ParityServerModelException;

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
        try { return getProfileModel().read(jabberId); }
        catch(final ParityServerModelException psmx) {
            logger.error(psmx);
            throw new RuntimeException(psmx);
        }
    }
}
