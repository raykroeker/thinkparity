/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.application.browser.component;

import javax.swing.Icon;
import javax.swing.JButton;

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
	 * Create a ButtonFactory.
	 */
	private ButtonFactory() { super(); }

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
}
