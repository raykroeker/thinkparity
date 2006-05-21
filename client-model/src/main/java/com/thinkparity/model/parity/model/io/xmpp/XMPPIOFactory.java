/*
 * Created On: Fri May 12 2006 11:38 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.io.xmpp;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.*;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * An xmpp implementation of the io factory.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class XMPPIOFactory extends IOFactory {

    /** Create XMPPIOFactory. */
    public XMPPIOFactory(final Workspace workspace) { super(workspace); }

    /** @see com.thinkparity.model.parity.model.io.IOFactory#createArtifactHandler() */
    public ArtifactIOHandler createArtifactHandler() {
        throw Assert.createNotYetImplemented("XMPPIOFactory#createArtifactHandler");
    }

    /** @see com.thinkparity.model.parity.model.io.IOFactory#createAuditHandler() */
    public AuditIOHandler createAuditHandler() {
        throw Assert.createNotYetImplemented("XMPPIOFactory#createAuditHandler");
    }

    /** @see com.thinkparity.model.parity.model.io.IOFactory#createDocumentHandler() */
    public DocumentIOHandler createDocumentHandler() {
        throw Assert.createNotYetImplemented("XMPPIOFactory#createDocumentHandler");
    }

    /** @see com.thinkparity.model.parity.model.io.IOFactory#createDocumentHistoryIOHandler() */
    public DocumentHistoryIOHandler createDocumentHistoryIOHandler() {
        throw Assert.createNotYetImplemented("XMPPIOFactory#createDocumentHistoryIOHandler");
    }

    /** @see com.thinkparity.model.parity.model.io.IOFactory#createLibraryHandler() */
    public LibraryIOHandler createLibraryHandler() {
        return new com.thinkparity.model.parity.model.io.xmpp.handler.LibraryIOHandler();
    }

    /** @see com.thinkparity.model.parity.model.io.IOFactory#createReleaseHandler() */
    public ReleaseIOHandler createReleaseHandler() {
        return new com.thinkparity.model.parity.model.io.xmpp.handler.ReleaseIOHandler();
    }

    /** @see com.thinkparity.model.parity.model.io.IOFactory#createSystemMessageHandler() */
    public SystemMessageIOHandler createSystemMessageHandler() {
        throw Assert.createNotYetImplemented("XMPPIOFactory#createSystemMessageHandler");
    }

    /** @see com.thinkparity.model.parity.model.io.IOFactory#createUserIOHandler() */
    public UserIOHandler createUserIOHandler() {
        throw Assert.createNotYetImplemented("XMPPIOFactory#createUserIOHandler");
    }

    /** @see com.thinkparity.browser.bootstrap.io.IOFactory#initialize() */
    public void initialize() {}
}
