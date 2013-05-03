/*
 * Created On: Jun 10, 2006 9:05:56 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.session;

import com.thinkparity.ophelia.model.events.SessionAdapter;
import com.thinkparity.ophelia.model.events.SessionListener;

/**
 * The session application's event dispatcher.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class EventDispatcher {

    /** The session application. */
    private final SessionApplication application;

    /** A thinkParity session listener. */
    private SessionListener sessionListener;

    /** Create EventDispatcher. */
    EventDispatcher(final SessionApplication application) {
        super();
        this.application = application;
    }

    /**
     * End the event dispatcher.
     *
     */
    void end() {
        application.getSessionModel().removeListener(sessionListener);
        sessionListener = null;
    }

    /**
     * Start the event dispatcher.
     *
     */
    void start() {
        sessionListener = createSessionListener();
        application.getSessionModel().addListener(sessionListener);
    }

    /**
     * Create a thinkParity session listener.
     * 
     * @return A thinkParity session listener.
     */
    private SessionListener createSessionListener() {
        return new SessionAdapter() {
            @Override
            public void sessionEstablished() {
                application.fireConnectionOnline();
            }
            @Override
            public void sessionTerminated() {
                application.fireConnectionOffline();
            }
        };
    }
}
