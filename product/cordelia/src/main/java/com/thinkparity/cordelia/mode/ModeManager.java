/*
 * Created On: Mon 0ct 16, 2006 10:14 
 */
package com.thinkparity.cordelia.mode;

import com.thinkparity.codebase.Mode;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ModeManager {

    /** Create ModeManager. */
    public ModeManager() {
        super();
    }

    /**
     * Select a thinkParity mode.
     * 
     * @return A thinkParity <code>Mode</code>.
     */
    public Mode select() {
        final String modeProperty = System.getProperty("thinkparity.mode");
        return Mode.valueOf(modeProperty);
    }
}
