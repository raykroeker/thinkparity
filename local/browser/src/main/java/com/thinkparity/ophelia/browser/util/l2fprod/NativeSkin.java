/*
 * Created On: 6-Oct-06 11:12:07 AM
 */
package com.thinkparity.ophelia.browser.util.l2fprod;

import java.awt.Window;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.l2fprod.gui.region.Region;

/**
 * A wrapper class for all native skin calls.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NativeSkin {

	/** The native skin library. */
	private static final com.l2fprod.gui.nativeskin.NativeSkin NATIVE_SKIN;

	/**
	 * A flag indicating whether or not to apply a native skin to the thinkParity
	 * UI.
	 */
	private static final Boolean NO_NATIVE_SKIN;

	static {
        switch (OSUtil.getOS()) {
        case LINUX:
        case OSX:
        case WINDOWS_2000:
            new Log4JWrapper().logInfo(
                    "Native skin library not available for your os {0}.",
                    OSUtil.getOS());
            NO_NATIVE_SKIN = Boolean.TRUE;
            break;
        case WINDOWS_XP:
            NO_NATIVE_SKIN = Boolean.getBoolean("thinkparity.nonativeskin");
            break;
        default:
            throw Assert.createUnreachable("UNKNOWN OS");
        }

		if (NO_NATIVE_SKIN) {
			NATIVE_SKIN = null;
		} else {
            NATIVE_SKIN = com.l2fprod.gui.nativeskin.NativeSkin.getInstance();
		}
	}

	/**
	 * Create NativeSkin.
	 *
	 */
	public NativeSkin() {
		super();
	}

	/**
	 * Apply rounded corners to the window.
	 * 
	 * @param window
	 *            A <code>Window</code>.
	 */
	public void roundCorners(final Window window) {
		if (!NO_NATIVE_SKIN) {
			final Region region = NATIVE_SKIN.createRoundRectangleRegion(0, 0,
					window.getWidth() + 1, window.getHeight() + 1, 9, 9);
			NATIVE_SKIN.setWindowRegion(window, region, true);
		}
	}
}
