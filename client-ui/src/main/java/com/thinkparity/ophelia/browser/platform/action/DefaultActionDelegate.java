/*
 * Created On:  4-Dec-06 11:07:29 AM
 */
package com.thinkparity.ophelia.browser.platform.action;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DefaultActionDelegate {

    /** A reference to the <code>ActionRegistry</code>. */
    private final ActionRegistry registry;

    /**
     * Create DefaultActionDelegate.
     *
     */
    public DefaultActionDelegate() {
        super();
        this.registry = new ActionRegistry();
    }

    /**
     * Obtain an instance of a thinkParity action.
     * 
     * @param actionId
     *            An <code>ActionId</code>.
     * @return An <code>AbstractAction</code>.
     */
    public AbstractAction getInstance(final ActionId actionId) {
        if (registry.contains(actionId)) {
            return registry.get(actionId);
        } else {
            return ActionFactory.create(actionId);
        }
    }

    /**
     * Invoke a thinkParity action.
     * 
     * @param action
     *            An <code>AbstractAction</code>.
     * @param data
     *            The action <code>Data</code>.
     */
    public void invoke(final AbstractAction action, final Data data) {
        action.invoke(data);
    }
}
