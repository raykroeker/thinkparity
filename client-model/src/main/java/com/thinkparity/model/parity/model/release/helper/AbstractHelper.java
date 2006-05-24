/*
 * Created On: May 22, 2006 12:57:24 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.release.helper;

import org.apache.log4j.Logger;

import com.thinkparity.model.log4j.ModelLoggerFactory;

/**
 * An abstraction of a release helper.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class AbstractHelper {

    /** An apache logger. */
    protected final Logger logger;

    /** Create AbstractHelper. */
    protected AbstractHelper() {
        this.logger = ModelLoggerFactory.getLogger(getClass());
    }
}
