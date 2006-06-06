/*
 * Created On: Jun 6, 2006 11:18:42 AM
 * $Id$
 */
package com.thinkparity.server;

import org.apache.log4j.Logger;

/**
 * thinkParity Remote Logger Factory
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class LoggerFactory {

    /**
     * Obtain an apache logger.
     * 
     * @param clasz
     *            The logger's category.
     * @return An apache logger.
     */
    public static Logger getLogger(final Class clasz) {
        return Logger.getLogger(clasz);
    }

    /** Create LoggerFactory. */
    private LoggerFactory() { super(); }
}
