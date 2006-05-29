/*
 * Created On: Sat May 27 2006 11:52 PDT

 * $Id$
 */
package com.thinkparity.model.parity.model.io.xmpp;

import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.*;
import com.thinkparity.model.parity.model.workspace.Workspace;

import com.thinkparity.codebase.assertion.Assert;

/**
 * A concrete implemenation of an io factory for xmpp io.  Note that as of yet
 * not all implementations of the xmpp handlers have been completed.  Those that
 * have not; throw runtime assertions.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class XMPPIOFactory extends IOFactory {

    /**
     * Create XMPPIOFactory.
     *
     * @param The parity workspace.
     */
    public XMPPIOFactory(final Workspace workspace) { super(workspace); }

    public ArtifactIOHandler createArtifactHandler() {
        throw Assert.createUnreachable("XMPPIOFactory#createArtifactHandler()");
    }
    public AuditIOHandler createAuditHandler() {
        throw Assert.createUnreachable("XMPPIOFactory#createAuditHandler()");
    }
    public DocumentIOHandler createDocumentHandler() {
        throw Assert.createUnreachable("XMPPIOFactory#createDocumentHandler()");
    }
    public DocumentHistoryIOHandler createDocumentHistoryIOHandler() {
        throw Assert.createUnreachable("XMPPIOFactory#createDocumentHistoryIOHandler()");
    }

    /**
     * Create an xmpp library io handler.
     *
     * @return An xmpp library io handler.
     */
    public LibraryIOHandler createLibraryHandler() {
        return new com.thinkparity.model.parity.model.io.xmpp.handler.LibraryIOHandler();
    }

    /**
     * Create an xmpp release io handler.
     *
     * @return An xmpp release io handler.
     */
    public ReleaseIOHandler createReleaseHandler() {
        return new com.thinkparity.model.parity.model.io.xmpp.handler.ReleaseIOHandler();
    }

    public SystemMessageIOHandler createSystemMessageHandler() {
        throw Assert.createUnreachable("XMPPIOFactory#createSystemMessageHandler()");
    }

    public UserIOHandler createUserIOHandler() {
        throw Assert.createUnreachable("XMPPIOFactory#createUserIOHandler()");
    }

    /** Initialize the io factory. */
    public void initialize() {}
}
