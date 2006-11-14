/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.queue;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;
import com.thinkparity.codebase.model.util.xstream.XStreamUtil;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.io.sql.QueueSql;
import com.thinkparity.desdemona.model.session.Session;

/**
 * The queue model is used to persistantly store text for jabber ids. The text
 * is limitless; however large items will degrade the queue performance.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
class QueueModelImpl extends AbstractModelImpl {

    /** A <code>QueueSql</code> interface. */
	private final QueueSql queueSql;

    /** An <code>XStreamUtil</code> xml streamer. */
    private final XStreamUtil xstreamUtil;

    /**
	 * Create a QueueModelImpl.
	 * 
	 * @param session
	 *            The user session.
	 */
	QueueModelImpl(final Session session) {
		super(session);
		this.queueSql = new QueueSql();
        this.xstreamUtil = XStreamUtil.getInstance();
	}

    void createEvent(final JabberId userId, final JabberId eventUserId,
            final XMPPEvent event) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("eventUserId", eventUserId);
        logger.logVariable("event", event);
        try {
            assertIsAuthenticatedUser(userId);

            event.setDate(currentDateTime());
            event.setId(buildEventId(eventUserId));
            queueSql.createEvent(eventUserId, event, new XMPPEventWriter() {
                public void write(final XMPPEvent event, final Writer writer) {
                    toXML(event, writer);
                }
            });
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    void deleteEvent(final JabberId userId, final String eventId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("eventId", eventId);
        try {
            assertIsAuthenticatedUser(userId);

            queueSql.deleteEvent(userId, eventId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<XMPPEvent> readEvents(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        try {
            assertIsAuthenticatedUser(userId);
            return queueSql.readEvents(userId, new XMPPEventReader() {
                public XMPPEvent read(final Reader xml) {
                    return fromXML(xml);
                }
            });
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Build an event id. The event id is a combination of the user id plus the
     * current date\time.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return An event id <code>String</code>.
     */
    private String buildEventId(final JabberId userId) {
        return buildUserTimestampId(userId);
    }

    /**
     * Create an xmpp event from xml.
     * 
     * @param xml
     *            The xml representing the <code>XMPPEvent</code>.
     * @return An <code>XMPPEvent</code>.
     */
    private XMPPEvent fromXML(final Reader xml) {
        XMPPEvent root = null;
        return xstreamUtil.eventFromXML(xml, root);
    }

    /**
     * Stream the xmpp event to xml.
     * 
     * @param event
     *            An <code>XMPPEvent</code>.
     * @return The xmpp event as an xml <code>String</code>.
     */
    private void toXML(final XMPPEvent event, final Writer xml) {
        xstreamUtil.toXML(event, xml);
    }
}
