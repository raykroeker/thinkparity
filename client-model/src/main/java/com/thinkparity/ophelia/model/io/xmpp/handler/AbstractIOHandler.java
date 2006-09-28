/*
 * Created On: Fri May 12 2006 11:34 PDT
 * $Id$
 */
package com.thinkparity.ophelia.model.io.xmpp.handler;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.io.xmpp.XMPPSession;

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
        this.logger = Logger.getLogger(getClass());
    }

    /**
     * Open an anonymous xmpp session.
     *
     * @return An anonymous xmpp session.
     */
    protected XMPPSession openAnonymousSession() {
        throw Assert.createNotYetImplemented("AbstractIOHandler#openAnonymousSession");
    }
}
