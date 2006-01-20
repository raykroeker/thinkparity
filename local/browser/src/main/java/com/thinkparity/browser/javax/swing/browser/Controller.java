/*
 * Jan 18, 2006
 */
package com.thinkparity.browser.javax.swing.browser;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.thinkparity.browser.javax.swing.document.DocumentShuffler;
import com.thinkparity.browser.log4j.LoggerFactory;
import com.thinkparity.browser.model.NetworkStatus;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Controller {

	/**
	 * Singleton instance of the controller.
	 * 
	 */
	private static final Controller singleton;

	static { singleton = new Controller(); }

	/**
	 * Obtain the instance of the controller.
	 * 
	 * @return The instance of the controller.
	 */
	public static Controller getInstance() { return singleton; }

	/**
	 * Apache logger.
	 * 
	 */
	protected final Logger logger =
		LoggerFactory.getLogger(getClass());

	/**
	 * Parity document list.
	 * 
	 */
	private DocumentShuffler documentList;

	/**
	 * Flag indicating whether or not the controller has been initialized.
	 * 
	 */
	private boolean isInitialized;

	/**
	 * Main panel.
	 * 
	 */
	private MainJPanel mainPanel;

	/**
	 * Status panel.
	 * 
	 */
	private StatusJPanel statusPanel;

	/**
	 * Create a Controller [Singleton]
	 * 
	 */
	private Controller() { super(); }

	/**
	 * Initialize the controller.
	 *
	 */
	public void initialize() {
		if(!isInitialized) {
			isInitialized = true;
		}
	}

	public void refreshDocumentList(final boolean doShow) {
		logger.info("refreshDocumentList(boolean)");
		logger.debug(doShow);
		getDocumentList().refresh();
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
		getMainPanel().removeSendForm();
		getMainPanel().addDocumentList();
		getMainPanel().revalidate();
		getMainPanel().repaint();
	}

	public void showSendForm(final UUID documentId) {
		logger.info("showSendForm()");
		getMainPanel().removeDocumentList();
		getMainPanel().removeLoginForm();
		getMainPanel().removeNewDocumentForm();
		getMainPanel().removeSendForm();
		getMainPanel().addSendForm(documentId);
		getMainPanel().revalidate();
		getMainPanel().repaint();
	}

	public void showLoginForm() {
		logger.info("showLoginForm()");
		getMainPanel().removeDocumentList();
		getMainPanel().removeLoginForm();
		getMainPanel().removeNewDocumentForm();
		getMainPanel().removeSendForm();
		getMainPanel().addLoginForm();
		getMainPanel().revalidate();
		getMainPanel().repaint();
	}

	public void showNewDocumentForm() {
		logger.info("showNewDocumentForm()");
		getMainPanel().removeDocumentList();
		getMainPanel().removeLoginForm();
		getMainPanel().removeNewDocumentForm();
		getMainPanel().removeSendForm();
		getMainPanel().addNewDocumentForm();
		getMainPanel().revalidate();
		getMainPanel().repaint();
	}

	private DocumentShuffler getDocumentList() {
		if(null == documentList) {
			documentList = getMainPanel().getDocumentList();
		}
		return documentList;
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
