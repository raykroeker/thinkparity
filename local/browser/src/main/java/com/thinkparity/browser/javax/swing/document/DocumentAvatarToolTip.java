/*
 * Jan 11, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentAvatarToolTip extends JPanel {

	/**
	 * @see java.io.Serialiable
	 */
	private static final long serialVersionUID = 1;

	private final DocumentAvatar avatar;

	private final JButton closeJButton;

	private final JButton deleteJButton;

	/**
	 * Document key holder.
	 * 
	 */
	private String keyHolder;

	/**
	 * Document key holder ui component.
	 * 
	 */
	private final JLabel keyHolderJLabel;

	/**
	 * The document name.
	 * 
	 */
	private String name;

	/**
	 * The document name display component.
	 * 
	 */
	private final JLabel nameJLabel;

	private final JButton requestKeyJButton;

	/**
	 * Send the document.
	 * 
	 */
	private final JButton sendJButton;

	private final JButton sendKeyJButton;

	/**
	 * Create a DocumentAvatarToolTip.
	 * 
	 */
	public DocumentAvatarToolTip(final DocumentAvatar avatar) {
		super();
		this.avatar = avatar;

		setOpaque(true);
		setBackground(DocumentAvatar.getHighlightColor());
		setLayout(new GridBagLayout());

		this.nameJLabel = new JLabel();
		this.nameJLabel.setFont(DocumentAvatar.getNameFont());
		this.nameJLabel.setForeground(DocumentAvatar.getNameForeground());
		add(nameJLabel, createNameJLabelConstraints());

		this.keyHolderJLabel = new JLabel();
		this.keyHolderJLabel.setFont(DocumentAvatar.getNameFont());
		this.keyHolderJLabel.setForeground(DocumentAvatar.getNameForeground());
		add(keyHolderJLabel, createKeyHolderJLabelConstraints());

		this.closeJButton = new JButton("Close");
		this.closeJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Action performed.");
			}
		});
		add(closeJButton, createCloseJButtonConstraints());

		this.deleteJButton = new JButton("Delete");
//		add(deleteJButton, createDeleteJButtonConstraints());

		this.sendJButton = new JButton("Send");
		add(sendJButton, createSendJButtonConstraints());

		this.sendKeyJButton = new JButton("Send Ownership");
		add(sendKeyJButton, createSendKeyJButtonConstraints());
		
		this.requestKeyJButton = new JButton("Request Ownership");
//		add(requestKeyJButton, createRequestKeyJButtonConstraints());
	}

	/**
	 * Obtain the document key holder.
	 * 
	 * @return The document key holder.
	 */
	public String getKeyHolder() { return keyHolder; }

	/**
	 * Obtain the document name.
	 * @return The document name.
	 */
	public String getName() { return name; }

	/**
	 * Set the document key holder.
	 * 
	 * @param keyHolder
	 *            The document key holder.
	 */
	public void setKeyHolder(String keyHolder) { this.keyHolder = keyHolder; }

	/**
	 * Set the document name.
	 * 
	 * @param name
	 *            The document name.
	 */
	public void setName(String name) { this.name = name; }

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g.create();
		// line separator
		g2.setColor(DocumentAvatar.getHighlightColor());
		g2.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
		g2.dispose();
	}

	void hideToolTip() { avatar.hideToolTip(); }

	/**
	 * Transfer the data from the members to the respective display controls.
	 * 
	 */
	void transferToDisplay() {
		this.nameJLabel.setText(name);
		this.keyHolderJLabel.setText(keyHolder);
	}
	
	private Object createCloseJButtonConstraints() {
		return new GridBagConstraints(0, 2,
				2, 1,
				1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private Object createDeleteJButtonConstraints() {
		return createSendJButtonConstraints();
	}

	/**
	 * Create the document key holder label's constraints.
	 * 
	 * @return The constraints.
	 */
	private Object createKeyHolderJLabelConstraints() {
		return new GridBagConstraints(0, GridBagConstraints.RELATIVE,
				4, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 12, 2, 0),
				0, 0);
	}

	/**
	 * Create the grid bag constriants for the name label.
	 * 
	 * @return The grid bag constraints.
	 */
	private Object createNameJLabelConstraints() {
		return new GridBagConstraints(0, 0,
				4, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 12, 2, 12),
				0, 2);
	}

	private Object createRequestKeyJButtonConstraints() {
		return new GridBagConstraints(3, 2,
				1, 1,
				1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private Object createSendJButtonConstraints() {
		return new GridBagConstraints(3, 2,
				1, 1,
				0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private Object createSendKeyJButtonConstraints() {
		return new GridBagConstraints(2, 2,
				1, 1,
				0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0, 0);
	}
}
