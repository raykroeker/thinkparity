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
     * @return An <code>ActionInvocation</code>.
     */
    public ActionInvocation getInstance(final ActionId actionId) {
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
     *            An <code>ActionInvocation</code>.
     * @param data
     *            The action <code>Data</code>.
     */
    public void invoke(final ActionInvocation action, final Data data) {
        action.invokeAction(data);
    }
}
