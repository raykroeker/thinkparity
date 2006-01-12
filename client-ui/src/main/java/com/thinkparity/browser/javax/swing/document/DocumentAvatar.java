/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.apache.log4j.Logger;

import com.thinkparity.browser.javax.swing.BrowserColorUtil;
import com.thinkparity.browser.javax.swing.action.BrowserActionFactory;
import com.thinkparity.browser.javax.swing.action.Data;
import com.thinkparity.browser.javax.swing.action.document.Open;
import com.thinkparity.browser.log4j.BrowserLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentAvatar extends JPanel /*JComponent*/ {

	/**
	 * Mouse listener for the document avatar. Used to display the tool tip on a
	 * delay; as well as to highlight the avatars.
	 * 
	 */
	private class DocumentAvatarMouseListener extends MouseAdapter {

		/**
		 * @see java.io.Serialiable
		 */
		private static final long serialVersionUID = 1;

		/**
		 * Indicates whether or not the mouse is currently within the document
		 * avatar.
		 * 
		 * @see DocumentAvatarMouseListener#mouseEntered(MouseEvent)
		 * @see DocumentAvatarMouseListener#mouseExited(MouseEvent)
		 */
		private boolean isMouseWithinAvatar;

		/**
		 * Timer used to display the tool tip on a delay.
		 * 
		 */
		private final Timer toolTipTimer;

		/**
		 * Create a DocumentAvatarMouseListener.
		 * 
		 */
		private DocumentAvatarMouseListener() {
			super();
			this.isMouseWithinAvatar = false;
			this.toolTipTimer = new Timer(750, new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					if(isMouseWithinAvatar) { showToolTip(); }
				}
			});
			this.toolTipTimer.setRepeats(false);
		}

		/**
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 * 
		 */
		public void mouseClicked(final MouseEvent e) {
			if(2 == e.getClickCount()) { runOpenDocument(); }
		}

		/**
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 * 
		 */
		public void mouseEntered(final MouseEvent e) {
			setHighlight(true);
			isMouseWithinAvatar = true;
			toolTipTimer.start();
		}

		/**
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 * 
		 */
		public void mouseExited(final MouseEvent e) {
			if(isToolTipVisible) { hideToolTip(); }
			setHighlight(false);
			isMouseWithinAvatar = false;
		}
	}

	/**
	 * Background color used when highlighing an avatar.
	 * 
	 */
	private static final Color backgroundColorHighlight;

	/**
	 * Font used to draw write the document's name.
	 * 
	 */
	private static final Font nameFont;

	/**
	 * Color of the nameFont use to write the document's name.
	 * 
	 */
	private static final Color nameFontColor;

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	static {
		// grab the font info for the name
		nameFont = new Font("Tahoma", Font.PLAIN, 12);
		nameFontColor = BrowserColorUtil.getBlack();

		// set a highlight color
		backgroundColorHighlight = BrowserColorUtil.getRGBColor(214, 217, 229, 255);
	}

	/**
	 * Handle to an apache logger
	 * 
	 */
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());

	/**
	 * Action used to open a document.
	 * 
	 */
	protected final Open openDocument =
		(Open) BrowserActionFactory.createAction(Open.class);

	/**
	 * The tool tip displayed on mouse hover.
	 * 
	 */
	private final DocumentAvatarToolTip avatarToolTip;

	/**
	 * The document id.
	 * 
	 */
	private UUID id;

	/**
	 * Indicates whether or not the tool tip is currently visible.
	 * 
	 * @see DocumentAvatar#hideToolTip()
	 * @see DocumentAvatar#showToolTip()
	 */
	private boolean isToolTipVisible;

	/**
	 * The document key holder.
	 * 
	 */
	private String keyHolder;

	/**
	 * The document name.
	 * 
	 */
	private String name;

	/**
	 * Label used to display the document name.
	 * 
	 */
	private final JLabel nameJLabel;

	/**
	 * Handle to the parent container. Is stored because the avatar is
	 * temporarily removed from the parent at one point.
	 * 
	 */
	private final Container parent;

	/**
	 * Create a DocumentAvatar.
	 * 
	 */
	public DocumentAvatar(final Container parent) {
		super();
		setOpaque(false);
		setBackground(backgroundColorHighlight);
		setLayout(new GridBagLayout());
		addMouseListener(new DocumentAvatarMouseListener());

		// components
		this.nameJLabel = new JLabel();
		this.nameJLabel.setFont(nameFont);
		this.nameJLabel.setForeground(nameFontColor);
		add(nameJLabel, createNameJLabelConstraints());

		this.avatarToolTip = new DocumentAvatarToolTip(this);
		this.parent = parent;
	}

	/**
	 * Obtain the highlight color.
	 * 
	 * @return The color to highlight the avatar.
	 */
	public Color getHighlightColor() { return backgroundColorHighlight; }

	/**
	 * Obtain the document id.
	 * 
	 * @return The document id.
	 */
	public UUID getId() { return id; }

	/**
	 * Obtain the document key holder.
	 * 
	 * @return The document key holder.
	 */
	public String getKeyHolder() { return keyHolder; }

	/**
	 * Obtain the document name.
	 * 
	 * @return The document name.
	 */
	public String getName() { return name; }

	/**
	 * Set the document id.
	 * 
	 * @param id
	 *            The document id.
	 */
	public void setId(UUID id) { this.id = id; }

	/**
	 * Set the document key holder.
	 * 
	 * @param keyHolder
	 *            The key holder.
	 */
	public void setKeyHolder(String keyHolder) { this.keyHolder = keyHolder; }

	/**
	 * Set the name.
	 * 
	 * @param name
	 *            The name.
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
		g2.setColor(backgroundColorHighlight);
		g2.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
		g2.dispose();
	}

	/**
	 * Create the grid bag constriants for the name label.
	 * 
	 * @return The grid bag constraints.
	 */
	Object createNameJLabelConstraints() {
		return new GridBagConstraints(0, 0,
				1, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 12, 2, 12),
				0, 2);
	}


	/**
	 * Obtain the JLabel for the document name.
	 * 
	 * @return The name JLabel.
	 */
	JLabel getNameJLabel() { return nameJLabel; }

	/**
	 * Hide the tool tip.
	 *
	 */
	void hideToolTip() {
		isToolTipVisible = false;

		parent.remove(avatarToolTip);
		parent.add(this, getClientProperty("addConstraints"), (Integer) getClientProperty("addIndex"));
		parent.validate();

		setHighlight(false);
	}

	/**
	 * Show the tool tip for the avatar.
	 *
	 */
	void showToolTip() {
		isToolTipVisible = true;

		avatarToolTip.setKeyHolder(keyHolder);
		avatarToolTip.setName(name);
		avatarToolTip.transferToDisplay();
		
		parent.remove(this);
		parent.add(avatarToolTip, getClientProperty("addConstraints"), (Integer) getClientProperty("addIndex"));
		parent.validate();
	}

	/**
	 * Transfer all information from the private data members to the display
	 * components.
	 * 
	 */
	void transferToDisplay() {
		this.nameJLabel.setText(name);
	}

	/**
	 * Run the open document action.
	 *
	 */
	private void runOpenDocument() {
		final Data data = new Data(1);
		data.set(Open.DataKey.DOCUMENT_ID, getId());
		openDocument.invoke(data);
	}

	/**
	 * Set the background highlight of the panel.
	 * 
	 * @param doHighlight
	 *            Whether or not to highlight the background.
	 */
	private void setHighlight(final boolean doHighlight) {
		setOpaque(doHighlight);	// this is how the highlight of the document
		repaint();				// is done.
	}
}
