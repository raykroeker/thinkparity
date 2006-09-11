/*
 * Created On: Feb 6, 2006
 */
package com.thinkparity.ophelia.model.io;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicIOFactory;
import com.thinkparity.ophelia.model.io.handler.*;
import com.thinkparity.ophelia.model.io.pdf.fob.FOPIOFactory;
import com.thinkparity.ophelia.model.io.xmpp.XMPPIOFactory;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class IOFactory {

	private static IOFactory ioFactory;

	private static IOFactory pdfIOFactory;

    private static IOFactory xmppIOFactory;

	public static IOFactory getDefault() {
		if(null == ioFactory) {
			final Workspace workspace = getWorkspace();
			final IOLayerId ioLayerId = IOLayerId.DB;
			switch(ioLayerId) {
			case DB:
				ioFactory = new HypersonicIOFactory(workspace);
				break;
			default: Assert.assertUnreachable("");
			}
			ioFactory.initialize();
		}
		return ioFactory;
	}

    public static IOFactory getPDF() {
		if(null == pdfIOFactory) {
			pdfIOFactory = new FOPIOFactory(getWorkspace());
		}
		return pdfIOFactory;
	}

	public static IOFactory getXMPP() {
        if(null == xmppIOFactory) {
            xmppIOFactory = new XMPPIOFactory(getWorkspace());
        }
        return xmppIOFactory;
    }

	private static Workspace getWorkspace() {
		return WorkspaceModel.getModel().getWorkspace();
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
     * Create a library io handler.
     * 
     * @return A library io handler.
     */
    public abstract LibraryIOHandler createLibraryHandler();

    /**
     * Create a thinkParity profile db io handler.
     * 
     * @return A thinkParity profile db io handler.
     */
    public abstract ProfileIOHandler createProfileHandler();

    /**
     * Create a release io handler.
     * 
     * @return A release io handler.
     */
    public abstract ReleaseIOHandler createReleaseHandler();

    /**
	 * Create a system message io handler.
	 * 
	 * @return The system message io handler.
	 */
	public abstract SystemMessageIOHandler createSystemMessageHandler();

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
