/*
 * Jan 19, 2006
 */
package com.thinkparity.browser;

import com.thinkparity.browser.model.EventDispatcher;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.util.log4j.LoggerFactory;

import com.thinkparity.codebase.PropertiesUtil;

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
		
		final String javaLibraryPath = new StringBuffer(System.getProperty("java.library.path"))
			.append(System.getProperty("user.dir"))
			.toString();
		System.setProperty("java.library.path", javaLibraryPath);
		final StringBuffer buffer = new StringBuffer();
		PropertiesUtil.print(buffer, System.getProperties());
		System.out.println(buffer);

		LoggerFactory.getLogger(Initializer.class);
		ModelFactory.getInstance().initialize();
		EventDispatcher.getInstance(null).initialize();
	}
}
