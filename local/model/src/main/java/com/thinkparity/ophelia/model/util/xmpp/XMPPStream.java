/*
 * Created On: Thu Sep 28 09:24:29 PDT 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;

final class XMPPStream extends AbstractXMPP {

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
        final XMPPMethod create = new XMPPMethod("stream:create");
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
        final XMPPMethod createSession = new XMPPMethod("stream:createsession");
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
        final XMPPMethod delete = new XMPPMethod("stream:delete");
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
        final XMPPMethod deleteSession = new XMPPMethod("stream:deletesession");
        deleteSession.setParameter("userId", userId);
        deleteSession.setParameter("sessionId", session.getId());
        execute(deleteSession);
    }

    StreamSession readSession(final JabberId userId, final String sessionId) {
        final XMPPMethod readSession = new XMPPMethod("stream:readsession");
        readSession.setParameter("userId", userId);
        readSession.setParameter("sessionId", sessionId);
        return execute(readSession, Boolean.TRUE).readResultStreamSession("session");
    }
}
