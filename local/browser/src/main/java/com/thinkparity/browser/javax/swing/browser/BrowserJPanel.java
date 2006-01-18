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
import com.thinkparity.browser.javax.swing.document.NewDocumentJPanel;
import com.thinkparity.browser.javax.swing.session.LoginJPanel;
import com.thinkparity.browser.log4j.BrowserLoggerFactory;
import com.thinkparity.browser.model.ModelFactory;
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

	private Component documentList;

	/**
	 * Handle to the main JFrame.
	 * 
	 */
	private final BrowserJFrame jFrame;

	private Component loginForm;

	private Component newDocumentForm;

	private static BrowserJPanel instance;
	static BrowserJPanel getInstance() { return instance; }

	/**
	 * Create a BrowserJPanel.
	 *  
	 */
	public BrowserJPanel(final BrowserJFrame jFrame) {
		super();
		instance = this;
		this.jFrame = jFrame;

		setLayout(new GridBagLayout());
		add(createButtonJPanel(), createButtonJPanelConstraints());
	}

	void addDocumentList() {
		if(null == documentList) {
			documentList = createDocumentList();
		}
		add(documentList, createDocumentListConstraints());
	}

	void addLoginForm() {
		if(null == loginForm) { loginForm = createLoginForm(); }
		add(loginForm, createLoginFormConstraints());
	}

	void addNewDocumentForm() {
		if(null == newDocumentForm) {
			newDocumentForm = createNewDocumentForm();
		}
		add(newDocumentForm, createNewDocumentFormConstraints());
	}

	private Object createNewDocumentFormConstraints() {
		return new GridBagConstraints(0, 1,
				1, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	void removeNewDocumentForm() { removeForm(newDocumentForm); }

	void removeDocumentList() { removeList(documentList); }

	void removeLoginForm() { removeForm(loginForm); }

	private void removeList(final Component list) {
		if(null == list) { return; }
		remove(list);
	}

	private void removeForm(final Component form) {
		if(null == form) { return; }
		remove(form);
	}

	/**
	 * Create the browser button JPanel.
	 * 
	 * @return The browser button JPanel.
	 */
	private Component createButtonJPanel() {
		final BrowserButtonJPanel buttonJPanel = new BrowserButtonJPanel(jFrame, this);
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
	private Component createDocumentList() {
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
	private Object createDocumentListConstraints() {
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
		final ProjectModel projectModel = ModelFactory.getProjectModel(getClass());
		try { return projectModel.getMyProjects(); }
		catch(ParityException px) {
			// NOTE Error Handler Code
			logger.fatal("Could not initialize browser.", px);
			return null;
		}
	}

	private Component createLoginForm() { return new LoginJPanel(this); }

	private Object createLoginFormConstraints() {
		return new GridBagConstraints(0, 1,
				1, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private Component createNewDocumentForm() {
		return new NewDocumentJPanel(this);
	}
}
