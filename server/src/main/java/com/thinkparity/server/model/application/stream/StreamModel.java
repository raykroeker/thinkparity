/*
 * Generated On: Oct 22 06 10:33:07 AM
 */
package com.thinkparity.desdemona.model.stream;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.model.AbstractModel;

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
	public static StreamModel getModel(final com.thinkparity.desdemona.model.session.Session session) {
		return new StreamModel(session);
	}

    /**
     * Create StreamModel.
     *
     * @param workspace
     *      The thinkParity workspace.
     */
    protected StreamModel(final com.thinkparity.desdemona.model.session.Session session) {
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
     * Initialize a session. If a previous session for the user exists; it will
     * be re-used to allow resume functionality.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>Session</code>.
     */
    public StreamSession initializeSession(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().initializeSession(userId);
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
