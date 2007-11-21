/*
 * Created On:  Nov 19, 2007 11:45:32 AM
 */
package com.thinkparity.ophelia.support.ui.action;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Map;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ActionRegistry {

    /** A message format pattern for the illegal state registry. */
    private static final String ILLEGAL_STATE_REGISTERED_PATTERN;

    /** The action registry map. */
    private static final Map<String, Action> REGISTRY;

    static {
        REGISTRY = new Hashtable<String, Action>(1, 0.75F);
    }

    static {
        ILLEGAL_STATE_REGISTERED_PATTERN = "Action \"{0}\" has already been registered.";
    }

    /**
     * Create ActionRegistry.
     *
     */
    public ActionRegistry() {
        super();
    }

    /**
     * Determine if an action is registered.
     * 
     * @param id
     *            A <code>String</code>.
     * @return A <code>Boolean</code>.
     */
    public Boolean isRegistered(final String id) {
        return Boolean.valueOf(REGISTRY.containsKey(id));
    }

    /**
     * Lookup an action.
     * 
     * @param id
     *            A <code>String</code>.
     * @return A <code>Action</code>.
     */
    public Action lookup(final String id) {
        return REGISTRY.get(id);
    }

    /**
     * Register an action.
     * 
     * @param id
     *            A <code>String</code>.
     * @param action
     *            A <code>Action</code>.
     */
    void register(final String id, final Action action) {
        synchronized (REGISTRY) {
            if (REGISTRY.containsKey(id)) {
                throw newIllegalStateRegistered(id);
            } else {
                REGISTRY.put(id, action);
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
