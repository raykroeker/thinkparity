/*
 * Created On: Jun 14, 2006 4:10:49 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.online;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.browser.platform.Platform;

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
        final Environment environment = platform.getEnvironment();
        Socket socket;
        try {
            socket = new Socket(
                    environment.getXMPPHost(), environment.getXMPPPort());
            socket.close();
            return Boolean.TRUE;
        } catch (final UnknownHostException uhx) {
            return Boolean.FALSE;
        } catch (final IOException iox) {
            return Boolean.FALSE;
        }
    }
}
