/*
 * Created On: Jul 19, 2006 2:31:39 PM
 */
package com.thinkparity.desdemona.wildfire.handler.profile;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.profile.Profile;

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
        final Profile profile =
            logVariable("profile", read(readJabberId("userId")));

        if(null != profile) {
            writeJabberId("id", profile.getId());
            writeString("name", profile.getName());
            if (profile.isSetOrganization())
                writeString("organization", profile.getOrganization());
            if (profile.isSetTitle())
                writeString("title", profile.getTitle());
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
