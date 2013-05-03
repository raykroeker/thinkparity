/*
 * Created On:  15-Nov-07 1:16:07 PM
 */
package com.thinkparity.desdemona.model.admin.message;

import com.thinkparity.codebase.model.util.InvalidTokenException;
import com.thinkparity.codebase.model.util.Token;

/**
 * <b>Title:</b>thinkParity Desdemona Admin Model Message Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface MessageModel {

    /**
     * Enqueue a message. The appropriate mechanism to enqueue a message is via
     * the message bus.
     * 
     * @param token
     *            A <code>Token</code>.
     * @param channel
     *            A <code>Channel</code>.
     * @param message
     *            A <code>Message</code>.
     * @throws InvalidTokenException
     *             if the token is invalid
     * 
     * @see {@link MessageBus#deliver(String)}
     * @see {@link MessageBus#deliver(com.thinkparity.desdemona.model.admin.message.Message.Level, String)
     * @see {@link MessageBus#deliver(String, String, Object...)}
     * @see {@link MessageBus#deliver(com.thinkparity.desdemona.model.admin.message.Message.Level, String, String, Object...)}
     */
    void enqueue(Token token, Channel channel, Message message)
            throws InvalidTokenException;
}
