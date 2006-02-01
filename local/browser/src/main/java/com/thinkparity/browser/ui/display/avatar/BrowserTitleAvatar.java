/*
 * Jan 27, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.browser.ui.UIConstants;
import com.thinkparity.browser.ui.component.ButtonFactory;
import com.thinkparity.browser.ui.component.LabelFactory;
import com.thinkparity.browser.util.State;

import com.thinkparity.codebase.ResourceUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class BrowserTitleAvatar extends Avatar {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The paint used to draw the gradient.
	 * 
	 * @see #paintComponent(Graphics)
	 */
	private Paint gradientPaint;

	/**
	 * The mouse input adapter.  Used to drag the window by this avatar.
	 * 
	 */
	private final MouseInputAdapter mouseInputAdapter;

	/**
	 * Create a BrowserTitleAvatar.
	 * 
	 */
	BrowserTitleAvatar() {
		super("BrowserTitleAvatar");
		this.mouseInputAdapter = new MouseInputAdapter() {
			int offsetX;
			int offsetY;
			public void mouseDragged(final MouseEvent e) {
				getController().moveMainWindow(
						new Point(e.getPoint().x - offsetX,
								e.getPoint().y - offsetY));
			}
			public void mousePressed(MouseEvent e) {
				offsetX = e.getPoint().x;
				offsetY = e.getPoint().y;
			}
		};
		addMouseListener(mouseInputAdapter);
		addMouseMotionListener(mouseInputAdapter);
		setLayout(new GridBagLayout());
		initBrowserTitleComponents();
	}

	/**
	 * Initialize the browser's title components.
	 *
	 */
	private void initBrowserTitleComponents() {
		//	 _______________________________________
		//	/										\
		//	|	thinkParity                      X  |
		//	|										|
		//	-----------------------------------------
		//  |-20px-|
		
		final GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0, 20, 0, 0);
		c.weightx = 1.0;
		add(LabelFactory.create(getString("Title"), UIConstants.DefaultFontBold,
				new Color(116, 120, 131, 255)), c.clone());

		final ImageIcon closeJButtonIcon =
			new ImageIcon(ResourceUtil.getURL("images/closeButton.png"));
		final JButton closeJButton = ButtonFactory.create(null);
		closeJButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				getController().closeMainWindow();
			}
		});
		closeJButton.setIcon(closeJButtonIcon);
		final Dimension iconSize = new Dimension(
				closeJButtonIcon.getIconWidth(), closeJButtonIcon.getIconHeight());
		closeJButton.setPreferredSize(iconSize);

		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0, 0, 0, 20);
		c.weightx = 0.0;
		add(closeJButton, c.clone());
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.BROWSER_TITLE; }

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() { return null; }

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setState(com.thinkparity.browser.util.State)
	 * 
	 */
	public void setState(final State state) {}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		try {
			g2.setPaint(getGradientPaint());
			g2.fillRect(0, 0, getWidth(), getHeight());
		}
		finally { g2.dispose(); }
		g2 = (Graphics2D) g.create();
		try {
			// overlay the border so that the cornered edges get a border as well
			// COLOR BLACK
			g2.setColor(Color.BLACK);
			g2.drawRoundRect(-1, -1, getWidth() + 1, getHeight() * 2,
					UIConstants.TitlePaneCurvature, UIConstants.TitlePaneCurvature);
		}
		finally { g2.dispose(); }
	}

	/**
	 * Obtain the paint for painting the avatar's gradient.
	 * 
	 * @return The gradient paint.
	 */
	private Paint getGradientPaint() {
		if(null == gradientPaint) {
			// COLOR 236, 243, 239, 255
			// COLOR 194, 208, 219, 255
			gradientPaint = new GradientPaint(new Point(0, 0),
					new Color(236, 243, 239, 255),
					new Point(0, getHeight()), new Color(194, 208, 219, 255));
		}
		return gradientPaint;
	}
}
