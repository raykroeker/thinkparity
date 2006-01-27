/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Color;

import com.thinkparity.browser.Controller;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.ui.display.Display;
import com.thinkparity.browser.ui.display.provider.ContentProvider;
import com.thinkparity.browser.util.State;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class Avatar extends AbstractJPanel {

	/**
	 * The avatar's content provider.
	 * 
	 */
	protected ContentProvider contentProvider;

	/**
	 * The current display.
	 * 
	 */
	protected Display display;

	/**
	 * The avatar input.
	 * 
	 */
	protected Object input;

	/**
	 * The main controller.
	 * 
	 */
	private Controller controller;

	/**
	 * Create a Avatar.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 */
	protected Avatar(final String l18nContext) { super(l18nContext); }

	/**
	 * Create an Avatar.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 * @param background
	 *            The background.
	 */
	protected Avatar(final String l18nContext, final Color background) {
		super(l18nContext, background);
	}

	/**
	 * Obtain the content provider.
	 * 
	 * @return The content provider.
	 */
	public ContentProvider getContentProvider() { return contentProvider; }

	/**
	 * Obtain the current display.
	 * 
	 * @return The current display.
	 */
	public Display getDisplay() { return display; }

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
	 * Set the current display.
	 * 
	 * @param display
	 *            The display.
	 */
	public void setDisplay(Display display) { this.display = display; }

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
	 * Obtain the main controller.
	 *
	 */
	protected Controller getController() {
		if(null == controller) { controller = Controller.getInstance(); }
		return controller;
	}
}
