/*
 * Jan 18, 2006
 */
package com.thinkparity.browser.javax.swing.browser;

import org.apache.log4j.Logger;

import com.thinkparity.browser.log4j.BrowserLoggerFactory;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserController {

	private static final BrowserController singleton;

	static {
		singleton = new BrowserController();
	}

	public static BrowserController getInstance() { return singleton; }

	private BrowserJPanel jPanel;

	/**
	 * Create a BrowserController [Singleton]
	 * 
	 */
	private BrowserController() {
		super();
	}

	protected final Logger logger =
		BrowserLoggerFactory.getLogger(getClass());

	public void showDocumentList() {
		logger.info("showDocumentList()");
		getPanel().removeDocumentList();
		getPanel().removeLoginForm();
		getPanel().removeNewDocumentForm();
		getPanel().addDocumentList();
		getPanel().revalidate();
		getPanel().repaint();
	}

	public void showLoginForm() {
		logger.info("showLoginForm()");
		getPanel().removeDocumentList();
		getPanel().removeLoginForm();
		getPanel().removeNewDocumentForm();
		getPanel().addLoginForm();
		getPanel().revalidate();
		getPanel().repaint();
	}

	public void showNewDocumentForm() {
		logger.info("showNewDocumentForm()");
		getPanel().removeDocumentList();
		getPanel().removeLoginForm();
		getPanel().removeNewDocumentForm();
		getPanel().addNewDocumentForm();
		getPanel().revalidate();
		getPanel().repaint();
	}

	private BrowserJPanel getPanel() {
		if(null == jPanel) { jPanel = BrowserJPanel.getInstance(); }
		return jPanel;
	}
}
