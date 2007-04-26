/*
 * Jan 13, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionRegistry;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;

/**
 * <b>Title:</b>thinkParity OpheliaUI Button Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public final class ButtonFactory extends ComponentFactory {

	/** A SINGLETON button factory. */
    private static final ButtonFactory SINGLETON;

    static {
        SINGLETON = new ButtonFactory();
    }

    /**
     * Create a JButton.
     * 
     * @return The JButton.
     */
    public static JButton create() {
        return SINGLETON.doCreate();
    }

    /**
     * Create a JButton for an action and its data.
     * 
     * @param actionId
     *            An action id.
     * @param data
     *            The action data.
     * @return The JButton
     */
    public static JButton create(final ActionId actionId, final Data data) {
        return SINGLETON.doCreate(actionId, data);
    }

    /**
     * Create a button and apply a font.
     * 
     * @param font
     *            A <code>Font</code>.
     * @return A <code>JButton</code>.
     */
    public static JButton create(final Font font) {
        return SINGLETON.doCreate(font);
    }

    public static JButton create(final Icon icon) {
        return SINGLETON.doCreate(icon);
    }

    /**
     * Create a JButton with the specified text.
     * 
     * @param text
     *            The button text.
     * @return The JButton.
     */
    public static JButton create(final String text) {
        return SINGLETON.doCreate(text);
    }

    /** An action registry. */
    private final ActionRegistry actionRegistry;

    /** The action wrapper registry. */
    private final Map<ActionId, ActionWrapper> wrapperRegistry;

    /**
     * Create ButtonFactory.
     * 
     */
    private ButtonFactory() {
        super();
        this.actionRegistry = new ActionRegistry();
        this.wrapperRegistry = new Hashtable<ActionId, ActionWrapper>(
                ActionId.values().length, 1.0F);
    }

    private void applyIcon(final JButton jButton, final Icon icon) {
        jButton.setIcon(icon);
    }

    /**
     * Create a JButton.
     * 
     * @return The JButton.
     */
    private JButton doCreate() {
        final JButton jButton = new JButton();
        applyDefaultFont(jButton);
        jButton.setOpaque(false);
        return jButton;
    }

    /**
     * Create a button associated with an action id.
     * 
     * @param actionId
     *            An action id.
     * @param data
     *            The action data.
     * @return A JButton.
     */
    private JButton doCreate(final ActionId actionId, final Data data) {
        final AbstractAction action;
        if (actionRegistry.contains(actionId)) {
            action = actionRegistry.get(actionId);
        } else {
            action = ActionFactory.create(actionId);
        }
        final ActionWrapper actionWrapper;
        if (wrapperRegistry.containsKey(actionId)) {
            actionWrapper = wrapperRegistry.get(actionId);
        } else {
            wrapperRegistry.put(actionId, new ActionWrapper(action));
            actionWrapper = wrapperRegistry.get(actionId);
        }
        actionWrapper.setData(data);
        JButton button = new JButton(actionWrapper);

        return button;
    }

    /**
     * Create a button and apply a font.
     * 
     * @param font
     *            A <code>Font</code>.
     * @return A <code>JButton</code>.
     */
    private JButton doCreate(final Font font) {
        final JButton jButton = doCreate();
        applyFont(jButton, font);
        return jButton;
    }

    private JButton doCreate(final Icon imageIcon) {
        final JButton jButton = doCreate();
        applyIcon(jButton, imageIcon);
        return jButton;
    }

    /**
     * Create a JButton with the specified text.
     * 
     * @param text
     *            The button text.
     * @return The JButton.
     */
    private JButton doCreate(final String text) {
        final JButton jButton = doCreate();
        jButton.setText(text);
        return jButton;
    }

    /**
     * A wrapper class for executing thinkParity actions from a popup menu.
     * 
     */
    private class ActionWrapper extends javax.swing.AbstractAction {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /** A thinkParity action. */
        private final AbstractAction action;

        /** The action data. */
        private Data data;

        /**
         * Create SwingAbstractAction.
         * 
         * @param action
         *            A thinkParity action.
         * @param data
         *            The action data.
         */
        private ActionWrapper(final AbstractAction action) {
            super();
            this.action = action;
            this.data = null;
            putValue(Action.NAME, action.isSetName() ? action.getName()
                    : "!No name set.!");
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         * 
         */
        public void actionPerformed(final ActionEvent e) {
            try {
                action.invokeAction(data);
            } catch (final Throwable t) {
                ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER))
                        .displayErrorDialog(t);
            }
        }

        /**
         * Set the action data.
         * 
         * @param data
         *            THe action data.
         */
        public void setData(final Data data) {
            this.data = data;
        }
    }
}
