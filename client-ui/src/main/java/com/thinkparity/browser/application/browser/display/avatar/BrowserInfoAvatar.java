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
import javax.swing.JCheckBoxMenuItem;
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

import com.thinkparity.model.parity.model.artifact.ArtifactState;
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

	private static final Icon FILTER_ICON;

	private static final Icon FILTER_ROLLOVER_ICON;

	/**
     * The filter popup menu.
     * 
     * @see #getFilterJPopupMenu()
     */
    private static JPopupMenu filterJPopupMenu;

	private static final Icon HIDE_HISTORY_ICON;

	private static final Icon HIDE_HISTORY_ROLLOVER_ICON;

	private static final Icon INVISIBLE_HISTORY_ICON;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

    private static final Icon SHOW_HISTORY_ICON;

    private static final Icon SHOW_HISTORY_ROLLOVER_ICON;

	static {
		INVISIBLE_HISTORY_ICON = ImageIOUtil.readIcon("InvisibleHistoryButton.png");
		HIDE_HISTORY_ICON = ImageIOUtil.readIcon("HideHistoryButton.png");
		HIDE_HISTORY_ROLLOVER_ICON = ImageIOUtil.readIcon("HideHistoryButtonRollover.png");
		FILTER_ICON = ImageIOUtil.readIcon("FilterButton.png");
		FILTER_ROLLOVER_ICON = ImageIOUtil.readIcon("FilterButtonRollover.png");
		SHOW_HISTORY_ICON = ImageIOUtil.readIcon("ShowHistoryButton.png");
		SHOW_HISTORY_ROLLOVER_ICON = ImageIOUtil.readIcon("ShowHistoryButtonRollover.png");
	}

	private JLabel filterJLabel;

	private JLabel infoJLabel;
	
	private JLabel toggleHistoryJLabel;

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
		if(getController().isHistoryEnabled()) {
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
     * Set a localized message in the info label.
     * 
     * @param infoMessageKey
     *            The localized message local key.
     * @param arguments
     *            The localized message arguments.
     */
	public void setInfoMessage(final String infoMessageKey,
            final Object[] arguments) {
		infoJLabel.setText(getString(infoMessageKey, arguments));
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

    /**
     * Create a checkbox menu item.
     * 
     * @param textLocalKey
     *            The menu item localization local key.
     * @return The checkbox menu item.
     */
	private JCheckBoxMenuItem createJCheckBoxMenuItem(final String textLocalKey) {
        return MenuItemFactory.createCheckBox(
                getString(textLocalKey), getMnemonic(textLocalKey));
	}

    /**
     * Create a menu item.
     * 
     * @param textLocalKey
     *            The menu item localization local key.
     * @return The menu item.
     */
    private JMenuItem createJMenuItem(final String textLocalKey) {
        return MenuItemFactory.create(
                getString(textLocalKey), getMnemonic(textLocalKey));
    }

	/**
     * Show the filter menu.
     * 
     * @param e
     *            The mouse event.
     */
	private void filterJLabelMouseReleased(final MouseEvent e) {
		getFilterJPopupMenu().show(filterJLabel, 0, filterJLabel.getSize().height);
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
     * Get the filter menu. It will be created the first time this api is
     * called.
     * 
     * @return The filter popup menu.
     */
    private JPopupMenu getFilterJPopupMenu() {
        if(null == filterJPopupMenu) {
            filterJPopupMenu = MenuFactory.createPopup();

            final JCheckBoxMenuItem closed = createJCheckBoxMenuItem("FilterShowClosed");
            final JCheckBoxMenuItem open = createJCheckBoxMenuItem("FilterShowOpen");
            final JCheckBoxMenuItem key = createJCheckBoxMenuItem("FilterShowKey");
            final JCheckBoxMenuItem notKey = createJCheckBoxMenuItem("FilterShowNotKey");
            final JMenuItem clear = createJMenuItem("FilterClearAll");

            closed.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(((JCheckBoxMenuItem) e.getSource()).isSelected()) {
                        open.setSelected(false);
                        
                        getController().removeStateFilter();
                        getController().applyStateFilter(ArtifactState.CLOSED);
                        }
                    else { getController().removeStateFilter(); }
                }});
            open.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(((JCheckBoxMenuItem) e.getSource()).isSelected()) {
                        closed.setSelected(false);

                        getController().removeStateFilter();
                        getController().applyStateFilter(ArtifactState.ACTIVE);
                    }
                    else { getController().removeStateFilter(); }
                }});

            key.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(((JCheckBoxMenuItem) e.getSource()).isSelected()) {
                        notKey.setSelected(false);

                        getController().removeKeyHolderFilter();
                        getController().applyKeyHolderFilter(Boolean.TRUE);
                    }
                    else { getController().removeKeyHolderFilter(); }
                }});
            notKey.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(((JCheckBoxMenuItem) e.getSource()).isSelected()) {
                        key.setSelected(false);

                        getController().removeKeyHolderFilter();
                        getController().applyKeyHolderFilter(Boolean.FALSE);
                    }
                    else { getController().removeKeyHolderFilter(); }
                }
            });

            clear.addActionListener(new ActionListener() {
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    closed.setSelected(false);
                    open.setSelected(false);
                    key.setSelected(false);
                    notKey.setSelected(false);

                    getController().clearFilters();
                }
            });

            filterJPopupMenu.add(closed);
            filterJPopupMenu.add(open);
            filterJPopupMenu.addSeparator();
            filterJPopupMenu.add(key);
            filterJPopupMenu.add(notKey);
            filterJPopupMenu.addSeparator();
            filterJPopupMenu.add(clear);
        }
        return filterJPopupMenu;
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

    /**
     * Determine which rollover icon to use. Check the browser to see if the history is
     * visible or not.
     * 
     * @return The image icon.
     */
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
        filterJLabel = LabelFactory.create(FILTER_ICON);
        filterJLabel.setToolTipText(getString("FilterButtonToolTip"));
        filterJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(final MouseEvent e) {
        		filterJLabel.setIcon(FILTER_ROLLOVER_ICON);
        	}
			public void mouseExited(MouseEvent e) {
        		filterJLabel.setIcon(FILTER_ICON);
        	}
        	public void mouseReleased(final MouseEvent e) {
				filterJLabelMouseReleased(e);
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
	 * Reload the info label.
	 *
	 */
	private void reloadInfo() {
		infoJLabel.setText(getString("Info.Empty"));
        if(getController().isFilterEnabled()) {
            infoJLabel.setText(getString("Info.FilterOn"));
        }
        else if(isTestMode()) {
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
}
