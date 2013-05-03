/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.io.sql;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class QueueSql extends AbstractSql {

    /** Sql to create an audit event. */
    private static final String SQL_CREATE_AUDIT_EVENT =
        new StringBuilder("insert into TPSD_AUDIT_USER_EVENT_QUEUE ")
        .append("(USER_ID,EVENT_ID,EVENT_DATE,EVENT_PRIORITY,EVENT_FILTER,")
        .append("EVENT_XML) ")
        .append("values (?,?,?,?,?,?)")
        .toString();

    /** Sql to create an event. */
    private static final String SQL_CREATE_EVENT =
        new StringBuilder("insert into TPSD_USER_EVENT_QUEUE ")
        .append("(USER_ID,EVENT_ID,EVENT_DATE,EVENT_PRIORITY,EVENT_FILTER,")
        .append("EVENT_XML) ")
        .append("values (?,?,?,?,?,?)")
        .toString();

    /** Sql to delete an event. */
    private static final String SQL_DELETE_EVENT =
        new StringBuilder("delete from TPSD_USER_EVENT_QUEUE ")
        .append("where USER_ID=? and EVENT_ID=?")
        .toString();

    /** Sql to delete all events for a user. */
    private static final String SQL_DELETE_EVENTS =
        new StringBuilder("delete from TPSD_USER_EVENT_QUEUE ")
        .append("where USER_ID=?")
        .toString();

    /** Sql to read a user's event count. */
    private static final String SQL_READ_EVENT_COUNT =
        new StringBuilder("select COUNT(UEQ.EVENT_ID) EVENT_COUNT ")
        .append("from TPSD_USER_EVENT_QUEUE UEQ ")
        .append("where UEQ.USER_ID=?")
        .toString();

    /** Sql to read events. */
    private static final String SQL_READ_EVENTS =
        new StringBuilder("select UEQ.EVENT_XML ")
        .append("from TPSD_USER_EVENT_QUEUE UEQ ")
        .append("where UEQ.USER_ID=? ")
        .append("order by UEQ.EVENT_PRIORITY asc,UEQ.EVENT_DATE asc")
        .toString();

    /** Sql to read events. */
    private static final String SQL_READ_FILTERED_EVENTS =
        new StringBuilder("select UEQ.EVENT_XML ")
        .append("from TPSD_USER_EVENT_QUEUE UEQ ")
        .append("where UEQ.USER_ID=? ")
        .append("and UEQ.EVENT_FILTER=? ")
        .append("order by UEQ.EVENT_PRIORITY asc,UEQ.EVENT_DATE asc")
        .toString();

    /** Sql to read the queue size. */
    private static final String SQL_READ_SIZE =
        new StringBuilder("select count(UEQ.EVENT_ID) \"EVENT_COUNT\" ")
        .append("from TPSD_USER_EVENT_QUEUE UEQ ")
        .append("where UEQ.USER_ID=?")
        .toString();

    /**
     * Create QueueSql.
     *
     */
	public QueueSql() {
        super();
	}

	/**
     * Create QueueSql.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     */
	public QueueSql(final DataSource dataSource) { 
	    super(dataSource);
	}

    public void createEvent(final Long userId, final XMPPEvent event,
            final Boolean filter) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_EVENT);
            session.setLong(1, userId);
            session.setString(2, event.getId());
            session.setCalendar(3, event.getDate());
            session.setInt(4, event.getPriority().priority());
            session.setBoolean(5, filter);
            session.setEvent(6, event);
            if (1 != session.executeUpdate()) {
                throw panic("Could not create queue event.");
            }

            session.prepareStatement(SQL_CREATE_AUDIT_EVENT);
            session.setLong(1, userId);
            session.setString(2, event.getId());
            session.setCalendar(3, event.getDate());
            session.setInt(4, event.getPriority().priority());
            session.setBoolean(5, filter);
            session.setEvent(6, event);
            if (1 != session.executeUpdate()) {
                throw panic("Could not create queue event.");
            }

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void deleteEvent(final Long userId, final String eventId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_EVENT);
            session.setLong(1, userId);
            session.setString(2, eventId);
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not delete queue event {0} for user {1}.",
                        eventId, userId);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void deleteEvents(final Long userId) {
        final HypersonicSession session = openSession();
        try {
            final Integer eventCount = readEventCount(userId);
            session.prepareStatement(SQL_DELETE_EVENTS);
            session.setLong(1, userId);
            if (eventCount.intValue() != session.executeUpdate())
                throw panic("Could not delete queue events.");

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Obtain a list of xmpp events for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param opener
     *            An <code>EventOpener</code>.
     * @return A <code>List</code> of <code>XMPPEvent</code>.
     */
    public List<XMPPEvent> readEvents(final User user) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_EVENTS);
            session.setLong(1, user.getLocalId());
            session.executeQuery();
            final List<XMPPEvent> events = new ArrayList<XMPPEvent>();
            while (session.nextResult()) {
                events.add(session.getEvent("EVENT_XML"));
            }
            return events;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Obtain a list of xmpp events for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param opener
     *            An <code>EventOpener</code>.
     * @return A <code>List</code> of <code>XMPPEvent</code>.
     */
    public List<XMPPEvent> readFilteredEvents(final User user, final Boolean filter) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_FILTERED_EVENTS);
            session.setLong(1, user.getLocalId());
            session.setBoolean(2, filter);
            session.executeQuery();
            final List<XMPPEvent> events = new ArrayList<XMPPEvent>();
            while (session.nextResult()) {
                events.add(session.getEvent("EVENT_XML"));
            }
            return events;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read the queue size for a user.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return The queue size.
     */
    public Integer readSize(final Long userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_SIZE);
            session.setLong(1, userId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getInteger("EVENT_COUNT");
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    private Integer readEventCount(final Long userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_EVENT_COUNT);
            session.setLong(1, userId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getInteger("EVENT_COUNT");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }
}
