/*
 * Created On: Feb 6, 2006
 */
package com.thinkparity.ophelia.model.io;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicIOFactory;
import com.thinkparity.ophelia.model.io.handler.*;
import com.thinkparity.ophelia.model.io.xmpp.XMPPIOFactory;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class IOFactory {

	public static IOFactory getDefault(final Workspace workspace) {
	    final IOFactory ioFactory;
        final IOLayerId ioLayerId = IOLayerId.DB;
		switch(ioLayerId) {
		case DB:
			ioFactory = new HypersonicIOFactory(workspace);
			break;
		default:
            throw Assert.createUnreachable("UNKNOWN IO LAYER ID");
		}
		ioFactory.initialize();
        return ioFactory;
	}

	public static IOFactory getXMPP(final Workspace workspace) {
        final IOFactory ioFactory = new XMPPIOFactory(workspace);
        ioFactory.initialize();
        return ioFactory;
    }

	/**
	 * The parity workspace.
	 * 
	 */
	protected final Workspace workspace;

	/**
	 * Create an IOFactory [Abstract Factory]
	 * 
	 */
	protected IOFactory(final Workspace workspace) {
		super();
		this.workspace = workspace;
	}

	/**
	 * Create an artifact io handler.
	 * 
	 * @return An artifact io handler.
	 */
	public abstract ArtifactIOHandler createArtifactHandler();

	/**
	 * Create an audit io handler.
	 * 
	 * @return The audit io handler.
	 */
	public abstract AuditIOHandler createAuditHandler();

	/**
     * Create a configuration io handler.
     * 
     * @return A configuration io handler.
     */
    public abstract ConfigurationIOHandler createConfigurationHandler();

    /**
     * Create a contact io handler.
     * 
     * @return A contact io handler.
     */
    public abstract ContactIOHandler createContactHandler();

    /**
     * Create a container io handler.
     * 
     * @return A container io handler.
     */
    public abstract ContainerIOHandler createContainerHandler();

    /**
	 * Create a document io handler.
	 * 
	 * @return The document io handler.
	 */
	public abstract DocumentIOHandler createDocumentHandler();

    /**
	 * Create a document history io handler.
	 * 
	 * @return A document history io handler.
	 */
	public abstract DocumentHistoryIOHandler createDocumentHistoryIOHandler();

    /**
     * Create a migrator handler.
     * 
     * @return An instance of <code>MigratorIOHandler</code>.
     */
    public abstract MigratorIOHandler createMigratorHandler();

    /**
     * Create a thinkParity profile db io handler.
     * 
     * @return A thinkParity profile db io handler.
     */
    public abstract ProfileIOHandler createProfileHandler();

	/**
     * Create a user io handler.
     *
     * @return A user io handler.
     */
    public abstract UserIOHandler createUserIOHandler();

    /**
	 * Initialize the io layer. Any directory\file creation should be done here.
	 * 
	 */
	public abstract void initialize();
}
