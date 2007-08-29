/*
 * Created On:  17-Aug-07 9:04:05 AM
 */
package com.thinkparity.desdemona.util;

import java.io.IOException;
import java.net.*;
import java.text.MessageFormat;
import java.util.List;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamRetryHandler;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.service.client.http.HttpServiceProxy;

/**
 * <b>Title:</b>thinkParity Desdemona Util Default Stream Retry Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DefaultRetryHandler implements StreamRetryHandler {

    /** A log4j wrapper. */
    private static final Log4JWrapper LOGGER;

    /** A proxy selector. */
    private static final ProxySelector PROXY_SELECTOR;

    /** A number of retry attempts for network validation. */
    private static final int VALIDATE_NETWORK_RETRY;

    /** A timeout for network validation. */
    private static final int VALIDATE_NETWORK_TIMEOUT;

    static {
        LOGGER = new Log4JWrapper(HttpServiceProxy.class);

        PROXY_SELECTOR = ProxySelector.getDefault();

        VALIDATE_NETWORK_TIMEOUT = 750 * 2;

        VALIDATE_NETWORK_RETRY = 3;
    }

    /**
     * Select a list of proxies for a host address and port.
     * 
     * @param host
     *            An <code>InetAddress</code>.
     * @param port
     *            A port <code>int</code>.
     * @return A <code>Proxy</code> <code>List</code>.
     */
    private static List<Proxy> selectProxies(
            final InetSocketAddress inetSocketAddress) {
        try {
            return PROXY_SELECTOR.select(new URI(MessageFormat.format(
                    "socket://{0}:{1}", inetSocketAddress.getHostName(),
                    inetSocketAddress.getPort())));
        } catch (final URISyntaxException urisx) {
            throw new IllegalArgumentException(urisx);
        }
    }

    /** An address host. */
    private final String host;

    /** An address port. */
    private final Integer port;

    /**
     * Create DefaultRetryHandler.
     *
     */
    public DefaultRetryHandler(final StreamSession session)
            throws URISyntaxException {
        super();
        this.host = new URI(session.getURI()).getHost();
        this.port = session.getPort();
    }

    /**
     * @see com.thinkparity.codebase.model.stream.StreamRetryHandler#retry()
     *
     */
    @Override
    public Boolean retry() {
        try {
            validateNetwork();
            return Boolean.TRUE;
        } catch (final Exception x) {
            return Boolean.FALSE;
        }
    }

    /**
     * Validate the underlying network by resolving the address of the context
     * host; and opening a socket to the host at the port.
     * 
     * @throws UnknownHostException
     *             if the address cannot be resolved; or if the address is null
     * @throws SocketException
     *             if the target host cannot be connected to
     */
    private void attemptValidateNetwork() throws UnknownHostException,
            SocketException {
        final InetAddress inetAddress = InetAddress.getByName(host);
        if (null == inetAddress) {
            throw new UnknownHostException(host);
        }
        final InetSocketAddress inetSocketAddress =
            new InetSocketAddress(inetAddress, port);
        /* attempt to establish a connection to a host/port through a specified
         * proxy - the first connection wins */
        final List<Proxy> proxies = selectProxies(inetSocketAddress);
        Socket socket = null;
        try {
            for (final Proxy proxy : proxies) {
                socket = new Socket(proxy);
                socket.connect(inetSocketAddress, VALIDATE_NETWORK_TIMEOUT);
                break;
            }
        } catch (final IOException iox) {
            LOGGER.logWarning(iox, "Online check failed.");
            throw new ConnectException(host);
        } finally {
            if (null != socket) {
                try {
                    socket.close();
                } catch (final IOException iox) {
                    LOGGER.logWarning(iox, "Online check could not close socket.");
                } finally {
                    socket = null;
                }
            }
        }
    }

    /**
     * Validate the underlying network by resolving the address of the context
     * host; and opening a socket to the host at the port.
     * 
     * @throws UnknownHostException
     *             if the address cannot be resolved; or if the address is null
     * @throws SocketException
     *             if the target host cannot be connected to
     */
    private void validateNetwork() throws UnknownHostException, SocketException {
        for (int i = 0; i < VALIDATE_NETWORK_RETRY; i++) {
            try {
                LOGGER.logInfo("Ensure online attempt {0}/{1}.", i, VALIDATE_NETWORK_RETRY);
                attemptValidateNetwork();
                break;
            } catch (final UnknownHostException uhx) {
                if (i < VALIDATE_NETWORK_RETRY - 1) {
                    LOGGER.logWarning(uhx, "Could not ensure online status.");
                } else {
                    LOGGER.logError(uhx, "Could not ensure online status.");
                    throw uhx;
                }
            } catch (final SocketException sx) {
                if (i < VALIDATE_NETWORK_RETRY - 1) {
                    LOGGER.logWarning(sx, "Could not ensure online status.");
                } else {
                    LOGGER.logError(sx, "Could not ensure online status.");
                    throw sx;
                }
            }
        }
    }

}
