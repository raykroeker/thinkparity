/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.io.sql;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.queue.EventOpener;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class QueueSql extends AbstractSql {

    /** Sql to create an event. */
    private static final String SQL_CREATE_EVENT =
        new StringBuilder("insert into TPSD_USER_EVENT_QUEUE ")
        .append("(USER_ID,EVENT_ID,EVENT_DATE,EVENT_PRIORITY,EVENT_XML) ")
        .append("values (?,?,?,?,?)")
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

    private final UserSql userSql;

    /**
     * Create QueueSql.
     *
     */
	public QueueSql() {
        super();
        this.userSql = new UserSql();
	}

    public void createEvent(final JabberId userId, final XMPPEvent event,
            final InputStream xml, final Long xmlLength,
            final Integer bufferSize) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_EVENT);
            session.setLong(1, readLocalUserId(userId));
            session.setString(2, event.getId());
            session.setCalendar(3, event.getDate());
            session.setInt(4, event.getPriority().priority());
            session.setAsciiStream(5, xml, xmlLength, bufferSize);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create queue event.");

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void deleteEvent(final JabberId userId, final String eventId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_EVENT);
            session.setLong(1, readLocalUserId(userId));
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

    public void deleteEvents(final JabberId userId) {
        final HypersonicSession session = openSession();
        try {
            final Integer eventCount = readEventCount(userId);
            session.prepareStatement(SQL_DELETE_EVENTS);
            session.setLong(1, readLocalUserId(userId));
            if (eventCount.intValue() != session.executeUpdate())
                throw new HypersonicException("Could not delete queue events.");

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
    public List<XMPPEvent> readEvents(final JabberId userId,
            final EventOpener opener) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_EVENTS);
            session.setLong(1, readLocalUserId(userId));
            session.executeQuery();
            final List<XMPPEvent> events = new ArrayList<XMPPEvent>();
            while (session.nextResult()) {
                final InputStream event = session.getClob("EVENT_XML");
                try {
                    events.add(opener.open(event));
                } finally {
                    event.close();
                }
            }
            return events;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    private Integer readEventCount(final JabberId userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_EVENT_COUNT);
            session.setLong(1, readLocalUserId(userId));
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

    private Long readLocalUserId(final JabberId userId) {
        return userSql.readLocalUserId(userId);
    }
}
