/*
 * Created On: Sep 26, 2006 2:46:56 PM
 */
package com.thinkparity.codebase;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.log4j.Log4JHelper;


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
        return MessageFormat.format("{0} - {1}.{2}({3}:{4})",
                t.getLocalizedMessage(),
                StackUtil.getFrameClassName(2),
                StackUtil.getFrameMethodName(2),
                StackUtil.getFrameFileName(2),
                StackUtil.getFrameLineNumber(2));
    }

    public String getErrorId(final Logger logger, final String errorPattern,
            final Object... errorArguments) {
        final String localizedMessage =
            Log4JHelper.renderAndFormat(logger, errorPattern, errorArguments);
        return MessageFormat.format("{0} - {1}.{2}({3}:{4})",
                localizedMessage,
                StackUtil.getFrameClassName(2),
                StackUtil.getFrameMethodName(2),
                StackUtil.getFrameFileName(2),
                StackUtil.getFrameLineNumber(2));
    }
}
