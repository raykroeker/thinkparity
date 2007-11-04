/*
 * Created On:  4-Jun-07 11:08:32 AM
 */
package com.thinkparity.ophelia.model.queue;

import java.text.MessageFormat;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.thinkparity.codebase.event.EventListener;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.queue.notification.NotificationClient;
import com.thinkparity.ophelia.model.queue.notification.NotificationClient.ObservableEvent;
import com.thinkparity.ophelia.model.session.OfflineCode;
import com.thinkparity.ophelia.model.session.OfflineException;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.QueueService;
import com.thinkparity.service.ServiceFactory;

/**
 * <b>Title:</b>thinkParity Ophelia Model Queue Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class QueueModelImpl extends Model<EventListener> implements
        QueueModel, InternalQueueModel {

    /** A workspace attribute key for the notification client. */
    private static final String WS_ATTRIBUTE_KEY_NOTIFICATION_CLIENT;

    /** A workspace attribute key for the queue processors. */
    private static final String WS_ATTRIBUTE_KEY_QUEUE_PROCESSOR_MAP;

    static {
        WS_ATTRIBUTE_KEY_NOTIFICATION_CLIENT = "QueueModelImpl#notificationClient";
        WS_ATTRIBUTE_KEY_QUEUE_PROCESSOR_MAP = "QueueModelImpl#queueProcessorMap";
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
        final QueueProcessor queueProcessor = newQueueProcessor();
        final Object id = setQueueProcessor(queueProcessor);
        try {
            queueProcessor.run();
        } finally {
            removeQueueProcessor(id);
        }
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
        final NotificationClient notificationClient;
        // stop the client if running
        if (isSetNotificationClient()) {
            notificationClient = getNotificationClient();
            notificationClient.stop();
        } else {
            notificationClient = newNotificationClient();
        }
        notificationClient.setNotify(Boolean.TRUE);
        notificationClient.setSession(queueService.createNotificationSession(
                getAuthToken()));
        setNotificationClient(notificationClient);

        newThread(notificationClient).start();
    }

    /**
     * @see com.thinkparity.ophelia.model.queue.InternalQueueModel#stopNotificationClient()
     *
     */
    @Override
    public void stopNotificationClient() {
        try {
            if (isSetNotificationClient()) {
                getNotificationClient().stop();
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.queue.InternalQueueModel#stopProcessors(com.thinkparity.ophelia.model.util.ProcessMonitor)
     * 
     */
    @Override
    public void stopProcessors(final ProcessMonitor monitor) {
        try {
            if (isSetQueueProcessorMap()) {
                /* wherever a queue processor is started; a try/finally is used
                 * to remove the processor from the map; therefore here we need
                 * not remove anything, and simply cancel */
                logger.logInfo("Queue processor map is set.");
                final QueueProcessorMap queueProcessorMap = getQueueProcessorMap();                
                final List<Object> queueProcessorIdList = queueProcessorMap.getAllIds();
                logger.logInfo("{0} queue processor(s).", queueProcessorIdList.size());
                notifyDetermine(monitor, queueProcessorIdList.size());
                notifyProcessBegin(monitor);
                if (queueProcessorIdList.isEmpty()) {
                    notifyProcessEnd(monitor);                    
                } else {
                    for (int i = 0; i < queueProcessorIdList.size(); i++) {
                        final QueueProcessor queueProcessor = queueProcessorMap.get(queueProcessorIdList.get(i));
                        final boolean last = i == queueProcessorIdList.size() - 1;
                        newThread(MessageFormat.format("TPS-OpheliaModel-StopQueueProcessors_{0}", i), new Runnable() {
                            /**
                             * @see java.lang.Runnable#run()
                             *
                             */
                            @Override
                            public void run() {
                                try {
                                    logger.logInfo("Cancelling queue processor");
                                    queueProcessor.cancel();
                                    if (true == last) {
                                        notifyProcessEnd(monitor);
                                    }
                                } catch (final Throwable t) {
                                    panic(t);
                                }
                            }
                        }).start();
                    }
                }
            } else {
                logger.logInfo("Queue processor map is not set.");
                notifyProcessEnd(monitor);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        // web-services
        final ServiceFactory serviceFactory = getServiceFactory();
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
     * Obtain the queue processor map.
     * 
     * @return A <code>QueueProcessorMap</code>.
     */
    private QueueProcessorMap getQueueProcessorMap() {
        if (workspace.isSetAttribute(WS_ATTRIBUTE_KEY_QUEUE_PROCESSOR_MAP)) {
            logger.logDebug("Queue processor map attribute is set.");
            return (QueueProcessorMap) workspace.getAttribute(WS_ATTRIBUTE_KEY_QUEUE_PROCESSOR_MAP);
        } else {
            logger.logDebug("Queue processor map attribute is not set.");
            return null;
        }
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
     * Determine if the queue processor is set.
     * 
     * @return True if the queue processor is set.
     */
    private boolean isSetQueueProcessorMap() {
        return workspace.isSetAttribute(
                WS_ATTRIBUTE_KEY_QUEUE_PROCESSOR_MAP).booleanValue();
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
                    final QueueProcessor queueProcessor = newQueueProcessor();
                    final Object id = setQueueProcessor(queueProcessor);
                    /* start the queue processor as a separate thread; if not
                     * the notification client is blocked from determining
                     * offline status while we are processing the queue */
                    workspace.newThread("QueueProcessor",
                            new Runnable() {
                                /**
                                 * @see java.lang.Runnable#run()
                                 *
                                 */
                                @Override
                                public void run() {
                                    try {
                                        queueProcessor.run();
                                    } catch (final OfflineException ox) {
                                        logger.logWarning("A network error has occured.  {0}",
                                                ox.getMessage());
                                    } finally {
                                        removeQueueProcessor(id);
                                    }
                                }
                        
                    }).start();
                    break;
                case OFFLINE:
                    if (getSessionModel().isOnline()) {
                        getSessionModel().pushOfflineCode(OfflineCode.CLIENT_NETWORK_UNAVAILABLE);
                        getSessionModel().notifySessionTerminated();
                    }
                    break;
                default:
                    logger.logWarning("Illegal observable event {0}.",
                            observableEvent);
                }
            }
        });
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
     * Remove the queue processor workspace attribute.
     * 
     * @return A <code>QueueProcessor</code>.
     */
    private void removeQueueProcessor(final Object id) {
        final QueueProcessorMap queueProcessorMap =
            (QueueProcessorMap) workspace.getAttribute(
                    WS_ATTRIBUTE_KEY_QUEUE_PROCESSOR_MAP);
        if (null == queueProcessorMap) {
            logger.logInfo("Queue processor map is not set.");
        } else {
            logger.logInfo("Queue processor map is set.");
            queueProcessorMap.remove(id);
            if (queueProcessorMap.isEmpty()) {
                workspace.removeAttribute(WS_ATTRIBUTE_KEY_QUEUE_PROCESSOR_MAP);
            }
        }
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

    /**
     * Set the queue processor workspace attribute.
     * 
     * @param queueProcessor
     *            A <code>QueueProcessor</code>.
     * @return An <code>Object</code>.
     */
    private Object setQueueProcessor(final QueueProcessor queueProcessor) {
        QueueProcessorMap queueProcessorMap;
        if (workspace.isSetAttribute(WS_ATTRIBUTE_KEY_QUEUE_PROCESSOR_MAP)) {
            queueProcessorMap = (QueueProcessorMap) workspace.getAttribute(
                    WS_ATTRIBUTE_KEY_QUEUE_PROCESSOR_MAP);
        } else {
            queueProcessorMap = new QueueProcessorMap();
            workspace.setAttribute(WS_ATTRIBUTE_KEY_QUEUE_PROCESSOR_MAP,
                    queueProcessorMap);
        }
        return queueProcessorMap.put(queueProcessor);
    }
}
