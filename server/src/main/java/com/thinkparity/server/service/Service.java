/*
 * Created On:  9-Oct-07 1:37:27 PM
 */
package com.thinkparity.desdemona.service;

import java.util.Properties;
import java.util.concurrent.Executors;

/**
 * <b>Title:</b>thinkParity Desdemona Service Abstraction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class Service {

    /**
     * Create DesdemonaService.
     *
     */
    protected Service() {
        super();
    }

    /**
     * Obtain a sleep duration.
     * 
     * @param properties
     *            A set of <code>Properties</code>.
     * @param key
     *            A <code>String</code>.
     * @param defaultSleep
     *            A <code>Long</code>.
     * @return A <code>Long</code>.
     */
    protected final Long getSleep(final Properties properties,
            final String key, final Long defaultSleep) {
        try {
            return Long.valueOf(properties.getProperty(key,
                    String.valueOf(defaultSleep)));
        } catch (final NumberFormatException nfx) {
            return defaultSleep;
        }
    }

    
    /**
     * Create a new daemon thread.
     * 
     * @param runnable
     *            A <code>Runnable</code>.
     * @param name
     *            A <code>String</code>.
     * @return A <code>Thread</code>.
     */
    protected final Thread startThread(final Runnable runnable,
            final String name) {
        final Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setDaemon(true);
        thread.setName(name);
        thread.start();
        return thread;
    }
}
