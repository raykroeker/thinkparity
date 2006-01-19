/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing.browser;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import org.apache.log4j.Logger;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.javax.swing.BrowserJFrame;
import com.thinkparity.browser.javax.swing.document.DocumentShuffler;
import com.thinkparity.browser.javax.swing.document.NewDocumentJPanel;
import com.thinkparity.browser.javax.swing.session.LoginJPanel;
import com.thinkparity.browser.log4j.LoggerFactory;
import com.thinkparity.browser.provider.ProviderFactory;

import com.thinkparity.model.parity.ParityException;

/**
 * The main browser is divided into 3 sections called areas:
 * 1.  Title Area
 *  * This contains buttons and other such miscellanea.
 * 2.  Content Area
 *  * This contains the forms for user input.
 * 3.  Status Area
 *  * This contains status information.
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainJPanel extends AbstractJPanel {

	private static MainJPanel instance;
	private static final long serialVersionUID = 1;
	static MainJPanel getInstance() { return instance; }
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private Component documentList;
	private final BrowserJFrame jFrame;
	private Component loginForm;
	private Component newDocumentForm;
	private Component statusJPanel;

	/**
	 * Create a MainJPanel.
	 *  
	 */
	public MainJPanel(final BrowserJFrame jFrame) {
		super("Main");
		instance = this;
		this.jFrame = jFrame;

		setLayout(new GridBagLayout());
		add(createButtonJPanel(), createTitleAreaConstraints());
		addStatusJPanel();
	}

	void addDocumentList() {
		if(null == documentList) {
			documentList = createDocumentList();
		}
		add(documentList, createContentAreaConstraints());
	}

	void addLoginForm() {
		if(null == loginForm) { loginForm = createLoginForm(); }
		add(loginForm, createContentAreaConstraints());
	}

	void addNewDocumentForm() {
		if(null == newDocumentForm) {
			newDocumentForm = createNewDocumentForm();
		}
		add(newDocumentForm, createContentAreaConstraints());
	}

	StatusJPanel getStatusPanel() { return (StatusJPanel) statusJPanel; }

	void removeDocumentList() { removeList(documentList); }

	void removeLoginForm() { removeForm(loginForm); }

	void removeNewDocumentForm() { removeForm(newDocumentForm); }

	private void addStatusJPanel() {
		if(null == statusJPanel) {
			statusJPanel = createStatusJPanel();
		}
		add(statusJPanel, createStatusAreaConstraints());
	}

	/**
	 * Create the browser button JPanel.
	 * 
	 * @return The browser button JPanel.
	 */
	private Component createButtonJPanel() {
		final ButtonJPanel buttonJPanel = new ButtonJPanel(jFrame, this);
		return buttonJPanel;
	}

	private Object createContentAreaConstraints() {
		return new GridBagConstraints(0, 1,
				1, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
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
	 * Create the input for the document shuffler.
	 * 
	 * @return The input for the document shuffler.
	 */
	private Object createDocumentShufflerInput() {
		try { return getProjectModel().getMyProjects(); }
		catch(ParityException px) {
			// NOTE Error Handler Code
			logger.fatal("Could not initialize browser.", px);
			return null;
		}
	}

	private Component createLoginForm() { return new LoginJPanel(this); }

	private Component createNewDocumentForm() {
		return new NewDocumentJPanel(this);
	}

	private Object createStatusAreaConstraints() {
		return new GridBagConstraints(0, 2,
				1, 1,
				1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				0, 0);		
	}

	private Component createStatusJPanel() {
		return new StatusJPanel();
	}

	private Object createTitleAreaConstraints() {
		return new GridBagConstraints(0, 0,
				1, 1,
				1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private void removeForm(final Component form) {
		if(null == form) { return; }
		remove(form);
	}

	private void removeList(final Component list) {
		if(null == list) { return; }
		remove(list);
	}
}
