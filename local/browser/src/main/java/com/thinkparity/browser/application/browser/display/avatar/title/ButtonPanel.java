/*
 * Mar 7, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.title;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.dnd.CreateDocumentTxHandler;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.javax.swing.dnd.CopyActionEnforcer;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.ImageIOUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ButtonPanel extends AbstractJPanel {

	private static final Icon CLOSE_ICON;

	private static final Icon CLOSE_ROLLOVER_ICON;

	private static final Icon CONTACTS_ICON;

	private static final Icon CONTACTS_ROLLOVER_ICON;

	private static final Icon MIN_ICON;

	private static final Icon MIN_ROLLOVER_ICON;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		CLOSE_ICON = ImageIOUtil.readIcon("CloseButton.png");
		CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("CloseButtonRollover.png");

		CONTACTS_ICON = ImageIOUtil.readIcon("ContactsButton.png");
		CONTACTS_ROLLOVER_ICON = ImageIOUtil.readIcon("ContactsButtonRollover.png");

		MIN_ICON = ImageIOUtil.readIcon("MinimizeButton.png");
		MIN_ROLLOVER_ICON = ImageIOUtil.readIcon("MinimizeButtonRollover.png");
	}

	/**
	 * The close button.
	 * 
	 */
	private JLabel closeJLabel;

	/**
	 * The contacts button.
	 * 
	 */
	private JLabel contactsJLabel;

	/**
	 * Handle to the container avatar.
	 * 
	 */
	private final Avatar container;

	/**
	 * The minimize button.
	 *
	 */
	private JLabel minJLabel;

	/**
	 * Create a ButtonPanel.
	 * 
	 */
	public ButtonPanel(final Avatar container,
			final MouseInputAdapter mouseInputAdapter) {
		super("BrowserTitleAvatar.ButtonPanel");
		this.container = container;
		addMouseListener(mouseInputAdapter);
		addMouseMotionListener(mouseInputAdapter);
		setOpaque(false);
		setLayout(new GridBagLayout());
        setTransferHandler(new CreateDocumentTxHandler(container.getController()));
        CopyActionEnforcer.applyEnforcer(this);
		initComponents();
	}

	/**
	 * Close the browser.
	 *
	 */
	private void closeJLabelMouseClicked(final MouseEvent e) {
		if(e.isShiftDown()) { getBrowser().end(getBrowser().getPlatform()); }
		else { getBrowser().close(); }
		closeJLabel.setIcon(CLOSE_ICON);
	}

	/**
	 * Open the contacts dialogue.
	 *
	 */
	private void displayManageContacts() {
		getBrowser().displaySessionManageContacts();
	}

	/**
	 * Obtain the browser application.
	 * 
	 * @return The browser application.
	 */
	private Browser getBrowser() { return container.getController(); }

	/**
	 * Initialize the panel's swing components.
	 *
	 */
	private void initComponents() {
		contactsJLabel = LabelFactory.create(CONTACTS_ICON);
		contactsJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				displayManageContacts();
			}
			public void mouseEntered(final MouseEvent e) {
				contactsJLabel.setIcon(CONTACTS_ROLLOVER_ICON);
			}
			public void mouseExited(final MouseEvent e) {
				contactsJLabel.setIcon(CONTACTS_ICON);
			}
		});
		closeJLabel = LabelFactory.create(CLOSE_ICON);
		closeJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				closeJLabelMouseClicked(e);
			}
			public void mouseEntered(final MouseEvent e) {
				closeJLabel.setIcon(CLOSE_ROLLOVER_ICON);
			}
			public void mouseExited(final MouseEvent e) {
				closeJLabel.setIcon(CLOSE_ICON);
			}
		});
		minJLabel = LabelFactory.create(MIN_ICON);
		minJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				minJLabelMouseClicked(e);
			}
			public void mouseEntered(final MouseEvent e) {
				minJLabel.setIcon(MIN_ROLLOVER_ICON);
			}
			public void mouseExited(final MouseEvent e) {
				minJLabel.setIcon(MIN_ICON);
			}
		});

		final GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHEAST;
        c.insets.right = 50;
        c.weightx = 1;
		c.weighty = 1;
		add(contactsJLabel, c.clone());

		c.anchor = GridBagConstraints.NORTH;
		c.insets.top = 6;
		c.insets.left = 4;
		c.insets.right = 2;
		c.weightx = 0;
		c.weighty = 0;
		add(minJLabel, c.clone());

		c.insets.left = 0;
		c.insets.right = 8;
		add(closeJLabel, c.clone());
	}

	/**
	 * The user clicked on the minimize button.
	 *
	 * @param e The mouse event.
	 */
	private void minJLabelMouseClicked(final MouseEvent e) {
		container.getController().minimize();
	}
}
