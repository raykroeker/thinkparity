/*
 * Created On:  21-Aug-07 4:43:16 PM
 */
package com.thinkparity.network;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class NetworkConnections {

    private static final List<WeakReference<NetworkConnection>> CONNECTIONS;

    static {
        CONNECTIONS = new Vector<WeakReference<NetworkConnection>>();
    }

    /**
     * Create NetworkConnections.
     *
     */
    NetworkConnections() {
        super();
    }

    synchronized void add(final NetworkConnection connection) {
        CONNECTIONS.add(new WeakReference<NetworkConnection>(connection));
    }

    synchronized List<NetworkConnection> get() {
        final List<NetworkConnection> list = new ArrayList<NetworkConnection>(CONNECTIONS.size());

        NetworkConnection refConnection;
        for (final Iterator<WeakReference<NetworkConnection>> iRef =
                CONNECTIONS.iterator(); iRef.hasNext();) {
            refConnection = iRef.next().get();
            if (null == refConnection) {
                iRef.remove();
            } else {
                list.add(refConnection);
            }
        }
        return list;
    }
}
