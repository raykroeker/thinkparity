/*
 * Created On: Sat May 27 2006 10:52 PDT

 * $Id$
 */
package com.thinkparity.model;

import org.apache.log4j.Logger;

/**
 * A logger factory for the local model.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class LoggerFactory {

	/**
	 * Obtain a logger.
	 * 
	 * @param clasz
	 *            The caller's class.
	 * @return The logger.
	 */
    public static Logger getLogger(final Class clasz) {
        return Logger.getLogger(clasz);
    }

    /** Create LoggerFactory. */
    private LoggerFactory() { super(); }
}
