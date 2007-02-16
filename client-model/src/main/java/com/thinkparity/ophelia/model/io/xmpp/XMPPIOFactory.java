/*
 * Created On: Sat May 27 2006 11:52 PDT

 * $Id$
 */
package com.thinkparity.ophelia.model.io.xmpp;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler;
import com.thinkparity.ophelia.model.io.handler.AuditIOHandler;
import com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler;
import com.thinkparity.ophelia.model.io.handler.ContactIOHandler;
import com.thinkparity.ophelia.model.io.handler.ContainerIOHandler;
import com.thinkparity.ophelia.model.io.handler.DocumentHistoryIOHandler;
import com.thinkparity.ophelia.model.io.handler.DocumentIOHandler;
import com.thinkparity.ophelia.model.io.handler.MigratorIOHandler;
import com.thinkparity.ophelia.model.io.handler.ProfileIOHandler;
import com.thinkparity.ophelia.model.io.handler.SystemMessageIOHandler;
import com.thinkparity.ophelia.model.io.handler.UserIOHandler;
import com.thinkparity.ophelia.model.workspace.Workspace;

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

    /** @see com.thinkparity.ophelia.model.io.IOFactory#createConfigurationHandler() */
    public ConfigurationIOHandler createConfigurationHandler() {
        throw Assert.createNotYetImplemented("XMPPIOFactory#createConfigurationHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createContactHandler()
     */
    @Override
    public ContactIOHandler createContactHandler() {
        throw Assert.createNotYetImplemented("XMPPIOFactory#createContactHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createContainerHandler()
     * 
     */
    public ContainerIOHandler createContainerHandler() {
        throw Assert.createNotYetImplemented("XMPPIOFactory#createContainerHandler");
    }

    public DocumentIOHandler createDocumentHandler() {
        throw Assert.createUnreachable("XMPPIOFactory#createDocumentHandler()");
    }
    public DocumentHistoryIOHandler createDocumentHistoryIOHandler() {
        throw Assert.createUnreachable("XMPPIOFactory#createDocumentHistoryIOHandler()");
    }
    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createMigratorHandler()
     *
     */
    @Override
    public MigratorIOHandler createMigratorHandler() {
        throw Assert.createUnreachable("Migrator handler not supported.");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createProfileHandler()
     */
    @Override
    public ProfileIOHandler createProfileHandler() {
        throw Assert.createNotYetImplemented("XMPPIOFactory#createProfileHandler");
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
