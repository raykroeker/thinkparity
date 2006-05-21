/*
 * Created On: Fri May 12 2006 11:34 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.io.xmpp.handler;

import org.apache.log4j.Logger;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.model.io.xmpp.XMPPSession;
import com.thinkparity.model.parity.model.io.xmpp.XMPPSessionManager;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * An abstraction of an xmpp io handler.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
abstract class AbstractIOHandler {

    /** An apache logger. */
    protected final Logger logger;

    /** The parity workspace. */
    protected final Workspace workspace;

    /** Create AbstractIOHandler. */
    protected AbstractIOHandler() {
        super();
        this.logger = ModelLoggerFactory.getLogger(getClass());
        this.workspace = WorkspaceModel.getModel().getWorkspace();
    }

    /**
     * Open an xmpp session.
     *
     * @return The xmpp session.
     */
    protected XMPPSession openSession() {
        return XMPPSessionManager.open(workspace.getPreferences());
    }
}
