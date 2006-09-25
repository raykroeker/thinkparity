/*
 * Apr 26, 2006
 */
package com.thinkparity.ophelia.browser.platform.action;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;

/**
 * An action registry. As actions are created via the factory they are stored in
 * the registry for future use.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see ActionFactory#create(ActionId)
 * @see ActionFactory#createAction(ActionId, Browser)
 */
public class ActionRegistry {

    /** The extension action registry. */
    private static final Map<ActionExtension, Object> EXTENSION_REGISTRY;

    /** The action registry. */
    private static final Map<ActionId, Object> REGISTRY;

    static {
        final int actionCount = ActionId.values().length;
        REGISTRY = new HashMap<ActionId, Object>(actionCount, 1.0F);

        EXTENSION_REGISTRY = new HashMap<ActionExtension, Object>(2, 1.0F);
    }

    /** Create ActionRegistry. */
    public ActionRegistry() { super(); }

    /**
     * Determine whether or not an action is contained within the registry.
     * 
     * @param actionId
     *            The action id.
     * @return True if the action exists in the registry; false otherwise.
     */
    public Boolean contains(final ActionExtension extension) {
        synchronized (EXTENSION_REGISTRY) {
            return EXTENSION_REGISTRY.containsKey(extension);
        }
    }

    /**
     * Determine whether or not an action is contained within the registry.
     * 
     * @param actionId
     *            The action id.
     * @return True if the action exists in the registry; false otherwise.
     */
    public Boolean contains(final ActionId id) {
        synchronized (REGISTRY) {
            return REGISTRY.containsKey(id);
        }
    }

    /**
     * Obtain an action.
     * 
     * @param actionId
     *            The action id.
     * @return The action.
     */
    public AbstractAction get(final ActionExtension extension) {
        synchronized (EXTENSION_REGISTRY) {
            return (AbstractAction) EXTENSION_REGISTRY.get(extension);
        }
    }

    /**
     * Obtain an action.
     * 
     * @param actionId
     *            The action id.
     * @return The action.
     */
    public AbstractAction get(final ActionId id) {
        synchronized(REGISTRY) {
            return (AbstractAction) REGISTRY.get(id);
        }
    }

    /**
     * Register an action.
     * 
     * @param action
     *            The action.
     */
    void put(final AbstractAction action) {
        synchronized(REGISTRY) { REGISTRY.put(action.getId(), action); }
    }

    /**
     * Register an action.
     * 
     * @param action
     *            The action.
     */
    void put(final AbstractAction action, final ActionExtension actionExtension) {
        synchronized (EXTENSION_REGISTRY) {
            EXTENSION_REGISTRY.put(actionExtension, action);
        }
    }
}
