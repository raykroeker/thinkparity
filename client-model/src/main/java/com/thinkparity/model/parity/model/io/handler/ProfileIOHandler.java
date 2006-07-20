/*
 * Created On: Jul 17, 2006 12:37:07 PM
 */
package com.thinkparity.model.parity.model.io.handler;

import com.thinkparity.model.parity.model.io.IOHandler;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public interface ProfileIOHandler extends IOHandler {
    public void create(final Profile profile);
    public Profile read(final JabberId jabberId);
    public void update(final Profile profile);
}
