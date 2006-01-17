/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing.browser;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.thinkparity.browser.javax.swing.component.BrowserButtonFactory;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserButtonJPanel extends JPanel {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Handle to the main browser JPanel.
	 * 
	 */
	private final BrowserJPanel jPanel;

	/**
	 * Create a BrowserButtonJPanel.
	 * 
	 */
	public BrowserButtonJPanel(final BrowserJPanel jPanel) {
		super();
		this.jPanel = jPanel;

		setBackground(Color.WHITE);
		setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setOpaque(true);

		add(createLoginJButton());
	}

	/**
	 * Create the login JButton.
	 * 
	 * @return The login JButton.
	 */
	private Component createLoginJButton() {
		return BrowserButtonFactory.createTop("Login...");
	}
}
