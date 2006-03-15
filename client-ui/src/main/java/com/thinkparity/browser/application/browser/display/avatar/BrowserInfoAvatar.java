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

import com.thinkparity.browser.Browser2Platform;
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

	private JLabel showHistory2JLabel;

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

	public void setInfoMessage(final String infoMessageKey, final Object[] arguments) {
		infoJLabel.setText(getString(infoMessageKey, arguments));
//		repaint();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

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

        showHistory2JLabel = LabelFactory.create(getHistoryIcon());
        showHistory2JLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				showHistory2JLabelMouseClicked(e);
			}
			public void mouseEntered(final MouseEvent e) {
				showHistory2JLabel.setIcon(getHistoryRolloverIcon());
			}
			public void mouseExited(final MouseEvent e) {
				showHistory2JLabel.setIcon(getHistoryIcon());
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

        add(showHistory2JLabel, c.clone());
    }

	/**
	 * Set a debug info message.
	 *
	 */
	private void setTestInfoMessage() {
		if(Browser2Platform.isTestMode()) {
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
		getController().toggleHistoryAvatar();
		showHistoryJLabel.setIcon(getHistoryRolloverIcon());
	}

	private void showHistory2JLabelMouseClicked(final MouseEvent e) {
		getController().toggleHistory2Avatar();
showHistory2JLabel.setVisible(false);
showHistoryJLabel.setVisible(false);
		showHistory2JLabel.setIcon(getHistoryRolloverIcon());
	}

	public void showHistoryButton() {
		showHistory2JLabel.setVisible(true);
		showHistoryJLabel.setVisible(true);
	}

	private void sortJLabelMouseClicked(final MouseEvent evt) {
		Assert.assertNotYetImplemented("[BROWSER2] [INFO] [SORT BY]");
	}
}
