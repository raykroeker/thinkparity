/*
 * Created On: Mar 9, 2006
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.window;

import com.thinkparity.codebase.swing.AbstractJFrame;

/**
 * <b>Title:</b>thinkParity OpheliaUI Platform Window Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
public final class WindowFactory {

	/** A singleton instance of window factory. */
	private static final WindowFactory SINGLETON;

	static {
        SINGLETON = new WindowFactory();
	}

    /**
     * Create a window.
     * 
     * @return A new <code>Window</code>.
     */
    public static Window create() {
        return create(null);
    }

    /**
     * Create a window.
     * 
     * @param windowId
     *            A <code>WindowId</code>.
     * @param owner
     *            An <code>AbstractJFrame</code> owner.
     * @return A new <code>Window</code>.
     */
    public static Window create(final AbstractJFrame owner) {
        return SINGLETON.createImpl(owner);
    }

    /**
     * Create WindowFactory.
     *
	 */
	private WindowFactory() {
		super();
	}

    /**
     * The window creation implementation. Create an instance of the window,
     * register it then return it.
     * 
     * @param owner
     *            An <code>AbstractJFrame</code> owner.
     * @return The new <code>Window</code>.
     */
	private Window createImpl(final AbstractJFrame owner) {
        return new Window(owner, Boolean.TRUE);
	}
}
