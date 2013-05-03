/*
 * Created On: Aug 12, 2006 12:55:35 PM
 */
package com.thinkparity.ophelia.browser.platform;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractFactory {

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /** Create AbstractFactory. */
    protected AbstractFactory() {
        super();
        this.logger = new Log4JWrapper();
    }
}
