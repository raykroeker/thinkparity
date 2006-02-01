/*
 * Jan 19, 2006
 */
package com.thinkparity.browser;

import com.thinkparity.browser.model.EventDispatcher;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.util.log4j.LoggerFactory;

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
		System.setProperty("sun.awt.noerasebackground", "true");
		System.setProperty("parity.insecure", "true");

		LoggerFactory.getLogger(Initializer.class);
		ModelFactory.getInstance().initialize();
		EventDispatcher.getInstance().initialize();

//		try {
//			final String username = "raymond";
//			final String password = "parity";
//			SessionModel.getModel().login(username, password);
//		}
//		catch(final ParityException px) { throw new RuntimeException(px); }
	}
}
