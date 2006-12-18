/*
 * Created On: Oct 2, 2006 11:56:42 AM
 */
package com.thinkparity.desdemona.wildfire.handler;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AnonymousHandler extends AbstractHandler {

    /**
     * Create AnonymousHandler.
     *
     * @param service
     *            The service <code>String</code> this handler can provide.
     */
    protected AnonymousHandler(final String service) {
        super(service);
    }
}
