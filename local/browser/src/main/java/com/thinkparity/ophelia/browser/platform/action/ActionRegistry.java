/*
 * Apr 26, 2006
 */
package com.thinkparity.ophelia.browser.platform.action;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.ophelia.browser.application.browser.Browser;

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

    /** The action registry. */
    private static final Map<ActionId, Object> REGISTRY;

    static {
        final int actionCount = ActionId.values().length;
        REGISTRY = new HashMap<ActionId, Object>(actionCount, 1.0F);
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
    public Boolean contains(final ActionId actionId) {
        synchronized(REGISTRY) { return REGISTRY.containsKey(actionId); }
    }

    /**
     * Obtain an action.
     * 
     * @param actionId
     *            The action id.
     * @return The action.
     */
    public AbstractAction get(final ActionId actionId) {
        synchronized(REGISTRY) {
            return (AbstractAction) REGISTRY.get(actionId);
        }
    }

    /**
     * Remove an action from the registry.
     * 
     * @param actionId
     *            The action id.
     * @return The action.
     */
    public AbstractAction remove(final ActionId actionId) {
        synchronized(REGISTRY) {
            return (AbstractAction) REGISTRY.remove(actionId);
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
}
