/*
 * Created On: 2007-04-14 10:48 -0700
 */
package com.thinkparity.ophelia.browser.util.window;

import java.awt.Window;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.util.window.win32.Win32WindowUtil;

/**
 * <b>Title:</b>thinkParity OpheliaUI Window Util Provider<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class WindowUtilProvider {

    /** A singleton instance of <code>WindowUtilProvider</code>. */
    private static WindowUtilProvider INSTANCE;

    /**
     * Obtain a window util provider.
     * 
     * @return An instance of <code>WindowUtilProvider</code>.
     */
    public static WindowUtilProvider getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new WindowUtilProvider();
        }
        return INSTANCE;
    }

    /** An instance of <code>WindowUtil</code>. */
    private final WindowUtil windowUtil;

    /**
     * Create WindowUtilProvider.
     *
     */
	private WindowUtilProvider() {
		super();
        switch (OSUtil.getOs()) {
        case WINDOWS_XP:
        case WINDOWS_VISTA:
            this.windowUtil = new Win32WindowUtil();
            break;
        case LINUX:
        case MAC_OSX:
            this.windowUtil = new WindowUtil() {
                public void applyRectangleEdges(final Window window) {}
                public void applyRoundedEdges(final Window window,
                        final Integer ellipseSize) {}
            };
            break;
        default:
            throw Assert.createUnreachable("Unknown os.");
        }
	}

    /**
     * Obtain a window util.
     * 
     * @return An instance of <code>WindowUtil</code>.
     */
    public WindowUtil getWindowUtil() {
        return windowUtil;
    }
}
