/*
 * Jan 16, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider;

import com.thinkparity.ophelia.model.profile.ProfileModel;

import org.apache.log4j.Logger;


/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public abstract class ContentProvider {

    /** An apache logger. */
    protected final Logger logger;

    /** A thinkParity profile. */
    protected final ProfileModel profileModel;

    /**
     * Create a ContentProvider.
     * 
     * @param profileModel
     *            A thinkParity profileModel.
     */
	protected ContentProvider(final ProfileModel profileModel) {
        super();
        this.logger = Logger.getLogger(getClass());
        this.profileModel = profileModel;
    }
}
