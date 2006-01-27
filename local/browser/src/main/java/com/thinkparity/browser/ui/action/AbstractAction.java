/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.ui.action;

import java.util.UUID;

import javax.swing.Icon;

import org.apache.log4j.Logger;

import com.thinkparity.browser.Controller;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractAction {

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger =
		LoggerFactory.getLogger(getClass());
	
	/**
	 * Parity model factory.
	 * 
	 */
	protected final ModelFactory modelFactory = ModelFactory.getInstance();

	/**
	 * The main controller.
	 * 
	 */
	private Controller controller;

	/**
	 * The action ICON.
	 * 
	 */
	private Icon icon;

	/**
	 * The action id.
	 * 
	 */
	private ActionId id;

	/**
	 * The action NAME.
	 * 
	 */
	private String name;

	/**
	 * Create a AbstractAction.
	 * 
	 * @param NAME
	 *            The action NAME.
	 * @param ICON
	 *            The action small ICON.
	 */
	protected AbstractAction(final ActionId id, final String name,
			final Icon icon) {
		super();
		this.id = id;
		this.icon = icon;
		this.name = name;
	}

	/**
	 * Obtain the action ICON.
	 * 
	 * @return The action ICON.
	 */
	public Icon getIcon() { return icon; }

	/**
	 * Obtain the action id.
	 * 
	 * @return The action id.
	 */
	public ActionId getId() { return id; }

	/**
	 * Obtain the action NAME.
	 * 
	 * @return The action NAME.
	 */
	public String getName() { return name; }

	/**
	 * Invoke the action.
	 * 
	 * @param data
	 *            The action data.
	 */
	public abstract void invoke(final Data data) throws Exception;

	/**
	 * Set the action ICON.
	 * 
	 * @param ICON
	 *            The action ICON.
	 */
	public void setIcon(Icon icon) { this.icon = icon; }

	/**
	 * Set the action NAME.
	 * 
	 * @param NAME
	 *            The action NAME.
	 */
	public void setName(String name) { this.name = name; }

	/**
	 * Obtain the main controller.
	 * 
	 * @return The main controller.
	 */
	protected Controller getController() {
		if(null == controller) { controller = Controller.getInstance(); }
		return controller;
	}

	/**
	 * Obtain the document model api.
	 * 
	 * @return The document model api.
	 */
	protected DocumentModel getDocumentModel() {
		return modelFactory.getDocumentModel(getClass());
	}

	/**
	 * Obtain the project id.
	 * 
	 * @return The project id.
	 * @throws ParityException
	 */
	protected UUID getProjectId() throws ParityException {
		return getProjectModel().getMyProjects().getId();
	}

	/**
	 * Obtain the project model api.
	 * 
	 * @return The project model api.
	 */
	protected ProjectModel getProjectModel() {
		return modelFactory.getProjectModel(getClass());
	}

	protected SessionModel getSessionModel() {
		return modelFactory.getSessionModel(getClass());
	}

	/**
	 * Register a parity error.
	 * 
	 * @param px
	 *            The parity error.
	 */
	protected void registerError(final ParityException px) {
		registerError((Throwable) px);
	}

	/**
	 * Register an error.
	 * 
	 * @param t
	 *            The error.
	 */
	private void registerError(final Throwable t) {
		// NOTE Error Handler Code
		logger.error("", t);
	}
}
