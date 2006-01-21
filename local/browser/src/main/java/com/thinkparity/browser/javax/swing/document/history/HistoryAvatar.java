/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.javax.swing.document.history;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.thinkparity.browser.javax.swing.document.DocumentAvatar;
import com.thinkparity.browser.ui.action.ActionFactory;
import com.thinkparity.browser.ui.action.Data;
import com.thinkparity.browser.ui.action.document.OpenVersion;
import com.thinkparity.browser.util.RandomData;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.DocumentVersion;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HistoryAvatar extends JPanel {

	/**
	 * Mouse listener for the document avatar. Used to display the tool tip on a
	 * delay; as well as to highlight the avatars.
	 * 
	 */
	private class HistoryAvatarMouseListener extends MouseAdapter {

		/**
		 * @see java.io.Serialiable
		 */
		private static final long serialVersionUID = 1;

		/**
		 * Create a DocumentAvatarMouseListener.
		 * 
		 */
		private HistoryAvatarMouseListener() { super(); }

		/**
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 * 
		 */
		public void mouseClicked(final MouseEvent e) {
			if(2 == e.getClickCount()) { runOpenDocumentVersion(); }
		}

		/**
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 * 
		 */
		public void mouseEntered(final MouseEvent e) {
			setHighlight(true);
		}

		/**
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 * 
		 */
		public void mouseExited(final MouseEvent e) {
			setHighlight(false);
		}
	}
	
	private static final Font font;

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	static {
		font = new Font("Tahoma", Font.PLAIN, 12);
	}

	/**
	 * Action used to open a document version.
	 * 
	 */
	protected final OpenVersion openVersion =
		(OpenVersion) ActionFactory.createAction(OpenVersion.class);

	/**
	 * The history item data.
	 * 
	 */
	private DocumentVersion historyItem;

	/**
	 * The display com.thinkparity.browser.javax.swing.component for the history item.
	 * 
	 */
	private final JLabel historyItemJLabel;

	/**
	 * Display input.
	 * 
	 */
	private DocumentVersion input;

	/**
	 * Create a HistoryAvatar.
	 */
	protected HistoryAvatar() {
		super();
		setOpaque(true);
		setBackground(Color.WHITE);
		setLayout(new GridBagLayout());
		addMouseListener(new HistoryAvatarMouseListener());

		this.historyItemJLabel = new JLabel();
		this.historyItemJLabel.setFont(font);
		this.historyItemJLabel.setForeground(DocumentAvatar.getNameForeground());
		add(historyItemJLabel, createHistoryItemJLabelConstraints());
	}

	public Object getInput() { return input; }

	public void setInput(Object input) {
		Assert.assertNotNull("", input);
		Assert.assertOfType("", DocumentVersion.class, input);

		this.input = (DocumentVersion) input;
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g.create();
		// separator
		g2.setColor(DocumentAvatar.getHighlightColor());
		g2.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
		g2.dispose();
	}

	/**
	 * Obtain the history item.
	 * 
	 * @return The history item.
	 */
	DocumentVersion getHistoryItem() { return historyItem; }

	/**
	 * Transfer the data from the members to the display elements.
	 *
	 */
	void transferToDisplay() {
		// NOTE Random data used.
		final RandomData randomData = new RandomData();
		final StringBuffer buffer = new StringBuffer(75)
			.append(randomData.getAction())
			.append(randomData.getActionUser())
			.append(randomData.getActionDate())
			.append(" by ")
			.append(randomData.getUser());
		this.historyItemJLabel.setText(buffer.toString());
	}

	private Object createHistoryItemJLabelConstraints() {
		return new GridBagConstraints(0, 0,
				1, 1,
				1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 12, 2, 12),
				0, 2);
	}

	/**
	 * Run the open version action for the document. 
	 *
	 */
	private void runOpenDocumentVersion() {
		final Data data = new Data(2);
		data.set(OpenVersion.DataKey.DOCUMENT_ID, input.getDocumentId());
		data.set(OpenVersion.DataKey.VERSION_ID, input.getVersionId());
		openVersion.invoke(data);
	}

	/**
	 * Set the background highlight of the panel.
	 * 
	 * @param doHighlight
	 *            Whether or not to highlight the background.
	 */
	private void setHighlight(final boolean doHighlight) {
		if(doHighlight) { setBackground(DocumentAvatar.getHighlightColor()); }
		else { setBackground(DocumentAvatar.getDefaultColor()); }
		repaint();
	}
}
