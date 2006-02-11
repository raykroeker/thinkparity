/*
 * Jan 19, 2006
 */
package com.thinkparity.browser;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.thinkparity.browser.model.EventDispatcher;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

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

		// Set the application lnf
		try { UIManager.setLookAndFeel(new WindowsLookAndFeel()); }
		catch(final UnsupportedLookAndFeelException ulafx) {
			throw new RuntimeException(ulafx);
		}
		
		// ensure a session is established
//		if(Status.ONLINE != ModelSession.getStatus()) {
//			ModelSession.establishSession();
//		}
//		if(Status.ONLINE != ModelSession.getStatus()) {
//			System.out.println("Must be logged in to use the parity browser.");
//			System.exit(1);
//		}
	}
}
