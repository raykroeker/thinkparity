/*
 * Mar 3, 2006
 */
package com.thinkparity.model.parity.model.io.pdf.fop;

import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler;
import com.thinkparity.model.parity.model.io.handler.AuditIOHandler;
import com.thinkparity.model.parity.model.io.handler.DocumentHistoryIOHandler;
import com.thinkparity.model.parity.model.io.handler.DocumentIOHandler;
import com.thinkparity.model.parity.model.io.handler.SystemMessageIOHandler;
import com.thinkparity.model.parity.model.io.handler.UserIOHandler;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class FOPIOFactory extends IOFactory {

	/**
	 * The parity preferences.
	 * 
	 */
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
	 * @see com.thinkparity.model.parity.model.io.IOFactory#createArtifactHandler()
	 * 
	 */
	public ArtifactIOHandler createArtifactHandler() { return null; }

	/**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#createAuditHandler()
	 * 
	 */
	public AuditIOHandler createAuditHandler() { return null; }

	/**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#createDocumentHandler()
	 * 
	 */
	public DocumentIOHandler createDocumentHandler() { return null; }

	/**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#createDocumentHistoryIOHandler()
	 * 
	 */
	public DocumentHistoryIOHandler createDocumentHistoryIOHandler() {
		return new com.thinkparity.model.parity.model.io.pdf.fop.handler.DocumentHistoryIOHandler(preferences.getArchiveOutputDirectory());
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#createSystemMessageHandler()
	 * 
	 */
	public SystemMessageIOHandler createSystemMessageHandler() { return null; }

    public UserIOHandler createUserIOHandler() { return null; }

	/**
	 * @see com.thinkparity.model.parity.model.io.IOFactory#initialize()
	 * 
	 */
	public void initialize() {}
}
