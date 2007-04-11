/*
 * Created On: Thu Sep 28 09:24:29 PDT 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.util.xmpp.event.StreamListener;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class XMPPStream extends AbstractXMPP<StreamListener> {

    /**
     * Create XMPPSystem.
     *
     * @param core
     *      The <code>XMPPCore</code> interface.
     */
    XMPPStream(final XMPPCore core) {
        super(core);
    }

    /**
     * Create a stream.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param session
     *            A <code>StreamSession</code>.
     * @return A stream id <code>String</code>.
     */
    String create(final JabberId userId, final StreamSession session) {
        final XMPPMethod create = xmppCore.createMethod("stream:create");
        create.setParameter("userId", userId);
        create.setParameter("sessionId", session.getId());
        return execute(create, Boolean.TRUE).readResultString("streamId");
    }

    /**
     * Create a stream session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>StreamSession</code>.
     */
    StreamSession createSession(final JabberId userId) {
        final XMPPMethod createSession = xmppCore.createMethod("stream:createsession");
        createSession.setParameter("userId", userId);
        return execute(createSession, Boolean.TRUE).readResultStreamSession("session");
    }

    /**
     * Delete a stream.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param session
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void delete(final JabberId userId, final StreamSession session,
            final String streamId) {
        final XMPPMethod delete = xmppCore.createMethod("stream:delete");
        delete.setParameter("userId", userId);
        delete.setParameter("sessionId", session.getId());
        delete.setParameter("streamId", streamId);
        execute(delete);
    }

    /**
     * Delete a stream session.
     * 
     * @param session
     *            A stream <code>Session</code>.
     */
    void deleteSession(final JabberId userId, final StreamSession session) {
        final XMPPMethod deleteSession = xmppCore.createMethod("stream:deletesession");
        deleteSession.setParameter("userId", userId);
        deleteSession.setParameter("sessionId", session.getId());
        execute(deleteSession);
    }

    StreamSession readSession(final JabberId userId, final String sessionId) {
        final XMPPMethod readSession = xmppCore.createMethod("stream:readsession");
        readSession.setParameter("userId", userId);
        readSession.setParameter("sessionId", sessionId);
        return execute(readSession, Boolean.TRUE).readResultStreamSession("session");
    }
}
