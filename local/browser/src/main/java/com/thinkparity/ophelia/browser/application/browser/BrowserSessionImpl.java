/*
 * Mar 21, 2006
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class BrowserSessionImpl {

    private static final Map<BrowserContext, BrowserSession> SESSIONS;

    static {
        SESSIONS = new Hashtable<BrowserContext, BrowserSession>();
    }

    BrowserSessionImpl() {
        super();
    }

    /**
     * Obtain a browser session for a context; creating if requested.
     * 
     * @param context
     *            A <code>BrowserContext</code>.
     * @param create
     *            Whether or not to create the session if it does not exist.
     * @return A <code>BrowserSession</code> or null if one does not exist and
     *         create is false.
     */
    BrowserSession getSession(final BrowserContext context, final Boolean create) {
        if (SESSIONS.containsKey(context)) {
            return SESSIONS.get(context);
        } else {
            if (create.booleanValue()) {
                SESSIONS.put(context, new BrowserSession() {
                    private final Long inception = Long.valueOf(System.currentTimeMillis());
                    private final Map<String, Object> sessionData = new Hashtable<String, Object>();
                    public Object getAttribute(final String name) {
                        return sessionData.get(name);
                    }
                    public Iterable<String> getAttributeNames() {
                        return Collections.unmodifiableSet(sessionData.keySet());
                    }
                    public Long getInception() {
                        return inception;
                    }
                    public void removeAttribute(final String name) {
                        sessionData.remove(name);
                    }
                    public void setAttribute(final String name,
                            final Object value) {
                        sessionData.put(name, value);
                    }
                });
                return getSession(context, Boolean.FALSE);
            } else {
                return null;
            }
        }
    }
}
