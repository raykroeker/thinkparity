/*
 * Created On: Thu Sep 28 09:24:29 PDT 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.util.smackx.packet.AbstractThinkParityIQ;
import com.thinkparity.ophelia.model.util.smackx.packet.AbstractThinkParityIQProvider;
import com.thinkparity.ophelia.model.util.xmpp.events.SystemListener;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;

final class XMPPSystem extends AbstractXMPP<SystemListener> {

    static {
        ProviderManager.addIQProvider("query", "jabber:iq:parity:system:queueupdated", new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandleQueueUpdatedIQ query = new HandleQueueUpdatedIQ();
                boolean isComplete = false;
                while (false == isComplete) {
                    if (isStartTag("updatedOn")) {
                        readCalendar2();
                    } else {
                        isComplete = true;
                    }
                }
                return query;
            }
        });
    }

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
     * @see com.thinkparity.ophelia.model.util.xmpp.AbstractXMPP#addEventHandlers()
     *
     */
    @Override
    protected void addEventHandlers() {
        addEventHandler(new XMPPEventHandler<HandleQueueUpdatedIQ>() {
            public void handleEvent(final HandleQueueUpdatedIQ query) {
                handleQueueUpdated(query);
            }
        }, HandleQueueUpdatedIQ.class);
    }

    /**
     * Process the queue of requests that have accumulated while the user was
     * offline.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    void processQueue(final JabberId userId) {
        logger.logApiId();
        final XMPPMethod processOfflineQueue = new XMPPMethod("system:processqueue");
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

    /**
     * Handle the queue updated remote event.
     * 
     * @param query
     *            The remote event <code>HandleQueueUpdatedIQ</code> event.
     */
    private void handleQueueUpdated(final HandleQueueUpdatedIQ query) {
        processQueue(xmppCore.getUserId());
    }

    /** A queue update event. */
    private static final class HandleQueueUpdatedIQ extends AbstractThinkParityIQ {
    }
}
