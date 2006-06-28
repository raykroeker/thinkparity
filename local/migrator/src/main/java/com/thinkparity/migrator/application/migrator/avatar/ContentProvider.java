/*
 * Created On: Jun 25, 2006 11:49:25 AM
 * $Id$
 */
package com.thinkparity.migrator.application.migrator.avatar;

import org.apache.log4j.Logger;

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
        this.logger = Logger.getLogger(getClass());
    }
}
