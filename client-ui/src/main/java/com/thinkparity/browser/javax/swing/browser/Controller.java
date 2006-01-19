/*
 * Jan 18, 2006
 */
package com.thinkparity.browser.javax.swing.browser;

import org.apache.log4j.Logger;

import com.thinkparity.browser.log4j.LoggerFactory;
import com.thinkparity.browser.model.NetworkStatus;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Controller {

	private static final Controller singleton;

	static {
		singleton = new Controller();
	}

	public static Controller getInstance() { return singleton; }

	protected final Logger logger =
		LoggerFactory.getLogger(getClass());

	/**
	 * Flag indicating whether or not the controller has been initialized.
	 * 
	 */
	private boolean isInitialized;

	private MainJPanel mainPanel;

	private StatusJPanel statusPanel;

	/**
	 * Create a Controller [Singleton]
	 * 
	 */
	private Controller() {
		super();
	}

	/**
	 * Initialize the controller.
	 *
	 */
	public void initialize() {
		if(!isInitialized) {
			isInitialized = true;
		}
	}

	public void set(final NetworkStatus networkStatus) {
		logger.info("setNetworkStatus()");
		getStatusPanel().set(networkStatus);
		getStatusPanel().repaint();
	}


	public void showDocumentList() {
		logger.info("showDocumentList()");
		getMainPanel().removeDocumentList();
		getMainPanel().removeLoginForm();
		getMainPanel().removeNewDocumentForm();
		getMainPanel().addDocumentList();
		getMainPanel().revalidate();
		getMainPanel().repaint();
	}

	public void showLoginForm() {
		logger.info("showLoginForm()");
		getMainPanel().removeDocumentList();
		getMainPanel().removeLoginForm();
		getMainPanel().removeNewDocumentForm();
		getMainPanel().addLoginForm();
		getMainPanel().revalidate();
		getMainPanel().repaint();
	}

	public void showNewDocumentForm() {
		logger.info("showNewDocumentForm()");
		getMainPanel().removeDocumentList();
		getMainPanel().removeLoginForm();
		getMainPanel().removeNewDocumentForm();
		getMainPanel().addNewDocumentForm();
		getMainPanel().revalidate();
		getMainPanel().repaint();
	}

	private MainJPanel getMainPanel() {
		if(null == mainPanel) { mainPanel = MainJPanel.getInstance(); }
		return mainPanel;
	}

	private StatusJPanel getStatusPanel() {
		statusPanel = getMainPanel().getStatusPanel();
		return statusPanel;
	}
}
