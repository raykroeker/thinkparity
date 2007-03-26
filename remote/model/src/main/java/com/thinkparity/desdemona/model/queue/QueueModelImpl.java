/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.queue;

import java.io.*;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
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
        createEvent(userId, eventUserId, event, XMPPEvent.Priority.NORMAL);
    }

    void createEvent(final JabberId userId, final JabberId eventUserId,
            final XMPPEvent event, final XMPPEvent.Priority priority) {
        try {
            assertIsAuthenticatedUser(userId);
            event.setDate(currentDateTime());
            event.setId(buildEventId(eventUserId));
            event.setPriority(priority);
            /* create a temporary file, write the event xml to it then use a
             * file input stream to create the clob */
            final File tempEventFile = session.createTempFile();
            try {
                final FileWriter fileWriter = new FileWriter(tempEventFile);
                try {
                    xstreamUtil.toXML(event, fileWriter);
                } finally {
                    fileWriter.close();
                }
                final InputStream inputStream = new FileInputStream(tempEventFile);
                try {
                    queueSql.createEvent(eventUserId, event, inputStream,
                            tempEventFile.length(), getDefaultBufferSize());
                } finally {
                    inputStream.close();
                }
            } finally {
                Assert.assertTrue(tempEventFile.delete(),
                        "Could not delete temporary file {0}.", tempEventFile);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    void deleteEvent(final JabberId userId, final String eventId) {
        try {
            assertIsAuthenticatedUser(userId);
            queueSql.deleteEvent(userId, eventId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    void deleteEvents(final JabberId userId) {
        try {
            assertIsAuthenticatedUser(userId);
            queueSql.deleteEvents(userId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    List<XMPPEvent> readEvents(final JabberId userId) {
        try {
            assertIsAuthenticatedUser(userId);
            return queueSql.readEvents(userId, new EventOpener() {
                public XMPPEvent open(final InputStream event) throws IOException {
                    XMPPEvent root = null;
                    return xstreamUtil.eventFromXML(new BufferedReader(
                            new InputStreamReader(event),
                            getDefaultBufferSize()), root);
                }
            });
        } catch (final Throwable t) {
            throw panic(t);
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
}
