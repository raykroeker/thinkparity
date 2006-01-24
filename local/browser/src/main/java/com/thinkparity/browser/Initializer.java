/*
 * Jan 19, 2006
 */
package com.thinkparity.browser;

import java.io.File;

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
		System.setProperty("parity.insecure", "true");

		System.loadLibrary("jawt");
//		System.loadLibrary("nativeskin-1.2.12.win32");
//		System.loadLibrary("nativeskin-1.2.12.win32JAWT");

		LoggerFactory.getLogger(Initializer.class);
		ModelFactory.getInstance().initialize();
		EventDispatcher.getInstance(null).initialize();
	}
}
