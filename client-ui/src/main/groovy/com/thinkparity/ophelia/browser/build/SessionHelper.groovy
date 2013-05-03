/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.ophelia.browser.build

import com.thinkparity.codebase.model.session.Credentials

import com.thinkparity.service.AuthToken
import com.thinkparity.service.SessionService
import com.thinkparity.service.client.ClientServiceFactory

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

    /** A session web-service. */
    SessionService sessionService

    /**
     * Initialize the session helper.  Obtain an instance of the session model.
     *
     */
    void init() {
        if (null == sessionService) {
            sessionService = ClientServiceFactory.getInstance().getSessionService()
            configuration["thinkparity.service-session"] = sessionService
        }
    }

    /**
     * Login.
     *
     */
    void login() {
        init()
        final AuthToken authToken = sessionService.login(
            configuration["thinkparity.credentials"])
        configuration["thinkparity.auth-token"] = authToken
    }

    /**
     * Determine if the session is established.
     *
     * @return True if the session is established.
     */
    Boolean isOnline() {
        init()
        return null != configuration["thinkparity.auth-token"]
    }

    /**
     * Terminate the session.
     *
     */
    void logout() {
        sessionService.logout(configuration["thinkparity.auth-token"])
    }
}
