/*
 * Created On: 2007-04-14 10:48 -0700
 */
package com.thinkparity.ophelia.support.util.process;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.support.util.process.win32.Win32ProcessUtil;

/**
 * <b>Title:</b>thinkParity OpheliaUI Window Util Provider<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProcessUtilProvider {

    /** A singleton instance of <code>ProcessUtilProvider</code>. */
    private static ProcessUtilProvider INSTANCE;

    /**
     * Obtain a process util provider.
     * 
     * @return An instance of <code>ProcessUtilProvider</code>.
     */
    public static ProcessUtilProvider getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ProcessUtilProvider();
        }
        return INSTANCE;
    }

    /** An instance of <code>ProcessUtil</code>. */
    private final ProcessUtil processUtil;

    /**
     * Create WindowUtilProvider.
     *
     */
	private ProcessUtilProvider() {
		super();
        switch (OSUtil.getOs()) {
        case WINDOWS_XP:
        case WINDOWS_VISTA:
            this.processUtil = new Win32ProcessUtil();
            break;
        case LINUX:
        case MAC_OSX:
        default:
            throw Assert.createUnreachable("Unknown os.");
        }
	}

    /**
     * Obtain a process util.
     * 
     * @return A <code>ProcessUtil</code>.
     */
    public ProcessUtil getProcessUtil() {
        return processUtil;
    }
}
