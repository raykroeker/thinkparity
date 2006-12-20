/*
 * Created On: Aug 21, 2006 8:54:27 AM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.util.xstream.XStreamUtil;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;

import com.thoughtworks.xstream.XStream;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class AbstractXMPP<T extends EventListener> {

    /** An <code>XStreamUtil</code> instance. */
    protected static final XStreamUtil XSTREAM_UTIL;

    /** An apache logger. */
    protected static final Log4JWrapper logger;

    static {
        logger = new Log4JWrapper();
        XSTREAM_UTIL = XStreamUtil.getInstance();
    }

    /** The xmpp core functionality. */
    protected final XMPPCore xmppCore;

    /** An xstream xml serializer. */
    protected final XStream xstream;

    /**
     * Create AbstractXMPP.
     * 
     */
    protected AbstractXMPP(final XMPPCore xmppCore) {
        super();
        this.xmppCore = xmppCore;
        this.xstream = new XStream();
    }

    /**
     * Assert that the user id matched that of the authenticated user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @see #isAuthenticatedUser(JabberId)
     */
    protected void assertIsAuthenticatedUser(final JabberId userId) {
        Assert.assertTrue("USER DOES NOT MATCH AUTHENTICATED USER",
                isAuthenticatedUser(userId));
    }

    /**
     * Execute an xmpp method.
     * 
     * @param method
     *            An xmpp method.
     * @return An xmpp method response.
     */
    protected XMPPMethodResponse execute(final XMPPMethod method) {
        return xmppCore.execute(method);
    }

    /**
     * Execute an xmpp method.
     * 
     * @param method
     *            An xmpp method.
     * @param assertResponse
     *            A <code>Boolean</code> flag indicating an expected response.
     * @return An xmpp method response.
     */
    protected XMPPMethodResponse execute(final XMPPMethod method,
            final Boolean assertResponse) {
        return xmppCore.execute(method, assertResponse);
    }

    /**
     * Determine if the user id is the authenticated user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the user id matches the currently authenticated user.
     */
    protected Boolean isAuthenticatedUser(final JabberId userId) {
        return xmppCore.getUserId().equals(userId);
    }

    /**
     * Translate an error into a runtime exception logging it if thinkParity
     * does not already know about the error.
     * 
     * @param t
     *            An error <code>Throwable</code>
     * @return A <code>RuntimeException</code>
     */
    protected RuntimeException translateError(final Throwable t) {
        return xmppCore.translateError(t);
    }
}
