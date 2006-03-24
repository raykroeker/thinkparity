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

import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.MenuItemFactory;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.ImageIOUtil;
import com.thinkparity.browser.platform.util.State;

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

	static {
		HIDE_HISTORY_ICON = ImageIOUtil.readIcon("HideHistoryButton.png");
		HIDE_HISTORY_ROLLOVER_ICON = ImageIOUtil.readIcon("HideHistoryButtonRollover.png");
		SORT_ICON = ImageIOUtil.readIcon("SortButton.png");
		SORT_ROLLOVER_ICON = ImageIOUtil.readIcon("SortButtonRollover.png");
		SHOW_HISTORY_ICON = ImageIOUtil.readIcon("ShowHistoryButton.png");
		SHOW_HISTORY_ROLLOVER_ICON = ImageIOUtil.readIcon("ShowHistoryButtonRollover.png");
	}

	private JLabel infoJLabel;

	private JLabel showHistoryJLabel;

	private JLabel sortJLabel;
	
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
		if(0 < getDocumentCount()) { showHistoryJLabel.setVisible(true); }
		else { showHistoryJLabel.setVisible(false); }
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
	 * Obtain the document count from the content provider.
	 * 
	 * @return The document count.
	 */
	private Integer getDocumentCount() {
		return (Integer) ((SingleContentProvider) contentProvider).getElement(null);
	}

	/**
	 * Determine which icon to use. Check the browser to see if the history is
	 * visible or not.
	 * 
	 * @return The image icon.
	 */
	private Icon getHistoryIcon() {
		if(getController().isHistoryVisible()) { return HIDE_HISTORY_ICON; }
		else { return SHOW_HISTORY_ICON; }
	}

	private Icon getHistoryRolloverIcon() {
		if(getController().isHistoryVisible()) { return HIDE_HISTORY_ROLLOVER_ICON; }
		else { return SHOW_HISTORY_ROLLOVER_ICON; }
	}

	private Integer getMnemonic(final String textLocalKey) {
		final String mnemonicString = getString(textLocalKey + "Mnemonic");
		return new Integer(mnemonicString.charAt(0));
	}

	private void initComponents() {
        sortJLabel = LabelFactory.create(SORT_ICON);
        sortJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(final MouseEvent e) {
        		sortJLabel.setIcon(SORT_ROLLOVER_ICON);
        	}
			public void mouseExited(MouseEvent e) {
        		sortJLabel.setIcon(SORT_ICON);
        	}
        	public void mouseReleased(final MouseEvent e) {
				sortJLabelMouseReleased(e);
			}
        });

        infoJLabel = LabelFactory.create();
        setTestInfoMessage();

        showHistoryJLabel = LabelFactory.create(getHistoryIcon());
        showHistoryJLabel.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(final MouseEvent e) {
        		showHistoryJLabelMouseClicked(e);
        	}
        	public void mouseEntered(final MouseEvent e) {
        		showHistoryJLabel.setIcon(getHistoryRolloverIcon());
        	}
        	public void mouseExited(final MouseEvent e) {
        		showHistoryJLabel.setIcon(getHistoryIcon());
        	}
        });

        final GridBagConstraints c = new GridBagConstraints();
        c.insets.left = 7;
        add(sortJLabel, c.clone());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets.left = 0;
        c.weightx = 1;
        add(infoJLabel, c.clone());

        c.fill = GridBagConstraints.NONE;
        c.insets.right = 8;
        c.weightx = 0;
        add(showHistoryJLabel, c.clone());
    }

	/**
	 * Set a debug info message.
	 *
	 */
	private void setTestInfoMessage() {
		if(isTestMode()) {
			final Object[] infoParityUserArguments = new Object[] {
					getPreferences().getUsername(),
					getPreferences().getServerHost()
			};
			final StringBuffer buffer =
				new StringBuffer(getString("Info.ParityUser", infoParityUserArguments))
				.append(infoJLabel.getText());
			infoJLabel.setText(buffer.toString());
		}
	}

	/**
	 * Display the document history.
	 * 
	 * @param e
	 *            The action event.
	 */
	private void showHistoryJLabelMouseClicked(final MouseEvent e) {
		getController().toggleHistory3Avatar();
		showHistoryJLabel.setIcon(getHistoryRolloverIcon());
	}

	private void sortJLabelMouseReleased(final MouseEvent e) {
		final JPopupMenu jPopupMenu = MenuFactory.createPopup();
		jPopupMenu.add(createJMenuItem("ReloadMainList", new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				getController().reloadMainList();
			}
		}));
		jPopupMenu.show(sortJLabel, 0, sortJLabel.getSize().height);
	}
}
