/*
 * Mar 8, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.MenuItemFactory;
import com.thinkparity.browser.application.browser.display.provider.CompositeSingleContentProvider;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.ImageIOUtil;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.SwingUtil;

import com.thinkparity.model.xmpp.contact.Contact;

/**
 * Displays the browser's info panel. This avatar contains the sort button as
 * well as the button to toggle the history. It can also display informational
 * messages to the user.
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserInfoAvatar extends Avatar {

	private static final Icon HIDE_HISTORY_ICON;

	private static final Icon HIDE_HISTORY_ROLLOVER_ICON;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	private static final Icon SHOW_HISTORY_ICON;

	private static final Icon SHOW_HISTORY_ROLLOVER_ICON;

	private static final Icon SORT_ICON;

	private static final Icon SORT_ROLLOVER_ICON;

	private static final Icon INVISIBLE_HISTORY_ICON;

	static {
		INVISIBLE_HISTORY_ICON = ImageIOUtil.readIcon("InvisibleHistoryButton.png");
		HIDE_HISTORY_ICON = ImageIOUtil.readIcon("HideHistoryButton.png");
		HIDE_HISTORY_ROLLOVER_ICON = ImageIOUtil.readIcon("HideHistoryButtonRollover.png");
		SORT_ICON = ImageIOUtil.readIcon("SortButton.png");
		SORT_ROLLOVER_ICON = ImageIOUtil.readIcon("SortButtonRollover.png");
		SHOW_HISTORY_ICON = ImageIOUtil.readIcon("ShowHistoryButton.png");
		SHOW_HISTORY_ROLLOVER_ICON = ImageIOUtil.readIcon("ShowHistoryButtonRollover.png");
	}

	private JLabel infoJLabel;

	private JLabel toggleHistoryJLabel;

	private JLabel filterJLabel;
	
	/**
	 * Create a BrowserInfoAvatar.
	 *
	 */
	BrowserInfoAvatar() {
	    super("BrowserInfo");
	    setLayout(new GridBagLayout());
	    setOpaque(false);
	    initComponents();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.BROWSER_INFO; }

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() { return null; }

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
	 * 
	 */
	public void reload() {
		reloadInfo();
		if(0 < getDocumentCount()) {
			toggleHistoryJLabel.setEnabled(true);
			if(SwingUtil.regionContains(toggleHistoryJLabel.getBounds(), getMousePosition())) {
				toggleHistoryJLabel.setIcon(getHistoryRolloverIcon());
			}
			else { toggleHistoryJLabel.setIcon(getHistoryIcon()); }
		}
		else {
			// if the history is visible; kill it
			if(getController().isHistoryVisible()) {
				getController().toggleHistory3Avatar();
			}
			toggleHistoryJLabel.setEnabled(false);
			toggleHistoryJLabel.setIcon(INVISIBLE_HISTORY_ICON);
		}
	}

	/**
	 * Reload the info label.
	 *
	 */
	private void reloadInfo() {
		infoJLabel.setText(getString("Info.Empty"));
		if(isTestMode()) {
			final Contact contact = getContact();

			final Object[] infoParityUserArguments = new Object[] {
					contact.getFirstName(),
					contact.getLastName(),
					contact.getOrganization()
			};
			final StringBuffer buffer =
				new StringBuffer(getString("Info.Contact", infoParityUserArguments))
				.append(infoJLabel.getText());
			infoJLabel.setText(buffer.toString());
		}
	}

	public void setInfoMessage(final String infoMessageKey, final Object[] arguments) {
		infoJLabel.setText(getString(infoMessageKey, arguments));
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

	private JMenuItem createJMenuItem(final String textLocalKey,
			final ActionListener actionListener) {
		return createJMenuItem(getString(textLocalKey),
				getMnemonic(textLocalKey), actionListener);
	}

	private JMenuItem createJMenuItem(final String text, final Integer mnemonic,
			final ActionListener actionListener) {
		final JMenuItem jMenuItem = MenuItemFactory.create(text, mnemonic);
		jMenuItem.addActionListener(actionListener);
		return jMenuItem;

	}

	/**
     * Obtain the contact from the content provider.
     * 
     * @return The contact.
     */
	private Contact getContact() {
		return (Contact) ((CompositeSingleContentProvider) contentProvider).getElement(1, null);
	}

	/**
	 * Obtain the document count from the content provider.
	 * 
	 * @return The document count.
	 */
	private Integer getDocumentCount() {
		return (Integer) ((CompositeSingleContentProvider) contentProvider).getElement(0, null);
	}

	/**
	 * Determine which icon to use. Check the browser to see if the history is
	 * visible or not.
	 * 
	 * @return The image icon.
	 */
	private Icon getHistoryIcon() {
		if(null != toggleHistoryJLabel && toggleHistoryJLabel.isEnabled()) {
			if(getController().isHistoryVisible()) { return HIDE_HISTORY_ICON; }
			else { return SHOW_HISTORY_ICON; }
		}
		else { return INVISIBLE_HISTORY_ICON; }
	}

	private Icon getHistoryRolloverIcon() {
		if(null != toggleHistoryJLabel && toggleHistoryJLabel.isEnabled()) {
			if(getController().isHistoryVisible()) { return HIDE_HISTORY_ROLLOVER_ICON; }
			else { return SHOW_HISTORY_ROLLOVER_ICON; }
		}
		else { return INVISIBLE_HISTORY_ICON; }
	}

	private Integer getMnemonic(final String textLocalKey) {
		final String mnemonicString = getString(textLocalKey + "Mnemonic");
		return new Integer(mnemonicString.charAt(0));
	}

	private void initComponents() {
        filterJLabel = LabelFactory.create(SORT_ICON);
        filterJLabel.setToolTipText(getString("FilterButtonToolTip"));
        filterJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(final MouseEvent e) {
        		filterJLabel.setIcon(SORT_ROLLOVER_ICON);
        	}
			public void mouseExited(MouseEvent e) {
        		filterJLabel.setIcon(SORT_ICON);
        	}
        	public void mouseReleased(final MouseEvent e) {
				sortJLabelMouseReleased(e);
			}
        });

        infoJLabel = LabelFactory.create();
        infoJLabel.setHorizontalAlignment(SwingConstants.CENTER);

        toggleHistoryJLabel = LabelFactory.create(getHistoryIcon());
        toggleHistoryJLabel.setToolTipText(getString("ToggleHistoryButtonToolTip"));
        toggleHistoryJLabel.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(final MouseEvent e) {
        		toggleHistoryJLabelMouseClicked(e);
        	}
        	public void mouseEntered(final MouseEvent e) {
        		toggleHistoryJLabel.setIcon(getHistoryRolloverIcon());
        	}
        	public void mouseExited(final MouseEvent e) {
        		toggleHistoryJLabel.setIcon(getHistoryIcon());
        	}
        });

        final GridBagConstraints c = new GridBagConstraints();
        c.insets.left = 7;
        add(filterJLabel, c.clone());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets.left = 0;
        c.weightx = 1;
        add(infoJLabel, c.clone());

        c.fill = GridBagConstraints.NONE;
        c.insets.right = 8;
        c.weightx = 0;
        add(toggleHistoryJLabel, c.clone());
    }

	/**
	 * Display the document history.
	 * 
	 * @param e
	 *            The action event.
	 */
	private void toggleHistoryJLabelMouseClicked(final MouseEvent e) {
		if(toggleHistoryJLabel.isEnabled()) {
			getController().toggleHistory3Avatar();
			toggleHistoryJLabel.setIcon(getHistoryRolloverIcon());
		}
	}

	private void sortJLabelMouseReleased(final MouseEvent e) {
		final JPopupMenu jPopupMenu = MenuFactory.createPopup();
		jPopupMenu.add(createJMenuItem("ReloadMainList", new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				getController().reloadMainList();
			}
		}));
		jPopupMenu.show(filterJLabel, 0, filterJLabel.getSize().height);
	}
}
