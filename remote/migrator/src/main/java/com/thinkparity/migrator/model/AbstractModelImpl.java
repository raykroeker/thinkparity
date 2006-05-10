/*
 * May 9, 2006
 */
package com.thinkparity.migrator.model;

import org.apache.log4j.Logger;

import com.thinkparity.migrator.LoggerFactory;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public abstract class AbstractModelImpl {

    /** An apache logger. */
    protected final Logger logger;

    /** Create AbstractModelImpl. */
    protected AbstractModelImpl() {
        super();
        this.logger = LoggerFactory.getLogger(getClass());
    }
}
