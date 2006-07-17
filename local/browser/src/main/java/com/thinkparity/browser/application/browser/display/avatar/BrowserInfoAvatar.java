/*
 * Mar 8, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.*;

import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.MenuItemFactory;
import com.thinkparity.browser.application.browser.dnd.CreateDocumentTxHandler;
import com.thinkparity.browser.javax.swing.dnd.CopyActionEnforcer;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.ImageIOUtil;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.model.parity.model.artifact.ArtifactState;

/**
 * Displays the browser's info panel. This avatar contains the sort button and
 * a simple info label.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserInfoAvatar extends Avatar {

    /** The filter's icon. */
	private static final Icon FILTER_ICON;

    /** The filter's rollover icon. */
	private static final Icon FILTER_ROLLOVER_ICON;

	/**
     * The filter popup menu.
     * 
     * @see #getFilterJPopupMenu()
     */
    private static JPopupMenu filterJPopupMenu;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		FILTER_ICON = ImageIOUtil.readIcon("FilterButton.png");
		FILTER_ROLLOVER_ICON = ImageIOUtil.readIcon("FilterButtonRollover.png");
    }

    /** The filter label. */
	private JLabel filterJLabel;

    /** The info label. */
	private JLabel infoJLabel;
	
	/** The info message to display. */
    private String infoMessage;

    /** Create a BrowserInfoAvatar. */
	BrowserInfoAvatar() {
	    super("BrowserInfo");
	    setLayout(new GridBagLayout());
	    setOpaque(false);
        setTransferHandler(new CreateDocumentTxHandler(getController()));
        CopyActionEnforcer.applyEnforcer(this);
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
	public void reload() { reloadInfo(); }

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
		this.infoMessage = getString("Info." + infoMessageKey, arguments);
	}

    /**
     * Set a localized message in the info label.
     * 
     * @param infoMessageKey
     *            The localized message local key.
     */
    public void setInfoMessage(final String infoMessageKey) {
        this.infoMessage = getString("Info." + infoMessageKey);
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

        final GridBagConstraints c = new GridBagConstraints();
        c.insets.left = 7;
        add(filterJLabel, c.clone());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets.left = 0;
        c.weightx = 1;
        add(infoJLabel, c.clone());
    }

    /**
	 * Reload the info label.
	 *
	 */
	private void reloadInfo() {
		infoJLabel.setText(getString("Info.Empty"));
        if(null != infoMessage) {
            infoJLabel.setText(infoMessage);
            final Timer reloadTimer = new Timer(3 * 1000, new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    infoMessage = null;
                    reloadInfo();
                }
            });
            reloadTimer.setRepeats(false);
            reloadTimer.start();
        }
        else {
//            infoJLabel.setText(ModelUtil.getName(readLocalUser()));
            // TODO Finish the info implementation.
            infoJLabel.setText("Alan Turing");
        }
	}
}
