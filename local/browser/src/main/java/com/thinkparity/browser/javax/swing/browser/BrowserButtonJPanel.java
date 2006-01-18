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
import javax.swing.JLabel;
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

	protected final BrowserController controller =
		BrowserController.getInstance();

	private Component documentsJButton;

	private Component fillerJLabel;

	private final JFrame jFrame;

	/**
	 * Handle to the main browser JPanel.
	 * 
	 */
	private final BrowserJPanel jPanel;

	private Component loginJButton;

	private Component newDocumentJButton;

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

		addFillerJLabel();
		addDocumentsJButton();
		addNewDocumentJButton();
		addLoginJButton();
	}

	private void addDocumentsJButton() {
		if(null == documentsJButton) {
			documentsJButton = createDocumentsJButton();
		}
		add(documentsJButton, createDocumentsJButtonConstraints());
	}

	private void addFillerJLabel() {
		if(null == fillerJLabel) { fillerJLabel = createFillerJLabel(); }
		add(fillerJLabel, createFillerJLabelConstraints());
	}

	private void addLoginJButton() {
		if(null == loginJButton) { loginJButton = createLoginJButton(); }
		add(loginJButton, createLoginJButtonConstraints());
	}

	private void addNewDocumentJButton() {
		if(null == newDocumentJButton) {
			newDocumentJButton = createNewDocumentJButton();
		}
		add(newDocumentJButton, createNewDocumentJButtonConstraints());
	}

	private JButton createDocumentsJButton() {
		final JButton jButton = BrowserButtonFactory.createTop("Documents");
		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				controller.showDocumentList();
			}
		});
		return jButton;
	}

	private Object createDocumentsJButtonConstraints() {
		return new GridBagConstraints(1, 0,
				1, 1,
				0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private Component createFillerJLabel() { return new JLabel(); }

	private Object createFillerJLabelConstraints() {
		return new GridBagConstraints(0, 0,
				1, 1,
				1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				0, 0);
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
		return new GridBagConstraints(3, 0,
				1, 1,
				0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private Component createNewDocumentJButton() {
		final JButton jButton = BrowserButtonFactory.createTop("Add Document...");
		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { runShowNewDocumentPanel(); }
		});
		return jButton;
	}

	private Object createNewDocumentJButtonConstraints() {
		return new GridBagConstraints(2, 0,
				1, 1,
				0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private void runShowLoginPanel() { controller.showLoginForm(); }

	private void runShowNewDocumentPanel() { controller.showNewDocumentForm(); }
}
