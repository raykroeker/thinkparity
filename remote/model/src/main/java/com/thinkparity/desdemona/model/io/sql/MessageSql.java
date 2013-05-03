/*
 * Created On:  15-Nov-07 2:25:42 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import java.util.Calendar;

import javax.sql.DataSource;

import com.thinkparity.desdemona.model.admin.message.Channel;
import com.thinkparity.desdemona.model.admin.message.Message;
import com.thinkparity.desdemona.model.admin.message.MessageData;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * <b>Title:</b>thinkParity Desdemona Admin Model Message IO<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MessageSql extends AbstractSql {

    /** Sql to create a message. */
    private static final String SQL_CREATE =
        new StringBuilder("insert into TPSD_MSG ")
        .append("(NODE_USERNAME,MSG_TIMESTAMP,MSG_CHANNEL,MSG) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create a channel. */
    private static final String SQL_CREATE_CHANNEL =
        new StringBuilder("insert into TPSD_MSG_CHANNEL ")
        .append("(CHANNEL_NAME) ")
        .append("values (?)")
        .toString();

    /** Sql to create a message. */
    private static final String SQL_CREATE_DATA =
        new StringBuilder("insert into TPSD_MSG_DATA ")
        .append("(MSG_ID,DATA_ORDINAL,DATA_NAME,DATA_TYPE,DATA_VALUE) ")
        .append("values (?,?,?,?,?)")
        .toString();

    /** Sql to create a message. */
    private static final String SQL_EXPIRE =
        new StringBuilder("delete from TPSD_MSG ")
        .append("where MSG_TIMESTAMP<?")
        .toString();

    /** Sql to create a message. */
    private static final String SQL_EXPIRE_DATA =
        new StringBuilder("delete from TPSD_MSG_DATA ")
        .append("where MSG_ID in ")
        .append("(select MSG_ID from TPSD_MSG where MSG_TIMESTAMP<?)")
        .toString();

    /** Sql to read a channel. */
    private static final String SQL_READ_CHANNEL =
        new StringBuilder("select CHANNEL_ID,CHANNEL_NAME ")
        .append("from TPSD_MSG_CHANNEL ")
        .append("where CHANNEL_NAME=?")
        .toString();

    /**
     * Create MessageSql.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     */
    public MessageSql(final DataSource dataSource) {
        super(dataSource, Boolean.TRUE);
    }

    /**
     * Create a message.
     * 
     * @param message
     *            A <code>Message</code>.
     */
    public void create(final Channel channel, final Message message) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE);
            session.setString(1, message.getNode().getUsername());
            session.setCalendar(2, message.getTimestamp());
            session.setLong(3, channel.getId());
            session.setString(4, message.getMessage());
            if (1 != session.executeUpdate()) {
                throw panic("Could not create message.");
            }
            message.setId(session.getIdentity("TPSD_MSG"));

            if (message.isSetData()) {
                int ordinal = 0;
                session.prepareStatement(SQL_CREATE_DATA);
                session.setLong(1, message.getId());
                for (final MessageData<Long> datum : message.getLongData()) {
                    session.setInt(2, Integer.valueOf(ordinal++));
                    session.setString(3, datum.getName());
                    session.setString(4, "java/lang/Long");
                    session.setString(5, datum.getStringValue());
                    if (1 != session.executeUpdate()) {
                        throw panic("Could not create long message data.");
                    }
                }
                for (final MessageData<String> datum : message.getStringData()) {
                    session.setInt(2, Integer.valueOf(ordinal++));
                    session.setString(3, datum.getName());
                    session.setString(4, "java/lang/String");
                    session.setString(5, datum.getStringValue());
                    if (1 != session.executeUpdate()) {
                        throw panic("Could not create string message data.");
                    }
                }
            }

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create a channel.
     * 
     * @param channel
     *            A <code>Channel</code>.
     */
    public void createChannel(final Channel channel) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_CHANNEL);
            session.setString(1, channel.getName());
            if (1 != session.executeUpdate()) {
                throw panic("Could not create channel.");
            }
            channel.setId(session.getIdentity("TPSD_MSG_CHANNEL"));

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Expire messages.
     * 
     * @param expiryDate
     *            A <code>Calendar</code>.
     */
    public void expire(final Calendar expiryDate) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_EXPIRE_DATA);
            session.setCalendar(1, expiryDate);
            session.executeUpdate();

            session.prepareStatement(SQL_EXPIRE);
            session.setCalendar(1, expiryDate);
            session.executeUpdate();

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read a channel by name.
     * 
     * @param name
     *            A <code>String</code>.
     * @return A <code>Channel</code>.
     */
    public Channel readChannel(final String name) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_CHANNEL);
            session.setString(1, name);
            session.executeQuery();
            if (session.nextResult()) {
                return extractChannel(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Extract a channel from the database session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return A <code>Channel</code>.
     */
    private Channel extractChannel(final HypersonicSession session) {
        final Channel channel = new Channel();
        channel.setId(session.getLong("CHANNEL_ID"));
        channel.setName(session.getString("CHANNEL_NAME"));
        return channel;
    }
}
