/*
 * May 9, 2006 11:59:13 AM
 * $Id$
 */
package com.thinkparity.migrator;

import org.apache.log4j.Logger;

/**
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
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
