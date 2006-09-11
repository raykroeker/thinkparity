/*
 * Jan 13, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserColourButton;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionRegistry;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ButtonFactory extends ComponentFactory {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final ButtonFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new ButtonFactory();
		singletonLock = new Object();
	}
    
    /** An action registry. */
    private final ActionRegistry actionRegistry;

    /** The action wrapper registry. */
    private final Map<ActionId, ActionWrapper> wrapperRegistry;

    /** Create ButtonFactory. */
    private ButtonFactory() {
        super();
        this.actionRegistry = new ActionRegistry();
        this.wrapperRegistry = new HashMap<ActionId, ActionWrapper>(ActionId.values().length, 1.0F);
    }

	/**
	 * Create a JButton.
	 * 
	 * @return The JButton.
	 */
	public static JButton create() {
		synchronized(singletonLock) { return singleton.doCreate(); }
	}

	public static JButton create(final Icon icon) {
		synchronized(singletonLock) { return singleton.doCreate(icon); }
	}

	/**
	 * Create a JButton with the specified text.
	 * 
	 * @param text
	 *            The button text.
	 * @return The JButton.
	 */
	public static JButton create(final String text) { 
		synchronized(singletonLock) { return singleton.doCreate(text); }
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
    public static JButton create(final ActionId actionId,
            final Data data) {
        return singleton.doCreate(actionId, data);
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
     * Create a popup menu item from an action id.
     * 
     * @param actionId
     *            An action id.
     * @param data
     *            The action data.
     * @return A popup menu item.
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
        
        BrowserColourButton button = new BrowserColourButton(actionWrapper);

        // This call allows us to take over drawing of the button.
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        // Background is transparent so it won't draw.
        // Then, BrowserColourButton overrides paintComponent.       
        button.setBackground(new Color(255,255,255,0));        
       
        return button;
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
            putValue(Action.NAME, action.isSetName() ? action.getName() : "!No name set.!");
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         * 
         */
        public void actionPerformed(final ActionEvent e) {
            try {
                action.invoke(data);
            } catch(final Throwable t) {
                ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER))
                        .displayErrorDialog(t.getLocalizedMessage(), t);
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
