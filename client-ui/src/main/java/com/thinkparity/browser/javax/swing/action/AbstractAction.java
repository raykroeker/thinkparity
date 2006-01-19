/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.javax.swing.action;

import javax.swing.Icon;

import org.apache.log4j.Logger;

import com.thinkparity.browser.log4j.LoggerFactory;
import com.thinkparity.browser.model.ModelFactory;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.ProjectModel;

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
	 * The action icon.
	 * 
	 */
	private Icon icon;

	/**
	 * The action name.
	 * 
	 */
	private String name;

	/**
	 * Create a AbstractAction.
	 * 
	 * @param name
	 *            The action name.
	 * @param icon
	 *            The action small icon.
	 */
	protected AbstractAction(final String name, final Icon icon) {
		super();
		this.icon = icon;
		this.name = name;
	}

	/**
	 * Obtain the action icon.
	 * 
	 * @return The action icon.
	 */
	public Icon getIcon() { return icon; }

	/**
	 * Obtain the action name.
	 * @return The action name.
	 */
	public String getName() { return name; }

	/**
	 * Invoke the action.
	 * 
	 * @param data
	 *            The action data.
	 */
	public abstract void invoke(final Data data);

	/**
	 * Set the action icon.
	 * 
	 * @param icon
	 *            The action icon.
	 */
	public void setIcon(Icon icon) { this.icon = icon; }

	/**
	 * Set the action name.
	 * 
	 * @param name
	 *            The action name.
	 */
	public void setName(String name) { this.name = name; }

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
		logger.error("", t);
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
	 * Obtain the project model api.
	 * 
	 * @return The project model api.
	 */
	protected ProjectModel getProjectModel() {
		return modelFactory.getProjectModel(getClass());
	}
}
