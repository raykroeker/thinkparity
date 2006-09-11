/*
 * Jan 16, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.model.profile.Profile;


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
        this.logger = Logger.getLogger(getClass());
        this.profile = profile;
    }
}
