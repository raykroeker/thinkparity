/*
 * Created On: Jul 19, 2006 2:31:39 PM
 */
package com.thinkparity.desdemona.wildfire.handler.profile;

import com.thinkparity.codebase.jabber.JabberId;


import com.thinkparity.desdemona.model.Constants.Xml;
import com.thinkparity.desdemona.model.profile.Profile;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class Read extends AbstractHandler {

    /** Create Read. */
    public Read() { super("profile:read"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
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
