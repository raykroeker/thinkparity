/*
 * Jan 20, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.platform.application.display.Display;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class DisplayFactory {

    /** A singleton instance. */
	private static final DisplayFactory SINGLETON;

	static { SINGLETON = new DisplayFactory(); }

    /**
     * Create a display.
     * 
     * @param id
     *            A <code>DisplayId</code>.
     * @return A <code>Display</code>.
     */
	public static Display create(final DisplayId id) {
	    return SINGLETON.doCreate(id);
	}

	/**
	 * Create DisplayFactory.
	 * 
	 */
	private DisplayFactory() { super(); }

    /**
     * Create a display.
     * 
     * @param id
     *            A <code>DisplayId</code>.
     * @return A <code>Display</code>.
     */
    private Display doCreate(final DisplayId id) {
        final Display display;
        switch(id) {
        case CONTENT:
            display = new ContentDisplay();
            break;
        case STATUS:
            display = new StatusDisplay();
            break;
        case TITLE:
            display = new TitleDisplay();
            break;
        default:
            throw Assert.createUnreachable("UNKOWN DISPLAY");
        }
        return display;
    }
}
