/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.io.sql;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.queue.XMPPEventReader;
import com.thinkparity.desdemona.model.queue.XMPPEventWriter;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class QueueSql extends AbstractSql {

    /** Sql to create an event. */
    private static final String SQL_CREATE_EVENT =
            new StringBuffer("insert into PARITY_EVENT_QUEUE ")
            .append("(EVENT_ID,EVENT_XML,EVENT_DATE,USERNAME) ")
            .append("values (?,?,?,?)")
            .toString();

    /** Sql to delete an event. */
    private static final String SQL_DELETE_EVENT =
            new StringBuffer("delete from PARITY_EVENT_QUEUE ")
            .append("where USERNAME=? and EVENT_ID=?")
            .toString();

    /** Sql to read events. */
	private static final String SQL_READ_EVENTS =
        new StringBuffer("select EVENT_ID,EVENT_DATE,EVENT_XML,USERNAME ")
        .append("from PARITY_EVENT_QUEUE PEQ ")
        .append("where PEQ.USERNAME=? ")
        .append("order by EVENT_DATE asc")
        .toString();

	/**
	 * Create a QueueSql.
	 */
	public QueueSql() { super(); }

	public void createEvent(final JabberId userId, final XMPPEvent event,
            final XMPPEventWriter eventWriter) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_EVENT);
            session.setString(1, event.getId());
            final StringWriter writer = new StringWriter();
            eventWriter.write(event, writer);
            session.setString(2, writer.toString());
            session.setCalendar(3, event.getDate());
            session.setString(4, userId.getUsername());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create queue event.");

            session.commit();
        } catch (final Throwable t) {
            translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void deleteEvent(final JabberId userId, final String eventId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_EVENT);
            session.setString(1, userId.getUsername());
            session.setString(2, eventId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not delete queue event.");

            session.commit();
        } catch (final Throwable t) {
            translateError(session, t);
        } finally {
            session.close();
        }
    }

    public List<XMPPEvent> readEvents(final JabberId userId,
            final XMPPEventReader eventReader) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_EVENTS);
            session.setString(1, userId.getUsername());
            session.executeQuery();
            final List<XMPPEvent> events = new ArrayList<XMPPEvent>();
            while (session.nextResult()) {
                events.add(extract(session, eventReader));
            }
            return events;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    private XMPPEvent extract(final HypersonicSession session,
            final XMPPEventReader eventReader) {
        final Reader reader = new StringReader(session.getString("EVENT_XML"));
        return eventReader.read(reader);
    }
}
