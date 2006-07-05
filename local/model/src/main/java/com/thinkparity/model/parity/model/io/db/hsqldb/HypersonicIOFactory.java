/*
 * Feb 6, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.db.hsqldb.util.HypersonicValidator;
import com.thinkparity.model.parity.model.io.handler.*;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicIOFactory extends IOFactory {

	/**
	 * Create a HypersonicIOFactory [Concrete Factory]
	 * 
	 */
	public HypersonicIOFactory(final Workspace workspace) {
		super(workspace);
		new HypersonicValidator(workspace).validate();
	}

    /**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#createArtifactHandler()
	 * 
	 */
	public ArtifactIOHandler createArtifactHandler() {
		return new com.thinkparity.model.parity.model.io.db.hsqldb.handler.ArtifactIOHandler();
	}

    /**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#createAuditHandler()
	 * 
	 */
	public AuditIOHandler createAuditHandler() {
		return new com.thinkparity.model.parity.model.io.db.hsqldb.handler.AuditIOHandler();
	}

    /** @see com.thinkparity.model.parity.model.io.IOFactory#createConfigurationHandler() */
    public ConfigurationIOHandler createConfigurationHandler() {
        return new com.thinkparity.model.parity.model.io.db.hsqldb.handler.ConfigurationIOHandler();
    }

    /**
     * @see com.thinkparity.model.parity.model.io.IOFactory#createContainerHandler()
     * 
     */
    public ContainerIOHandler createContainerHandler() {
        return new com.thinkparity.model.parity.model.io.db.hsqldb.handler.ContainerIOHandler();
    }

	/**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#createDocumentHandler()
	 * 
	 */
	public DocumentIOHandler createDocumentHandler() {
		return new com.thinkparity.model.parity.model.io.db.hsqldb.handler.DocumentIOHandler();
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#createDocumentHistoryIOHandler()
	 * 
	 */
	public DocumentHistoryIOHandler createDocumentHistoryIOHandler() { return null; }

	/**
     * @see com.thinkparity.model.parity.model.io.IOFactory#createLibraryHandler()
     */
    public LibraryIOHandler createLibraryHandler() {
        throw Assert.createNotYetImplemented("HypersonicIOFactory#createLibraryHandler");
    }

	/**
     * @see com.thinkparity.model.parity.model.io.IOFactory#createReleaseHandler()
     */
    public ReleaseIOHandler createReleaseHandler() {
        throw Assert.createNotYetImplemented("HypersonicIOFactory#createReleaseHandler");
    }

    /**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#createSystemMessageHandler()
	 * 
	 */
	public SystemMessageIOHandler createSystemMessageHandler() {
		return new com.thinkparity.model.parity.model.io.db.hsqldb.handler.SystemMessageIOHandler();
	}

	/**
     * @see com.thinkparity.model.parity.model.io.IOFactory#createUserIOHandler()
     *
     */
    public UserIOHandler createUserIOHandler() {
        return new com.thinkparity.model.parity.model.io.db.hsqldb.handler.UserIOHandler();
    }

    /**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#initialize()
	 * 
	 */
	public void initialize() {}
}
