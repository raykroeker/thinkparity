/*
 * Mar 8, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;

import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.ImageIOUtil;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.assertion.Assert;

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
	public BrowserInfoAvatar() {
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
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

	private void initComponents() {
        sortJLabel = LabelFactory.create(SORT_ICON);
        sortJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(final MouseEvent e) {
        		sortJLabelMouseClicked(e);
        	}
        	public void mouseEntered(final MouseEvent e) {
        		sortJLabel.setIcon(SORT_ROLLOVER_ICON);
        	}
        	public void mouseExited(MouseEvent e) {
        		sortJLabel.setIcon(SORT_ICON);
        	}
        });

        infoJLabel = LabelFactory.create();

        showHistoryJLabel = LabelFactory.create(SHOW_HISTORY_ICON);
        showHistoryJLabel.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(final MouseEvent e) {
        		showHistoryJLabelMouseClicked(e);
        	}
        	public void mouseEntered(final MouseEvent e) {
        		showHistoryJLabel.setIcon(SHOW_HISTORY_ROLLOVER_ICON);
        	}
        	public void mouseExited(final MouseEvent e) {
        		showHistoryJLabel.setIcon(SHOW_HISTORY_ICON);
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
	 * Display the document history.
	 * 
	 * @param e
	 *            The action event.
	 */
	private void showHistoryJLabelMouseClicked(final MouseEvent e) {
		getController().toggleHistoryAvatar();
	}

	private void sortJLabelMouseClicked(final MouseEvent evt) {
		Assert.assertNotYetImplemented("[BROWSER2] [INFO] [SORT BY]");
	}
}
