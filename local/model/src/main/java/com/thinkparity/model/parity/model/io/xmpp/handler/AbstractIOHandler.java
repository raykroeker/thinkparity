/*
 * Created On: Fri May 12 2006 11:34 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.io.xmpp.handler;

import org.apache.log4j.Logger;

import com.thinkparity.model.LoggerFactory;
import com.thinkparity.model.Constants.Connection;
import com.thinkparity.model.parity.model.io.xmpp.XMPPSession;
import com.thinkparity.model.parity.model.io.xmpp.XMPPSessionManager;

/**
 * An abstraction of an xmpp io handler.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
abstract class AbstractIOHandler {

    /** An apache logger. */
    protected final Logger logger;

    /** Create AbstractIOHandler. */
    protected AbstractIOHandler() {
        super();
        this.logger = LoggerFactory.getLogger(getClass());
    }

    /**
     * Open an xmpp session.
     *
     * @return The xmpp session.
     */
    protected XMPPSession openSession() {
        return XMPPSessionManager.open(
                Connection.SERVER_HOST, Connection.SERVER_PORT);
    }
}
