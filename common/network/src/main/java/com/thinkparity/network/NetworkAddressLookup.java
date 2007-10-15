/*
 * Created On:  29-Aug-07 6:11:58 PM
 */
package com.thinkparity.network;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity Network Address Lookup<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class NetworkAddressLookup {

    /** A cache of network addresses to their socket address counterparts. */
    private static final Map<NetworkAddress, CacheValue> CACHE;

    /** The resolution thread name. */
    private static final String RESOLVE_THREAD_NAME;

    /** A list of threads working on resolving an address. */
    private static final Map<NetworkAddress, Thread> RESOLVE_THREADS;

    /** A temporary resolved address map. */
    private static final Map<Runnable, SocketAddress> RESOLVED;

    static {
        CACHE = new Hashtable<NetworkAddress, CacheValue>();
        RESOLVE_THREAD_NAME = "TPS-Network-ResolveAddress";
        RESOLVE_THREADS = new Hashtable<NetworkAddress, Thread>();
        RESOLVED = new Hashtable<Runnable, SocketAddress>();
    }

    /** The network configuration. */
    private final NetworkConfiguration configuration;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /**
     * Create NetworkAddressCache.
     *
     */
    NetworkAddressLookup(final NetworkConfiguration configuration) {
        super();
        this.configuration = configuration;
        this.logger = new Log4JWrapper("com.thinkparity.network");
    }

    /**
     * Lookup a socket address.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>SocketAddress</code>.
     */
    SocketAddress lookup(final NetworkAddress address) {
        final CacheValue value = CACHE.get(address);
        if (null == value) {
            return null;
        } else {
            if (System.currentTimeMillis() < value.timeout) {
                return value.address;
            } else {
                remove(address);
                return null;
            }
        }
    }

    /**
     * Resolve a socket address from a network address. The inet socket address
     * queries the dns service with a timeout of about 80s. This resolution is
     * used to reduce that timeout.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>SocketAddress</code>.
     */
    SocketAddress resolve(final NetworkAddress address) {
        remove(address);

        if (RESOLVE_THREADS.containsKey(address)) {
            /* we are still trying to resolve */
            return null;
        } else {
            final Runnable runnable = new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    try {
                        final CacheValue value = new CacheValue();
                        value.address = new InetSocketAddress(address.getHost(),
                                address.getPort());
                        value.timeout = System.currentTimeMillis()
                                + getCacheTTL(address);
                        /* check the address for resolution */
                        if (((InetSocketAddress) value.address).isUnresolved()) {
                            return;
                        } else {
                            RESOLVED.put(this, value.address);
                            CACHE.put(address, value);
                        }
                    } finally {
                        RESOLVE_THREADS.remove(address);
                    }
                    synchronized (this) {
                        notifyAll();
                    }
                }
            };
            final Thread thread = new Thread(runnable, RESOLVE_THREAD_NAME);
            thread.setDaemon(true);
            RESOLVE_THREADS.put(address, thread);
            thread.start();
            /* wait for a timeout before moving on */
            synchronized (runnable) {
                try {
                    runnable.wait(getLookupTimeout());
                } catch (final InterruptedException ix) {
                    logger.logWarning("Address lookup interrupted.  {0}",
                            ix.getMessage());
                }
            }

            return RESOLVED.remove(runnable);
        }
    }

    /**
     * Obtain the time to live for the address in the cache.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return An <code>Integer</code>.
     */
    private Integer getCacheTTL(final NetworkAddress address) {
        return configuration.getAddressCacheTTL(address);
    }

    /**
     * Obtain the timeout for a resolution lookup.
     * 
     * @return A <code>Integer</code>.
     */
    private Integer getLookupTimeout() {
        return configuration.getAddressLookupTimeout();
    }

    /**
     * Remove a network address from the cache.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     */
    private void remove(final NetworkAddress address) {
        CACHE.remove(address);
    }

    /** <b>Title:</b>Cache Value<br> */
    private static class CacheValue {
        private SocketAddress address;
        private Long timeout;
    }
}
