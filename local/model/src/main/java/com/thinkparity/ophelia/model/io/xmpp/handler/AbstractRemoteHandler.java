/*
 * Created On: Jun 22, 2006 10:08:19 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.io.xmpp.handler;


import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.io.xmpp.XMPPSession;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class AbstractRemoteHandler extends AbstractIOHandler {

    /** Create AbstractRemoteHandler. */
    public AbstractRemoteHandler(final Credentials credentials) {
        super();
    }

    /**
     * Open an authenticated session.
     * 
     * @return An authenticated xmpp session.
     */
    protected XMPPSession openAuthenticatedSession() {
        throw Assert.createNotYetImplemented("AbstractRemoteHandler#openAuthenticatedSession");
    }
}
