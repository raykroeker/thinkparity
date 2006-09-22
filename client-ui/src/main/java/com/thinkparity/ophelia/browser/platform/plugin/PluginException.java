/*
 * Created On: Sep 21, 2006 3:54:57 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PluginException extends RuntimeException {

    static PluginException translate(final String errorId, final Throwable cause) {
        return new PluginException(errorId, cause);
    }

    /**
     * Create PluginException.
     * 
     * @param errorId
     *            The error id.
     * @param cause
     *            The error cause.
     */
    private PluginException(final String errorId, final Throwable cause) {
        super(errorId, cause);
    }
}
