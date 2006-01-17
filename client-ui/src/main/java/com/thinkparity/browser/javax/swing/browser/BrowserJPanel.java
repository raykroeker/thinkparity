/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing.browser;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.javax.swing.BrowserJFrame;
import com.thinkparity.browser.javax.swing.document.DocumentShuffler;
import com.thinkparity.browser.log4j.BrowserLoggerFactory;
import com.thinkparity.browser.model.ModelProvider;
import com.thinkparity.browser.provider.ProviderFactory;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.project.ProjectModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserJPanel extends JPanel {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());

	/**
	 * Handle to the main JFrame.
	 * 
	 */
	private final BrowserJFrame jFrame;

	/**
	 * Create a BrowserJPanel.
	 *  
	 */
	public BrowserJPanel(final BrowserJFrame jFrame) {
		super();
		this.jFrame = jFrame;

		setLayout(new GridBagLayout());
		add(createButtonJPanel(), createButtonJPanelConstraints());
		add(createDocumentShuffler(), createDocumentShufflerConstraints());
	}

	/**
	 * Create the browser button JPanel.
	 * 
	 * @return The browser button JPanel.
	 */
	private Component createButtonJPanel() {
		final BrowserButtonJPanel buttonJPanel = new BrowserButtonJPanel(this);
		return buttonJPanel;
	}

	/**
	 * Create the browser button JPanel constraints.
	 * 
	 * @return The browser button JPanel constraints.
	 */
	private Object createButtonJPanelConstraints() {
		return new GridBagConstraints(0, 0,
				1, 1,
				1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	/**
	 * Create the document shuffler com.thinkparity.browser.javax.swing.component.
	 * 
	 * @return The document shuffler com.thinkparity.browser.javax.swing.component.
	 */
	private Component createDocumentShuffler() {
		final DocumentShuffler documentShuffler = new DocumentShuffler();
		documentShuffler.setContentProvider(
				ProviderFactory.getDocumentProvider());
		documentShuffler.setInput(createDocumentShufflerInput());
		return documentShuffler;
	}

	/**
	 * Create the constraints for the document shuffler.
	 * 
	 * @return The constraints.
	 */
	private Object createDocumentShufflerConstraints() {
		return new GridBagConstraints(0, 1,
				1, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	/**
	 * Create the input for the document shuffler.
	 * 
	 * @return The input for the document shuffler.
	 */
	private Object createDocumentShufflerInput() {
		final ProjectModel projectModel = ModelProvider.getProjectModel(getClass());
		try { return projectModel.getMyProjects(); }
		catch(ParityException px) {
			// NOTE Error Handler Code
			logger.fatal("Could not initialize browser.", px);
			return null;
		}
	}
}
