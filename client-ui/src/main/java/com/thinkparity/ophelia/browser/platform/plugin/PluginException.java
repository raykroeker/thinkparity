/*
 * Created On: Sep 21, 2006 3:54:57 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

/**
 * <b>Title:</b>A thinkParity Browser Platform Plugin Exception<br>
 * <b>Description:</b>An error thrown by any plugin.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * 
 * @see PluginServices#translateError(Throwable)
 */
public class PluginException extends RuntimeException {

    /**
     * Translate an error into a plugin error.
     * 
     * @param errorId
     *            An error id <code>String</code>.
     * @param cause
     *            The cause <code>Throwable</code> of the error.
     * @return A <code>PluginException</code>.
     * @see PluginServices#translateError(Throwable)
     */
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
