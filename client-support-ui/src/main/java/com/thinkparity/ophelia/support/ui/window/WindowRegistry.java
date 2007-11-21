/*
 * Created On:  Nov 19, 2007 11:33:57 AM
 */
package com.thinkparity.ophelia.support.ui.window;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Map;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class WindowRegistry {

    /** A message format pattern for the illegal state registry. */
    private static final String ILLEGAL_STATE_REGISTERED_PATTERN;

    /** The window registry map. */
    private static final Map<String, Window> REGISTRY;

    static {
        REGISTRY = new Hashtable<String, Window>(1, 0.75F);
    }

    static {
        ILLEGAL_STATE_REGISTERED_PATTERN = "Window \"{0}\" has already been registered.";
    }

    /**
     * Create WindowRegistry.
     *
     */
    public WindowRegistry() {
        super();
    }

    /**
     * Determine if a window is registered.
     * 
     * @param id
     *            A <code>String</code>.
     * @return A <code>Boolean</code>.
     */
    public Boolean isRegistered(final String id) {
        return Boolean.valueOf(REGISTRY.containsKey(id));
    }

    /**
     * Lookup a window.
     * 
     * @param id
     *            A <code>String</code>.
     * @return A <code>Window</code>.
     */
    public Window lookup(final String id) {
        return REGISTRY.get(id);
    }

    /**
     * Register a window.
     * 
     * @param id
     *            A <code>String</code>.
     * @param window
     *            A <code>Window</code>.
     */
    void register(final String id, final Window window) {
        synchronized (REGISTRY) {
            if (REGISTRY.containsKey(id)) {
                throw newIllegalStateRegistered(id);
            } else {
                REGISTRY.put(id, window);
            }
        }
    }

    /**
     * Instantiate an illegal state exception for a registered state.
     * 
     * @param id
     *            A <code>String</code>.
     */
    private IllegalStateException newIllegalStateRegistered(final String id) {
        return new IllegalStateException(MessageFormat.format(
                ILLEGAL_STATE_REGISTERED_PATTERN, id));
    }
}
