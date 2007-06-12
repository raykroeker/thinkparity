/*
 * Created On:  19-Feb-07 6:12:01 PM
 */
package com.thinkparity.codebase.model.session;

import javax.xml.ws.WebFault;

/**
 * <b>Title:</b>thinkParity Invalid Credentials Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebFault
public class InvalidCredentialsException extends Exception {

    /** Exception message format pattern <code>String</code>. */
    private static final String PATTERN;

    static {
        PATTERN = "Cannot login using the given credentials.";
    }

    /**
     * Create InvalidCredentialsException.
     *
     */
    public InvalidCredentialsException() {
        super(PATTERN);
    }
}
