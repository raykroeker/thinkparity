/*
 * Created On:  4-Jun-07 11:08:32 AM
 */
package com.thinkparity.ophelia.model.queue;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.queue.notification.NotificationClient;
import com.thinkparity.ophelia.model.queue.notification.NotificationClient.ObservableEvent;
import com.thinkparity.ophelia.model.session.OfflineCode;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.QueueService;
import com.thinkparity.service.client.ServiceFactory;

/**
 * <b>Title:</b>thinkParity Ophelia Model Queue Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class QueueModelImpl extends Model implements QueueModel,
        InternalQueueModel {

    /** A workspace attribute key for the notification client. */
    private static final String WS_ATTRIBUTE_KEY_NOTIFICATION_CLIENT;

    static {
        WS_ATTRIBUTE_KEY_NOTIFICATION_CLIENT = "QueueModelImpl#notificationClient";
    }

    /** A queue web-service interface. */
    private QueueService queueService;

    /** An executors thread factory. */
    private ThreadFactory threadFactory;

    /**
     * Create QueueModelImpl.
     *
     */
    public QueueModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.model.queue.InternalQueueModel#deleteEvent(com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent)
     *
     */
    public void deleteEvent(final XMPPEvent event) {
        try {
            queueService.deleteEvent(getAuthToken(), event);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.queue.InternalQueueModel#process(com.thinkparity.ophelia.model.util.ProcessMonitor)
     *
     */
    public void process(final ProcessMonitor monitor) {
        newQueueProcessor().run();
    }

    /**
     * @see com.thinkparity.ophelia.model.queue.InternalQueueModel#readEvents()
     *
     */
    public List<XMPPEvent> readEvents() {
        try {
            return queueService.readEvents(getAuthToken());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.queue.InternalQueueModel#readSize()
     *
     */
    public Integer readSize() {
        try {
            return queueService.readSize(getAuthToken());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.queue.InternalQueueModel#startNotificationClient()
     * 
     */
    public void startNotificationClient() {
        // stop the client if running
        if (isSetNotificationClient()) {
            try {
                removeNotificationClient().disconnect();
            } catch (final Throwable t) {
                logger.logWarning(t, "An error occured disconnecting notification client.");
            } finally {
                if (getSessionModel().isOnline()) {
                    getSessionModel().pushOfflineCode(OfflineCode.NETWORK_UNAVAILABLE);
                    getSessionModel().notifySessionTerminated();
                }
            }
        }
        setNotificationClient(newNotificationClient());
        newThread(getNotificationClient()).start();
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        // web-services
        final ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.queueService = serviceFactory.getQueueService();
        // thread factory
        this.threadFactory = Executors.defaultThreadFactory();
    }

    /**
     * Obtain the session authentication token.
     * 
     * @return A session <code>AuthToken</code>.
     */
    private AuthToken getAuthToken() {
        return getSessionModel().getAuthToken();
    }

    /**
     * Get the notification client workspace attribute.
     * 
     * @return A notification client <code>NotificationReaderRunnable</code>.
     */
    private NotificationClient getNotificationClient() {
        return (NotificationClient) workspace.getAttribute(
                WS_ATTRIBUTE_KEY_NOTIFICATION_CLIENT);
    }

    /**
     * Determine if the notification client is set.
     * 
     * @return True if the notification client is set.
     */
    private boolean isSetNotificationClient() {
        return workspace.isSetAttribute(
                WS_ATTRIBUTE_KEY_NOTIFICATION_CLIENT).booleanValue();
    }

    /**
     * Create an instance of a notification client. The notification client will
     * <i>always</i> fire an initial notification regardless of the queue
     * situation on the server.
     * 
     * @param session
     *            A <code>NotificationSession</code>.
     * @return A <code>NotificationClient</code>.
     */
    private NotificationClient newNotificationClient() {
        final NotificationClient notificationClient = new NotificationClient();
        notificationClient.addObserver(new Observer() {
            public void update(final Observable o, final Object arg) {
                final ObservableEvent observableEvent = (ObservableEvent) arg;
                switch (observableEvent) {
                case PENDING_EVENTS:
                    newQueueProcessor().run();
                    break;
                case CLIENT_OFFLINE:
                    if (getSessionModel().isOnline()) {
                        getSessionModel().pushOfflineCode(OfflineCode.NETWORK_UNAVAILABLE);
                        getSessionModel().notifySessionTerminated();
                    }
                    break;
                default:
                    logger.logWarning("Illegal observable event {0}.",
                            observableEvent);
                }
            }
        });
        notificationClient.setNotify(Boolean.TRUE);
        notificationClient.setSession(queueService.createNotificationSession(
                getAuthToken()));
        return notificationClient;
    }

    /**
     * Create a new queue processor.
     * 
     * @return An instance of <code>QueueProcessor</code>.
     */
    private QueueProcessor newQueueProcessor() {
        final QueueProcessor queueProcessor = new QueueProcessor();
        queueProcessor.setModelFactory(modelFactory);
        queueProcessor.setWorkspace(workspace);
        return queueProcessor;
    }

    /**
     * Create a new thread for a notification client.
     * 
     * @param notificationClient
     *            A <code>NotificationClient</code>.
     * @return A <code>Thread</code>.
     */
    private Thread newThread(final NotificationClient notificationClient) {
        // THREAD - QueueModelImpl#newThread()
        final Thread thread = threadFactory.newThread(notificationClient);
        thread.setDaemon(true);
        thread.setName("TPS-OpheliaModel-NotificationClient");
        return thread;
    }

    /**
     * Remove the notification client workspace attribute.
     * 
     * @return The <code>NotificationReaderRunnable</code>.
     */
    private NotificationClient removeNotificationClient() {
        final NotificationClient notificationClient = getNotificationClient();
        if (workspace.isSetAttribute(WS_ATTRIBUTE_KEY_NOTIFICATION_CLIENT)) {
            workspace.removeAttribute(WS_ATTRIBUTE_KEY_NOTIFICATION_CLIENT);
        }
        return notificationClient;
    }

    /**
     * Set the notification client workspace attribute.
     * 
     * @param notificationClient
     *            A notification client <code>Thread</code>.
     */
    private void setNotificationClient(final NotificationClient notificationClient) {
        workspace.setAttribute(WS_ATTRIBUTE_KEY_NOTIFICATION_CLIENT, notificationClient);
    }
}
