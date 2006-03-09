/*
 * Jan 21, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.javax.swing.border.TopBottomBorder;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.ImageIOUtil;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.ResourceUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class BrowserLogoAvatar extends Avatar {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The logo icon.
	 * 
	 */
	private Image logoImage;

	/**
	 * The size of the logo image.
	 * 
	 * @see #getLogoImageSize()
	 */
	private Dimension logoImageSize;

	/**
	 * Create a BrowserLogoAvatar.
	 * 
	 */
	public BrowserLogoAvatar() {
		super("BrowserLogoAvatar");
		setLayout(new GridBagLayout());

		initComponents();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.BROWSER_LOGO; }

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() { return null; }

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(State state) {}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g.create();
		try {
			final int y = (getSize().height - getLogoImageSize().height) / 2;
			g2.drawImage(getLogoImage(), 13, y, this);
		}
		finally { g2.dispose(); }
	}

	/**
	 * Create a JLabel component that behaves as a link. It is clickable and
	 * colored blue.
	 * 
	 * @param text
	 *            The link text.
	 * @param actionListener
	 *            The action to perform when the link is clicked.
	 * @return The JLabel.
	 */
	private JLabel createJLabelLink(final String text,
			final ActionListener actionListener) {
		final JLabel jLabelLink =
			LabelFactory.createLink(text, BrowserConstants.SmallFont);
		jLabelLink.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				actionListener.actionPerformed(
						new ActionEvent(e.getSource(),
								ActionEvent.ACTION_PERFORMED, ""));
			}
		});
		return jLabelLink;
	}

	/**
	 * Create a JLabel component that acts as a separator between the labels.
	 * 
	 * @return The JLabel.
	 */
	private JLabel createJLabelSeparator() {
		final JLabel jLabelSeparator = LabelFactory.create(
				new ImageIcon(ResourceUtil.getURL("images/logoSeparator.png")));
		return jLabelSeparator;
	}

	/**
	 * Create the JLabel used to border the search field on the left.
	 * 
	 * @return The JLabel.
	 */
	private JLabel createLeftSearchJLabel() {
		final JLabel jLabel = LabelFactory.create(
				new ImageIcon(ResourceUtil.getURL("images/searchLeft.png")));
		return jLabel;
	}

	/**
	 * Create the JLabel used to border the search field on the right.
	 * 
	 * @return The JLabel.
	 */
	private JLabel createRightSearchJLabel() {
		final JLabel jLabel = LabelFactory.create(
				new ImageIcon(ResourceUtil.getURL("images/searchRight.png")));
		return jLabel;
	}

	/**
	 * Create the JTextField used for search.
	 * 
	 * @return The JTextField.
	 */
	private JTextField createSearchTextField() {
		final JTextField jTextField = TextFactory.create();
		// COLOR 237, 241, 244, 255
		jTextField.setBackground(new Color(237, 241, 244, 255));
		// COLOR 195, 209, 220, 255
		jTextField.setBorder(new TopBottomBorder(new Color(195, 209, 220, 255)));
		return jTextField;
	}

	/**
	 * Obtain the logo image. After the first read; it will remain in memory.
	 * 
	 * @return The logo image.
	 */
	private Image getLogoImage() {
		if(null == logoImage) { logoImage = ImageIOUtil.read("parityLogo.png"); }
		return logoImage;
	}

	/**
	 * Obtain the size of the logo.
	 * 
	 * @return The logo size.
	 */
	private Dimension getLogoImageSize() {
		if(null == logoImageSize) {
			final Image logoImage = getLogoImage();
			logoImageSize = new Dimension(
					logoImage.getWidth(this), logoImage.getHeight(this));
		}
		return logoImageSize;
	}

	/**
	 * Initialize the avatar components.
	 *
	 */
	private void initComponents() {
		final GridBagConstraints c = new GridBagConstraints();
		final GridBagConstraints c2 = new GridBagConstraints();

		final JLabel addJLabelLink = createJLabelLink(
				getString("AddContact"),
				new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						runAddContact();
					}
				});
		c.anchor = GridBagConstraints.EAST;
		c.insets.top = 21;
		c.weightx = 1;
		add(addJLabelLink, c.clone());

		final JLabel separatorJLabel = createJLabelSeparator();
		c.anchor = GridBagConstraints.CENTER;
		c.insets.left = c.insets.right = 7;
		c.weightx = 0;
		add(separatorJLabel, c.clone());

		final JLabel inviteJLabelLink =
			createJLabelLink(getString("Invite"), new ActionListener() {
				public void actionPerformed(final ActionEvent e) {}
			});
		c.insets.left = 0;
		c.insets.right = 15;
		add(inviteJLabelLink, c.clone());

		final JPanel searchJPanel = new JPanel();
		searchJPanel.setLayout(new GridBagLayout());
		searchJPanel.setOpaque(false);

		final JLabel leftSearchJLabel = createLeftSearchJLabel();
		c2.anchor = GridBagConstraints.EAST;
		c2.insets.left = 209;
		searchJPanel.add(leftSearchJLabel, c2.clone());

		final JTextField searchJTextField = createSearchTextField();
		c2.fill = GridBagConstraints.BOTH;
		c2.insets.top = c2.insets.bottom = 1;
		c2.insets.left = 0;
		c2.weightx = 1;
		searchJPanel.add(searchJTextField, c2.clone());

		final JLabel rightSearchJLabel = createRightSearchJLabel();
		c2.fill = GridBagConstraints.NONE;
		c2.insets.top = c2.insets.bottom = 0;
		c2.insets.right = 14;
		c2.weightx = 0;
		searchJPanel.add(rightSearchJLabel, c2.clone());

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 3;
		c.gridy = 1;
		c.insets.top = c.insets.left = c.insets.right = 0;
		c.insets.bottom = 18;
		c.weighty = 1;
		add(searchJPanel, c.clone());
	}

	private void runAddContact() { getController().runInviteContact(); }
}
