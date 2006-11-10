/*
 * Generated On: Oct 22 06 10:33:07 AM
 */
package com.thinkparity.desdemona.model.stream;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity Stream Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public class StreamModel extends AbstractModel<StreamModelImpl> {

	/**
     * Obtain a thinkParity stream interface.
     * 
     * @return A thinkParity <code>StreamModel</code>.
     */
    public static StreamModel getModel() {
        return new StreamModel();
    }

    /**
     * Obtain a thinkParity Stream interface.
     * 
     * @return A thinkParity Stream interface.
     */
    public static StreamModel getModel(final Session session) {
        return new StreamModel(session);
    }

    /**
     * Obtain a thinkParity Stream interface.
     * 
     * @return A thinkParity Stream interface.
     */
    public static InternalStreamModel getInternalModel(final Context context,
            final Session session) {
        return new InternalStreamModel(context, session);
    }

    /**
     * Create StreamModel.
     *
     * @param workspace
     *      The thinkParity workspace.
     */
    protected StreamModel(final Session session) {
        super(new StreamModelImpl(session));
    }

    /**
     * Create StreamModel.
     *
     * @param workspace
     *      The thinkParity workspace.
     */
    private StreamModel() {
        super(new StreamModelImpl());
    }

    /**
     * Create a stream.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param sessionId
     *            A session id <code>String</code>.
     * @return A stream id <code>String</code>.
     */
    public String create(final JabberId userId, final String sessionId) {
        synchronized (getImplLock()) {
            return getImpl().create(userId, sessionId);
        }
    }

    /**
     * Create a session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>StreamSession</code>.
     */
    public StreamSession createSession(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().createSession(userId);
        }
    }

    /**
     * Delete a stream.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param sessionId
     *            A stream session id <code>String</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    public void delete(final JabberId userId, final String sessionId,
            final String streamId) {
        synchronized (getImplLock()) {
            getImpl().delete(userId, sessionId, streamId);
        }
    }

    /**
     * Delete a session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param sessionId
     *            A session id <code>String</code>.
     */
    public void deleteSession(final JabberId userId, final String sessionId) {
        synchronized (getImplLock()) {
            getImpl().deleteSession(userId, sessionId);
        }
    }

    /**
     * Read a session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param sessionId
     *            A session id <code>String</code>.
     * @return A <code>StreamSession</code>.
     */
    public StreamSession readSession(final JabberId userId,
            final String sessionId) {
        synchronized (getImplLock()) {
            return getImpl().readSession(userId, sessionId);
        }
    }

    /**
     * Start the stream service.
     *
     */
    public void start() {
        synchronized (getImplLock()) {
            getImpl().start();
        }
    }

    /**
     * Stop the stream service.
     *
     */
    public void stop() {
        synchronized (getImplLock()) {
            getImpl().stop();
        }
    }
}
