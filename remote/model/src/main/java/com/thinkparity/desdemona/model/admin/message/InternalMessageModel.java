/*
 * Created On:  15-Nov-07 1:16:24 PM
 */
package com.thinkparity.desdemona.model.admin.message;

import java.util.Calendar;

import com.thinkparity.codebase.model.util.InvalidTokenException;
import com.thinkparity.codebase.model.util.Token;


/**
 * <b>Title:</b>thinkParity Desdemona Admin Internal Message Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalMessageModel extends MessageModel {

    /**
     * Create a channel.
     * 
     * @param token
     *            A <code>Token</code>.
     * @param channel
     *            A <code>Channel</code>.
     */
    void createChannel(Token token, Channel channel)
            throws InvalidTokenException;

    /**
     * Delete expired messages.
     * 
     * @param token
     *            A <code>Token</code>.
     * @param expiryDate
     *            A <code>Calendar</code>.
     * @throws InvalidTokenException
     */
    void expire(Token token, Calendar expiryDate) throws InvalidTokenException;

    /**
     * Read a channel.
     * 
     * @param token
     *            A <code>Token</code>.
     * @param channel
     *            A <code>Channel</code>.
     */
    Channel readChannel(Token token, String name) throws InvalidTokenException;
}
