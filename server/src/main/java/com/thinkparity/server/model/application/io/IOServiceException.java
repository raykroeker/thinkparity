/*
 * Created On:  7-Oct-07 12:47:32 PM
 */
package com.thinkparity.desdemona.model.io;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>Title:</b>thinkParity Desdemona Model IO Service Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IOServiceException extends Exception {

    /** A message pattern. */
    private static final Map<Code, String> PATTERNS;

    static {
        PATTERNS = new HashMap<Code, String>(1, 1.0F);
        PATTERNS.put(Code.INSTANTIATION, "Could not instantiate io service.");
    }

    /**
     * Create IOServiceException.
     * 
     * @param code
     *            A <code>Code</code>.
     * @param cause
     *            A <code>Throwable</code>.
     */
    IOServiceException(final Code code, final Throwable cause) {
        super(PATTERNS.get(code), cause);
    }

    /** <b>Title:</b>IO Error Codes<br> */
    enum Code { INSTANTIATION }
}
