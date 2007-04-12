/*
 * Created On: Jun 14, 2006 4:10:49 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.online;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.session.SessionModel;

import com.thinkparity.ophelia.browser.platform.Platform;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class OnlineHelper {

    /** The <code>Platform</code>. */
    private final Platform platform;
    
    /** An instance of <code>SessionModel</code>. */
    private final SessionModel model;

    /**
     * Create OnlineHelper.
     * 
     * @param platform
     *            The <code>Platform</code>.
     */
    public OnlineHelper(final Platform platform) {
        super();
        this.platform = platform;
        this.model = platform.getModelFactory().getSessionModel(getClass());
    }

    /**
     * Determine whether or not the platform is online.
     * 
     * @return True if the platform is online.
     */
    public Boolean isOnline() {
        return model.isOnline();
    }

    /**
     * Determine whether or not the platform has online capability.
     * 
     * @return True if the platform has online capability.
     */
    public Boolean isXMPPHostReachable() {
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
