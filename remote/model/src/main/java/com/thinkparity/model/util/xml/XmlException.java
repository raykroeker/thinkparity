/*
 * Created On: Sep 4, 2006 9:05:47 AM
 */
package com.thinkparity.model.util.xml;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.StackUtil;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class XmlException extends RuntimeException {

    /**
     * Translate an error.
     * 
     * @param t
     *            An error <code>Throwable</code>.
     * @return An xml error <code>XmlException</code>.
     */
    static XmlException translateError(final Throwable t) {
        if (XmlException.class.isAssignableFrom(t.getClass())) {
            return (XmlException) t;
        }
        else {
            final Object errorId = getErrorId(t);
            Logger.getLogger(XmlException.class).error(errorId, t);
            return new XmlException(errorId.toString(), t);
        }
    }

    /**
     * Obtain an error id.
     * 
     * @return An error id.
     */
    private static final Object getErrorId(final Throwable t) {
        return MessageFormat.format("[{0}] [{1}] [{2}] - [{3}]",
                    StackUtil.getFrameClassName(2),
                    StackUtil.getFrameMethodName(2),
                    t.getMessage());
    }

    /**
     * Create XmlException.
     * 
     * @param message
     *            The error message <code>String</code>.
     * @param cause
     *            The xml exception cause.
     */
    private XmlException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
