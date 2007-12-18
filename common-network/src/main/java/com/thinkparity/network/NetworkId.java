/*
 * Created On:  21-Aug-07 3:26:20 PM
 */
package com.thinkparity.network;

import java.text.MessageFormat;

import com.thinkparity.codebase.StackUtil;

/**
 * <b>Title:</b>thinkParity Network Id<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class NetworkId {

    /** The current id. */
    private static long currentId;

    /** A stack filter. */
    private static final StackUtil.Filter NETWORK_STACK_FILTER;

    static {
        currentId = 0;
        NETWORK_STACK_FILTER = new StackUtil.Filter() {
            /**
             * @see com.thinkparity.codebase.StackUtil.Filter#accept(java.lang.StackTraceElement)
             *
             */
            @Override
            public Boolean accept(final StackTraceElement stackElement) {
                /* accept all non-network stack trace elements */
                return !stackElement.getClassName().startsWith(
                        "com.thinkparity.network");
            }
        };
    }

    /**
     * Create NetworkId.
     *
     */
    NetworkId() {
        super();
    }

    /**
     * Increment the current network id.
     * 
     */
    synchronized String nextId() {
        ++currentId;
        final StackTraceElement frame = StackUtil.getFrame(NETWORK_STACK_FILTER);
        return MessageFormat.format("{0,number,0000} {1}.{2}({3}:{4})", currentId,
                frame.getClassName(), frame.getMethodName(), frame.getFileName(),
                frame.getLineNumber());
    }
}
