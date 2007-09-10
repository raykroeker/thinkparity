/*
 * Created On:  8-Sep-07 1:10:45 PM
 */
package com.thinkparity.desdemona.model;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.thinkparity.desdemona.util.DateTimeProvider;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Model Test Case Cache<br>
 * <b>Description:</b>A non-expiring cache of domain objects.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <T>
 *            An object.
 */
class Cache<T> {

    /** A cache of authentication token's to an object. */
    private final Map<AuthToken, T> map;

    /**
     * Create Cache.
     *
     */
    Cache() {
        super();
        this.map = new Hashtable<AuthToken, T>();
    }

    /**
     * Cache a value.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param value
     *            A <code>T</code>.
     * @return A <code>T</code>.
     */
    T cache(final AuthToken authToken, final T value) {
        expire();
        map.put(authToken, value);
        return value;
    }

    /**
     * Clear the entire cache.
     * 
     */
    void clear() {
        map.clear();
    }

    /**
     * Determine if the cache contains the authentication token.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return True if the token exists.
     */
    Boolean contains(final AuthToken authToken) {
        expire();
        return map.containsKey(authToken);
    }

    /**
     * Lookup the cached object.
     * 
     * @param authToken
     *            An <code>AuthenticationToken</code>.
     * @return A <code>T</code>.
     */
    T lookup(final AuthToken authToken) {
        expire();
        return map.get(authToken);
    }

    /**
     * Iterate the cache authentication tokens and remote those that are
     * expired.
     * 
     */
    private synchronized void expire() {
        final long now = DateTimeProvider.getCurrentDateTime().getTimeInMillis();
        final Iterator<AuthToken> iAuthToken = map.keySet().iterator();
        AuthToken authToken;
        while (iAuthToken.hasNext()) {
            authToken = iAuthToken.next();
            if (now < authToken.getExpiresOn().getTime()) {
                iAuthToken.remove();
            }
        }
    }
}
