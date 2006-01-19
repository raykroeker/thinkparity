/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Color;
import java.util.UUID;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.l10n.JPanelLocalisation;
import com.thinkparity.browser.log4j.LoggerFactory;
import com.thinkparity.browser.model.ModelFactory;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AbstractJPanel extends JPanel {

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
	 * Localisation helper utility.
	 * 
	 */
	protected final JPanelLocalisation localisation;

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger =
		LoggerFactory.getLogger(getClass());

	/**
	 * Handle to the parity model factory.
	 * 
	 */
	protected final ModelFactory modelFactory = ModelFactory.getInstance();

	/**
	 * Create an AbstractJPanel.
	 * 
	 */
	protected AbstractJPanel(final String l18nContext) {
		super();
		this.localisation = new JPanelLocalisation(l18nContext);
		setOpaque(true);
		setDefaultBackground();
	}

	/**
	 * Obtain a handle to the document api.
	 * 
	 * @return The document api.
	 */
	protected DocumentModel getDocumentModel() {
		return modelFactory.getDocumentModel(getClass());
	}

	/**
	 * Obtain a handle to the parity model preferences.
	 * 
	 * @return The parity preferences.
	 */
	protected Preferences getPreferences() {
		return modelFactory.getPreferences(getClass());
	}

	/**
	 * Obtain a handle to the single project id.
	 * 
	 * @return The project id.
	 * @throws ParityException
	 */
	protected UUID getProjectId() throws ParityException {
		final ProjectModel projectModel = modelFactory.getProjectModel(getClass());
		return projectModel.getMyProjects().getId();
	}

	/**
	 * Obtain a handle to the project model api.
	 * 
	 * @return The project model api.
	 */
	protected ProjectModel getProjectModel() {
		return modelFactory.getProjectModel(getClass());
	}

	/**
	 * Obtain a handle to the session api.
	 * 
	 * @return The session api.
	 */
	protected SessionModel getSessionModel() {
		return modelFactory.getSessionModel(getClass());
	}

	/**
	 * @see JPanelLocalisation#getString(String)
	 * 
	 */
	protected String getString(final String localKey) {
		return localisation.getString(localKey);
	}

	/**
	 * Obtain a handle to the parity workspace.
	 * 
	 * @return The parity workspace.
	 */
	protected Workspace getWorkspace() {
		return modelFactory.getWorkspace(getClass());
	}

	/**
	 * Set a default background color. 
	 *
	 */
	protected void setDefaultBackground() {
		setBackground(defaultBackground);
	}
}
