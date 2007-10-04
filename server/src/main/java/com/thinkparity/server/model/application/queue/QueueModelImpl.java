/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.queue;

import java.nio.charset.Charset;
import java.util.List;

import com.thinkparity.codebase.model.annotation.ThinkParityFilterEvent;
import com.thinkparity.codebase.model.queue.notification.NotificationSession;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.io.sql.QueueSql;
import com.thinkparity.desdemona.model.queue.notification.NotificationService;
import com.thinkparity.desdemona.model.queue.notification.ServerNotificationSession;
import com.thinkparity.desdemona.util.DesdemonaProperties;

/**
 * The queue model is used to persistantly store text for jabber ids. The text
 * is limitless; however large items will degrade the queue performance.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class QueueModelImpl extends AbstractModelImpl implements QueueModel,
        InternalQueueModel {

    /** The notification service. */
    private NotificationService notificationService;

    /** The desdemona properties. */
    private DesdemonaProperties properties;

    /** A queue sql interface. */
	private QueueSql queueSql;

    /**
	 * Create a QueueModelImpl.
	 * 
	 * @param session
	 *            The user session.
	 */
	public QueueModelImpl() {
		super();
	}

    /**
     * @see com.thinkparity.desdemona.model.queue.QueueModel#createNotificationSession()
     *
     */
    public NotificationSession createNotificationSession() {
        try {
            final ServerNotificationSession session = newNotificationSession(
                    newNotificationSessionId());
            // TODO persist the session
            notificationService.initialize(user, session);
            return session.getClientSession();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.queue.QueueModel#deleteEvent(com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent)
     * 
     */
    public void deleteEvent(final XMPPEvent event) {
        try {
            queueSql.deleteEvent(user.getLocalId(), event.getId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.queue.InternalQueueModel#deleteEvents()
     *
     */
    public void deleteEvents() {
        try {
            queueSql.deleteEvents(user.getLocalId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.queue.InternalQueueModel#createEvent(com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent)
     *
     */
    public void enqueueEvent(final XMPPEvent event) {
        try {
            enqueueEvent(event, XMPPEvent.Priority.NORMAL);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.queue.InternalQueueModel#createEvent(com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent,
     *      com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent.Priority)
     * 
     */
    public void enqueueEvent(final XMPPEvent event,
            final XMPPEvent.Priority priority) {
        try {
            event.setDate(currentDateTime());
            event.setId(newEventId());
            event.setPriority(priority);
            queueSql.createEvent(user.getLocalId(), event,
                    isFiltered(event.getClass()));
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.queue.InternalQueueModel#flush()
     * 
     */
    public void flush() {
        notificationService.logStatistics();
        notificationService.send(user);
    }

    /**
     * @see com.thinkparity.desdemona.model.queue.QueueModel#readEvents()
     * 
     */
    public List<XMPPEvent> readEvents() {
        try {
            if (getProfileModel().isQueueReadable()) {
                return queueSql.readEvents(user);
            } else {
                return queueSql.readFilteredEvents(user, Boolean.FALSE);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.queue.QueueModel#readSize()
     *
     */
    public Integer readSize() {
        try {
            return queueSql.readSize(user.getLocalId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initialize()
     *
     */
    @Override
    protected void initialize() {
        properties = DesdemonaProperties.getInstance();
        notificationService = NotificationService.getInstance();
        queueSql = new QueueSql();
    }
    
    /**
     * Determine the filterability of an event.
     * 
     * @param eventClass
     *            A <code>Class<? extends XMPPEvent></code>.
     * @return True if the filter annotation is present.
     */
    private Boolean isFiltered(final Class<? extends XMPPEvent> eventClass) {
        return Boolean.valueOf(
                eventClass.isAnnotationPresent(ThinkParityFilterEvent.class));
    }

    /**
     * Create a new event id for the model user. The event id is a combination
     * of the user id plus the current date\time.
     * 
     * @return An event id <code>String</code>.
     */
    private String newEventId() {
        return buildUserTimestampId(user.getId());
    }

    /**
     * Create a new server notification session. The charset; host; and port are
     * determined by querying the properties.
     * 
     * @param id
     *            A session id <code>String</code>.
     * @return A <code>ServerNotificationSession</code>.
     */
    private ServerNotificationSession newNotificationSession(final String id) {
        final ServerNotificationSession session = new ServerNotificationSession();
        session.setCharset(Charset.forName(properties.getProperty(
                "thinkparity.queue.notification-charset")));
        session.setId(id);
        session.setServerHost(properties.getProperty(
                "thinkparity.queue.notification-host"));
        session.setServerPort(Integer.valueOf(properties.getProperty(
                "thinkparity.queue.notification-port")));
        return session;
    }

    /**
     * Create an instance of a notification session id for the model user.
     * 
     * @return A notification session id.
     */
    private String newNotificationSessionId() {
        return buildUserTimestampId(user.getId());
    }
}
