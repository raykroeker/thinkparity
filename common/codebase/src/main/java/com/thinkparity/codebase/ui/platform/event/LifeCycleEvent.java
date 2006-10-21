/*
 * Created On: Sep 20, 2006 11:01:23 AM
 */
package com.thinkparity.codebase.ui.platform.event;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class LifeCycleEvent {

    /** The life cycle event source. */
    private final Object source;

    /** Create LifeCycleEvent. */
    public LifeCycleEvent(final Object source) {
        super();
        this.source = source;
    }

    /**
     * Obtain the event source.
     * 
     * @return The event source.
     */
    public Object getSource() {
        return source;
    }
}
