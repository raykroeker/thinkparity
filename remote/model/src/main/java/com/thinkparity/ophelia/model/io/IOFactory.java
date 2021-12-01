/*
 * Created On: Feb 6, 2006
 */
package com.thinkparity.ophelia.model.io;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicIOFactory;
import com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler;
import com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler;
import com.thinkparity.ophelia.model.io.handler.ContainerIOHandler;
import com.thinkparity.ophelia.model.io.handler.DocumentIOHandler;
import com.thinkparity.ophelia.model.io.handler.UserIOHandler;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raymond@raykroeker.com
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
     * Create a configuration io handler.
     * 
     * @return A configuration io handler.
     */
    public abstract ConfigurationIOHandler createConfigurationHandler();

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
