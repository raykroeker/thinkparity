/*
 * Created On: Jun 14, 2006 4:10:49 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.online;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.model.workspace.Preferences;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class OnlineHelper {

    /** The thinkParity platform. */
    private final Platform platform;

    /** Create OnlineHelper. */
    public OnlineHelper(final Platform platform) {
        super();
        this.platform = platform;
    }

    /**
     * Determine whether or not the platform has online capability.
     * 
     * @return True if the platform is online.
     */
    public Boolean isOnline() {
        final Preferences preferences = platform.getPreferences();
        Socket socket;
        try {
            socket = new Socket(
                    preferences.getServerHost(), preferences.getServerPort());
            socket.close();
            return Boolean.TRUE;
        } catch (final UnknownHostException uhx) {
            return Boolean.FALSE;
        } catch (final IOException iox) {
            return Boolean.FALSE;
        }
    }
}
