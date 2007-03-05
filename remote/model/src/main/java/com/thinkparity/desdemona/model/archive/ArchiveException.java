/*
 * Created On:  5-Mar-07 10:00:38 AM
 */
package com.thinkparity.desdemona.model.archive;

import java.text.MessageFormat;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Archive Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveException extends RuntimeException {

    /**
     * Create ArchiveException.
     *
     */
    ArchiveException(final Throwable cause, final String message,
            final Object... messageArguments) {
        super(new MessageFormat(message).format(messageArguments), cause);
    }
}
