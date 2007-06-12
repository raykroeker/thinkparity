/*
 * Created On:  26-May-07 12:37:21 PM
 */
package com.thinkparity.codebase.net.online;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity CommonCodebase Net Online Socket Validator<br>
 * <b>Description:</b>An online validator that uses the socket interface to
 * validate the internet is "reachable".<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class SocketValidator implements Validator {

    /** A <code>DecimalFormat</code> used to output duration. */
    private static final DecimalFormat FORMAT;

    /** A <code>Log4JWrapper</code>. */
    private static final Log4JWrapper LOGGER;

    static {
        FORMAT = new DecimalFormat("000");
        LOGGER = new Log4JWrapper("com.thinkparity.codebase.net.online");
    }

    /** The amount of time it took to perform the previous validation. */
    private long duration;

    /** A <code>List<String></code> of hostnames used to validate. */ 
    private final List<String> hostnames;

    /** An <code>Int</code> port to connect on. */
    private int port;

    /** A <code>List<Proxy></code>. */
    private final List<Proxy> proxies;

    /** A <code>ProxySelector</code>. */
    private final ProxySelector proxySelector;

    /** The <code>Socket</code> to connect. */
    private Socket socket;

    /** An <code>InetSocketAddress</code> to attempt to connect to. */
    private InetSocketAddress socketAddress;

    /** An <code>int</code> connect timeout. */
    private int timeout;

    /** The select proxies uri pattern <code>String</code>. */
    private final String uriPattern;

    
    /**
     * Create SocketValidator.
     *
     */
    public SocketValidator() {
        super();
        this.hostnames = new ArrayList<String>(25);
        this.proxies = new ArrayList<Proxy>(3);
        this.proxySelector = ProxySelector.getDefault();
        this.uriPattern = "socket://{0}:{1}";
    }

    /**
     * @see com.thinkparity.codebase.net.online.Validator#initialize(java.util.Properties)
     *
     */
    public void initialize(final Properties properties) {
        hostnames.clear();
        final StringTokenizer tokenizer = new StringTokenizer(
                properties.getProperty("hostnames"), ",");
        while (tokenizer.hasMoreTokens()) {
            hostnames.add(tokenizer.nextToken());
        }
        if (1 > hostnames.size())
            throw new IllegalArgumentException("More than one hostname must be configured.");
        try {
            port = Integer.parseInt(properties.getProperty("port"));
        } catch (final NumberFormatException nfx) {
            throw new IllegalArgumentException("Could not determine port.");
        }
        try {
            timeout = Integer.parseInt(properties.getProperty("timeout"));
        } catch (final NumberFormatException nfx) {
            throw new IllegalArgumentException("Could not determine timeout.");
        }
    }

    /**
     * @see com.thinkparity.codebase.net.online.Validator#validate()
     *
     */
    public Boolean validate() {
        LOGGER.logTrace("Begin validate.");
        LOGGER.logVariable("hostnames.size()", hostnames.size());
        Collections.shuffle(hostnames);
        for (final String hostname : hostnames) {
            LOGGER.logTrace("Begin validate \"{0}\".", hostname);
            resolveSocket(hostname);
            resolveProxies();
            if (validateSocket()) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.logInfo("{0}ms - {1}", FORMAT.format(duration),
                            socketAddress);
                }
                return Boolean.TRUE;
            }
            LOGGER.logTrace("End validate \"{0}\".", hostname);
        }
        LOGGER.logTrace("End validate.");
        return Boolean.FALSE;
    }

    /**
     * Resolve a list of proxies for a host address and port.
     * 
     */
    private void resolveProxies() {
        proxies.clear();
        try {
            proxies.addAll(proxySelector.select(new URI(MessageFormat.format(
                    uriPattern, socketAddress.getHostName(),
                    socketAddress.getPort()))));
        } catch (final URISyntaxException urisx) {
            throw new IllegalArgumentException(urisx);
        }
    }

    /**
     * Resolve the socket address to connect to.
     * 
     * @param hostname
     *            A hostname <code>String</code>.
     */
    private void resolveSocket(final String hostname) {
        socketAddress = new InetSocketAddress(hostname, port);
    }

    /**
     * Validate the socket address.
     * 
     * @return True if a connection can be established.
     */
    private boolean validateSocket() {
        final long begin = System.currentTimeMillis();
        try {
            for (final Proxy proxy : proxies) {
                LOGGER.logVariable("proxy", proxy);
                socket = new Socket(proxy);
                try {
                    LOGGER.logVariable("socketAddress", socketAddress);
                    LOGGER.logVariable("timeout", timeout);
                    socket.connect(socketAddress, timeout);
                } finally {
                    try {
                        socket.close();
                    } catch (final IOException iox) {
                        LOGGER.logWarning("Could not disconnect from {0}.",
                                socketAddress);
                    }
                }
                return true;
            }
            return false;
        } catch (final IOException iox) {
            LOGGER.logWarning("Could not connect to {0}.", socketAddress);
            return false;
        } finally {
            duration = System.currentTimeMillis() - begin;
        }
    }
}
