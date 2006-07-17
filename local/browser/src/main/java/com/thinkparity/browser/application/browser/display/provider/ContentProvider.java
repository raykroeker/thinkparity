/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import org.apache.log4j.Logger;

import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.model.profile.Profile;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ContentProvider {

    /** An apache logger. */
    protected final Logger logger;

    /** A thinkParity profile. */
    protected final Profile profile;

	/**
     * Create a ContentProvider.
     * 
     * @param profile
     *            A thinkParity profile.
     */
	protected ContentProvider(final Profile profile) {
        super();
        this.logger = LoggerFactory.getLogger(getClass());
        this.profile = profile;
    }
}
