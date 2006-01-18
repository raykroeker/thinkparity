/*
 * Jan 17, 2006
 */
package com.thinkparity.browser.javax.swing.browser;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
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

	private final JFrame jFrame;

	/**
	 * Create a BrowserButtonJPanel.
	 * 
	 */
	public BrowserButtonJPanel(final JFrame jFrame, final BrowserJPanel jPanel) {
		super();
		this.jFrame = jFrame;
		this.jPanel = jPanel;

		setBackground(Color.WHITE);
		setLayout(new GridBagLayout());
		setOpaque(true);

		add(createLoginJButton(), createLoginJButtonConstraints());
	}

	/**
	 * Create the login JButton.
	 * 
	 * @return The login JButton.
	 */
	private Component createLoginJButton() {
		final JButton jButton = BrowserButtonFactory.createTop("Login...");
		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { runShowLoginPanel(); }
		});
		return jButton;
	}

	private Object createLoginJButtonConstraints() {
		return new GridBagConstraints(0, 1,
				1, 1,
				1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private void runShowLoginPanel() {
		jPanel.addLoginJPanel();
		jPanel.removeDocumentShuffler();
		jPanel.revalidate();
		jPanel.repaint();
	}
}
