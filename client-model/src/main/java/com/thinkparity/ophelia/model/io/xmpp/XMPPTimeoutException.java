/*
 * Created On:  12-Apr-07 3:39:24 PM
 */
package com.thinkparity.ophelia.model.io.xmpp;

import java.text.MessageFormat;

import com.thinkparity.codebase.StringUtil.Separator;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class XMPPTimeoutException extends XMPPException {

    /** The exception messsage pattern <code>String</code>. */
    private static final String PATTERN;

    static {
        PATTERN = "XMPP method {0} has timed out.{1}  {2}ms execution time.{1}  {3}ms serialization time.";
    }

    /**
     * Create XMPPTimeoutException.
     * 
     * 
     * @param xmppMethod
     *            A <code>XMPPMethod</code>.
     */
    XMPPTimeoutException(final XMPPMethod method) {
        super(MessageFormat.format(PATTERN, method.getName(),
                Separator.SystemNewLine, method.getExecutionTime(),
                method.getSerializationTime()));
    }
}
