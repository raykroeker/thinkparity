/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import org.apache.log4j.Logger;

import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ContentProvider {

    /** An apache logger. */
    protected final Logger logger;

	/**
	 * Create a ContentProvider.
	 * 
	 */
	protected ContentProvider() {
        super();
        this.logger = LoggerFactory.getLogger(getClass());
    }
}
