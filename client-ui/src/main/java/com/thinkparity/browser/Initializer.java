/*
 * Jan 19, 2006
 */
package com.thinkparity.browser;

import com.thinkparity.browser.javax.swing.browser.Controller;
import com.thinkparity.browser.log4j.LoggerFactory;
import com.thinkparity.browser.model.EventDispatcher;
import com.thinkparity.browser.model.ModelFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Initializer {

	private static final Initializer INSTANCE;

	static { INSTANCE = new Initializer(); }

	public static Initializer getInstance() { return INSTANCE; }

	/**
	 * Create a Initializer.
	 */
	private Initializer() { super(); }

	/**
	 * Initialize the parity browser.
	 *
	 */
	public void initialize() {
		System.setProperty("parity.insecure", "true");

		LoggerFactory.getLogger(Initializer.class);
		ModelFactory.getInstance().initialize();
		EventDispatcher.getInstance().initialize();
		Controller.getInstance().initialize();
	}
}
