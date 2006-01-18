/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Color;
import java.util.UUID;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.log4j.BrowserLoggerFactory;
import com.thinkparity.browser.model.ModelFactory;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AbstractBrowserJPanel extends JPanel {

	/**
	 * Default background color.
	 * 
	 */
	private static final Color defaultBackground;

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	static {
		defaultBackground =	// Google Talk:  List BG Color
			BrowserColorUtil.getRGBColor(249, 249, 249, 255);
	}

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger =
		BrowserLoggerFactory.getLogger(getClass());

	/**
	 * Create an AbstractBrowserJPanel.
	 * 
	 */
	protected AbstractBrowserJPanel() {
		super();
		setOpaque(true);
		setDefaultBackground();
	}

	/**
	 * Obtain a handle to the document api.
	 * 
	 * @return The document api.
	 */
	protected DocumentModel getDocumentModel() {
		return ModelFactory.getDocumentModel(getClass());
	}

	/**
	 * Obtain a handle to the parity model preferences.
	 * 
	 * @return The parity preferences.
	 */
	protected Preferences getPreferences() {
		return ModelFactory.getPreferences(getClass());
	}

	/**
	 * Obtain a handle to the single project id.
	 * 
	 * @return The project id.
	 * @throws ParityException
	 */
	protected UUID getProjectId() throws ParityException {
		final ProjectModel projectModel = ModelFactory.getProjectModel(getClass());
		return projectModel.getMyProjects().getId();
	}

	/**
	 * Obtain a handle to the parity workspace.
	 * 
	 * @return The parity workspace.
	 */
	protected Workspace getWorkspace() {
		return ModelFactory.getWorkspace(getClass());
	}

	/**
	 * Set a default background color. 
	 *
	 */
	protected void setDefaultBackground() {
		setBackground(defaultBackground);
	}
}
