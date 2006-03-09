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
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.ImageIOUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ButtonPanel extends AbstractJPanel {

	private static final Icon CLOSE_ICON;

	private static final Icon CLOSE_ROLLOVER_ICON;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	private static final Icon STD_BUTTON_ICON;

	private static final Icon STD_BUTTON_ROLLOVER_ICON;

	static {
		CLOSE_ICON = ImageIOUtil.readIcon("CloseButton.png");
		CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("CloseButtonRollover.png");

		STD_BUTTON_ICON = ImageIOUtil.readIcon("StandardButton.png");
		STD_BUTTON_ROLLOVER_ICON = ImageIOUtil.readIcon("StandardButtonRollover.png");
	}

	private JLabel closeJLabel;
	
	private JLabel contactsJLabel;

	/**
	 * Handle to the container avatar.
	 * 
	 */
	private final Avatar container;

	private JLabel invitePartnerJLabel;

	/**
	 * Create a ButtonPanel.
	 * 
	 */
	public ButtonPanel(final Avatar container,
			final MouseInputAdapter mouseInputAdapter) {
		super("ButtonPanel");
		this.container = container;
		addMouseListener(mouseInputAdapter);
		addMouseMotionListener(mouseInputAdapter);
		setOpaque(false);
		setLayout(new GridBagLayout());
		initComponents();
	}

	/**
	 * Obtain the browser application.
	 * 
	 * @return The browser application.
	 */
	private Browser getBrowser() { return container.getController(); }

	private void initComponents() {
		contactsJLabel = LabelFactory.create(STD_BUTTON_ICON);
		contactsJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				runManageContacts();
			}
			public void mouseEntered(final MouseEvent e) {
				contactsJLabel.setIcon(STD_BUTTON_ROLLOVER_ICON);
			}
			public void mouseExited(final MouseEvent e) {
				contactsJLabel.setIcon(STD_BUTTON_ICON);
			}
		});
		closeJLabel = LabelFactory.create(CLOSE_ICON);
		closeJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				runCloseBrowser();
			}
			public void mouseEntered(final MouseEvent e) {
				closeJLabel.setIcon(CLOSE_ROLLOVER_ICON);
			}
			public void mouseExited(final MouseEvent e) {
				closeJLabel.setIcon(CLOSE_ICON);
			}
		});
		invitePartnerJLabel = LabelFactory.create(STD_BUTTON_ICON);
		invitePartnerJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				runInvitePartner();
			}
			public void mouseEntered(final MouseEvent e) {
				invitePartnerJLabel.setIcon(STD_BUTTON_ROLLOVER_ICON);
			}
			public void mouseExited(final MouseEvent e) {
				invitePartnerJLabel.setIcon(STD_BUTTON_ICON);
			}
		});

		final GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHEAST;
		c.weightx = 1;
		c.weighty = 1;
		add(contactsJLabel, c.clone());

		c.insets.left = 11;
		c.weightx = 0;
		c.weighty = 0;
		add(invitePartnerJLabel, c.clone());

		c.anchor = GridBagConstraints.NORTH;
		c.insets.top = 6;
		c.insets.right = 8;
		add(closeJLabel, c.clone());
	}

	/**
	 * Close the browser.
	 *
	 */
	private void runCloseBrowser() { getBrowser().end(); }

	/**
	 * Invite a partner.
	 *
	 */
	private void runInvitePartner() {}

	/**
	 * Open the contacts dialogue.
	 *
	 */
	private void runManageContacts() {}
}
