/*
 * Jan 11, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.javax.swing.button.BrowserButtonFactory;
import com.thinkparity.browser.log4j.BrowserLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentAvatarToolTip extends JPanel {

	/**
	 * Font used to display the key holder label.
	 * 
	 */
	private static final Font keyHolderFont;

	/**
	 * Foreground color used to display the key holder label.
	 * 
	 */
	private static final Color keyHolderForeground;

	/**
	 * @see java.io.Serialiable
	 */
	private static final long serialVersionUID = 1;

	static {
		keyHolderFont = new Font("Tahoma", Font.PLAIN, 12);
		keyHolderForeground = Color.BLACK;
	}

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());

	/**
	 * Flag indicating whether the user can close.
	 * 
	 */
	private boolean canClose;

	/**
	 * Flag indicating whether the user can delete.
	 * 
	 */
	private boolean canDelete;

	/**
	 * The close button.
	 * 
	 */
	private final JButton closeJButton;

	/**
	 * The delete button.
	 * 
	 */
	private final JButton deleteJButton;

	/**
	 * Flag indicating whether or not the user is also the key holder.
	 * 
	 */
	private boolean isKeyHolder;

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
	 * The request key button.
	 * 
	 */
	private final JButton requestKeyJButton;

	/**
	 * The send button.
	 * 
	 */
	private final JButton sendJButton;

	/**
	 * The send key button.
	 * 
	 */
	private final JButton sendKeyJButton;

	/**
	 * The history send button.
	 * 
	 */
	private final JButton historyJButton;

	/**
	 * Create a DocumentAvatarToolTip.
	 * 
	 */
	public DocumentAvatarToolTip() {
		super();
		this.canClose = false;
		this.canDelete = false;
		this.isHistoryDisplayed = false;
		this.isKeyHolder = false;

		setOpaque(true);
		setBackground(DocumentAvatar.getHighlightColor());
		setLayout(new GridBagLayout());

		this.nameJLabel = new JLabel();
		this.nameJLabel.setFont(DocumentAvatar.getNameFont());
		this.nameJLabel.setForeground(DocumentAvatar.getNameForeground());
		add(nameJLabel, createNameJLabelConstraints());

		this.keyHolderJLabel = new JLabel();
		this.keyHolderJLabel.setFont(keyHolderFont);
		this.keyHolderJLabel.setForeground(keyHolderForeground);
		add(keyHolderJLabel, createKeyHolderJLabelConstraints());

		this.closeJButton = BrowserButtonFactory.create("Close");
		this.closeJButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				e.consume();
				logger.info("Close click.");
			}
		});

		this.deleteJButton = BrowserButtonFactory.create("Delete");
		this.deleteJButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				e.consume();
				logger.info("Delete click.");
			}
		});

		this.historyJButton = BrowserButtonFactory.create("History");
		this.historyJButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				e.consume();
				runToggleHistory();
			}
		});
		add(historyJButton, createHistoryJButtonConstraints());

		this.sendJButton = BrowserButtonFactory.create("Send");
		this.sendJButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				e.consume();
				logger.info("Send click.");
			}
		});

		this.sendKeyJButton = BrowserButtonFactory.create("Send Ownership");
		this.sendKeyJButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				e.consume();
				logger.info("Send key click.");
			}
		});

		this.requestKeyJButton = BrowserButtonFactory.create("Request Ownership");
		this.requestKeyJButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				e.consume();
				logger.info("Request key click.");
			}
		});
	}

	/**
	 * Obtain the document can close flag.
	 * 
	 * @return The document can close flag.
	 */
	public boolean canClose() { return canClose; }

	/**
	 * Obtain the document can delete flag.
	 * @return The document can delete flag.
	 */
	public boolean canDelete() { return canDelete; }

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
	 * Determine if the document key holder flag is set.
	 * 
	 * @return The document key holder flag.
	 */
	public boolean isKeyHolder() { return isKeyHolder; }

	/**
	 * Set the document can close flag.
	 * 
	 * @param canClose
	 *            The document can close flag.
	 * @see DocumentAvatarToolTip#transferToDisplay()
	 */
	public void setCanClose(boolean canClose) { this.canClose = canClose; }

	/**
	 * Set the document can delete flag.
	 * 
	 * @param canDelete
	 *            The document can delete flag.
	 */
	public void setCanDelete(boolean canDelete) { this.canDelete = canDelete; }

	/**
	 * Set the document key holder flag.
	 * 
	 * @param isKeyHolder
	 *            The document key holder flag.
	 */
	public void setKeyHolder(boolean isKeyHolder) {
		this.isKeyHolder = isKeyHolder;
	}


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

	/**
	 * Transfer the data from the members to the respective display controls.
	 * 
	 */
	void transferToDisplay() {
		this.nameJLabel.setText(name);
		this.keyHolderJLabel.setText(keyHolder);
		add(sendJButton, createSendJButtonConstraints());
		if(canClose) { add(closeJButton, createCloseJButtonConstraints()); }
		if(canDelete) { add(deleteJButton, createDeleteJButtonConstraints()); }
		if(isKeyHolder) { add(sendKeyJButton, createSendKeyJButtonConstraints()); }
		else { add(requestKeyJButton, createRequestJButtonConstraints()); }
	}

	/**
	 * Create the grid bag constraints for the history button.
	 * 
	 * @return The grid bag constraints for the history button.
	 */
	private Object createHistoryJButtonConstraints() {
		return new GridBagConstraints(4, 2,
				1, 1,
				0.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.VERTICAL,
				new Insets(0, 3, 0, 0),
				0, 0);
	}

	/**
	 * Create the grid bag constraints for the close button.
	 * 
	 * @return The grid bag constraints for the close button.
	 */
	private Object createCloseJButtonConstraints() {
		return new GridBagConstraints(0, 2,
				2, 1,
				1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	/**
	 * Create the grid bag constraints for the delete button.
	 * 
	 * @return The grid bag constraints for the delete button.
	 */
	private Object createDeleteJButtonConstraints() {
		return new GridBagConstraints(0, 2,
				2, 1,
				1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	/**
	 * Create the grid bag constraints for the key holder label.
	 * 
	 * @return The grid bag constraints.
	 */
	private Object createKeyHolderJLabelConstraints() {
		return new GridBagConstraints(0, GridBagConstraints.RELATIVE,
				5, 1,
				1.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH,
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
				5, 1,
				1.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 12, 2, 12),
				0, 2);
	}

	/**
	 * Create the grid bag constraints for the request key button.
	 * 
	 * @return The grid bag constraints.
	 */
	private Object createRequestJButtonConstraints() {
		return new GridBagConstraints(2, 2,
				1, 1,
				0.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	/**
	 * Create the grid bag constraints for the send button.
	 * 
	 * @return The grid bag constraints.
	 */
	private Object createSendJButtonConstraints() {
		return new GridBagConstraints(3, 2,
				1, 1,
				0.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	/**
	 * Create the grid bag constraints for the send key button.
	 * 
	 * @return The grid bag constraints.
	 */
	private Object createSendKeyJButtonConstraints() {
		return new GridBagConstraints(2, 2,
				1, 1,
				0.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	/**
	 * Flag indicating whether or not the document history is displayed.
	 * 
	 */
	private boolean isHistoryDisplayed;

	private void runToggleHistory() {
		if(isHistoryDisplayed) { runHideHistory(); }
		else { runShowHistory(); }
	}

	private void runHideHistory() {
		
	}

	private void runShowHistory() {}
}
