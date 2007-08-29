/*
 * Created On:  4-Jun-07 11:38:49 AM
 */
package com.thinkparity.ophelia.model.queue.notification;

import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.queue.notification.NotificationHeader;
import com.thinkparity.codebase.model.queue.notification.NotificationSession;

import com.thinkparity.network.Network;
import com.thinkparity.network.NetworkAddress;
import com.thinkparity.network.NetworkConnection;
import com.thinkparity.network.NetworkException;
import com.thinkparity.network.NetworkProtocol;

/**
 * <b>Title:</b>thinkParity Ophelia Model Queue Notification Client<br>
 * <b>Description:</b>A runnable designed to establish a tcp connection to a
 * server an monitor for notification events. When notification events are
 * received by the client; a queue processor is invoked.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NotificationClient extends Observable implements Runnable {

    /** A number of retry attempts for a connect. */
    private static final int CONNECT_RETRY;

    static {
        CONNECT_RETRY = 3;
    }

    /** A network connection. */
    private NetworkConnection connection;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** The network. */
    private final Network network;

    /** A notify flag. */
    private Boolean notify;

    /** An online flag. */
    private Boolean online;

    /** A run indicator. */
    private boolean run;

    /** A notification session. */
    private NotificationSession session;

    /**
     * Create NotificationReaderRunnable.
     * 
     * @param session
     *            A <code>NotificationSession</code>.
     * @throws IOException
     */
    public NotificationClient() {
        super();
        this.logger = new Log4JWrapper(getClass());
        this.network = new Network();
    }

    /**
     * @see java.lang.Runnable#run()
     *
     */
    public void run() {
        run = true;
        if (null == connection || !connection.isConnected()) {
            connect();
        }
        writeHeader();

        while (run) {
            if (online) {
                if (notify) {
                    notify = Boolean.FALSE;
                    setChanged();
                    notifyPendingEvents();
                } else {
                    update();
                }
            } else {
                run = false;
                setChanged();
                notifyOffline();
            }
        }
    }

    /**
     * Set the notify override.
     * 
     * @param notify
     *            A <code>Boolean</code>.
     */
    public void setNotify(final Boolean notify) {
        this.notify = notify;
    }

    /**
     * Set the notification session.
     * 
     * @param session
     *            A <code>NotificationSession</code>.
     */
    public void setSession(final NotificationSession session) {
        this.session = session;
    }

    /**
     * Stop the notification client.
     * 
     */
    public void stop() {
        run = false;
    }

    /**
     * Connect the notification client.
     * 
     */
    private void connect() {
        online = Boolean.FALSE;
        connection = network.newConnection(
                NetworkProtocol.getProtocol("socket"), new NetworkAddress(
                        session.getServerHost(), session.getServerPort()));
        network.getConfiguration().setConnectTimeout(connection, 750 * 2);
        network.getConfiguration().setSoTimeout(connection, 35 * 1000);

        for (int i = 0; i < CONNECT_RETRY; i++) {
            try {
                connection.connect();
                connection.write(getHeaderBytes());
                online = Boolean.TRUE;
                break;
            } catch (final NetworkException nx) {
                logger.logError("Cannot connect.", nx.getMessage());
            }
        }
    }

    /**
     * Obtain the header bytes to write to the connection.
     * 
     * @return A <code>byte[]</code>.
     */
    private byte[] getHeaderBytes() {
        return new NotificationHeader(NotificationHeader.Type.SESSION_ID,
                session.getId()).toHeader().getBytes(session.getCharset());
    }

    /**
     * Notify all observers about the event.
     * 
     * @param observableEvent
     *            An <code>ObservableEvent</code>.
     */
    private void notifyObservableEvent(final ObservableEvent observableEvent) {
        try {
            notifyObservers(observableEvent);
        } catch (final Throwable t) {
            logger.logError(t, "An error occured posting event {0}.",
                    observableEvent);
        }        
    }

    /**
     * Notify all observers that the client is offline.
     *
     */
    private void notifyOffline() {
        notifyObservableEvent(ObservableEvent.OFFLINE);
    }

    /**
     * Notify all observers that there are pending events.
     *
     */
    private void notifyPendingEvents() {
        notifyObservableEvent(ObservableEvent.PENDING_EVENTS);
    }

    /**
     * Update. Read the connection and if any bytes are returned; we are online;
     * if the bytes match the session id; we have new events.
     * 
     */
    private void update() {
        notify = online = Boolean.FALSE;
        final String sessionId = session.getId();
        final byte[] bytes = new byte[sessionId.getBytes().length];
        try {
            connection.read(bytes);
            online = Boolean.TRUE;
        } catch (final NetworkException nx) {
            Arrays.fill(bytes, (byte) 0);
            logger.logWarning("Network error reading connection.  {0}",
                    nx.getMessage());
        }
        if (Arrays.equals(sessionId.getBytes(session.getCharset()), bytes)) {
            notify = Boolean.TRUE;
        }
    }

    /**
     * Write the session id header.
     * 
     */
    private void writeHeader() {
        online = Boolean.FALSE;
        try {
            connection.write(getHeaderBytes());
            online = Boolean.TRUE;
        } catch (final NetworkException nx) {
            logger.logError("Cannot write header.", nx.getMessage());
        }
    }

    /** <b>Title:</b>Observable Event<br> */
    public enum ObservableEvent { OFFLINE, PENDING_EVENTS }
}
