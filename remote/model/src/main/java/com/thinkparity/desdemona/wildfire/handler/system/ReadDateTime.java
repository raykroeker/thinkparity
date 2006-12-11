/*
 * Created On: Sep 28, 2006 8:41:26 AM
 */
package com.thinkparity.desdemona.wildfire.handler.system;

import com.thinkparity.codebase.DateUtil;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadDateTime extends AbstractHandler {

    /**
     * Create ReadDateTime.
     * 
     */
    public ReadDateTime() {
        super("system:readdatetime");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        writeCalendar("datetime", DateUtil.getInstance());
    }
}
