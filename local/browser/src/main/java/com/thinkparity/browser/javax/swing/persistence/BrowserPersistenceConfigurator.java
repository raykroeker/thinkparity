/*
 * Jan 6, 2006
 */
package com.thinkparity.browser.javax.swing.persistence;

import java.util.Properties;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserPersistenceConfigurator {

	public static void configure(final Properties properties) {
		new BrowserPersistenceConfigurator().doConfigure(properties);
	}

	/**
	 * Create a BrowserPersistenceConfigurator.
	 */
	private BrowserPersistenceConfigurator() {
		super();
	}

	/**
	 * 
	 * @param properties
	 */
	private void doConfigure(final Properties properties) {
		
	}
}
