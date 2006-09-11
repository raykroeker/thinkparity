/*
 * Mar 3, 2006
 */
package com.thinkparity.ophelia.model.io.pdf.fob;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.*;
import com.thinkparity.ophelia.model.workspace.Preferences;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class FOPIOFactory extends IOFactory {

	/** The parity preferences. */
	private final Preferences preferences;

    /**
	 * Create a FOPIOFactory.
	 * 
	 */
	public FOPIOFactory(final Workspace workspace) {
		super(workspace);
		this.preferences = workspace.getPreferences();
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.IOFactory#createArtifactHandler()
	 * 
	 */
	public ArtifactIOHandler createArtifactHandler() { return null; }

    /**
	 * @see com.thinkparity.ophelia.model.io.IOFactory#createAuditHandler()
	 * 
	 */
	public AuditIOHandler createAuditHandler() { return null; }

	/** @see com.thinkparity.ophelia.model.io.IOFactory#createConfigurationHandler() */
    public ConfigurationIOHandler createConfigurationHandler() {
        throw Assert.createNotYetImplemented("FOPIOFactory#createConfigurationHandler");
    }

	/**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createContactHandler()
     */
    @Override
    public ContactIOHandler createContactHandler() {
        throw Assert.createNotYetImplemented("FOPIOFactory#createContactHandler");
    }

	/**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createContainerHandler()
     * 
     */
    public ContainerIOHandler createContainerHandler() {
        throw Assert.createNotYetImplemented("FOPIOFactory#createContainerHandler");
    }

	/**
	 * @see com.thinkparity.ophelia.model.io.IOFactory#createDocumentHandler()
	 * 
	 */
	public DocumentIOHandler createDocumentHandler() { return null; }

	/**
	 * @see com.thinkparity.ophelia.model.io.IOFactory#createDocumentHistoryIOHandler()
	 * 
	 */
	public DocumentHistoryIOHandler createDocumentHistoryIOHandler() {
		return new com.thinkparity.ophelia.model.io.pdf.fob.handler.DocumentHistoryIOHandler(preferences.getArchiveOutputDirectory());
	}

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createLibraryHandler()
     */
    @Override
    public LibraryIOHandler createLibraryHandler() {
        throw Assert.createNotYetImplemented("FOPIOFactory#createLibraryHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createProfileHandler()
     */
    @Override
    public ProfileIOHandler createProfileHandler() {
        throw Assert.createNotYetImplemented("FOPIOFactory#createProfileHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createReleaseHandler()
     */
    @Override
    public ReleaseIOHandler createReleaseHandler() {
        throw Assert.createNotYetImplemented("FOPIOFactory#createReleaseHandler");
    }

    /**
	 * @see com.thinkparity.ophelia.model.io.IOFactory#createSystemMessageHandler()
	 * 
	 */
	public SystemMessageIOHandler createSystemMessageHandler() { return null; }

    public UserIOHandler createUserIOHandler() { return null; }

    /**
	 * @see com.thinkparity.ophelia.model.io.IOFactory#initialize()
	 * 
	 */
	public void initialize() {}
}
