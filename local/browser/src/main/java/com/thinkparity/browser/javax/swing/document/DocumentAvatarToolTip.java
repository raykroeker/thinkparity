/*
 * Jan 11, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

	/**
	 * Handle to the avatar.
	 * 
	 */
	private final DocumentAvatar avatar;

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

	/**
	 * Create a DocumentAvatarToolTip.
	 * 
	 */
	public DocumentAvatarToolTip(final DocumentAvatar avatar) {
		super();
		this.avatar = avatar;
		this.name = avatar.getName() + ":  Tool tip";

		setOpaque(true);
		setBackground(avatar.getHighlightColor());
		setLayout(new GridBagLayout());
		addMouseListener(new MouseAdapter() {
			public void mouseExited(final MouseEvent e) { avatar.hideToolTip(); }
		});

		this.nameJLabel = new JLabel();
		this.nameJLabel.setFont(avatar.getNameJLabel().getFont());
		this.nameJLabel.setForeground(avatar.getNameJLabel().getForeground());
		add(nameJLabel, avatar.createNameJLabelConstraints());

		this.keyHolderJLabel = new JLabel();
		this.keyHolderJLabel.setFont(avatar.getNameJLabel().getFont());
		this.keyHolderJLabel.setForeground(avatar.getNameJLabel().getForeground());
		add(keyHolderJLabel, createKeyHolderJLabelConstraints());
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
		g2.setColor(avatar.getHighlightColor());
		g2.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
		g2.dispose();
	}

	/**
	 * Transfer the data from the members to the respective display controls.
	 * 
	 */
	void transferToDisplay() {
		this.nameJLabel.setText(name);
		this.keyHolderJLabel.setText(keyHolder);
	}

	/**
	 * Create the document key holder label's constraints.
	 * 
	 * @return The constraints.
	 */
	private Object createKeyHolderJLabelConstraints() {
		return new GridBagConstraints(0, GridBagConstraints.RELATIVE,
				1, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 12, 2, 0),
				0, 0);
	}

}
