/*
 * Created On: Aug 22, 2006 12:05:44 PM
 */
package com.thinkparity.model.util;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Monitor {

    /**
     * Notify the monitor that a change has occured.
     * 
     * @param e
     *            A monitor event.
     */
    public void notifyChange(final MonitorEvent e);
}
