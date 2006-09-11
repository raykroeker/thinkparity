/*
 * Created On: Jun 22, 2006 10:08:19 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.io.xmpp.handler;


import com.thinkparity.ophelia.model.Constants.Connection;
import com.thinkparity.ophelia.model.io.xmpp.XMPPSession;
import com.thinkparity.ophelia.model.io.xmpp.XMPPSessionManager;
import com.thinkparity.ophelia.model.session.Credentials;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class AbstractRemoteHandler extends AbstractIOHandler {

    /** The remote session credentials. */
    private final Credentials credentials;

    /** Create AbstractRemoteHandler. */
    public AbstractRemoteHandler(final Credentials credentials) {
        super();
        this.credentials = credentials;
    }

    /**
     * Open an authenticated session.
     * 
     * @return An authenticated xmpp session.
     */
    protected XMPPSession openAuthenticatedSession() {
        return XMPPSessionManager.openAuthenticated(
                Connection.SERVER_HOST, Connection.SERVER_PORT, credentials);
    }
}
