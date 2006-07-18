/*
 * Created On: Jul 17, 2006 12:41:13 PM
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.parity.model.profile.Profile;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class ProfileIOHandler extends AbstractIOHandler implements
        com.thinkparity.model.parity.model.io.handler.ProfileIOHandler {

    /** Create ProfileIOHandler. */
    public ProfileIOHandler() { super(); }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ProfileIOHandler#create(com.thinkparity.model.parity.model.profile.Profile)
     */
    public void create(final Profile profile) {
        throw Assert.createNotYetImplemented("ProfileIOHandler#create");
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ProfileIOHandler#read()
     */
    public Profile read() {
        throw Assert.createNotYetImplemented("ProfileIOHandler#read()");
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ProfileIOHandler#update(com.thinkparity.model.parity.model.profile.Profile)
     */
    public void update(final Profile profile) {
        throw Assert.createNotYetImplemented("ProfileIOHandler#update");
    }

    /**
     * Extract the profile from the database session.
     * 
     * @param session
     *            A database session.
     * @return A profile.
     */
    Profile extractProfile(final Session session) {
        final Profile profile = new Profile();
        profile.setJabberId(session.getQualifiedUsername("JABBER_ID"));
        return profile;
    }
}
