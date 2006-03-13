/*
 * Mar 7, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.title;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.MenuItemFactory;
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

	private static final Icon CONTACTS_ICON;

	private static final Icon CONTACTS_ROLLOVER_ICON;

	private static final Icon DOCUMENTS_ICON;

	private static final Icon DOCUMENTS_ROLLOVER_ICON;

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

		DOCUMENTS_ICON = ImageIOUtil.readIcon("DocumentsButton.png");
		DOCUMENTS_ROLLOVER_ICON = ImageIOUtil.readIcon("DocumentsButtonRollover.png");
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
	 * The contacts popup menu.
	 * 
	 * @see #getContactsMenu()
	 */
	private JPopupMenu contactsJPopupMenu;

	/**
	 * Handle to the container avatar.
	 * 
	 */
	private final Avatar container;

	/**
	 * The documents button.
	 * 
	 */
	private JLabel documentsJLabel;

	/**
	 * The documents popup menu.
	 * 
	 * @see #getDocumentsMenu
	 */
	private JPopupMenu documentsJPopupMenu;

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
		initComponents();
	}

	/**
	 * Create a menu item and attach an action listener.
	 * 
	 * @param menuItemKey
	 *            The menu item localization key.
	 * @param actionListener
	 *            The action listener.
	 * @return The menu item.
	 */
	private JMenuItem createMenuItem(final String menuItemKey,
			final ActionListener actionListener) {
		final String text = getString(menuItemKey);
		final Integer mnemonic = new Integer(getString(menuItemKey + "Mnemonic").charAt(0));
		final JMenuItem jMenuItem = MenuItemFactory.create(text, mnemonic);
		jMenuItem.addActionListener(actionListener);
		return jMenuItem;
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
	 * Create the contacts popup menu if it does not exist; and return it.
	 * 
	 * @return The contacts popup menu.
	 */
	private JPopupMenu getContactsMenu() {
		if(null == contactsJPopupMenu) {
			contactsJPopupMenu = MenuFactory.createPopup();
			contactsJPopupMenu.add(createMenuItem("Contacts.Invite", new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					runInviteContact();
				}
			}));
			contactsJPopupMenu.add(createMenuItem("Contacts.Manage", new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					displayManageContacts();
				}
			}));
		}
		return contactsJPopupMenu;
	}

	/**
	 * Create the documents popup menu if it does not exist; and return it.
	 * 
	 * @return The documents popup menu.
	 */
	private JPopupMenu getDocumentsMenu() {
		if(null == documentsJPopupMenu) {
			documentsJPopupMenu = MenuFactory.createPopup();
			documentsJPopupMenu.add(createMenuItem("Documents.Add", new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					runAddDocument();
				}
			}));
		}
		return documentsJPopupMenu;
	}

	/**
	 * Initialize the panel's swing components.
	 *
	 */
	private void initComponents() {
		contactsJLabel = LabelFactory.create(CONTACTS_ICON);
		contactsJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				showMenu(contactsJLabel, getContactsMenu());
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
				runCloseBrowser();
			}
			public void mouseEntered(final MouseEvent e) {
				closeJLabel.setIcon(CLOSE_ROLLOVER_ICON);
			}
			public void mouseExited(final MouseEvent e) {
				closeJLabel.setIcon(CLOSE_ICON);
			}
		});
		documentsJLabel = LabelFactory.create(DOCUMENTS_ICON);
		documentsJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				showMenu(documentsJLabel, getDocumentsMenu());
			}
			public void mouseEntered(final MouseEvent e) {
				documentsJLabel.setIcon(DOCUMENTS_ROLLOVER_ICON);
			}
			public void mouseExited(final MouseEvent e) {
				documentsJLabel.setIcon(DOCUMENTS_ICON);
			}
		});

		final GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHEAST;
		c.weightx = 1;
		c.weighty = 1;
		add(documentsJLabel, c.clone());

		c.insets.left = 11;
		c.weightx = 0;
		c.weighty = 0;
		add(contactsJLabel, c.clone());

		c.anchor = GridBagConstraints.NORTH;
		c.insets.top = 6;
		c.insets.right = 8;
		add(closeJLabel, c.clone());
	}

	/**
	 * Add a document.
	 *
	 */
	private void runAddDocument() { getBrowser().runCreateDocument(); }

	/**
	 * Close the browser.
	 *
	 */
	private void runCloseBrowser() { getBrowser().end(); }

	/**
	 * Open an invite contact dialogue.
	 *
	 */
	private void runInviteContact() { getBrowser().displaySessionInviteContact(); }

	/**
	 * Display a popup menu below the component.
	 * 
	 * @param jComponent
	 *            The component.
	 * @param jPopupMenu
	 *            The popup menu.
	 */
	private void showMenu(final JComponent jComponent,
			final JPopupMenu jPopupMenu) {
		jPopupMenu.show(jComponent, 0, jComponent.getSize().height);
	}
}
