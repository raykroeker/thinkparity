/*
 * Created On: Oct 2, 2006 12:04:21 PM
 */
package com.thinkparity.desdemona.wildfire.handler.system;

import com.thinkparity.desdemona.wildfire.handler.AnonymousHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class CreateResource extends AnonymousHandler {

    /** Create CreateResource. */
    public CreateResource() {
        super("system:createresource");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        writeString("resource", "" + System.currentTimeMillis());
    }
}
