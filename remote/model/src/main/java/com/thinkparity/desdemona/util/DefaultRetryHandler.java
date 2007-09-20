/*
 * Created On:  17-Aug-07 9:04:05 AM
 */
package com.thinkparity.desdemona.util;

import java.net.URI;
import java.net.URISyntaxException;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamRetryHandler;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.network.Network;
import com.thinkparity.network.NetworkAddress;
import com.thinkparity.network.NetworkConnection;
import com.thinkparity.network.NetworkException;
import com.thinkparity.network.NetworkProtocol;
import com.thinkparity.service.client.http.HttpServiceProxy;

/**
 * <b>Title:</b>thinkParity Desdemona Util Default Stream Retry Handler<br>
 * <b>Description:</b>Attempt to open a socket connection to the stream
 * host/port. If one cannot be attempted within a finite number of retries; we
 * give up.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DefaultRetryHandler implements StreamRetryHandler {

    /** A connect timeout for network validation. */
    private static final Integer CONNECT_TIMEOUT;

    /** A log4j wrapper. */
    private static final Log4JWrapper LOGGER;

    /** A network. */
    private static final Network network;

    /** A period of time to wait between retries. */
    private static final Long PERIOD;

    /** A retry count. */
    private static final int RETRY_VALIDATE;

    static {
        // TIMEOUT - DefaultRetryHandler#<cinit> - Connect - 7s
        CONNECT_TIMEOUT = Integer.valueOf(7 * 1000); 

        LOGGER = new Log4JWrapper(HttpServiceProxy.class);

        // PERIOD - DefaultRetryHandler#<cinit> - Validate - 1s
        PERIOD = Long.valueOf(1L * 1000L);

        // RETRY - DefaultRetryHandler#<cinit> - Validate - 3
        RETRY_VALIDATE = 3; 

        network = new Network();
    }

    /** A connection. */
    private final NetworkConnection connection;

    /** The current retry attempt. */
    private int retryAttempt;

    /** The maximum number of retry attempts. */
    private final int retryAttempts;

    /**
     * Create DefaultRetryHandler.
     *
     */
    public DefaultRetryHandler(final StreamSession session)
            throws URISyntaxException {
        super();
        final URI uri = new URI(session.getURI());
        final NetworkAddress address = new NetworkAddress(uri.getHost(), uri.getPort());
        final NetworkProtocol protocol = NetworkProtocol.getProtocol("socket");
        this.connection = network.newConnection(protocol, address);
        this.retryAttempts = 4;
        this.retryAttempt = 0;

        network.getConfiguration().setConnectTimeout(connection, CONNECT_TIMEOUT);
    }

    /**
     * @see com.thinkparity.codebase.model.stream.StreamRetryHandler#retry()
     *
     */
    @Override
    public Boolean retry() {
        if (++retryAttempt <= retryAttempts) {
            try {
                validateNetwork();
                return Boolean.TRUE;
            } catch (final Exception x) {
                return Boolean.FALSE;
            }
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * @see com.thinkparity.codebase.model.stream.StreamRetryHandler#period()
     *
     */
    @Override
    public Long waitPeriod() {
        return PERIOD;
    }

    /**
     * Validate the underlying network by resolving the address of the context
     * host; and opening a socket to the host at the port.
     * 
     * @throws NetworkException
     *             if a network cannot be established
     */
    private void attemptValidateNetwork() throws NetworkException {
        connection.connect();
        connection.disconnect();
    }

    /**
     * Validate the underlying network by resolving the address of the context
     * host; and opening a socket to the host at the port.
     * 
     * @throws NetworkException
     *             if a network cannot be established
     */
    private void validateNetwork() throws NetworkException {
        for (int i = 0; i < RETRY_VALIDATE; i++) {
            try {
                LOGGER.logInfo("Ensure online attempt {0}/{1}.", i, RETRY_VALIDATE);
                attemptValidateNetwork();
                break;
            } catch (final NetworkException nx) {
                if (i < RETRY_VALIDATE - 1) {
                    LOGGER.logWarning("Could not ensure online status.  {0}",
                            nx.getMessage());
                } else {
                    LOGGER.logError(nx, "Could not ensure online status.");
                    throw nx;
                }
            }
        }
    }
}
