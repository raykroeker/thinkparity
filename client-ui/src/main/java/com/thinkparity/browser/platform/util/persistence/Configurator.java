/*
 * Jan 6, 2006
 */
package com.thinkparity.browser.platform.util.persistence;

import java.util.Properties;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Configurator {

	public static void configure(final Properties properties) {
		new Configurator().doConfigure(properties);
	}

	/**
	 * Create a Configurator.
	 */
	private Configurator() {
		super();
	}

	/**
	 * 
	 * @param properties
	 */
	private void doConfigure(final Properties properties) {
		
	}
}
