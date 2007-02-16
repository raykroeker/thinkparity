/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.ophelia.browser.build

import com.thinkparity.codebase.model.session.Credentials

import com.thinkparity.ophelia.model.util.xmpp.XMPPSession
import com.thinkparity.ophelia.model.util.xmpp.XMPPSessionImpl

/**
 * <b>Title:</b>thinkParity OpheliaUI Build Task Session Helper<br>
 * <b>Description:</b>A helper class to maintain a session to the server.<br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class SessionHelper {

    /** A build task configuration <code>Map</code>. */
    Map configuration

    /** An instance of <code>XMPPSession</code>. */
    XMPPSession xmppSession

    /**
     * Initialize the session helper.  Obtain an instance of the session model.
     *
     */
    void init() {
        if (null == xmppSession) {
            xmppSession = new XMPPSessionImpl()
            configuration["thinkparity.xmpp-session"] = xmppSession
        }
    }

    /**
     * Login.
     *
     */
    void login() {
        init()
        xmppSession.login(configuration["thinkparity.environment"],
            configuration["thinkparity.credentials"])
    }

    /**
     * Determine if the session is established.
     *
     * @return True if the session is established.
     */
    Boolean isLoggedIn() {
        init()
        return xmppSession.isLoggedIn()
    }

    /**
     * Terminate the session.
     *
     */
    void logout() {
        xmppSession.logout()
    }
}
