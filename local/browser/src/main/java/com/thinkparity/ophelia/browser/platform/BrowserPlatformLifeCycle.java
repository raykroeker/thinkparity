/*
 * Created On:  Nov 5, 2007 12:40:55 PM
 */
package com.thinkparity.ophelia.browser.platform;


/**
 * <b>Title:</b>thinkParity Browser Platform Life Cycle State<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class BrowserPlatformLifeCycle {

    /** The life-cycle state. */
    private State state;

    /**
     * Create BrowserPlatformLifeCycle.
     *
     */
    BrowserPlatformLifeCycle() {
        super();
    }

    /**
     * Determine if the life cycle state is ended.
     *
     */
    Boolean isEnded() {
        return state == State.ENDED;
    }

    /**
     * Determine if the life cycle state is ending.
     *
     */
    Boolean isEnding() {
        return state == State.ENDING;
    }

    /**
     * Set the life cycle state to ended.
     *
     */
    void setEnded() {
        this.state = State.ENDED;
    }

    /**
     * Set the life cycle state to ending.
     *
     */
    void setEnding() {
        this.state = State.ENDING;
    }

    private enum State { ENDED, ENDING, STARTED, STARTING }
}
