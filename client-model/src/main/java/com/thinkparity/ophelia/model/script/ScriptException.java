/*
 * Created On: Oct 15, 2006 1:13:11 PM
 */
package com.thinkparity.ophelia.model.script;

/**
 * <b>Title:</b>thinkParity OpheliaModel Script Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public class ScriptException extends RuntimeException {

    /**
     * Create ScriptException.
     * 
     * @param cause
     *            The <code>Throwable</code> cause of the script exception.
     */
    public ScriptException(final Throwable cause) {
        super(cause);
    }
}
