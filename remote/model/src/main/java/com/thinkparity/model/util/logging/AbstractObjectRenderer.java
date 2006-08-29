/*
 * Created On: Aug 29, 2006 12:33:45 PM
 */
package com.thinkparity.model.util.logging;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.log4j.Log4JHelper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractObjectRenderer {

    /** An apache logger. */
    private final Logger logger;

    /** Create AbstractRenderer. */
    protected AbstractObjectRenderer() {
        super();
        this.logger = Logger.getLogger(getClass());
    }

    /**
     * Render an object.
     * 
     * @param o
     *            An <code>Object</code>.
     * @return A rendering of the object <code>String</code>.
     */
    protected String render(final Object o) {
        return Log4JHelper.render(logger, o);
    }
}
