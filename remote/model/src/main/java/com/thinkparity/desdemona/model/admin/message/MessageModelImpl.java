/*
 * Created On:  15-Nov-07 1:17:58 PM
 */
package com.thinkparity.desdemona.model.admin.message;

import java.util.Calendar;

import javax.sql.DataSource;

import com.thinkparity.codebase.model.util.InvalidTokenException;
import com.thinkparity.codebase.model.util.Token;

import com.thinkparity.desdemona.model.admin.AdminModel;
import com.thinkparity.desdemona.model.io.IOService;
import com.thinkparity.desdemona.model.io.sql.MessageSql;

import com.thinkparity.desdemona.service.persistence.PersistenceService;

/**
 * <b>Title:</b>thinkParity Desdemona Admin Model Message Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MessageModelImpl extends AdminModel implements MessageModel,
        InternalMessageModel {

    /** A message io interface. */
    private MessageSql messageIO;

    /**
     * Create MessageModelImpl.
     *
     */
    public MessageModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.admin.message.InternalMessageModel#createChannel(com.thinkparity.codebase.model.util.Token, com.thinkparity.desdemona.model.admin.message.Channel)
     *
     */
    @Override
    public void createChannel(final Token token, final Channel channel)
            throws InvalidTokenException {
        validate(token);
        try {
            messageIO.createChannel(channel);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.admin.message.MessageModel#enqueue(com.thinkparity.codebase.model.util.Token,
     *      com.thinkparity.desdemona.model.admin.message.Channel,
     *      com.thinkparity.desdemona.model.admin.message.Message)
     * 
     */
    @Override
    public void enqueue(final Token token, final Channel channel, final Message message)
            throws InvalidTokenException {
        validate(token);
        try {
            messageIO.create(channel, message);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.admin.message.InternalMessageModel#delete(com.thinkparity.codebase.model.util.Token, java.util.Calendar)
     *
     */
    @Override
    public void expire(final Token token, final Calendar expiryDate)
            throws InvalidTokenException {
        validate(token);
        try {
            messageIO.expire(expiryDate);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.admin.message.InternalMessageModel#readChannel(com.thinkparity.codebase.model.util.Token, java.lang.String)
     *
     */
    @Override
    public Channel readChannel(Token token, String name)
            throws InvalidTokenException {
        validate(token);
        try {
            return messageIO.readChannel(name);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.admin.AdminModel#initialize()
     *
     */
    @Override
    protected void initialize() {
        final DataSource dataSource = PersistenceService.getInstance().getOperationalDataSource();
        messageIO = IOService.getInstance().getFactory().newMessageIO(dataSource);
    }

    /**
     * Validate the token.
     * 
     * @param token
     *            A <code>Token</code>.
     * @throws InvalidTokenException
     */
    private void validate(final Token token) throws InvalidTokenException {
        if (token == MessageBus.getToken()) {
            return;
        } else {
            throw new InvalidTokenException();
        }
    }
}
