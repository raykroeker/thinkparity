/*
 * Created On:  11-Jan-07 1:00:00 PM
 */
package com.thinkparity.ophelia.model;

import java.util.Calendar;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ThinkParityInterfacePluginContext {

    /** The generated on date. */
    private final String generatedOn;

    /**
     * Create ThinkParityInterfacePluginContext.
     *
     */
    public ThinkParityInterfacePluginContext() {
        super();
        final Calendar now = ThinkParityUtils.getCurrentDateTime();
        this.generatedOn = ThinkParityUtils.format(now);
    }

    /**
     * Obtain generatedOn.
     *
     * @return A String.
     */
    public String getGeneratedOn() {
        return generatedOn;
    }
}
