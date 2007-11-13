/*
 * Created On:  9-Nov-07 3:53:00 PM
 */
package com.thinkparity.ophelia.model.util.configuration;

import java.util.Properties;

import com.thinkparity.codebase.event.EventListener;

/**
 * <b>Title:</b>thinkParity Ophelia Model Reconfigure Listener<br>
 * <b>Description:</b>A listener that is notified when a model component's
 * configuration is updated.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <T>
 *            A properties configuration type.
 */
public interface ReconfigureListener<T extends Properties> extends
        EventListener {

    /**
     * Reconfigure.
     * 
     * @param event
     *            A <code>ReconfigureEvent<T></code>.
     */
    void reconfigure(ReconfigureEvent<T> event);
}
