/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.l10n.JPanelLocalization;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.model.document.DocumentModel;
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
	private static final Color DEFAULT_BACKGROUND;

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	static {
		// COLOR 249, 249, 249, 255
		DEFAULT_BACKGROUND = new Color(249, 249, 249, 255);
	}

	/**
	 * Localisation helper utility.
	 * 
	 */
	protected final JPanelLocalization localization;

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Handle to the parity model factory.
	 * 
	 */
	protected final ModelFactory modelFactory = ModelFactory.getInstance();

	/**
	 * Swing container tools.
	 * 
	 */
	private final ContainerTools containerTools;

	/**
	 * The debug mouse adapter for a jpanel. This mouse adapter will print the
	 * geometry and component composition to the logger.
	 * 
	 */
	private final MouseAdapter debugMouseAdapter = new MouseAdapter() {
		public void mouseClicked(final MouseEvent e) {
			if(2 == e.getClickCount()) {
				if(e.isShiftDown()) {
					debugGeometry();
					debugComponents();
				}
			}
		}
	};

	/**
	 * Create a AbstractJPanel.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 */
	protected AbstractJPanel(final String l18nContext) { this(l18nContext, null); }

	/**
	 * Create a AbstractJPanel.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 * @param background
	 *            The background.
	 */
	protected AbstractJPanel(final String l18nContext, final Color background) {
		super();
		this.containerTools = new ContainerTools(this);
		this.localization = new JPanelLocalization(l18nContext);
		addMouseListener(debugMouseAdapter);
		setOpaque(true);
		setBackground(background);
	}

	/**
	 * Debug the list of components attached to this JPanel. This api is
	 * recursive if the component is an AbstractJPanel implementation.
	 * 
	 */
	public void debugComponents() { containerTools.debugComponents(); }

	/**
	 * Debug the geometry of the JPanel. This includes the location; bounds and
	 * insets.
	 * 
	 */
	public void debugGeometry() { containerTools.debugGeometry(); }

	/**
	 * Determine whether the user input for the frame is valid.
	 * 
	 * @return True if the input is valid; false otherwise.
	 */
	public Boolean isInputValid() { return Boolean.TRUE; }

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
	 * Obtain a handle to the session api.
	 * 
	 * @return The session api.
	 */
	protected SessionModel getSessionModel() {
		return modelFactory.getSessionModel(getClass());
	}

	/**
	 * @see JPanelLocalization#getString(String)
	 * 
	 */
	protected String getString(final String localKey) {
		return localization.getString(localKey);
	}

	/**
	 * @see JPanelLocalization#getString(String, Object[])
	 * 
	 */
	protected String getString(final String localKey, final Object[] arguments) {
		return localization.getString(localKey, arguments);
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
		setBackground(DEFAULT_BACKGROUND);
	}
}
