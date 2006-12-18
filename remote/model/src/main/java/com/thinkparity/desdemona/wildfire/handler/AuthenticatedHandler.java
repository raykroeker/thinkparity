/*
 * Created On: Jun 22, 2006 2:51:33 PM
 */
package com.thinkparity.desdemona.wildfire.handler;

/**
 * <b>Title:</b>thinkParity Model Controller <br>
 * <b>Description:</b>An abstraction of an xmpp controller.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.9
 */
public abstract class AuthenticatedHandler extends AbstractHandler {

    /**
     * Create AbstractHandler.
     * 
     * @param service
     *            The service <code>String</code> this handler can provide.
     */
    public AuthenticatedHandler(final String action) {
        super(action);
    }
}
