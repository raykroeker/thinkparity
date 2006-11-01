/*
 * Created On: Thu Sep 28 09:24:29 PDT 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.util.xmpp.events.SystemListener;

final class XMPPSystem extends AbstractXMPP<SystemListener> {

    /**
     * Create XMPPSystem.
     *
     * @param core
     *      The <code>XMPPCore</code> interface.
     */
    XMPPSystem(final XMPPCore core) {
        super(core);
    }

    /**
     * Process the queue of requests that have accumulated while the user was
     * offline.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    void processOfflineQueue(final JabberId userId) {
        logger.logApiId();
        final XMPPMethod processOfflineQueue = new XMPPMethod("system:processofflinequeue");
        processOfflineQueue.setParameter("userId", userId);
        execute(processOfflineQueue);
    }

    /**
     * Read the thinkParity remote version.
     *
     * @return The version.
     */
    String readVersion() {
        logger.logApiId();
        final XMPPMethod readVersion = new XMPPMethod("system:readversion");
        return execute(readVersion, Boolean.TRUE).readResultString("version");
    }
}
