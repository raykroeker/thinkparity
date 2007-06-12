/*
 * Generated On: Oct 22 06 10:33:07 AM
 */
package com.thinkparity.desdemona.model.stream;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * <b>Title:</b>thinkParity Stream Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public interface StreamModel {

    /**
     * Create a stream for the model user.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @return A stream id <code>String</code>.
     */
    public String create(final String sessionId);

    /**
     * Create a session for the model user.
     * 
     * @return A <code>StreamSession</code>.
     */
    public StreamSession createSession();

    /**
     * Delete a stream for the model user.
     * 
     * @param sessionId
     *            A stream session id <code>String</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    public void delete(final String sessionId, final String streamId);

    /**
     * Delete a session for the model user.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     */
    public void deleteSession(final String sessionId);

    /**
     * Read a session for the model user.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @return A <code>StreamSession</code>.
     */
    public StreamSession readSession(final String sessionId);
}
