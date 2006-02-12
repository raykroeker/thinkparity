/*
 * Feb 3, 2006
 */
package com.thinkparity.browser.application.session.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import com.thinkparity.browser.javax.swing.AbstractJFrame;
import com.thinkparity.browser.platform.util.SwingUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionWindow extends AbstractJFrame implements SessionUI {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The session window.
	 * 
	 * @see #open()
	 * @see #getSessionWindow()
	 */
	private static SessionWindow sessionWindow;

	/**
	 * The size of the session window.
	 * 
	 * @see #getSessionWindowSize()
	 */
	private static Dimension sessionWindowSize;

	/**
	 * Obtain the session window.
	 * 
	 * @return The session window;
	 */
	public static SessionWindow getSessionWindow() { return sessionWindow; }

	/**
	 * Obtain the size of the session window.
	 * 
	 * @return The size of the session window.
	 */
	public static Dimension getSessionWindowSize() {
		if(null == sessionWindowSize) {
			// DIMENSION 402x552
			sessionWindowSize = new Dimension(402, 162);
		}
		return sessionWindowSize;
	}

	/**
	 * Open the session window.
	 * 
	 * @return The session window.
	 */
	public static SessionWindow open() {
		sessionWindow = new SessionWindow();
		sessionWindow.setVisible(true);
		SwingUtil.centreOnScreen(sessionWindow);
		((Graphics2D) sessionWindow.getGraphics())
			.setRenderingHint(
					RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
		sessionWindow.debugGeometry();
		sessionWindow.debugLookAndFeel();
		return sessionWindow;
	}

	/**
	 * The session panel.
	 * 
	 */
	private SessionPanel sessionPanel;

	/**
	 * Create a SessionWindow.
	 */
	private SessionWindow() {
		super("SessionWindow");

		addWindowListener(new WindowAdapter() {
			public void windowClosed(final WindowEvent e) {
				synchronized(sessionWindow) { sessionWindow.notifyAll(); }
			}
		});
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(SessionWindow.getSessionWindowSize());
		
		initComponents();
	}

	/**
	 * @see com.thinkparity.browser.application.session.ui.SessionUI#extractRememberPassword()
	 * 
	 */
	public Boolean extractDoRememberPassword() {
		return sessionPanel.extractDoRememberPassword();
	}

	/**
	 * @see com.thinkparity.browser.application.session.ui.SessionUI#extractPassword()
	 * 
	 */
	public String extractPassword() {
		return sessionPanel.extractPassword();
	}

	/**
	 * @see com.thinkparity.browser.application.session.ui.SessionUI#extractUsername()
	 * 
	 */
	public String extractUsername() {
		return sessionPanel.extractUsername();
	}

	/**
	 * Obtain the window return code.
	 * 
	 * @return The window return code.
	 */
	public ReturnCode getReturnCode() { return sessionPanel.getReturnCode(); }

	/**
	 * @see com.thinkparity.browser.javax.swing.AbstractJFrame#isInputValid()
	 * 
	 */
	public Boolean isInputValid() {
		final String username = extractUsername();
		final String password = extractPassword();
		return null != username
			&& 0 < username.length()
			&& null != password
			&& 0 < password.length();
	}

	/**
	 * Determine whether or not the session window is open.
	 * 
	 * @return True if the session window is open; false otherwise.
	 */
	public Boolean isOpen() {
		return null != sessionWindow && sessionWindow.isVisible();
	}

	/**
	 * Give the UI an opportunity to process the event queue; and sleep while
	 * the user is logging in.
	 * 
	 */
	public void processEventQueue() {
		synchronized(sessionWindow) {
			try { sessionWindow.wait(); }
			catch(final InterruptedException ix) {
				throw new RuntimeException(ix);
			}
		}
	}

	/**
	 * Initialize the session window swing components.
	 *
	 */
	private void initComponents() {
		sessionPanel = new SessionPanel("SessionWindow");
		add(sessionPanel);
	}

	/**
	 * The window return code.
	 * 
	 */
	public enum ReturnCode { CANCEL, LOGIN }
}
