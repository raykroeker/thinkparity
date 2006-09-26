/*
 * Created On: Sep 26, 2006 2:46:56 PM
 */
package com.thinkparity.codebase;

import java.text.MessageFormat;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ErrorHelper {

    /** Create ErrorHelper. */
    public ErrorHelper() {
        super();
    }

    public String getErrorId(final Throwable t) {
        return MessageFormat.format("{0}#{1} - {2}",
                StackUtil.getFrameClassName(2),
                StackUtil.getFrameMethodName(2), t.getLocalizedMessage());
    }

}
