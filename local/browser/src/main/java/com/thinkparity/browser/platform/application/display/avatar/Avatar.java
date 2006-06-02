/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.platform.application.display.avatar;

import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class Avatar extends AbstractJPanel {

    /**
     * Obtain the enter key stroke.
     *
     * @return A key stroke.
     */
    private static KeyStroke getEnterKeyStroke() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
    }

    /**
     * Obtain the escape key stroke.
     *
     * @return A key stroke.
     */
    private static KeyStroke getEscapeKeyStroke() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    }

	/**
	 * The avatar's content provider.
	 * 
	 */
	protected ContentProvider contentProvider;

	/**
	 * The avatar's error.
	 * 
	 */
	protected final List<Throwable> errors;

	/**
	 * The avatar input.
	 * 
	 */
	protected Object input;

	/**
	 * The main controller.
	 * 
	 */
	private Browser controller;

	/**
	 * The avatar's scrolling policy.
	 * 
	 */
	private final ScrollPolicy scrollPolicy;

	/**
	 * Create a Avatar.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 */
	protected Avatar(final String l18nContext) {
		this(l18nContext, ScrollPolicy.NONE);
	}

    /**
     * Create an Avatar.
     * 
     * @param l18nContext
     *            The localiztaion context.
     * @param background
     *            The avatar background colour.
     */
	protected Avatar(final String l18nContext, final Color background) {
		this(l18nContext, ScrollPolicy.NONE, background);
	}

	/**
	 * Create an Avatar.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 * @param scrollPolicy
	 *            The scrolling policy.
	 */
	protected Avatar(final String l18nContext, final ScrollPolicy scrollPolicy) {
		super(l18nContext);
		this.errors = new LinkedList<Throwable>();
		this.scrollPolicy = scrollPolicy;
	}

	/**
	 * Create an Avatar.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 * @param scrollPolicy
	 *            The scrolling policy.
	 * @param background
	 *            The background color.
	 */
	protected Avatar(final String l18nContext, final ScrollPolicy scrollPolicy,
			final Color background) {
		super(l18nContext, background);
		this.errors = new LinkedList<Throwable>();
		this.scrollPolicy = scrollPolicy;
	}

	/**
	 * Set an error for display.
	 * 
	 * @param error
	 *            The error.
	 */
	public void addError(final Throwable error) { errors.add(error); }

	/**
	 * Clear all display errors.
	 *
	 */
	public void clearErrors() { errors.clear(); }

	/**
	 * Determine whether or not the error has been set.
	 * 
	 * @return True if error has been set; false otherwise.
	 */
	public Boolean containsErrors() { return 0 < errors.size(); }

	/**
	 * Obtain the content provider.
	 * 
	 * @return The content provider.
	 */
	public ContentProvider getContentProvider() { return contentProvider; }

	/**
	 * Obtain the main controller.
	 *
	 */
	public Browser getController() {
		if(null == controller) { controller = Browser.getInstance(); }
		return controller;
	}

	/**
	 * Obtain the error.
	 * 
	 * @return The error.
	 */
	public List<Throwable> getErrors() { return errors; }

	/**
	 * Obtain the avatar id.
	 * 
	 * @return The avatar id.
	 */
	public abstract AvatarId getId();

	/**
	 * Obtain the avatar's input.
	 * 
	 * @return The input.
	 */
	public Object getInput() { return input; }

	/**
	 * Obtain the scroll policy for the avatar.
	 * 
	 * @return The scroll policy for the avatar.
	 */
	public ScrollPolicy getScrollPolicy() { return scrollPolicy; }

	/**
	 * Obtain the avatar's state information.
	 * 
	 * @return The avatar's state information.
	 */
	public abstract State getState();

	/**
	 * Reload the avatar. This event is called when either the content provider
	 * or the input has changed; or as a manual reload of the avatar.
	 * 
	 */
	public void reload() {}

	/**
	 * Set the content provider.
	 * 
	 * @param contentProvider
	 *            The content provider.
	 */
	public void setContentProvider(final ContentProvider contentProvider) {
		Assert.assertNotNull(
				"Cannot set a null content provider:  " + getId(), contentProvider);
		if(this.contentProvider == contentProvider
				|| contentProvider.equals(this.contentProvider)) { return; }
		
		this.contentProvider = contentProvider;
		reload();
	}

	/**
	 * Set the avatar's input.
	 * 
	 * @param input
	 *            The avatar input.
	 */
	public void setInput(final Object input) {
		Assert.assertNotNull("Cannot set null input:  " + getId(), input);
		if(this.input == input || input.equals(this.input)) { return; }
		
		this.input = input;
		reload();
	}

	/**
	 * Set the avatar state.
	 * 
	 * @param state
	 *            The avatar's state information.
	 */
	public abstract void setState(final State state);

	/**
     * Causes <i>doRun.run()</i> to be executed asynchronously on the AWT event
     * dispatching thread.
     * 
     * @param doRun
     *            The runnable to execute.
     * @see SwingUtilities#invokeLater(java.lang.Runnable)
     */
	protected void invokeLater(final Runnable doRun) {
        SwingUtilities.invokeLater(doRun);
    }

	/**
	 * Determine whether or not the platform is running in test mode.
	 * 
	 * @return True if the platform is in test mode; false otherwise.
	 */
	protected final Boolean isDebugMode() {
		return getController().getPlatform().isDebugMode();
	}

	/**
	 * Determine whether or not the platform is running in test mode.
	 * 
	 * @return True if the platform is in test mode; false otherwise.
	 */
	protected final Boolean isTestMode() {
		return getController().getPlatform().isTestMode();
	}

	/**
	 * Provide a visual cue to the user that work is being done.
	 *
	 */
	protected void toggleVisualFeedback(final Boolean isWorking) {
		if(isWorking) { setIsWorking(); }
		else { setIsNotWorking(); }
	}

	private void setIsNotWorking() {
		final Component[] components = getComponents();
		for(final Component c : components) {
			if(c.getClass().isAssignableFrom(JButton.class)) {
				c.setEnabled(true);
			}
		}

	}

	private void setIsWorking() {
		final Component[] components = getComponents();
		for(final Component c : components) {
			if(c.getClass().isAssignableFrom(JButton.class)) {
				c.setEnabled(false);
			}
		}
	}

    /**
     * Bind the enter key to the action.
     *
     * @param action
     *      The action to perform when enter is pressed.
     */
    protected void bindEnterKey(final String command,
            final Action action) {
        bindKey(getEnterKeyStroke(), command, action);
    }

    /**
     * Bind the escape key to an action.
     *
     * @param action
     *      The action to perform when escape is pressed.
     */
    protected void bindEscapeKey(final String command,
            final Action action) {
        bindKey(getEscapeKeyStroke(), command, action);
    }

    /**
     * Bind a key stroke to an action through a binding.
     *
     * @param keyStroke
     *      A key stroke.
     * @param action
     *      An action.
     */
    private void bindKey(final KeyStroke keyStroke, final String command,
            final Action action) {
        final ActionMap actionMap = getActionMap();
        final InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        actionMap.put(command, action);
        inputMap.put(keyStroke, command);
    }

    /**
	 * The scrolling policy for the avatar.
	 * 
	 */
	public enum ScrollPolicy { BOTH, HORIZONTAL, NONE, VERTICAL }
}
