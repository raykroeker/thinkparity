/*
 * Created On:  28-Sep-07 2:15:52 PM
 */
package com.thinkparity.codebase.model.util.xmpp.event.profile;

import java.util.Calendar;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b>thinkParity Common Model Profile Active Event<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ActiveEvent extends XMPPEvent {

    /** An active state. */
    private Boolean active;

    /** An activate/deactivate date. */
    private Calendar activeOn;

    /**
     * Create ActiveEvent.
     *
     */
    public ActiveEvent() {
        super();
    }

    /**
     * Obtain the active.
     *
     * @return A <code>Boolean</code>.
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * Obtain the activeOn.
     *
     * @return A <code>Calendar</code>.
     */
    public Calendar getActiveOn() {
        return activeOn;
    }

    /**
     * Set the active.
     *
     * @param active
     *		A <code>Boolean</code>.
     */
    public void setActive(final Boolean active) {
        this.active = active;
    }

    /**
     * Set the activeOn.
     *
     * @param activeOn
     *		A <code>Calendar</code>.
     */
    public void setActiveOn(final Calendar activeOn) {
        this.activeOn = activeOn;
    }
}