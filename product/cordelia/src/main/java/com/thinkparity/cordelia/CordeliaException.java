/**
 * 
 */
package com.thinkparity.cordelia;

import com.thinkparity.codebase.model.Context;


/**
 * @author raymond
 *
 */
public class CordeliaException extends RuntimeException {

    public static CordeliaException translate(final Context context,
            final String message, final Throwable cause) {
        return new CordeliaException(message, cause);
    }

    private CordeliaException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
